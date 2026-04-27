package main.game_engine.material.shader;

import main.game_engine.material.Material;
import main.game_engine.material.Texture;
import main.game_engine.math.Float2;
import main.game_engine.math.Float3;
import main.game_engine.scene.Scene;

public class FragmentData {
    public Float2 screenUV;
    public Float3 weights;
    public Material mat;
    public Float2[] triUVs;
    public Float2 texUV;
    public Texture tex;
    public Float3 worldNormal;
    public float depth;
    public int object;
    public int triangle;
    public Float3 worldPos;

    public FragmentData(Float2 screenUV, Float3 weights, Material mat, Float2[] UVs, Float3 worldNormal, float depth, int oi, int ti) {
        this.screenUV = screenUV;
        this.weights = weights;

        this.mat = mat;
        this.triUVs = UVs;
        this.texUV = mat.convertUV(UVs[0].scale(weights.x).add(UVs[1].scale(weights.y)).add(UVs[2].scale(weights.z)));
        this.tex = (mat.tex == null) ? Scene.errorMat.tex : mat.tex;

        this.worldNormal = worldNormal;
        this.depth = depth;

        this.object = oi;
        this.triangle = ti;
        Float3[] tri = Scene.getObject(oi).getTriVertexes(ti);
        this.worldPos = tri[0].scale(weights.x).add(tri[1].scale(weights.y)).add(tri[2].scale(weights.z));
    }
}
