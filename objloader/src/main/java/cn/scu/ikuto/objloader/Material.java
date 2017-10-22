package cn.scu.ikuto.objloader;

import cn.scu.ikuto.objloader.util.Vec3;

public class Material {
    private String m_MtlName;

    private Vec3 m_Ambient;     //Ka
    private Vec3 m_Diffuse;     //Kd
    private Vec3 m_Specular;    //Ks

    private float m_Shininess;  //Ns
    private float m_Ior;        //Ni
    private float m_Dissolve;   //d
    private int m_Illum;        //illum

    private String m_AmbientTexname;            // map_Ka
    private String m_DiffuseTexname;            // map_Kd
    private String m_SpecularTexname;           // map_Ks
    private String m_SpecularHighlightTexname;  // map_Ns
    private String m_BumpTexname;               // map_bump, bump
    private String m_DisplacementTexname;       // disp
    private String m_AlphaTexname;              // map_d

    public String getMtlName() {
        return m_MtlName;
    }

    public void setMtlName(String vMtlName) {
        this.m_MtlName = vMtlName;
    }

    public Vec3 getAmbient() {
        return m_Ambient;
    }

    public void setAmbient(Vec3 vAmbient) {
        this.m_Ambient = vAmbient;
    }

    public Vec3 getDiffuse() {
        return m_Diffuse;
    }

    public void setDiffuse(Vec3 vDiffuse) {
        this.m_Diffuse = vDiffuse;
    }

    public Vec3 getSpecular() {
        return m_Specular;
    }

    public void setSpecular(Vec3 vSpecular) {
        this.m_Specular = vSpecular;
    }

    public float getShininess() {
        return m_Shininess;
    }

    public void setShininess(float vShininess) {
        this.m_Shininess = vShininess;
    }

    public float getIor() {
        return m_Ior;
    }

    public void setIor(float vIor) {
        this.m_Ior = vIor;
    }

    public float getDissolve() {
        return m_Dissolve;
    }

    public void setDissolve(float vDissolve) {
        this.m_Dissolve = vDissolve;
    }

    public int getIllum() {
        return m_Illum;
    }

    public void setIllum(int vIllum) {
        this.m_Illum = vIllum;
    }

    public String getAmbientTexname() {
        return m_AmbientTexname;
    }

    public void setAmbientTexname(String vAmbientTexname) {
        this.m_AmbientTexname = vAmbientTexname;
    }

    public String getDiffuseTexname() {
        return m_DiffuseTexname;
    }

    public void setDiffuseTexname(String vDiffuseTexname) {
        this.m_DiffuseTexname = vDiffuseTexname;
    }

    public String getSpecularTexname() {
        return m_SpecularTexname;
    }

    public void setSpecularTexname(String vSpecularTexname) {
        this.m_SpecularTexname = vSpecularTexname;
    }

    public String getSpecularHighlightTexname() {
        return m_SpecularHighlightTexname;
    }

    public void setSpecularHighlightTexname(String vSpecularHighlightTexname) {
        this.m_SpecularHighlightTexname = vSpecularHighlightTexname;
    }

    public String getBumpTexname() {
        return m_BumpTexname;
    }

    public void setBumpTexname(String m_BumpTexname) {
        this.m_BumpTexname = m_BumpTexname;
    }

    public String getDisplacementTexname() {
        return m_DisplacementTexname;
    }

    public void setDisplacementTexname(String vDisplacementTexname) {
        this.m_DisplacementTexname = vDisplacementTexname;
    }

    public String getAlphaTexname() {
        return m_AlphaTexname;
    }

    public void setAlphaTexname(String vAlphaTexname) {
        this.m_AlphaTexname = vAlphaTexname;
    }
}
