package main.game_engine.scene;

import main.game_engine.math.Float2;
import main.game_engine.math.Float3;

public class Triangle {
    private final int A;
    private final int B;
    private final int C;
    private final int uvA;
    private final int uvB;
    private final int uvC;
    private final int N;
    public Triangle(int A, int B, int C, int uvA, int uvB, int uvC, int N) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.uvA = uvA;
        this.uvB = uvB;
        this.uvC = uvC;
        this.N = N;
    }

    public Float3[] getVertexes(Float3[] vertexes) {
        Float3 a = vertexes[A];
        Float3 b = vertexes[B];
        Float3 c = vertexes[C];
        return new Float3[]{a, b, c};
    }
    public Float2[] getUVs(Float2[] UVs) {
        Float2 a = UVs[uvA];
        Float2 b = UVs[uvB];
        Float2 c = UVs[uvC];
        return new Float2[]{a, b, c};
    }
    public Float3 getNormal(Float3[] normals) {
        return normals[N];
    }
}
