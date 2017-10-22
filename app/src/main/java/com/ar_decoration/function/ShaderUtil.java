package com.ar_decoration.function;

import android.opengl.GLES30;
import android.util.Log;


public class ShaderUtil {
    private static final String TAG = "ShaderUtil";

    public static int compileShader(final int vShaderType, final String vShaderSource) {
        int ShaderHandle = GLES30.glCreateShader(vShaderType);

        if (ShaderHandle != 0) {
            GLES30.glShaderSource(ShaderHandle, vShaderSource);
            GLES30.glCompileShader(ShaderHandle);

            final int[] CompileStatus = new int[1];
            GLES30.glGetShaderiv(ShaderHandle, GLES30.GL_COMPILE_STATUS, CompileStatus, 0);

            if (CompileStatus[0] == 0) {
                Log.e(TAG, "Error compiling shader: " + GLES30.glGetShaderInfoLog(ShaderHandle));
                GLES30.glDeleteShader(ShaderHandle);
                ShaderHandle = 0;
            }
        }
        if (ShaderHandle == 0) {
            Log.e(TAG, "create shader failed.");
            throw new RuntimeException("Error creating shader.");
        }

        return ShaderHandle;
    }

    public static int createAndLinkProgram(final int vVertexShaderHandle, final int vFragmentShaderHandle) {
        int ProgramHandle = GLES30.glCreateProgram();

        if (ProgramHandle != 0) {
            GLES30.glAttachShader(ProgramHandle, vVertexShaderHandle);
            GLES30.glAttachShader(ProgramHandle, vFragmentShaderHandle);

            GLES30.glLinkProgram(ProgramHandle);
            final int[] linkStatus = new int[1];
            GLES30.glGetProgramiv(ProgramHandle, GLES30.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] == 0) {
                Log.e(TAG, "Error compiling program: " + GLES30.glGetProgramInfoLog(ProgramHandle));
                GLES30.glDeleteProgram(ProgramHandle);
                ProgramHandle = 0;
            }
        }

        if (ProgramHandle == 0) {
            throw new RuntimeException("Error creating program.");
        }

        return ProgramHandle;
    }
}
