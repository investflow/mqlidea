package parser;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static ru.investflow.mql.MQL4FileType.HEADER_FILE_EXTENSION;
import static ru.investflow.mql.MQL4FileType.SOURCE_FILE_EXTENSION;

public class ParserTestUtils {
    public static List<Path> getFilesRecursively(File samplesDir) throws IOException {
        return Files.walk(samplesDir.toPath())
                    .filter(p -> isValidMqlFile(p.toFile()))
                    .collect(Collectors.toList());
    }

    public static PsiErrorElement findErrorElement(@NotNull PsiElement element) {
        if (element instanceof PsiErrorElement) {
            return (PsiErrorElement) element;
        }
        for (PsiElement child : element.getChildren()) {
            PsiErrorElement e = findErrorElement(child);
            if (e != null) {
                return e;
            }
        }
        return null;
    }

    public static boolean isValidMqlFile(File file) {
        String name = file.getName();
        return name.endsWith(SOURCE_FILE_EXTENSION) || name.endsWith(HEADER_FILE_EXTENSION);
    }
}
