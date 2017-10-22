package cn.scu.ikuto.objloader.util;

public class Vec2 {
    public float x = 0.0f;
    public float y = 0.0f;

    public Vec2(float vX, float vY) {
        this.x = vX;
        this.y = vY;
    }

    @Override
    public boolean equals(Object vObj) {
        Vec2 v = (Vec2) vObj;
        return v.x == x && v.y == y;
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        return result;
    }
}