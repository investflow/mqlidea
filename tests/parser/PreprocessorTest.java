package parser;

public class PreprocessorTest extends MQL4ParserTestBase {

    public PreprocessorTest() {
        super("parser/preprocessor");
    }

    public void testProperty() {
        doTest();
    }

    public void testInclude() {
        doTest();
    }

    public void testUndef() {
        doTest();
    }

    public void testDefine() {
        doTest();
    }

}
