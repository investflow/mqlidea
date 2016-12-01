package ru.investflow.mql.doc;

import org.jetbrains.annotations.NotNull;

public class DocEntry {

    @NotNull
    public final String token;

    @NotNull
    public final String quickNavigateInfo;

    @NotNull
    public final String link;

    @NotNull
    public final DocEntryType type;

    public DocEntry(@NotNull String token, @NotNull String quickNavigateInfo, @NotNull String link, @NotNull DocEntryType type) {
        this.token = token;
        this.quickNavigateInfo = quickNavigateInfo;
        this.link = link;
        this.type = type;
    }
}
