package cn.scu.ikuto.objloader;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import cn.scu.ikuto.objloader.util.SeparatorUtils;

public class OBJLoader {
    private RawModel m_RawModel = new RawModel();

    public RawModel load(final Context vContex, final String vObjPath) {
        return load(vContex, vObjPath, AssetManager.ACCESS_STREAMING);
    }

    public RawModel load(final Context vContext, final String vObjPath, int vAccessMode) {
        InputStream objStream;
        try {
            objStream = vContext.getAssets().open(vObjPath, vAccessMode);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        OBJParser objParser = parseObjStream(objStream);

        File file = new File(vObjPath);
        String mtlPath = file.getParent() + SeparatorUtils.getFileSeparator() + objParser.getMtlFileName();
        InputStream mtlStream;
        try {
            mtlStream = vContext.getAssets().open(mtlPath, vAccessMode);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        parseMtlStream(mtlStream);

        return m_RawModel;
    }

    public RawModel loadFromStream(InputStream vObjStream, InputStream vMtlStream) {
        parseObjStream(vObjStream);
        parseMtlStream(vMtlStream);

        return m_RawModel;
    }

    private OBJParser parseObjStream(InputStream vObjStream) {
        OBJParser objParser = new OBJParser();
        objParser.parseStream(vObjStream);
        for (int i = 0; i < objParser.getNumRawMeshes(); ++i) {
            m_RawModel.addRawMesh(objParser.getRawMeshAt(i));
        }

        return objParser;
    }

    private MTLParser parseMtlStream(InputStream vMtlStream) {
        MTLParser mtlParser = new MTLParser();
        mtlParser.parseStream(vMtlStream);
        for (int i = 0; i < mtlParser.getNumMaterials(); ++i) {
            m_RawModel.addMaterial(mtlParser.getMaterialAt(i));
        }

        return mtlParser;
    }
}