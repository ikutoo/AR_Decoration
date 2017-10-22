package com.ar_decoration.component;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.ar_decoration.activities.R;
import com.ar_decoration.function.RawResourceReader;
import com.ar_decoration.function.ShaderUtil;
import com.ar_decoration.objRenderer.Model;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cn.scu.ikuto.objloader.util.Vec2;

public class ItemRenderer implements GLSurfaceView.Renderer {
    private float m_RotateXAngle;
    private float m_RotateYAngle;

    private float m_TranslateX;
    private float m_TranslateY;

    private float m_Scale = 1.0f;

    private float[] m_ViewMatrix = new float[16];
    private float[] m_ProjectionMatrix = new float[16];

    private int m_ProgramHandle;

    private List<Model> m_Models = new ArrayList<>();

    private Context m_ActivityContext;

    private long m_PreTime = System.nanoTime();

    ItemRenderer(Context vActivityContext) {
        m_ActivityContext = vActivityContext;
    }

    public void loadModel(String vPath, float x, float y) {
        GLES30.glUseProgram(m_ProgramHandle);
        Model model = new Model(m_ActivityContext, vPath);
        model.setScreenPosition(new Vec2(x, y));
        for (Model m : m_Models) {
            m.setOperatable(false);
        }
        model.setOperatable(true);
        m_Models.add(model);
    }

    public void setRotateYAngle(float vYAngle) {
        m_RotateYAngle = vYAngle;
    }

    public void setRotateXAngle(float vXAngle) {
        m_RotateXAngle = vXAngle;
    }

    public void setScale(float vScale) {
        m_Scale = vScale;
    }

    public float getScale() {
        return m_Scale;
    }

    public void setTranslateY(float vY) {
        m_TranslateY = vY;
    }

    public void setTranslateX(float vX) {
        m_TranslateX = vX;
    }

    public float getRotateYAngle() {
        return m_RotateYAngle;
    }

    public float getRotateXAngle() {
        return m_RotateXAngle;
    }

    public float getTranslateY() {
        return m_TranslateY;
    }

    public float getTranslateX() {
        return m_TranslateX;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES30.glEnable(GLES30.GL_CULL_FACE);
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);

        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 3.0f;

        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        Matrix.setLookAtM(m_ViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
        GLES30.glUniform3f(GLES30.glGetUniformLocation(m_ProgramHandle, "viewPos"), eyeX, eyeY, eyeZ);

        final String vertexShader = getVertexShader();
        final String fragmentShader = getFragmentShader();

        final int vertexShaderHandle = ShaderUtil.compileShader(GLES30.GL_VERTEX_SHADER, vertexShader);
        final int fragmentShaderHandle = ShaderUtil.compileShader(GLES30.GL_FRAGMENT_SHADER, fragmentShader);

        m_ProgramHandle = ShaderUtil.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle);
        GLES30.glUseProgram(m_ProgramHandle);

        GLES30.glUniform3f(GLES30.glGetUniformLocation(m_ProgramHandle, "pointLights[0].position"), 0.0f, 0.0f, 0.8f);
        GLES30.glUniform3f(GLES30.glGetUniformLocation(m_ProgramHandle, "pointLights[0].ambient"), 0.05f, 0.05f, 0.05f);
        GLES30.glUniform3f(GLES30.glGetUniformLocation(m_ProgramHandle, "pointLights[0].diffuse"), 1.0f, 1.0f, 1.0f);
        GLES30.glUniform3f(GLES30.glGetUniformLocation(m_ProgramHandle, "pointLights[0].specular"), 2.0f, 2.0f, 2.0f);
        GLES30.glUniform1f(GLES30.glGetUniformLocation(m_ProgramHandle, "pointLights[0].constant"), 1.0f);
        GLES30.glUniform1f(GLES30.glGetUniformLocation(m_ProgramHandle, "pointLights[0].linear"), 0.009f);
        GLES30.glUniform1f(GLES30.glGetUniformLocation(m_ProgramHandle, "pointLights[0].quadratic"), 0.0032f);
        GLES30.glUniform3f(GLES30.glGetUniformLocation(m_ProgramHandle, "pointLights[1].position"), 0.0f, 1.5f, 0.5f);
        GLES30.glUniform3f(GLES30.glGetUniformLocation(m_ProgramHandle, "pointLights[1].ambient"), 0.05f, 0.05f, 0.05f);
        GLES30.glUniform3f(GLES30.glGetUniformLocation(m_ProgramHandle, "pointLights[1].diffuse"), 1.0f, 1.0f, 1.0f);
        GLES30.glUniform3f(GLES30.glGetUniformLocation(m_ProgramHandle, "pointLights[1].specular"), 2.0f, 2.0f, 2.0f);
        GLES30.glUniform1f(GLES30.glGetUniformLocation(m_ProgramHandle, "pointLights[1].constant"), 1.0f);
        GLES30.glUniform1f(GLES30.glGetUniformLocation(m_ProgramHandle, "pointLights[1].linear"), 0.009f);
        GLES30.glUniform1f(GLES30.glGetUniformLocation(m_ProgramHandle, "pointLights[1].quadratic"), 0.0032f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int vWidth, int vHeight) {
        GLES30.glViewport(0, 0, vWidth, vHeight);

        final float ratio = (float) vWidth / vHeight;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 10.0f;

        Matrix.frustumM(m_ProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        double dTime = (System.nanoTime() - m_PreTime) * 1E-9;
        m_PreTime = System.nanoTime();
        double fps = 1.0d / dTime;
        //Log.e("sxf", "fps = " + fps);

        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);

        GLES30.glUseProgram(m_ProgramHandle);

        for (Model model : m_Models) {
            if (!model.isModelLoaded()) {
                model.load();
            }
            if (model.isOperatable()) {
                model.setIdentity(0);
                model.scale(0, 0.9f, 0.9f, 0.9f);
                model.scale(0, m_Scale, m_Scale, m_Scale);
                model.translate(0, 0.0f, -1.5f, 0.0f);
                model.translate(0, m_TranslateX, 0.0f, 0.0f);
                model.translate(0, 0.0f, -m_TranslateY, 0.0f);
                //model.translate(0, model.getScreenPosition().x / 1000, model.getScreenPosition().y / 1000, 0.0f);
                model.rotate(0, m_RotateXAngle, 0.0f, 1.0f, 0.0f);
                model.rotate(0, m_RotateYAngle, 1.0f, 0.0f, 0.0f);
                model.rotate(0, angleInDegrees, 0.0f, 1.0f, 0.0f);
            }

            GLES30.glUniformMatrix4fv(GLES30.glGetUniformLocation(m_ProgramHandle, "view"), 1, false, m_ViewMatrix, 0);
            GLES30.glUniformMatrix4fv(GLES30.glGetUniformLocation(m_ProgramHandle, "projection"), 1, false, m_ProjectionMatrix, 0);
            GLES30.glUniformMatrix4fv(GLES30.glGetUniformLocation(m_ProgramHandle, "model"), 1, false, model.getModelMatrix(), 0);
            model.draw(m_ProgramHandle);
        }

        GLES30.glFlush();
    }

    protected String getVertexShader() {
        return RawResourceReader.readTextFileFromRawResource(m_ActivityContext, R.raw.cube_vertex_shader);
    }

    protected String getFragmentShader() {
        return RawResourceReader.readTextFileFromRawResource(m_ActivityContext, R.raw.cube_fragment_shader);
    }
}
