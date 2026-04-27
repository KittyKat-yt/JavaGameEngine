package main.game_engine.script;

import main.game_engine.math.Float3;
import main.game_engine.scene.GameObject;

public class RotationScript extends Script {
    @Override
    public void update(GameObject obj) {
        Float3 rot = new Float3(50f, 100f, 25f);
        obj.transform.rotate(rot.scale(deltaTime));
    }
}
