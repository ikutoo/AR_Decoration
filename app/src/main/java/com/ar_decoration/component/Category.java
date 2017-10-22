package com.ar_decoration.component;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private String m_ID;

    private String m_Name;

    private List<SubCategory> m_SubCategories = new ArrayList<>();

    public Category(String id, String name) {
        this.m_ID = id;
        this.m_Name = name;
    }

    public String getName() {
        return m_Name;
    }

    public void setName(String name) {
        this.m_Name = name;
    }

    public String getId() {
        return m_ID;
    }

    public void setId(String id) {
        this.m_ID = id;
    }

    public int getNumSubCategories() {
        return m_SubCategories.size();
    }

    public SubCategory getSubCategoryAt(int vIndex) {
        return m_SubCategories.get(vIndex);
    }

    public void addSubCategory(SubCategory vSubCategory) {
        m_SubCategories.add(vSubCategory);
    }
}
