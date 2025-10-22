package game_engine.math;

import game_engine.scene.Transform;

public abstract class Maths {
    public static final double DEG_TO_RAD = Math.PI / 180d;

    public static int boolToInt(boolean b) {
        return (b ? 1 : 0);
    }

    public static int clamp(int min, int max, int val) {
        return Math.max(Math.min(max, val), min);
    }
    public static float clamp(float min, float max, float val) {
        return Math.max(Math.min(max, val), min);
    }
    public static double clamp(double min, double max, double val) {
        return Math.max(Math.min(max, val), min);
    }

    public static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }
    public static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    public static float signedTriArea(Float2 a, Float2 b, Float2 c) {
        return c.sub(a).dotProduct(b.sub(a).rotate90()) / 2f;
    }
    public static boolean pointTriangleTest(Float2 a, Float2 b, Float2 c, Float2 p, Float3 baryOut) {
        float areaABP = signedTriArea(a, b, p);
        float areaBCP = signedTriArea(b, c, p);
        float areaCAP = signedTriArea(c, a, p);

        float totalArea = areaABP + areaBCP + areaCAP;
        if (totalArea == 0f) return false;

        float invTotalArea = 1f / totalArea;
        float A = areaBCP * invTotalArea;
        float B = areaCAP * invTotalArea;
        float C = areaABP * invTotalArea;
        baryOut.set(A, B, C);

        return ((areaABP >= 0f) == (areaBCP >= 0f)) && ((areaABP >= 0f) == (areaCAP >= 0f));
    }

    public static Float3 vertexToView(Float3 vertex, Transform camTransform) {
        Float3[] vectors = camTransform.invBasisVectors;
        return rotate(vertex.sub(camTransform.pos), vectors[0], vectors[1], vectors[2]);
    }
    public static Float3[] triToView(Float3[] tri, Transform camTransform) {
        Float3 A = vertexToView(tri[0], camTransform);
        Float3 B = vertexToView(tri[1], camTransform);
        Float3 C = vertexToView(tri[2], camTransform);
        return new Float3[]{A, B, C};
    }
    public static Float3 vertexToScreen(Float3 vertex, Float2 screenSize, float zScale) {
        Float2 offset = new Float2(-vertex.x, -vertex.y).scale(zScale / vertex.z);
        Float2 screenPos = screenSize.scale(.5f).add(offset);
        return new Float3(screenPos.x, screenPos.y, vertex.z);
    }
    public static Float3[] triToScreen(Float3 A, Float3 B, Float3 C, Float2 screenSize, float zScale) {
        Float3 a = vertexToScreen(A, screenSize, zScale);
        Float3 b = vertexToScreen(B, screenSize, zScale);
        Float3 c = vertexToScreen(C, screenSize, zScale);
        return new Float3[]{a, b, c};
    }

    public static Float3[] getBasisVectors(Float3 rot) {
        float sPitch = (float) Math.sin(rot.x * DEG_TO_RAD);
        float cPitch = (float) Math.cos(rot.x * DEG_TO_RAD);
        Float3 iHatPitch = new Float3(1f, 0f, 0f);
        Float3 jHatPitch = new Float3(0f, cPitch, -sPitch);
        Float3 kHatPitch = new Float3(0f, sPitch, cPitch);

        float sYaw = (float) Math.sin(rot.y * DEG_TO_RAD);
        float cYaw = (float) Math.cos(rot.y * DEG_TO_RAD);
        Float3 iHatYaw = new Float3(cYaw, 0f, sYaw);
        Float3 jHatYaw = new Float3(0f, 1f, 0f);
        Float3 kHatYaw = new Float3(-sYaw, 0f, cYaw);

        float sRoll = (float) Math.sin(rot.z * DEG_TO_RAD);
        float cRoll = (float) Math.cos(rot.z * DEG_TO_RAD);
        Float3 iHatRoll = new Float3(cRoll, sRoll, 0f);
        Float3 jHatRoll = new Float3(-sRoll, cRoll, 0f);
        Float3 kHatRoll = new Float3(0f, 0f, 1f);

        Float3 iHatPY = rotate(iHatPitch, iHatYaw, jHatYaw, kHatYaw);
        Float3 jHatPY = rotate(jHatPitch, iHatYaw, jHatYaw, kHatYaw);
        Float3 kHatPY = rotate(kHatPitch, iHatYaw, jHatYaw, kHatYaw);

        Float3 iHat = rotate(iHatRoll, iHatPY, jHatPY, kHatPY);
        Float3 jHat = rotate(jHatRoll, iHatPY, jHatPY, kHatPY);
        Float3 kHat = rotate(kHatRoll, iHatPY, jHatPY, kHatPY);
        return new Float3[]{iHat, jHat, kHat};
    }
    public static Float3[] getInverseBasisVectors(Float3[] vectors) {
        Float3 iHat = new Float3(vectors[0].x, vectors[1].x, vectors[2].x);
        Float3 jHat = new Float3(vectors[0].y, vectors[1].y, vectors[2].y);
        Float3 kHat = new Float3(vectors[0].z, vectors[1].z, vectors[2].z);
        return new Float3[]{iHat, jHat, kHat};
    }

    public static Float3 rotate(Float3 f3, Float3 iHat, Float3 jHat, Float3 kHat) {
        return (iHat.scale(f3.x).add(jHat.scale(f3.y)).add(kHat.scale(f3.z)));
    }
    public static Float3 rotate(Float3 f3, Transform transform) {
        Float3[] vectors = transform.basisVectors;
        return rotate(f3, vectors[0], vectors[1], vectors[2]);
    }

    public static Float3 transform(Float3 f3, Transform transform) {
        return rotate(f3.multiply(transform.scale), transform).add(transform.pos);
    }
    public static Float3[] transformTri(Float3[] tri, Transform transform) {
        Float3 A = transform(tri[0], transform);
        Float3 B = transform(tri[1], transform);
        Float3 C = transform(tri[2], transform);
        return new Float3[]{A, B, C};
    }
}
