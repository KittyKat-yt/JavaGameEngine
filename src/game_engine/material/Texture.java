package game_engine.material;

import game_engine.math.Float2;
import game_engine.math.Float3;
import game_engine.math.Maths;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Texture {
    public final int width;
    public final int height;
    private final BufferedImage texture;
    private final Graphics2D g;
    public Texture(int width, int height) {
        this.width = width;
        this.height = height;
        this.texture = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.g = this.texture.createGraphics();

        int halfWidth = width / 2;
        int halfHeight = height / 2;
        this.fill(0x3f3f3f);
        this.fill(0, 0, halfWidth, halfHeight, 0xff00ff);
        this.fill(halfWidth, halfHeight, halfWidth, halfHeight, 0xff00ff);
    }

    public Float3 sample(Float2 uv) {
        uv = uv.modulo(1f);
        int x = Maths.clamp(0, width - 1, (int) (uv.x * width));
        int y = Maths.clamp(0, height - 1, (int) (uv.y * height));
        return Float3.fromRGB(texture.getRGB(x, y));
    }

    public void clear() {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
    }
    public void fill(int color) {
        this.fill(0, 0, width, height, color);
    }
    public void fill(int x, int y, int width, int height, int color) {
        g.setColor(new Color(color));
        g.fillRect(x, y, width, height);
    }
}
