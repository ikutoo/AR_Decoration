package com.ar_decoration.objRenderer;

public class Texture {
    private int m_ID;
    private String m_Type;

    public Texture(int vID, String vType) {
        this.m_ID = vID;
        this.m_Type = vType;
    }

    public int getID() {
        return m_ID;
    }

    public String getType() {
        return m_Type;
    }
}

