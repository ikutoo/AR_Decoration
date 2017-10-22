package cn.scu.ikuto.objloader;

import java.util.ArrayList;
import java.util.List;

public class RawModel {
    private List<RawMesh> m_RawMeshes = new ArrayList<>();

    private List<Material> m_Materials = new ArrayList<>();

    public void addRawMesh(final RawMesh vRawMesh) {
        m_RawMeshes.add(vRawMesh);
    }

    public int getNumRawMeshes() {
        return m_RawMeshes.size();
    }

    public RawMesh getRawMeshAt(int vIndex) {
        return m_RawMeshes.get(vIndex);
    }

    public void addMaterial(final Material vMat) {
        m_Materials.add(vMat);
    }

    public int getNumMaterials() {
        return m_Materials.size();
    }

    public Material getMaterialAt(int vIndex) {
        return m_Materials.get(vIndex);
    }
}
