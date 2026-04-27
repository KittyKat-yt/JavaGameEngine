package main.game_engine.script;

import main.game_engine.scene.GameObject;

public abstract class Script {
    private static long lastFrameNanoTime = -1L;
    public static double deltaTime = 0d;
    public static double time = 0d;
    public static double fps = 0d;
    public static double fpsMin = Float.MAX_VALUE;
    public static double fpsMax = Float.MIN_VALUE;
    public static void updateDeltaTime() {
        long currentTime = System.nanoTime();
        if (lastFrameNanoTime != -1L) {
            deltaTime = (currentTime - lastFrameNanoTime) / 1_000_000_000d;
            time += deltaTime;

            fps = 1f / deltaTime;
            if (fps < fpsMin) fpsMin = fps;
            if (fps > fpsMax) fpsMax = fps;
        }
        lastFrameNanoTime = currentTime;
    }
    public static void resetStats() {
        fpsMin = Float.MAX_VALUE;
        fpsMax = Float.MIN_VALUE;
    }

    public abstract void update(GameObject obj);
}
