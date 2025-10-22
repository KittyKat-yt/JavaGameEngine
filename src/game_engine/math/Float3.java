package game_engine.math;

public class Float3 {
    public float x;
    public float y;
    public float z;

    public Float3() {
        this.x = 0f;
        this.y = 0f;
        this.z = 0f;
    }
    public Float3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Float3 fromRGB(int rgb) {
        Float3 color = new Float3();
        color.x = ((rgb & 0xff0000) >> 16) / 255f;
        color.y = ((rgb & 0x00ff00) >> 8) / 255f;
        color.z = (rgb & 0x0000ff) / 255f;
        return color;
    }
    public static Float3 fromHSV(float hue, float saturation, float value) {
        hue = (hue % 360 + 360) % 360;
        saturation /= 100f;
        value /= 100f;

        float c = value * saturation;
        float x = c * (1 - Math.abs((hue / 60f) % 2 - 1));
        float m = value - c;

        float rPrime, gPrime, bPrime;

        if (hue < 60) {
            rPrime = c; gPrime = x; bPrime = 0;
        } else if (hue < 120) {
            rPrime = x; gPrime = c; bPrime = 0;
        } else if (hue < 180) {
            rPrime = 0; gPrime = c; bPrime = x;
        } else if (hue < 240) {
            rPrime = 0; gPrime = x; bPrime = c;
        } else if (hue < 300) {
            rPrime = x; gPrime = 0; bPrime = c;
        } else {
            rPrime = c; gPrime = 0; bPrime = x;
        }

        Float3 color = new Float3();
        color.x = rPrime + m;
        color.y = gPrime + m;
        color.z = bPrime + m;
        return color;
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public void offset(float dx, float dy, float dz) {
        x += dx;
        y += dy;
        z += dz;
    }
    public void offset(Float3 f3) {
        this.offset(f3.x, f3.y, f3.z);
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Float3 add(Float3 f3) {
        return new Float3(x + f3.x, y + f3.y, z + f3.z);
    }
    public Float3 sub(Float3 f3) {
        return new Float3(x - f3.x, y - f3.y, z - f3.z);
    }
    public Float3 multiply(Float3 f3) {
        return new Float3(x * f3.x, y * f3.y, z * f3.z);
    }
    public Float3 scale(double s) {
        return new Float3((float) (x * s), (float) (y * s), (float) (z * s));
    }
    public Float3 modulo(float v) {
        return new Float3(x % v, y % v, z % v);
    }
    public Float3 inverse() {
        return new Float3(1f / x, 1f / y, 1f / z);
    }
    public Float3 normalize() {
        double length = this.length();
        return (length == 0d) ? new Float3() : this.scale(length);
    }

    public Float3 lerp(Float3 f3, double t) {
        return this.add(f3.sub(this).scale(t));
    }
    public float dotProduct(Float3 f3) {
        return (x * f3.x) + (y * f3.y) + (z * f3.z);
    }

    public Float2 to2D() {
        return new Float2(x, y);
    }
    public int getColor() {
        int r = (int) (x * 255);
        int g = (int) (y * 255);
        int b = (int) (z * 255);
        return ((r << 16) | (g << 8) | b);
    }

    @Override
    public String toString() {
        return String.format("%f : %f : %f", x, y, z);
    }
}
