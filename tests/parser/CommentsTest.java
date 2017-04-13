package parser;

public class CommentsTest extends MQL4ParserTestBase {

    public CommentsTest() {
        super("comments");
    }

    public void testLineComments() {
        doTest();
    }

    public void testBlockComments() {
        doTest();
    }

}
