package com.ar_decoration.util;

import android.app.Application;
import android.content.Context;

import com.ar_decoration.function.ContextUtil;

public class ApplicationHelper extends Application {

    private static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = getApplicationContext();

        ContextUtil.getInstance().setContext(getApplicationContext());
    }

    public static Context getContext() {
        return instance;
    }
}