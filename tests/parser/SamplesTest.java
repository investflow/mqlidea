package parser;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.ParsingTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import ru.investflow.mql.parser.MQL4ParserDefinition;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static ru.investflow.mql.MQL4FileType.HEADER_FILE_EXTENSION;
import static ru.investflow.mql.MQL4FileType.SOURCE_FILE_EXTENSION;

/**
 * Ensures that there is no parsing errors in sample files
 */
public class SamplesTest extends ParsingTestCase {
    public SamplesTest() {
        super("samples", "mq4", new MQL4ParserDefinition());
    }

    public void testSamples() throws IOException {
        File samplesDir = new File(myFullDataPath).getAbsoluteFile();
        int samplesDirPathLen = samplesDir.getAbsolutePath().length();

        List<Path> files = Files.walk(samplesDir.toPath())
                .filter(p -> isValidMqlFile(p.toFile()))
                .collect(Collectors.toList());

        Assert.assertTrue(!files.isEmpty());

        for (Path p : files) {
            String subPath = p.toFile().getAbsolutePath().substring(samplesDirPathLen);
            testFile(subPath);
        }
    }

    private void testFile(String subPath) throws IOException {
        String text = loadFile(subPath);
        PsiFile file = this.createPsiFile("any-name", text);
        ensureParsed(file);
        ensureCorrectReparse(file);

        PsiErrorElement errorElement = findErrorElement(file);
        Assert.assertNull("File has errors: " + subPath + ", error: " + errorElement, errorElement);
    }

    private static PsiErrorElement findErrorElement(@NotNull PsiElement element) {
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

    private boolean isValidMqlFile(File file) {
        String name = file.getName();
        return name.endsWith(SOURCE_FILE_EXTENSION) || name.endsWith(HEADER_FILE_EXTENSION);
    }


    @NotNull
    @Override
    protected String getTestDataPath() {
        return "testData";
    }
}
