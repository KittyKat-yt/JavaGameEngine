package game_engine.material.shader.debug;

import game_engine.material.shader.FragmentData;
import game_engine.material.shader.Shader;
import game_engine.math.Float3;
import game_engine.scene.Scene;
import game_engine.script.Script;

import java.util.Random;

public class WireframeShader extends Shader {
    private static final Random random = new Random();

    @Override
    public Float3 fragment(FragmentData f) {
        float min = Math.min(Math.min(f.weights.x, f.weights.y), f.weights.z);
        if (min < .025f) {
            random.setSeed(f.object * 601281556L);
            float hue = random.nextFloat(360f);
            return Float3.fromHSV(hue, 50f, 100f);
        }
        return discard;
    }
}
