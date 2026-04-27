package main.game_engine.scene;

import main.game_engine.math.Float2;
import main.game_engine.math.Float3;

public class Model {
    private final Float3[] vertexes;
    private final Float2[] UVs;
    private final Float3[] normals;
    private final Triangle[] triangles;
    public Model(Float3[] vertexes, Float2[] UVs, Float3[] normals, Triangle[] triangles) {
        this.vertexes = vertexes;
        this.UVs = UVs;
        this.normals = normals;
        this.triangles = triangles;
    }

    public int triangleCount() {
        return triangles.length;
    }
    public Float3[] getTriVertexes(int i) {
        return triangles[i].getVertexes(vertexes);
    }
    public Float2[] getTriUVs(int i) {
        return triangles[i].getUVs(UVs);
    }
    public Float3 getTriNormal(int i) {
        return triangles[i].getNormal(normals);
    }
}
