package com.ar_decoration.objRenderer;

import android.content.Context;
import android.opengl.Matrix;
import android.util.Log;

import com.ar_decoration.function.TextureUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.scu.ikuto.objloader.Material;
import cn.scu.ikuto.objloader.OBJLoader;
import cn.scu.ikuto.objloader.RawModel;
import cn.scu.ikuto.objloader.util.SeparatorUtils;
import cn.scu.ikuto.objloader.util.Vec2;

public class Model {
    private List<Mesh> m_Meshes = new ArrayList<>();
    private List<Material> m_Materials = new ArrayList<>();
    private List<Texture> m_LoadedTextures = new ArrayList<>();
    private Context m_Context;
    private String m_ModelPath;
    private boolean m_IsMoedlLoaded = false;
    private boolean m_Operatable = false;

    private float[] m_ModelMatrix = new float[16];
    private Vec2 m_ScreenPosition;

    public Model(Context vContext, final String vFilePath) {
        m_Context = vContext;
        m_ModelPath = vFilePath;
    }

    public void load() {
        long startTime1 = System.nanoTime();
        OBJLoader objLoader = new OBJLoader();
        RawModel rawModel = objLoader.load(m_Context, m_ModelPath);
        long endTime1 = System.nanoTime();
        double time1 = (endTime1 - startTime1) * 1E-9;
        Log.e("sxf", "load raw models: " + time1 + "s");

        long startTime2 = System.nanoTime();
        for (int i = 0; i < rawModel.getNumRawMeshes(); ++i) {
            Mesh mesh = new Mesh(rawModel.getRawMeshAt(i));
            String name = rawModel.getRawMeshAt(i).getMtlName();
            mesh.setMtlName(name);
            m_Meshes.add(mesh);
        }

        for (int i = 0; i < rawModel.getNumMaterials(); ++i) {
            m_Materials.add(rawModel.getMaterialAt(i));
        }
        long endTime2 = System.nanoTime();
        double time2 = (endTime2 - startTime2) * 1E-9;
        Log.e("sxf", "process raw models: " + time2 + "s");

        long startTime3 = System.nanoTime();
        File file = new File(m_ModelPath);
        String basePath = file.getParent() + SeparatorUtils.getFileSeparator();
        for (int i = 0; i < m_Materials.size(); ++i) {
            Material mat = m_Materials.get(i);
            List<Texture> textures = new ArrayList<>();
            if (mat.getDiffuseTexname() != null) {
                textures.add(loadMaterialTexture(basePath + mat.getDiffuseTexname(), "material.texture_diffuse"));
            }
            if (mat.getSpecularTexname() != null) {
                textures.add(loadMaterialTexture(basePath + mat.getSpecularTexname(), "material.texture_specular"));
            }

            for (Texture tex : textures) {
                m_LoadedTextures.add(tex);
            }
            String name = mat.getMtlName();
            for (int k = 0; k < m_Meshes.size(); ++k) {
                if (m_Meshes.get(k).getMtlName().equals(name))
                    for (Texture tex : textures)
                        m_Meshes.get(k).addTexture(tex);
            }
        }
        long endTime3 = System.nanoTime();
        double time3 = (endTime3 - startTime3) * 1E-9;
        Log.e("sxf", "load textures: " + time3 + "s");

        m_IsMoedlLoaded = true;
    }

    public void draw(int vProgramHandle) {
        for (Mesh mesh : m_Meshes) {
            mesh.draw(vProgramHandle);
        }
    }

    public boolean isModelLoaded() {
        return m_IsMoedlLoaded;
    }

    public boolean isOperatable() {
        return m_Operatable;
    }

    public void setOperatable(boolean vOperatable) {
        this.m_Operatable = vOperatable;
    }

    public void setIdentity(int vOffset) {
        Matrix.setIdentityM(m_ModelMatrix, vOffset);
    }

    public void translate(int vOffset, float vX, float vY, float vZ) {
        Matrix.translateM(m_ModelMatrix, vOffset, vX, vY, vZ);
    }

    public void scale(int vOffset, float vX, float vY, float vZ) {
        Matrix.scaleM(m_ModelMatrix, vOffset, vX, vY, vZ);
    }

    public void rotate(int vOffset, float vA, float vX, float vY, float vZ) {
        Matrix.rotateM(m_ModelMatrix, vOffset, vA, vX, vY, vZ);
    }

    public float[] getModelMatrix() {
        return m_ModelMatrix;
    }

    public Vec2 getScreenPosition() {
        return m_ScreenPosition;
    }

    public void setScreenPosition(Vec2 vScreenPosition) {
        this.m_ScreenPosition = vScreenPosition;
    }

    private Texture loadMaterialTexture(final String vFilePath, final String vTexType) {
        Texture texture = new Texture(TextureUtil.loadTexture(m_Context, vFilePath), vTexType);
        return texture;
    }
}
