package cn.scu.ikuto.objloader;

import cn.scu.ikuto.objloader.util.Vec2;
import cn.scu.ikuto.objloader.util.Vec3;

public class Vertex {
    private Vec3 m_Position;
    private Vec3 m_Normal;
    private Vec2 m_TexCoord;

    public Vertex(Vec3 m_Position, Vec3 m_Normal, Vec2 m_TexCoord) {
        this.m_Position = m_Position;
        this.m_Normal = m_Normal;
        this.m_TexCoord = m_TexCoord;
    }

    public Vec3 getPosition() {
        return m_Position;
    }

    public Vec3 getNormal() {
        return m_Normal;
    }

    public Vec2 getTexCoord() {
        return m_TexCoord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex vertex = (Vertex) o;

        if (!m_Position.equals(vertex.m_Position)) return false;
        if (!m_Normal.equals(vertex.m_Normal)) return false;
        return m_TexCoord.equals(vertex.m_TexCoord);

    }

    @Override
    public int hashCode() {
        int result = m_Position.hashCode();
        result = 31 * result + m_Normal.hashCode();
        result = 31 * result + m_TexCoord.hashCode();
        return result;
    }
}

