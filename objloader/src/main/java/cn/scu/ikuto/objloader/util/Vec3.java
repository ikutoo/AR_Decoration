package cn.scu.ikuto.objloader.util;

public class Vec3 {
    public float x = 0.0f;
    public float y = 0.0f;
    public float z = 0.0f;

    public Vec3(float vX, float vY, float vZ) {
        this.x = vX;
        this.y = vY;
        this.z = vZ;
    }

    @Override
    public boolean equals(Object vObj) {
        Vec3 v = (Vec3) vObj;
        return v.x == x && v.y == y && v.z == z;
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (z != +0.0f ? Float.floatToIntBits(z) : 0);
        return result;
    }
}