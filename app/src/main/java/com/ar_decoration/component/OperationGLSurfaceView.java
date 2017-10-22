package com.ar_decoration.component;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import com.ar_decoration.activities.OperationActivity;
import com.ar_decoration.component.Types.DataType;
import com.ar_decoration.component.Types.ControlMode;

public class OperationGLSurfaceView extends GLSurfaceView {
    private static final float TOUCH_ANGLE_SCALE_FACTOR = 180.0f / 320;
    private static final float TOUCH_MOVE_SCALE_FACTOR = 100.0f;
    private static final String MODEL_BASE_PATH = "models/";

    private float m_PreX;
    private float m_PreY;
    private float m_PreDistance;

    private ItemRenderer m_Renderer;

    public OperationGLSurfaceView(Context vContext) {
        super(vContext);
        final ActivityManager activityManager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs3 = configurationInfo.reqGlEsVersion >= 0x30000;

        setZOrderOnTop(true);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);

        if (supportsEs3) {
            m_Renderer = new ItemRenderer(vContext);
            setEGLContextClientVersion(3);
            setRenderer(m_Renderer);
            setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        }
        this.setOnDragListener(new GLViewOnDragListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent vEvent) {
        float DistanceY = vEvent.getY() - m_PreY;
        float DistanceX = vEvent.getX() - m_PreX;

        if (OperationActivity.controlMode == ControlMode.MODE_TRANSLATE) {
            switch (vEvent.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    m_Renderer.setTranslateY(m_Renderer.getTranslateY() + DistanceY / TOUCH_MOVE_SCALE_FACTOR);
                    m_Renderer.setTranslateX(m_Renderer.getTranslateX() + DistanceX / TOUCH_MOVE_SCALE_FACTOR);
                    requestRender();
                    break;
            }
            m_PreX = vEvent.getX();
            m_PreY = vEvent.getY();

            return true;
        }

        if (OperationActivity.controlMode == ControlMode.MODE_ROTATE) {
            switch (vEvent.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    m_Renderer.setRotateXAngle(m_Renderer.getRotateXAngle() + DistanceX * TOUCH_ANGLE_SCALE_FACTOR);
                    m_Renderer.setRotateYAngle(m_Renderer.getRotateYAngle() + DistanceY * TOUCH_ANGLE_SCALE_FACTOR);
                    requestRender();
                    break;
            }
            m_PreX = vEvent.getX();
            m_PreY = vEvent.getY();

            return true;
        }

        if (OperationActivity.controlMode == ControlMode.MODE_SCALE) {
            if (vEvent.getAction() == MotionEvent.ACTION_MOVE) {
                if (vEvent.getPointerCount() == 2) {
                    float x = vEvent.getX(0) - vEvent.getX(1);
                    float y = vEvent.getY(0) - vEvent.getY(1);
                    float distance = (float) Math.sqrt(x * x + y * y);
                    if (m_PreDistance == 0) {
                        m_PreDistance = distance;
                    }
                    if (distance - m_PreDistance >= 10 || distance - m_PreDistance <= -10) {
                        float scale = distance / m_PreDistance;
                        m_Renderer.setScale(scale);
                        // m_PreDistance = distance;
                        requestRender();
                    }
                }
            }
            return true;
        }

        return false;
    }

    class GLViewOnDragListener implements View.OnDragListener {

        public boolean onDrag(View vView, DragEvent vEvent) {
            final int action = vEvent.getAction();
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    if (vEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        vView.invalidate();
                        return true;
                    } else {
                        return false;
                    }

                case DragEvent.ACTION_DRAG_ENTERED:
                    vView.invalidate();
                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:
                    vView.invalidate();
                    return true;

                case DragEvent.ACTION_DROP:
                    String label = vEvent.getClipData().getDescription().getLabel().toString();
                    if (label.equals(DataType.MODEL_DATA.toString())) {
                        ClipData.Item item = vEvent.getClipData().getItemAt(0);
                        String modelPath = MODEL_BASE_PATH + item.getText().toString();
                        float x = vEvent.getX();
                        float y = vEvent.getY();
                        Log.e("sxf", "ModelPath = " + modelPath);
                        Log.e("sxf", "Location = " + x + "," + y);
                        m_Renderer.loadModel(modelPath, x, y);
                        vView.invalidate();
                        return true;
                    } else {
                        return false;
                    }
            }
            return false;
        }
    }
}
