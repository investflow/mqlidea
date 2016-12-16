package ru.investflow.mql.util;

import com.intellij.openapi.projectRoots.Sdk;

import java.util.Comparator;

public class SdkByNameComparator implements Comparator<Sdk> {

    public static final SdkByNameComparator INSTANCE = new SdkByNameComparator();

    public int compare(Sdk sdk1, Sdk sdk2) {
        if (sdk1 == null && sdk2 == null) {
            return 0;
        }
        if (sdk1 == null) {
            return -1;
        }
        if (sdk2 == null) {
            return 1;
        }
        return sdk1.getName().compareToIgnoreCase(sdk2.getName());
    }
}
