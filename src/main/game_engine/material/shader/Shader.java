package main.game_engine.material.shader;

import main.game_engine.math.Float3;
import main.game_engine.math.Maths;
import main.game_engine.scene.Scene;

public abstract class Shader {
    protected static final Float3 discard = new Float3(-1f, 0f, 0f);

    public abstract Float3 fragment(FragmentData f);

    protected static float calculateLightIntensity(Float3 normal) {
        float intensity = (normal.dotProduct(Scene.dirToSun) + 1f) * .5f;
        return Maths.lerp(intensity, 1f, Scene.envLight);
    }
}
