package main.game_engine.material.shader.debug;

import main.game_engine.material.shader.FragmentData;
import main.game_engine.material.shader.Shader;
import main.game_engine.math.Float3;

public class NormalsShader extends Shader {
    @Override
    public Float3 fragment(FragmentData f) {
        return f.worldNormal.add(new Float3(1f, 1f, 1f)).scale(.5f);
    }
}
