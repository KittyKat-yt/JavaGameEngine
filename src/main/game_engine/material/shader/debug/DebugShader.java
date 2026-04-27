package main.game_engine.material.shader.debug;

import main.game_engine.material.shader.FragmentData;
import main.game_engine.material.shader.Shader;
import main.game_engine.math.Float3;
import main.game_engine.script.Script;

public class DebugShader extends Shader {
    @Override
    public Float3 fragment(FragmentData f) {
        if (Math.abs(((f.worldPos.x * .3f) + f.worldPos.y + (f.worldPos.z * .1f) - (Script.time * .6f)) % 1f) > .7f) return discard;
        double l = f.weights.to2D().length();
        Float3 color = f.screenUV.to3D(1f);
        if ((l < .35d) || (l > .65d)) color = color.scale(.5f);
        return color.scale(Math.min(1f, 4f / f.depth));
    }
}
