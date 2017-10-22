package com.ar_decoration.objRenderer;

public class BoundingBox {
    private float m_MinX;
    private float m_MaxX;
    private float m_MinY;
    private float m_MaxY;
    private float m_MinZ;
    private float m_MaxZ;

    public BoundingBox(float vMinX, float vMaxX, float vMinY, float vMaxY, float vMinZ, float vMaxZ) {
        this.m_MinX = vMinX;
        this.m_MaxX = vMaxX;
        this.m_MinY = vMinY;
        this.m_MaxY = vMaxY;
        this.m_MinZ = vMinZ;
        this.m_MaxZ = vMaxZ;
    }

    public float getMinX() {
        return m_MinX;
    }

    public void setMinX(float vMinX) {
        this.m_MinX = vMinX;
    }

    public float getMaxX() {
        return m_MaxX;
    }

    public void setMaxX(float vMaxX) {
        this.m_MaxX = vMaxX;
    }

    public float getMinY() {
        return m_MinY;
    }

    public void setMinY(float vMinY) {
        this.m_MinY = vMinY;
    }

    public float getMaxY() {
        return m_MaxY;
    }

    public void setMaxY(float vMaxY) {
        this.m_MaxY = vMaxY;
    }

    public float getMinZ() {
        return m_MinZ;
    }

    public void setMinZ(float vMinZ) {
        this.m_MinZ = vMinZ;
    }

    public float getMaxZ() {
        return m_MaxZ;
    }

    public void setMaxZ(float vMaxZ) {
        this.m_MaxZ = vMaxZ;
    }
}
