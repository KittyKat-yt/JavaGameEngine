package main.game_engine.script;

import main.game_engine.math.Float2;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public abstract class Input {
    private static Float2 mouseDelta = new Float2();

    public static Float2 getMouseDelta() {
        return mouseDelta;
    }

    public static void moveMouse(float dx, float dy) {
        mouseDelta.offset(dx, dy);
    }


    private static final Set<Integer> lastKeys = new HashSet<>();
    private static final Set<Integer> pressedKeys = new HashSet<>();

    public static boolean keyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }
    public static boolean keyDown(int keyCode) {
        return keyPressed(keyCode) && (!lastKeys.contains(keyCode));
    }
    public static boolean keyUp(int keyCode) {
        return (!keyPressed(keyCode)) && lastKeys.contains(keyCode);
    }

    public static Float2 movementInput() {
        float forward = 0f;
        forward += (keyPressed(KeyEvent.VK_W) ? 1f : 0f);
        forward -= (keyPressed(KeyEvent.VK_S) ? 1f : 0f);
        float side = 0f;
        side += (keyPressed(KeyEvent.VK_A) ? 1f : 0f);
        side -= (keyPressed(KeyEvent.VK_D) ? 1f : 0f);
        return new Float2(side, forward).normalize();
    }

    public static void pressKey(int key) {
        pressedKeys.add(key);
    }
    public static void releaseKey(int key) {
        pressedKeys.remove(key);
    }


    public static void updateInput() {
        lastKeys.clear();
        lastKeys.addAll(pressedKeys);
        mouseDelta = new Float2();
    }
}
