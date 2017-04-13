package parser;

import com.intellij.core.CoreApplicationEnvironment;
import com.intellij.lang.LanguageExtensionPoint;
import com.intellij.lang.ParserDefinition;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.testFramework.ParsingTestCase;
import com.intellij.testFramework.UsefulTestCase;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.parser.MQL4ParserDefinition;

import java.io.File;
import java.io.IOException;

public abstract class MQL4ParserTestBase extends ParsingTestCase {

    public MQL4ParserTestBase(String dataPath) {
        this(dataPath, "mq4", new MQL4ParserDefinition());
    }

    public MQL4ParserTestBase(String dataPath, String fileExt, ParserDefinition... definitions) {
        super("parser/" + dataPath, fileExt, definitions);
    }

    @NotNull
    @Override
    protected String getTestDataPath() {
        return "testData";
    }

    @Override
    protected boolean skipSpaces() {
        return false;
    }

    @Override
    protected boolean includeRanges() {
        return true;
    }

    protected void doTest() {
        super.doTest(true);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        CoreApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), "com.intellij.lang.braceMatcher", LanguageExtensionPoint.class);
    }
}