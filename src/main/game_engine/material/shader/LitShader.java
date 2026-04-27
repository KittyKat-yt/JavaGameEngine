package main.game_engine.material.shader;

import main.game_engine.math.Float3;

public class LitShader extends Shader {
    @Override
    public Float3 fragment(FragmentData f) {
        float light = calculateLightIntensity(f.worldNormal);
        return f.tex.sample(f.texUV).scale(light);
    }
}
