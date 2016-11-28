package ru.investflow.mql.doc;

import org.jetbrains.annotations.NotNull;

public class DocEntry {

    @NotNull
    public String token;

    @NotNull
    public String quickNavigateInfo;

    @NotNull
    public String mount;

    public DocEntry(@NotNull String token, @NotNull String quickNavigateInfo, @NotNull String mount) {
        this.token = token;
        this.quickNavigateInfo = quickNavigateInfo;
        this.mount = mount;
    }
}
