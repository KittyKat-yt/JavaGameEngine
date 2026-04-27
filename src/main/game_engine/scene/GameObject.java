package main.game_engine.scene;

import main.game_engine.material.Material;
import main.game_engine.math.Float2;
import main.game_engine.math.Float3;
import main.game_engine.math.Maths;
import main.game_engine.script.Script;

public class GameObject {
    public boolean active;
    public String name;
    public Transform transform;
    public Material mat;
    public Script script;
    public String modelKey;
    public GameObject(String name, String model, float xP, float yP, float zP, float pR, float yR, float rR, float xS, float yS, float zS, Material mat, Script script) {
        this.active = true;
        this.name = name;
        this.modelKey = model;
        this.transform = new Transform(xP, yP, zP, pR, yR, rR, xS, yS, zS);
        this.mat = mat;
        this.script = script;
    }
    public GameObject(String name, String model, float xP, float yP, float zP, float pR, float yR, float rR, float S, Material mat, Script script) {
        this(name, model, xP, yP, zP, pR, yR, rR, S, S, S, mat, script);
    }
    public GameObject(String name, String model, float xP, float yP, float zP, float pR, float yR, float rR, Material mat, Script script) {
        this(name, model, xP, yP, zP, pR, yR, rR, 1f, mat, script);
    }
    public GameObject(String name, String model, float xP, float yP, float zP, Material mat, Script script) {
        this(name, model, xP, yP, zP, 0f, 0f, 0f, mat, script);
    }
    public GameObject(String name, String model, Material mat, Script script) {
        this(name, model, 0f, 0f, 0f, mat, script);
    }
    public GameObject(String name) {
        this(name, "", null, null);
    }


    public Model getModel() {
        return Scene.getModel(modelKey);
    }

    public int triangleCount() {
        return getModel().triangleCount();
    }
    public Float3[] getTriVertexes(int i) {
        return Maths.transformTri(getModel().getTriVertexes(i), transform);
    }
    public Float2[] getTriUVs(int i) {
        return getModel().getTriUVs(i);
    }
    public Float3 getTriNormal(int i) {
        return Maths.rotate(getModel().getTriNormal(i), transform);
    }

    public void updateScript() {
        if (script != null) {
            script.update(this);
        }
    }
}
