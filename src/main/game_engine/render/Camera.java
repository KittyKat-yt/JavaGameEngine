package main.game_engine.render;

import main.game_engine.material.Material;
import main.game_engine.material.shader.FragmentData;
import main.game_engine.math.Float2;
import main.game_engine.math.Float3;
import main.game_engine.math.Maths;
import main.game_engine.scene.GameObject;
import main.game_engine.scene.Scene;
import main.game_engine.script.Input;
import main.game_engine.script.Script;
import main.game_engine.material.shader.Shader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public class Camera extends JPanel {
    private static final float NEAR_CLIP_DST = .01f;
    private static final Float3[] STANDARD_WEIGHTS = new Float3[]{
            new Float3(1f, 0f, 0f), new Float3(0f, 1f, 0f), new Float3(0f, 0f, 1f)};

    public static boolean shaderOverride = false;
    public static boolean showOverdraw = false;
    private static final float OVERDRAW_LIMIT = 8f;
    private static final Float3 OVER_OVERDRAW_COLOR = new Float3(1f, .6f, .6f);
    public static boolean shaderStatus = false;
    private static final Float3 SHADER_RETURN = new Float3(1f, 1f, 1f);
    private static final Float3 SHADER_DISCARD = new Float3(1f, 0f, 0f);

    public static float fov;
    private static int resX;
    private static float invResX;
    private static int resY;
    private static float invResY;
    private static Float2 screenRes;
    private static int width;
    private static int height;
    private static float[] depthBuffer;
    private static BufferedImage renderedImage;
    private static int[] colorBuffer;

    public Camera(float camFov, int resolutionX, int resolutionY, int screenWidth, int screenHeight) {
        fov = camFov;

        resX = resolutionX;
        invResX = 1f / resX;
        resY = resolutionY;
        invResY = 1f / resY;
        screenRes = new Float2(resX, resY);

        width = screenWidth;
        height = screenHeight;

        depthBuffer = new float[resX * resY];
        renderedImage = new BufferedImage(resX, resY, BufferedImage.TYPE_INT_RGB);
        colorBuffer = ((DataBufferInt) renderedImage.getRaster().getDataBuffer()).getData();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.white);

        Arrays.fill(colorBuffer, 0x000000);
        Arrays.fill(depthBuffer, Float.MAX_VALUE);

        Script.updateDeltaTime();

        if (Scene.camera.active) {
            float screenHeightWorld = (float) (2f * Math.tan(fov * Maths.DEG_TO_RAD / 2f));
            float zScale = resY / screenHeightWorld;

            Scene.updateCamera();
            for (int o = 0; o < Scene.objectCount(); o++) {
                GameObject object = Scene.getObject(o);
                if (object.active) {
                    object.updateScript();
                    renderObject(object, o, zScale);
                }
            }
        }
        Input.updateInput();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.drawImage(renderedImage, 0, 0, width, height, null);

        g2.drawString(String.format("fps: %3.2f | min: %3.2f | max: %3.2f", Script.fps, Script.fpsMin, Script.fpsMax), 5, 15);

        g2.drawString(String.format("%s%s%s",
                shaderOverride ? "shaderOverride " : "", showOverdraw ? "showOverdraw " : "", shaderStatus ? "shaderStatus " : ""), 5, 30);
        if (shaderOverride) {
            g2.drawString(Scene.camera.mat.shader.getClass().getSimpleName(), 5, 50);
        } else if (shaderStatus) {
            g2.drawString("white - regular return", 5, 50);
            g2.drawString("red   - discarded fragment", 5, 65);
        }
    }

    private static void renderObject(GameObject object, int objectIndex, float zScale) {
        for (int t = 0; t < object.triangleCount(); t++) {
            Float2[] UVs = object.getTriUVs(t);
            Float3 worldNormal = object.getTriNormal(t);
            Float3[] tri = Maths.triToView(object.getTriVertexes(t), Scene.camera.transform);
            Material mat = object.mat;
            renderTriangle(tri, zScale, worldNormal, UVs, (mat == null) ? Scene.errorMat : mat, objectIndex, t);
        }
    }

    private static void renderTriangle(Float3[] tri, float zScale, Float3 worldNormal, Float2[] UVs, Material mat, int oi, int ti) {
        Float3[] vectors = Scene.camera.transform.invBasisVectors;
        boolean facingCam = (Maths.rotate(worldNormal, vectors[0], vectors[1], vectors[2]).dotProduct(tri[0]) < 0f);

        if (facingCam) {
            boolean clipA = (tri[0].z <= NEAR_CLIP_DST);
            boolean clipB = (tri[1].z <= NEAR_CLIP_DST);
            boolean clipC = (tri[2].z <= NEAR_CLIP_DST);
            int clipCount = Maths.boolToInt(clipA) + Maths.boolToInt(clipB) + Maths.boolToInt(clipC);

            switch (clipCount) {
                case 0:
                    drawTriangle(triToScreen(tri, zScale), STANDARD_WEIGHTS, worldNormal, UVs, mat, oi, ti);
                    break;
                case 1:
                    int clipIndex = (clipA ? 0 : (clipB ? 1 : 2));
                    int nextI = (clipIndex + 1) % 3;
                    int prevI = (clipIndex + 2) % 3;
                    Float3 clippedP = tri[clipIndex];
                    Float3 A = tri[nextI];
                    Float3 B = tri[prevI];

                    float fracA = (NEAR_CLIP_DST - clippedP.z) / (A.z - clippedP.z);
                    float fracB = (NEAR_CLIP_DST - clippedP.z) / (B.z - clippedP.z);
                    Float3 clipPointA = clippedP.lerp(A, fracA);
                    Float3 clipPointB = clippedP.lerp(B, fracB);

                    Float3 weightA = STANDARD_WEIGHTS[clipIndex].lerp(STANDARD_WEIGHTS[nextI], fracA);
                    Float3 weightB = STANDARD_WEIGHTS[clipIndex].lerp(STANDARD_WEIGHTS[prevI], fracB);
                    Float3[] weights1 = new Float3[]{STANDARD_WEIGHTS[nextI], STANDARD_WEIGHTS[prevI], weightB};
                    Float3[] weights2 = new Float3[]{STANDARD_WEIGHTS[nextI], weightA, weightB};

                    drawTriangle(triToScreen(A, B, clipPointB, zScale), weights1, worldNormal, UVs, mat, oi, ti);
                    drawTriangle(triToScreen(A, clipPointA, clipPointB, zScale), weights2, worldNormal, UVs, mat, oi, ti);
                    break;
                case 2:
                    int nonClipI = ((!clipA) ? 0 : ((!clipB) ? 1 : 2));
                    int clipIA = (nonClipI + 1) % 3;
                    int clipIB = (nonClipI + 2) % 3;
                    Float3 P = tri[nonClipI];
                    Float3 clippedA = tri[clipIA];
                    Float3 clippedB = tri[clipIB];

                    float fracX = (NEAR_CLIP_DST - P.z) / (clippedA.z - P.z);
                    float fracY = (NEAR_CLIP_DST - P.z) / (clippedB.z - P.z);
                    Float3 clipPointX = P.lerp(clippedA, fracX);
                    Float3 clipPointY = P.lerp(clippedB, fracY);

                    Float3 weightX = STANDARD_WEIGHTS[nonClipI].lerp(STANDARD_WEIGHTS[clipIA], fracX);
                    Float3 weightY = STANDARD_WEIGHTS[nonClipI].lerp(STANDARD_WEIGHTS[clipIB], fracY);
                    Float3[] weights = new Float3[]{STANDARD_WEIGHTS[nonClipI], weightX, weightY};

                    drawTriangle(triToScreen(P, clipPointX, clipPointY, zScale), weights, worldNormal, UVs, mat, oi, ti);
                    break;
                default:
                    break;
            }
        }
    }
    private static void drawTriangle(Float3[] tri, Float3[] triWeights, Float3 worldNormal, Float2[] UVs, Material mat, int oi, int ti) {
        Float2 a = tri[0].to2D();
        Float2 b = tri[1].to2D();
        Float2 c = tri[2].to2D();
        Float3 invDepths = new Float3(tri[0].z, tri[1].z, tri[2].z).inverse();

        float minX = Math.min(Math.min(a.x, b.x), c.x);
        float minY = Math.min(Math.min(a.y, b.y), c.y);
        float maxX = Math.max(Math.max(a.x, b.x), c.x);
        float maxY = Math.max(Math.max(a.y, b.y), c.y);
        int startX = Maths.clamp(0, resX - 1, (int) minX);
        int startY = Maths.clamp(0, resY - 1, (int) minY);
        int endX = Maths.clamp(0, resX, (int) Math.ceil(maxX));
        int endY = Maths.clamp(0, resY, (int) Math.ceil(maxY));

        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                Float3 bary = new Float3();
                if (Maths.pointTriangleTest(a, b, c, new Float2(x, y), bary)) {
                    float depth = 1f / bary.dotProduct(invDepths);
                    if (showOverdraw || (depth < getDepth(x, y))) {
                        Float3 shaderCol = new Float3(1f, 0f, 1f);

                        Shader shader = shaderOverride ? Scene.camera.mat.shader : mat.shader;
                        if (shader != null) {
                            Float2 screenUV = new Float2(x * invResX, y * invResY);
                            Float3 weights = (triWeights[0].scale(bary.x * invDepths.x)
                                    .add(triWeights[1].scale(bary.y * invDepths.y))
                                    .add(triWeights[2].scale(bary.z * invDepths.z))).scale(depth);

                            shaderCol = shader.fragment(new FragmentData(screenUV, weights, mat, UVs, worldNormal, depth, oi, ti));

                            if (shaderStatus) {
                                if (shaderCol.x < 0f) shaderCol = SHADER_DISCARD;
                                else shaderCol = SHADER_RETURN;
                            } else {
                                if (shaderCol.x < 0f) continue;
                            }
                        }

                        if (showOverdraw) {
                            depth = getDepth(x, y);
                            if (depth == Float.MAX_VALUE) {
                                depth = 1f;
                            } else {
                                depth += 1f;
                            }
                            shaderCol = (depth > OVERDRAW_LIMIT) ? OVER_OVERDRAW_COLOR : (new Float3(depth, depth, depth).scale(1 / OVERDRAW_LIMIT));
                        }
                        setRGB(x, y, shaderCol.getColor());
                        setDepth(x, y, depth);
                    }
                }
            }
        }
    }
    private static Float3[] triToScreen(Float3 A, Float3 B, Float3 C, float zScale) {
        return Maths.triToScreen(A, B, C, screenRes, zScale);
    }
    private static Float3[] triToScreen(Float3[] tri, float zScale) {
        return triToScreen(tri[0], tri[1], tri[2], zScale);
    }
    private static float getDepth(int x, int y) {
        return depthBuffer[(y * resX) + x];
    }
    private static void setDepth(int x, int y, float d) {
        depthBuffer[(y * resX) + x] = d;
    }
    private static void setRGB(int x, int y, int color) {
        colorBuffer[(y * resX) + x] = color;
    }

    public void startRendering() {
        new Thread(() -> {
            while (true) {
                try {
                    repaint();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
