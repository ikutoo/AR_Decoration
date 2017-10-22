package cn.scu.ikuto.objloader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.scu.ikuto.objloader.util.LineReader;

public class OBJParser {
    private int m_TotalPositionsCount = 0;
    private int m_TotalNormalsCount = 0;
    private int m_TotalTexCoordsCount = 0;

    private RawMesh m_CurrentRawMesh;
    private List<RawMesh> m_RawMeshes = new ArrayList<>();
    private String m_MtlFileName;

    public void parseStream(InputStream vInStream) {
        LineReader lineReader = new LineReader(vInStream);

        String line;
        while ((line = lineReader.readLine()) != null) {
            if (line.startsWith("mtllib ")) {
                parseMtlFileName(line);
                continue;
            }

            if (line.startsWith("o ")) {
                createRawMesh();
                parseObjectName(line);
                continue;
            }

            if (line.startsWith("usemtl ")) {
                parseMtlName(line);
                continue;
            }

            if (line.startsWith("v ")) {
                parsePosition(line);
                continue;
            }

            if (line.startsWith("vn ")) {
                parseNormal(line);
                continue;
            }

            if (line.startsWith("vt ")) {
                parseTexCoord(line);
                continue;
            }

            if (line.startsWith("f ")) {
                parseFace(line);
                continue;
            }
        }
    }

    public int getNumRawMeshes() {
        return m_RawMeshes.size();
    }

    public RawMesh getRawMeshAt(int vIndex) {
        return m_RawMeshes.get(vIndex);
    }

    public String getMtlFileName() {
        return m_MtlFileName;
    }

    private void createRawMesh() {
        m_CurrentRawMesh = new RawMesh();
        m_CurrentRawMesh.setVertexOffset(
                m_TotalPositionsCount,
                m_TotalTexCoordsCount,
                m_TotalNormalsCount);
        m_RawMeshes.add(m_CurrentRawMesh);
    }

    private void parseMtlFileName(final String vLine) {
        String[] tokens = vLine.split("[ ]+");
        m_MtlFileName = tokens[1];
    }

    private void parseObjectName(final String vLine) {
        String[] tokens = vLine.split("[ ]+");
        m_CurrentRawMesh.setObjectName(tokens[1]);
    }

    private void parseMtlName(final String vLine) {
        String[] tokens = vLine.split("[ ]+");
        m_CurrentRawMesh.setMtlName(tokens[1]);
    }

    private void parsePosition(final String vLine) {
        String[] tokens = vLine.split("[ ]+");
        m_CurrentRawMesh.addPosition(
                Float.parseFloat(tokens[1]),
                Float.parseFloat(tokens[2]),
                Float.parseFloat(tokens[3]));
        m_TotalPositionsCount++;
    }

    private void parseNormal(final String vLine) {
        String[] tokens = vLine.split("[ ]+");
        m_CurrentRawMesh.addNormal(
                Float.parseFloat(tokens[1]),
                Float.parseFloat(tokens[2]),
                Float.parseFloat(tokens[3]));
        m_TotalNormalsCount++;
    }

    private void parseTexCoord(final String vLine) {
        String[] tokens = vLine.split("[ ]+");
        m_CurrentRawMesh.addTexCoord(
                Float.parseFloat(tokens[1]),
                Float.parseFloat(tokens[2]));
        m_TotalTexCoordsCount++;
    }

    private void parseFace(final String vLine) {
        String[] tokens = vLine.split("[ ]+");
        Face face = new Face();
        face.setNumVertices(tokens.length - 1);
        if (face.getNumVertices() < 3) {
            return;
        }
        for (int i = 1; i < tokens.length; ++i) {
            String token = tokens[i];
            String[] parts = token.split("/");
            face.addPositionIndex(getIndex(parts[0]));

            if (parts.length > 2 && parts[2] != "") {
                face.addNormalIndex(getIndex(parts[2]));
            }

            if (parts.length > 1 && parts[1] != "") {
                face.addTexCoordIndex(getIndex(parts[1]));
            }
        }
        m_CurrentRawMesh.addFace(face);
    }

    private int getIndex(String vStrIndex) {
        int idx = Integer.parseInt(vStrIndex);
        return idx - 1;
    }
}