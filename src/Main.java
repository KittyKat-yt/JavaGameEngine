import main.game_engine.material.Material;
import main.game_engine.material.Texture;
import main.game_engine.material.shader.LitShader;
import main.game_engine.material.shader.Shader;
import main.game_engine.math.Float2;
import main.game_engine.render.RenderWindow;
import main.game_engine.scene.GameObject;
import main.game_engine.scene.Scene;
import main.game_engine.script.CameraScript;
import main.game_engine.script.RotationScript;
import main.game_engine.script.Script;

public class Main {
    public static void main(String[] args) {
        RenderWindow window = new RenderWindow("Java CPU Render Engine", 90f);

        setupScene();

        window.startRendering();
    }

    private static void setupScene() {
        Scene.initializeScene();

        Scene.setSunAngle(new Float2(40f, 200f));
        Scene.envLight = .1f;
        Shader lit = new LitShader();

        Material quadMat = new Material(lit, null, 2f, 2f);
        Material cubeMat = new Material(lit, null, 1f, 1f);
        setupMatTextures(quadMat, cubeMat);

        Scene.camera = new GameObject("cam", "", 0f, 1.5f, 0f, new Material(), new CameraScript());

        Script rotScript = new RotationScript();

        Scene.add(new GameObject("floor", "quad", 0f, 0f, 10f, -90f, 0f, 0f, 8f, quadMat, null));
        Scene.add(new GameObject("cubeRot", "cube", 0f, 5f, 10f, 0f, 0f, 0f, 1.5f, cubeMat, rotScript));
        Scene.add(new GameObject("cube01", "cube", 0f, .3f, 10f, 0f, 0f, 0f, .5f, .3f, .5f, cubeMat, null));
        Scene.add(new GameObject("cube02", "cube", 5f, 2f, 15f, 0f, 60f, 0f, 2f, cubeMat, null));
        Scene.add(new GameObject("cube03", "cube", -5f, 1.6f, 15f, 0f, -30f, 0f, 1.6f, cubeMat, null));
        Scene.add(new GameObject("cube04", "cube", 5f, 1.2f, 5f, 0f, 45f, 0f, 1.2f, cubeMat, null));
        Scene.add(new GameObject("cube05", "cube", -5f, .8f, 5f, 0f, -20f, 0f, .8f, cubeMat, null));
        Scene.add(new GameObject("error", "", 0f, 1.5f, 15f, null, null));
    }
    private static void setupMatTextures(Material quad, Material cube) {
        Texture quadTex = new Texture(16, 16);
        quad.tex = quadTex;
        quadTex.fill(0x5f5f5f);
        quadTex.fill(0, 0, 8, 8, 0x7f7f7f);
        quadTex.fill(8, 8, 8, 8, 0x7f7f7f);

        Texture cubeTex = new Texture(32, 64);
        cube.tex = cubeTex;
        cubeTex.clear();
        cubeTex.fill(0, 0, 32, 16, 0x7f7fff);
        cubeTex.fill(0, 0, 8, 8, 0x0000ff);
        cubeTex.fill(8, 8, 8, 8, 0x0000ff);
        cubeTex.fill(16, 0, 8, 8, 0x0000ff);
        cubeTex.fill(24, 8, 8, 8, 0x0000ff);
        cubeTex.fill(0, 16, 32, 16, 0xff7f7f);
        cubeTex.fill(0, 16, 8, 8, 0xff0000);
        cubeTex.fill(8, 24, 8, 8, 0xff0000);
        cubeTex.fill(16, 16, 8, 8, 0xff0000);
        cubeTex.fill(24, 24, 8, 8, 0xff0000);
        cubeTex.fill(0, 32, 32, 16, 0x7fff7f);
        cubeTex.fill(0, 32, 8, 8, 0x00ff00);
        cubeTex.fill(8, 40, 8, 8, 0x00ff00);
        cubeTex.fill(16, 32, 8, 8, 0x00ff00);
        cubeTex.fill(24, 40, 8, 8, 0x00ff00);
    }
}