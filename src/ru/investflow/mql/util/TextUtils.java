package ru.investflow.mql.util;

import com.intellij.xml.util.XmlUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Plain text related utils.
 */
public class TextUtils {

    @Contract("null -> true")
    public static boolean isEmpty(@Nullable String v) {
        return v == null || v.isEmpty();
    }

    @Contract("null,_ -> null")
    public static String limit(@Nullable String t, int maxLen) {
        if (t == null || t.length() <= maxLen) {
            return t;
        }
        return t.substring(0, maxLen);
    }

    @Contract("null,_ -> null")
    public static String abbreviate(@Nullable String text, int maxLen) {
        if (text == null || text.length() <= maxLen) {
            return text;
        }
        if (maxLen <= 1) {
            return text.substring(0, maxLen);
        }
        return text.substring(0, maxLen - 1) + "…";
    }

    public static boolean equals(String s1, String s2) {
        return (s1 == null && s2 == null) || (s1 != null && s1.equals(s2));
    }

    private static final Pattern WS_PATTERN = Pattern.compile("\\s+");

    @Contract("null -> null")
    public static String simplify(@Nullable String s) {
        if (s == null) {
            return null;
        }
        Matcher matcher = WS_PATTERN.matcher(s);
        return matcher.replaceAll(" ");
    }

    public static String unescape(String val) {
        return XmlUtil.unescape(val);
    }
}
