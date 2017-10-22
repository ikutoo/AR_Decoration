package com.ar_decoration.component;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;

public class ImageViewDragShadowBuilder extends View.DragShadowBuilder {

    private static Drawable m_Shadow;

    public ImageViewDragShadowBuilder(View vView, Drawable vDrawable) {
        super(vView);

        m_Shadow = vDrawable.getConstantState().newDrawable();
    }

    @Override
    public void onProvideShadowMetrics(Point size, Point touch) {
        int width, height;
        width = getView().getWidth() / 2;
        height = getView().getHeight() / 2;

        m_Shadow.setBounds(0, 0, width, height);
        size.set(width, height);
        touch.set(width / 2, height / 2);
    }

    @Override
    public void onDrawShadow(Canvas canvas) {
        m_Shadow.draw(canvas);
    }
}