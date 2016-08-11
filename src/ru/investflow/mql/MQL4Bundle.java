package ru.investflow.mql;

import com.intellij.CommonBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.ResourceBundle;

public class MQL4Bundle {
    private static Reference<ResourceBundle> MQL4_BUNDLE;

    @NonNls
    private static final String BUNDLE_ID = "messages.plugin";

    private MQL4Bundle() {
    }

    public static String message(@PropertyKey(resourceBundle = BUNDLE_ID) String key, Object... params) {
        return CommonBundle.message(getBundle(), key, params);
    }

    private static ResourceBundle getBundle() {
        ResourceBundle bundle = null;
        if (MQL4_BUNDLE != null) bundle = MQL4_BUNDLE.get();
        if (bundle == null) {
            bundle = ResourceBundle.getBundle(BUNDLE_ID);
            MQL4_BUNDLE = new SoftReference<>(bundle);
        }
        return bundle;
    }
}
