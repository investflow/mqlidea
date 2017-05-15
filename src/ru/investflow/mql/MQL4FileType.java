package ru.investflow.mql;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

/* Each file in IDEA has type. This is type for MQL4 Language sources. */
public class MQL4FileType extends LanguageFileType {

    public static final MQL4FileType INSTANCE = new MQL4FileType();

    public static final String SOURCE_FILE_EXTENSION = "mq4";
    public static final String HEADER_FILE_EXTENSION = "mqh";

    private MQL4FileType() {
        super(MQL4Language.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "MQL4 file";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "MQL4 language file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return SOURCE_FILE_EXTENSION;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return MQL4Icons.File;
    }
}
