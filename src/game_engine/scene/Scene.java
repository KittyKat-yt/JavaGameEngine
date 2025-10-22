package game_engine.scene;

import game_engine.material.Material;
import game_engine.material.Texture;
import game_engine.material.shader.UnlitShader;
import game_engine.math.Float2;
import game_engine.math.Float3;
import game_engine.math.Maths;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class Scene {
    public static Float3 dirToSun = new Float3();
    public static Float2 sunAngle = new Float2();
    public static float envLight = 0f;
    public static void setSunAngle(Float2 angle) {
        sunAngle = angle;
        dirToSun = Maths.getBasisVectors(new Float3(sunAngle.x, sunAngle.y, 0f))[2];
    }

    public static GameObject camera;
    public static void updateCamera() {
        if (camera.script != null) {
            camera.script.update(camera);
        }
    }

    public static final Material errorMat = new Material(new UnlitShader(), new Texture(2, 2), 4f, 4f);
    private static final Model errorModel = new Model(
            new Float3[]{
                    new Float3(1f, 1f, 0f), new Float3(-1f, 1f, 0f),
                    new Float3(1f, -1f, 0f), new Float3(-1f, -1f, 0f),
                    new Float3(1f, .9f, 0f), new Float3(.9f, 1f, 0f),
                    new Float3(-.9f, -1f, 0f), new Float3(-1f, -.9f, 0f),
                    new Float3(-1f, .9f, 0f), new Float3(-.9f, 1f, 0f),
                    new Float3(.9f, -1f, 0f), new Float3(1f, -.9f, 0f)},
            new Float2[]{new Float2(0f, 0f), new Float2(1f, 0f), new Float2(0f, 1f), new Float2(1f, 1f)},
            new Float3[]{new Float3(0f, 0f, 1f), new Float3(0f, 0f, -1f)},
            new Triangle[]{
                    new Triangle(0, 3, 1, 1, 2, 0, 0), new Triangle(0, 3, 2, 1, 2, 3, 0),
                    new Triangle(4, 7, 5, 0, 3, 1, 1), new Triangle(4, 7, 6, 0, 3, 2, 1),
                    new Triangle(8, 11, 9, 0, 3, 1, 1), new Triangle(8, 11, 10, 0, 3, 2, 1)});
    private static final Map<String, Model> models = new HashMap<>();
    public static void addModel(String key, Model model) {
        models.put(key, model);
    }
    public static void replaceModel(String key, Model model) {
        removeModel(key);
        addModel(key, model);
    }
    public static void removeModel(String key) {
        models.remove(key);
    }
    public static Model getModel(String key) {
        Model model = models.get(key);
        if (model == null) {
            return errorModel;
        }
        return models.get(key);
    }

    private static final Map<String, GameObject> objects = new HashMap<>();
    public static void add(GameObject object) {
        objects.put(object.name, object);
    }
    public static void delete(String name) {
        objects.remove(name);
    }
    public static GameObject getObject(String name) {
        return objects.get(name);
    }
    public static GameObject getObject(int index) {
        return getObjects()[index];
    }
    public static GameObject[] getObjects() {
        return objects.values().toArray(new GameObject[0]);
    }
    public static int objectCount() {
        return objects.size();
    }

    public static void initializeScene() {
        Float3[] cubeV = {
                new Float3(1f, 1f, 1f), new Float3(-1f, 1f, 1f),
                new Float3(1f, -1f, 1f), new Float3(-1f, -1f, 1f),
                new Float3(1f, 1f, -1f), new Float3(-1f, 1f, -1f),
                new Float3(1f, -1f, -1f), new Float3(-1f, -1f, -1f)};
        Float2[] cubeUVs = {
                new Float2(0f, 0f), new Float2(.5f, 0f), new Float2(0f, .25f), new Float2(.5f, .25f),
                new Float2(.5f, 0f), new Float2(1f, 0f), new Float2(.5f, .25f), new Float2(1f, .25f),
                new Float2(0f, .25f), new Float2(.5f, .25f), new Float2(0f, .5f), new Float2(.5f, .5f),
                new Float2(.5f, .25f), new Float2(1f, .25f), new Float2(.5f, .5f), new Float2(1f, .5f),
                new Float2(0f, .5f), new Float2(.5f, .5f), new Float2(0f, .75f), new Float2(.5f, .75f),
                new Float2(.5f, .5f), new Float2(1f, .5f), new Float2(.5f, .75f), new Float2(1f, .75f)};
        Float3[] cubeN = {
                new Float3(0f, 0f, 1f), new Float3(0f, 0f, -1f),
                new Float3(1f, 0f, 0f), new Float3(-1f, 0f, 0f),
                new Float3(0f, 1f, 0f), new Float3(0f, -1f, 0f)};
        Triangle[] cubeT = {
                new Triangle(0, 3, 1, 0, 3, 1, 0), new Triangle(0, 3, 2, 0, 3, 2, 0),
                new Triangle(4, 7, 5, 4, 7, 5, 1), new Triangle(4, 7, 6, 4, 7, 6, 1),
                new Triangle(0, 6, 2, 8, 11, 9, 2), new Triangle(0, 6, 4, 8, 11, 10, 2),
                new Triangle(1, 7, 3, 12, 15, 13, 3), new Triangle(1, 7, 5, 12, 15, 14, 3),
                new Triangle(0, 5, 1, 16, 19, 17, 4), new Triangle(0, 5, 4, 16, 19, 18, 4),
                new Triangle(2, 7, 3, 20, 23, 21, 5), new Triangle(2, 7, 6, 20, 23, 22, 5)};
        Scene.addModel("cube", new Model(cubeV, cubeUVs, cubeN, cubeT));

        Float3[] cubeInvN = {
                new Float3(0f, 0f, -1f), new Float3(0f, 0f, 1f),
                new Float3(-1f, 0f, 0f), new Float3(1f, 0f, 0f),
                new Float3(0f, -1f, 0f), new Float3(0f, 1f, 0f)};
        Scene.addModel("cubeInv", new Model(cubeV, cubeUVs, cubeInvN, cubeT));

        Float3[] quadV = {
                new Float3(1f, 1f, 0f), new Float3(-1f, 1f, 0f),
                new Float3(1f, -1f, 0f), new Float3(-1f, -1f, 0f)};
        Float2[] quadUVs = {new Float2(0f, 0f), new Float2(1f, 0f), new Float2(0f, 1f), new Float2(1f, 1f)};
        Float3[] quadN = {new Float3(0f, 0f, -1f)};
        Triangle[] quadT = {new Triangle(0, 3, 1, 0, 3, 1, 0), new Triangle(0, 3, 2, 0, 3, 2, 0)};
        Scene.addModel("quad", new Model(quadV, quadUVs, quadN, quadT));
    }
}
