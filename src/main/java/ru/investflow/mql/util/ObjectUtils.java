package ru.investflow.mql.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ObjectUtils {

    @NotNull
    public static <T> T firstNonNull(@Nullable T v1, @NotNull T v2) {
        return v1 == null ? v2 : v1;
    }
    
}
