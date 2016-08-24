package parser;

import ru.investflow.mql.MQL4ParserDefinition;

public class CommentsTest extends MQL4ParserTestBase {
    public CommentsTest() {
        super("comments", "mq4", new MQL4ParserDefinition());
    }

    public void testLineComments() {
        doTest(true);
    }

    public void testBlockComments() {
        doTest(true);
    }

}
