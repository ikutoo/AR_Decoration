package cn.scu.ikuto.objloader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.scu.ikuto.objloader.util.LineReader;
import cn.scu.ikuto.objloader.util.Vec3;

public class MTLParser {
    private Material m_CurrentMaterial;
    private List<Material> m_Materials = new ArrayList<>();

    public void parseStream(InputStream vInStream) {
        LineReader lineReader = new LineReader(vInStream);

        String line;
        while ((line = lineReader.readLine()) != null) {
            if (line.startsWith("newmtl ")) {
                createMaterial();
                m_CurrentMaterial.setMtlName(getStringFromLine(line));
            }

            if (line.startsWith("Ka ")) {
                m_CurrentMaterial.setAmbient(getVec3FromLine(line));
            }

            if (line.startsWith("Kd ")) {
                m_CurrentMaterial.setDiffuse(getVec3FromLine(line));
            }

            if (line.startsWith("Ks ")) {
                m_CurrentMaterial.setSpecular(getVec3FromLine(line));
            }

            if (line.startsWith("Ns ")) {
                m_CurrentMaterial.setShininess(getFloatFromLine(line));
            }

            if (line.startsWith("Ni ")) {
                m_CurrentMaterial.setIor(getFloatFromLine(line));
            }

            if (line.startsWith("d ")) {
                m_CurrentMaterial.setDissolve(getFloatFromLine(line));
            }

            if (line.startsWith("illum ")) {
                m_CurrentMaterial.setIllum(getIntFromLine(line));
            }

            if (line.startsWith("map_Ka ")) {
                m_CurrentMaterial.setAmbientTexname(getStringFromLine(line));
            }

            if (line.startsWith("map_Kd ")) {
                m_CurrentMaterial.setDiffuseTexname(getStringFromLine(line));
            }

            if (line.startsWith("map_Ks ")) {
                m_CurrentMaterial.setSpecularTexname(getStringFromLine(line));
            }

            if (line.startsWith("map_Ns ")) {
                m_CurrentMaterial.setSpecularHighlightTexname(getStringFromLine(line));
            }

            if (line.startsWith("map_Bump ") || line.startsWith("bump ")) {
                m_CurrentMaterial.setBumpTexname(getStringFromLine(line));
            }

            if (line.startsWith("disp ")) {
                m_CurrentMaterial.setDisplacementTexname(getStringFromLine(line));
            }

            if (line.startsWith("map_d ")) {
                m_CurrentMaterial.setAlphaTexname(getStringFromLine(line));
            }
        }
    }

    public int getNumMaterials() {
        return m_Materials.size();
    }

    public Material getMaterialAt(int vIndex) {
        return m_Materials.get(vIndex);
    }

    private void createMaterial() {
        m_CurrentMaterial = new Material();
        m_Materials.add(m_CurrentMaterial);
    }

    private String getStringFromLine(final String vLine) {
        String[] tokens = vLine.split("[ ]+");
        return tokens[1];
    }

    private float getFloatFromLine(final String vLine) {
        String[] tokens = vLine.split("[ ]+");
        return Float.parseFloat(tokens[1]);
    }

    private int getIntFromLine(final String vLine) {
        String[] tokens = vLine.split("[ ]+");
        return Integer.parseInt(tokens[1]);
    }

    private Vec3 getVec3FromLine(final String vLine) {
        String[] tokens = vLine.split("[ ]+");
        return new Vec3(
                Float.parseFloat(tokens[1]),
                Float.parseFloat(tokens[2]),
                Float.parseFloat(tokens[3])
        );
    }
}
