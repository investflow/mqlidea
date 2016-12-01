package ru.investflow.mql.doc;

import org.jetbrains.annotations.NotNull;

public class DocEntry {

    @NotNull
    public final String text;

    @NotNull
    public final String quickNavigateInfo;

    @NotNull
    public final String link;

    @NotNull
    public final DocEntryType type;

    public DocEntry(@NotNull String text, @NotNull String quickNavigateInfo, @NotNull String link, @NotNull DocEntryType type) {
        this.text = text;
        this.quickNavigateInfo = quickNavigateInfo;
        this.link = link;
        this.type = type;
    }
}
