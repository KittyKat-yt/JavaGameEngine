package main.game_engine.material.shader.debug;

import main.game_engine.material.shader.FragmentData;
import main.game_engine.material.shader.Shader;
import main.game_engine.math.Float3;

public class UVShader extends Shader {
    @Override
    public Float3 fragment(FragmentData f) {
        return f.texUV.to3D();
    }
}
