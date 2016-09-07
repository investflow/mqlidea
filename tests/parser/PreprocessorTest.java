package parser;

import ru.investflow.mql.MQL4ParserDefinition;

public class PreprocessorTest extends MQL4ParserTestBase {
    public PreprocessorTest() {
        super("preprocessor", "mq4", new MQL4ParserDefinition());
    }

    public void testProperty() {
        doTest(false);
    }

    public void testPropertyMix() {
        doTest(false);
    }

}
