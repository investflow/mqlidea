package parser;

import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static parser.ParserTestUtils.findErrorElement;
import static parser.ParserTestUtils.getFilesRecursively;

/**
 * Ensures that there is no parsing errors in VALID blocks
 */
public class ValidBlocksTest extends MQL4ParserTestBase {

    public ValidBlocksTest() {
        super("parser");
    }

    public void testValidBlocks() throws IOException {
        File dir = new File(myFullDataPath).getAbsoluteFile();
        List<Path> files = getFilesRecursively(dir);
        Assert.assertTrue(!files.isEmpty());

        int samplesDirPathLen = dir.getAbsolutePath().length();
        for (Path p : files) {
            String subPath = p.toFile().getAbsolutePath().substring(samplesDirPathLen);
            if (subPath.contains("/comments/")) { // these files have no VALID block
                continue;
            }
            testValidBlock(subPath);
        }
    }

    private void testValidBlock(String subPath) throws IOException {
        String fullText = loadFile(subPath);
        int validBlockMarkerStartIdx = fullText.indexOf("--- VALID ---");
        Assert.assertTrue("No VALID block found in file: " + subPath, validBlockMarkerStartIdx > 0);
        int validBlockStartIdx = fullText.indexOf("\n", validBlockMarkerStartIdx);
        String validText = fullText.substring(validBlockStartIdx);

        PsiFile file = this.createPsiFile("any-name", validText);
        ensureParsed(file);
        ensureCorrectReparse(file);

        PsiErrorElement errorElement = findErrorElement(file);
        Assert.assertNull("File has errors: " + subPath + ", error: " + errorElement, errorElement);
    }
}
