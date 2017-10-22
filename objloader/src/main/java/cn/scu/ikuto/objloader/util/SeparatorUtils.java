package cn.scu.ikuto.objloader.util;

import java.util.Properties;

public class SeparatorUtils {
    static final Properties PROPERTIES = new Properties(System.getProperties());

    public static String getFileSeparator() {
        return PROPERTIES.getProperty("file.separator");
    }
}
