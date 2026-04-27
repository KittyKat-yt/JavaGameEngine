package main.game_engine.material;

import main.game_engine.material.shader.Shader;
import main.game_engine.material.shader.UnlitShader;
import main.game_engine.math.Float2;

public class Material {
    public Shader shader;
    public Texture tex;
    public Float2 scale;
    public Material() {
        this.shader = new UnlitShader();
        this.tex = new Texture(2, 2);
        this.scale = new Float2(1f, 1f);
    }
    public Material(Shader shader, Texture texture, float scaleX, float scaleY) {
        this.shader = shader;
        this.tex = texture;
        this.scale = new Float2(scaleX, scaleY);
    }

    public Float2 convertUV(Float2 uv) {
        return uv.multiply(scale);
    }
}
