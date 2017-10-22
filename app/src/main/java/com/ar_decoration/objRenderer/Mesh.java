package com.ar_decoration.objRenderer;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.scu.ikuto.objloader.Face;
import cn.scu.ikuto.objloader.RawMesh;
import cn.scu.ikuto.objloader.Vertex;
import cn.scu.ikuto.objloader.util.Vec2;
import cn.scu.ikuto.objloader.util.Vec3;

public class Mesh {
    private List<Vertex> m_Vertices = new ArrayList<>();
    private List<Integer> m_Indices = new ArrayList<>();
    private List<Texture> m_Textures = new ArrayList<>();

    private FloatBuffer m_VertexBuffer;
    private IntBuffer m_IndexBuffer;

    private String m_MtlName;

    private int[] m_VBO = new int[1];
    private int[] m_IBO = new int[1];

    private static final int BYTES_PER_FLOAT = 4;
    private static final int BYTES_PER_Int = 4;
    private static final int POSITION_DATA_SIZE = 3;
    private static final int POSITION_OFFSET = 0;
    private static final int NORMAL_DATA_SIZE = 3;
    private static final int NORMAL_OFFSET = POSITION_OFFSET + POSITION_DATA_SIZE * BYTES_PER_FLOAT;
    private static final int TEXCOORD_DATA_SIZE = 2;
    private static final int TEXCOORD_OFFSET = NORMAL_OFFSET + NORMAL_DATA_SIZE * BYTES_PER_FLOAT;
    private static final int VERTEX_SIZE = POSITION_DATA_SIZE + NORMAL_DATA_SIZE + TEXCOORD_DATA_SIZE;

    Mesh(RawMesh vRawMesh) {
        setUpMeshData(vRawMesh);
        GLES30.glGenBuffers(1, m_VBO, 0);
        GLES30.glGenBuffers(1, m_IBO, 0);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, m_VBO[0]);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, m_VertexBuffer.capacity() * BYTES_PER_FLOAT,
                m_VertexBuffer, GLES30.GL_STATIC_DRAW);

        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, m_IBO[0]);
        GLES30.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER, m_IndexBuffer.capacity()
                * BYTES_PER_Int, m_IndexBuffer, GLES30.GL_STATIC_DRAW);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public String getMtlName() {
        return m_MtlName;
    }

    public void setMtlName(String vMtlName) {
        this.m_MtlName = vMtlName;
    }

    public void addTexture(Texture vTexture) {
        m_Textures.add(vTexture);
    }

    public void draw(int vProgramHandle) {
        bindTextures(vProgramHandle);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, m_VBO[0]);
        GLES30.glEnableVertexAttribArray(0);
        GLES30.glVertexAttribPointer(0, POSITION_DATA_SIZE, GLES30.GL_FLOAT, false, VERTEX_SIZE * BYTES_PER_FLOAT, POSITION_OFFSET);

        GLES30.glEnableVertexAttribArray(1);
        GLES30.glVertexAttribPointer(1, NORMAL_DATA_SIZE, GLES30.GL_FLOAT, false, VERTEX_SIZE * BYTES_PER_FLOAT, NORMAL_OFFSET);

        GLES30.glEnableVertexAttribArray(2);
        GLES30.glVertexAttribPointer(2, TEXCOORD_DATA_SIZE, GLES30.GL_FLOAT, false, VERTEX_SIZE * BYTES_PER_FLOAT, TEXCOORD_OFFSET);

        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, m_IBO[0]);
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, m_Indices.size(), GLES30.GL_UNSIGNED_INT, 0);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, 0);
        for (int i = 0; i < m_Textures.size(); ++i) {
            GLES30.glActiveTexture(GLES30.GL_TEXTURE0 + i);
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        }
    }

    private void setUpMeshData(RawMesh vRawMesh) {
        HashMap<Vertex, Integer> uniqueVertices = new HashMap<>();
        for (int i = 0; i < vRawMesh.getNumFaces(); ++i) {
            Face face = vRawMesh.getFaceAt(i);
            int offsetPosition = vRawMesh.getOffsetPositionIndex();
            int offsetTexCoord = vRawMesh.getOffsetTexCoordIndex();
            int offsetNormal = vRawMesh.getOffsetNormalIndex();
            for (int k = 0; k < face.getNumVertices(); ++k) {
                Vertex vertex = new Vertex(
                        vRawMesh.getPositionAt(face.getPositionIndexAt(k) - offsetPosition),
                        vRawMesh.getNormalAt(face.getNormalIndexAt(k) - offsetNormal),
                        vRawMesh.getTexCoordAt(face.getTexCoordIndexAt(k) - offsetTexCoord)
                );
                if (!uniqueVertices.containsKey(vertex)) {
                    uniqueVertices.put(vertex, m_Vertices.size());
                    m_Vertices.add(vertex);
                }
                m_Indices.add(uniqueVertices.get(vertex));
            }
        }
        float verticesData[] = new float[m_Vertices.size() * VERTEX_SIZE];
        for (int i = 0; i < m_Vertices.size(); ++i) {
            int step = VERTEX_SIZE, offset = 0;
            Vec3 position = m_Vertices.get(i).getPosition();
            verticesData[step * i + (offset++)] = position.x;
            verticesData[step * i + (offset++)] = position.y;
            verticesData[step * i + (offset++)] = position.z;

            Vec3 normal = m_Vertices.get(i).getNormal();
            verticesData[step * i + (offset++)] = normal.x;
            verticesData[step * i + (offset++)] = normal.y;
            verticesData[step * i + (offset++)] = normal.z;

            Vec2 texCoord = m_Vertices.get(i).getTexCoord();
            verticesData[step * i + (offset++)] = texCoord.x;
            verticesData[step * i + (offset++)] = 1 - texCoord.y;
        }
        int indicesData[] = new int[m_Indices.size()];
        for (int i = 0; i < m_Indices.size(); ++i) {
            indicesData[i] = m_Indices.get(i);
        }

        m_VertexBuffer = ByteBuffer.allocateDirect(verticesData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        m_IndexBuffer = ByteBuffer.allocateDirect(indicesData.length * BYTES_PER_Int)
                .order(ByteOrder.nativeOrder()).asIntBuffer();

        m_VertexBuffer.put(verticesData).position(0);
        m_IndexBuffer.put(indicesData).position(0);
    }

    private void bindTextures(int vProgramHandle) {
        for (int i = 0; i < m_Textures.size(); ++i) {
            int diffuseNr = 1;
            int specularNr = 1;
            GLES30.glActiveTexture(GLES30.GL_TEXTURE0 + i);
            String name = m_Textures.get(i).getType();
            if (name.equals("material.texture_diffuse")) {
                name += diffuseNr;
            } else if (name.equals("material.texture_specular")) {
                name += specularNr;
            }
            GLES30.glUniform1i(GLES30.glGetUniformLocation(vProgramHandle, name), i);
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, m_Textures.get(i).getID());
        }
        GLES30.glUniform1f(GLES30.glGetUniformLocation(vProgramHandle, "material.shininess"), 1.0f);
    }
}