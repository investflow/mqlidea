package ru.investflow.mql.doc;

import org.jetbrains.annotations.NotNull;

public class DocEntry {

    @NotNull
    public final String text;

    @NotNull
    public final String link;

    @NotNull
    public final DocEntryType type;

    public DocEntry(@NotNull String text, @NotNull String link, @NotNull DocEntryType type) {
        this.text = text;
        this.link = link;
        this.type = type;
    }
}
