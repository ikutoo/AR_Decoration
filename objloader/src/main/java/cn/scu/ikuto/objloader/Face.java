package cn.scu.ikuto.objloader;

import java.util.ArrayList;
import java.util.List;

public class Face {
    private final List<Integer> m_PositionIndices = new ArrayList<>();
    private final List<Integer> m_NormalIndices = new ArrayList<>();
    private final List<Integer> m_TexCoordIndices = new ArrayList<>();
    private int m_NumVertices;

    public Integer getNumVertices() {
        return this.m_NumVertices;
    }

    public void setNumVertices(int vNum) {
        this.m_NumVertices = vNum;
    }

    public void addPositionIndex(Integer vIndex) {
        this.m_PositionIndices.add(vIndex);
    }

    public void addNormalIndex(Integer vIndex) {
        this.m_NormalIndices.add(vIndex);
    }

    public void addTexCoordIndex(Integer vIndex) {
        this.m_TexCoordIndices.add(vIndex);
    }

    public Integer getPositionIndexAt(int vIndex) {
        return this.m_PositionIndices.get(vIndex);
    }

    public Integer getNormalIndexAt(int vIndex) {
        return this.m_NormalIndices.get(vIndex);
    }

    public Integer getTexCoordIndexAt(int vIndex) {
        return this.m_TexCoordIndices.get(vIndex);
    }
}
