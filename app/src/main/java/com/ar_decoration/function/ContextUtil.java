package com.ar_decoration.function;

import android.content.Context;

public class ContextUtil {
    private static Context APP_CONTEXT;
    private static ContextUtil CONTEXT_UTIL;
    private ContextUtil(){}
    public static ContextUtil getInstance(){
        if(CONTEXT_UTIL == null) CONTEXT_UTIL = new ContextUtil();
        return CONTEXT_UTIL;
    }

    public Context getContext(){
        return APP_CONTEXT;
    }

    public void setContext(Context vContext){
        APP_CONTEXT = vContext;
    }

}
