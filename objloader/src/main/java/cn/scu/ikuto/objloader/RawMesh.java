package cn.scu.ikuto.objloader;

import java.util.ArrayList;
import java.util.List;

import cn.scu.ikuto.objloader.util.Vec2;
import cn.scu.ikuto.objloader.util.Vec3;

public class RawMesh {

    private String m_ObjectName;
    private String m_MtlName;

    private int m_OffsetPositonIndex;
    private int m_OffsetTexCoordIndex;
    private int m_OffsetNormalIndex;

    private final List<Vec3> m_Positions = new ArrayList<>();
    private final List<Vec3> m_Normals = new ArrayList<>();
    private final List<Vec2> m_TexCoords = new ArrayList<>();
    private final List<Face> m_Faces = new ArrayList<>();

    public void setObjectName(String vName) {
        m_ObjectName = vName;
    }

    public String getObjectName() {
        return m_ObjectName;
    }

    public String getMtlName() {
        return m_MtlName;
    }

    public void setMtlName(String vMtlName) {
        this.m_MtlName = vMtlName;
    }

    public void setVertexOffset(int vPosition, int vTexCoord, int vNormal) {
        this.m_OffsetPositonIndex = vPosition;
        this.m_OffsetTexCoordIndex = vTexCoord;
        this.m_OffsetNormalIndex = vNormal;
    }

    public int getOffsetPositionIndex() {
        return this.m_OffsetPositonIndex;
    }

    public int getOffsetTexCoordIndex() {
        return this.m_OffsetTexCoordIndex;
    }

    public int getOffsetNormalIndex() {
        return this.m_OffsetNormalIndex;
    }

    public void addPosition(float vX, float vY, float vZ) {
        m_Positions.add(new Vec3(vX, vY, vZ));
    }

    public void addNormal(float vX, float vY, float vZ) {
        m_Normals.add(new Vec3(vX, vY, vZ));
    }

    public void addTexCoord(float vX, float vY) {
        m_TexCoords.add(new Vec2(vX, vY));
    }

    public void addFace(Face vFace) {
        m_Faces.add(vFace);
    }

    public int getNumPositions() {
        return m_Positions.size();
    }

    public int getNumNormals() {
        return m_Normals.size();
    }

    public int getNumTexCoords() {
        return m_TexCoords.size();
    }

    public int getNumFaces() {
        return m_Faces.size();
    }

    public Vec3 getPositionAt(int vIndex) {
        return m_Positions.get(vIndex);
    }

    public Vec3 getNormalAt(int vIndex) {
        return m_Normals.get(vIndex);
    }

    public Vec2 getTexCoordAt(int vIndex) {
        return m_TexCoords.get(vIndex);
    }

    public Face getFaceAt(int vIndex) {
        return m_Faces.get(vIndex);
    }
}

