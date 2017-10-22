package com.ar_decoration.component;

import android.graphics.drawable.Drawable;

import com.ar_decoration.util.ApplicationHelper;

import java.io.IOException;
import java.io.InputStream;

public class SubCategory {

    private String m_CategoryName;

    private String m_Name;

    private String m_ImagePath;

    private String m_ModelPath;

    private Drawable m_Drawable;

    private static final String IMAGE_BASE_PATH = "images/preview/";

    public SubCategory(String categoryName, String name, String imagePath, String modelPath) {
        this.m_CategoryName = categoryName;
        this.m_Name = name;
        this.m_ImagePath = imagePath;
        this.m_ModelPath = modelPath;

        try {
            InputStream inputStream = ApplicationHelper.getContext().getResources().getAssets().open(IMAGE_BASE_PATH + m_ImagePath);
            m_Drawable = Drawable.createFromStream(inputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCategoryName() {
        return m_CategoryName;
    }

    public void setCategoryName(String categoryName) {
        this.m_CategoryName = categoryName;
    }

    public String getName() {
        return m_Name;
    }

    public void setName(String name) {
        this.m_Name = name;
    }

    public String getImagePath() {
        return m_ImagePath;
    }

    public void setImagePath(String imagePath) {
        this.m_ImagePath = imagePath;
    }

    public String getModelPath() {
        return m_ModelPath;
    }

    public void setModelPath(String modelPath) {
        this.m_ModelPath = modelPath;
    }

    public Drawable getDrawable() {
        return m_Drawable;
    }
}
