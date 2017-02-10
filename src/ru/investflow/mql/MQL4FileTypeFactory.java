package ru.investflow.mql;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

public class MQL4FileTypeFactory extends FileTypeFactory{
    @Override
    public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer) {
        fileTypeConsumer.consume(MQL4FileType.INSTANCE, MQL4FileType.SOURCE_FILE_EXTENSION);
        fileTypeConsumer.consume(MQL4FileType.INSTANCE, MQL4FileType.HEADER_FILE_EXTENSION);
    }
}
