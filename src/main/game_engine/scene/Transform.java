package main.game_engine.scene;

import main.game_engine.math.Float3;
import main.game_engine.math.Maths;

public class Transform {
    public Float3 pos;
    public Float3 rot;
    public Float3[] basisVectors;
    public Float3[] invBasisVectors;
    public Float3 scale;

    public Transform(float xP, float yP, float zP, float pR, float yR, float rR, float xS, float yS, float zS) {
        this.pos = new Float3(xP, yP, zP);
        this.rot = new Float3(pR, yR, rR);
        this.scale = new Float3(xS, yS, zS);
        updateRotation();
    }

    public void move(float dx, float dy, float dz) {
        pos.offset(dx, dy, dz);
    }
    public void move(Float3 f3) {
        pos.offset(f3);
    }
    public void rotate(float dp, float dy, float dr) {
        rot.offset(dp, dy, dr);
        updateRotation();
    }
    public void rotate(Float3 f3) {
        rot.offset(f3);
        updateRotation();
    }

    public void updateRotation() {
        rot = rot.modulo(360f);
        basisVectors = Maths.getBasisVectors(rot);
        invBasisVectors = Maths.getInverseBasisVectors(basisVectors);
    }
}
