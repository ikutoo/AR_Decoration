package com.ar_decoration.function;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.IOException;

public class TextureUtil {
    public static int loadTexture(final Context vContext, final int vResourceId) {
        final int[] TextureHandle = new int[1];
        GLES30.glGenTextures(1, TextureHandle, 0);

        if (TextureHandle[0] != 0) {
            final BitmapFactory.Options Option = new BitmapFactory.Options();
            Option.inScaled = false;

            final Bitmap Bitmap = BitmapFactory.decodeResource(vContext.getResources(), vResourceId, Option);

            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, TextureHandle[0]);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_NEAREST);

            GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, Bitmap, 0);

            Bitmap.recycle();
        }

        if (TextureHandle[0] == 0) {
            Log.e("TextureUtil", "Load texture failed.");
            throw new RuntimeException("Error loading texture.");
        }

        return TextureHandle[0];
    }

    public static int loadTexture(final Context vContext, final String vFilePath) {
        final int[] TextureHandle = new int[1];
        GLES30.glGenTextures(1, TextureHandle, 0);

        if (TextureHandle[0] != 0) {
            final BitmapFactory.Options Option = new BitmapFactory.Options();
            Option.inScaled = false;

            final Bitmap Bitmap;
            try {
                Bitmap = BitmapFactory.decodeStream(vContext.getAssets().open(vFilePath));
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }

            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, TextureHandle[0]);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_NEAREST);

            GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, Bitmap, 0);

            Bitmap.recycle();
        }

        if (TextureHandle[0] == 0) {
            Log.e("TextureUtil", "Load texture failed.");
            throw new RuntimeException("Error loading texture.");
        }

        return TextureHandle[0];
    }
}
