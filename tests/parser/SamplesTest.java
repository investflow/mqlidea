package parser;

import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * Ensures that there is no parsing errors in sample files
 */
public class SamplesTest extends MQL4ParserTestBase {

    public SamplesTest() {
        super("samples");
    }

    public static final List<String> IGNORED_FILES = Arrays.asList(
            //todo: multiline preprocessor
            "Lang/Mql.mqh", "Lang/App.mqh", "Lang/Indicator.mqh", "Lang/EventApp.mqh"
    );


    public void testSamples() throws IOException {
        File samplesDir = new File(myFullDataPath).getAbsoluteFile();
        List<Path> files = ParserTestUtils.getFilesRecursively(samplesDir);
        Assert.assertTrue(!files.isEmpty());

        int samplesDirPathLen = samplesDir.getAbsolutePath().length();
        for (Path p : files) {
            String absolutePath = p.toFile().getAbsolutePath();
            if (IGNORED_FILES.stream().anyMatch(absolutePath::contains)) {
                continue; // file is ignored
            }
            String subPath = absolutePath.substring(samplesDirPathLen);
            testFile(subPath);
        }
    }

    private void testFile(@NotNull String subPath) throws IOException {
        String text = loadFile(subPath);
        PsiFile file = this.createPsiFile("any-name", text);
        ensureParsed(file);
        ensureCorrectReparse(file);

        PsiErrorElement errorElement = ParserTestUtils.findErrorElement(file);
        Assert.assertNull("File has errors: " + subPath + ", error: " + errorElement, errorElement);
    }
}
