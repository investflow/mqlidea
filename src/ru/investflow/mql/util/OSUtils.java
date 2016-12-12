package ru.investflow.mql.util;

public class OSUtils {

    public static boolean isWindowsOS() {
        String prop = System.getProperty("os.name");
        return prop == null || prop.startsWith("Windows");
    }
}
