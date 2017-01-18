package ru.investflow.mql.parser.parsing;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.parser.ParsingContext;
import ru.investflow.mql.parser.parsing.util.ParsingErrors;
import ru.investflow.mql.parser.parsing.util.ParsingUtils;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4TokenSets;

import static ru.investflow.mql.parser.parsing.CommentParsing.parseComment;
import static ru.investflow.mql.parser.parsing.statement.StatementParsing.parseEmptyStatement;

public class BracketBlockParsing implements MQL4Elements {

    public static boolean parseBracketsBlock(PsiBuilder b, int l, @NotNull ParsingContext ctx) {
        if (!ParsingUtils.nextTokenIn(b, l, "parseBracketBlock", MQL4TokenSets.LEFT_BRACKETS)) {
            return false;
        }
        PsiBuilder.Marker m = b.mark(); // starting  new bracket section
        IElementType openBracket = b.getTokenType();
        assert openBracket != null;
        IElementType topBlockLeftBracket = ctx.bracketsStack.isEmpty() ? null : ctx.bracketsStack.peek();
        ctx.bracketsStack.push(openBracket);
        try {
            IElementType closeBracket = MQL4TokenSets.getRightBracketFor(openBracket);
            b.advanceLexer(); // open bracket
            while (!b.eof()) {
                IElementType t = b.rawLookup(0);
                assert t != null;
                if (t == closeBracket) {
                    b.advanceLexer(); // close bracket
                    m.done(BRACKETS_BLOCK);
                    return true;
                }
                if (MQL4TokenSets.RIGHT_BRACKETS.contains(t)) { // different kind of close bracket
                    boolean closeBracketForUpperBlock = topBlockLeftBracket != null && topBlockLeftBracket == MQL4TokenSets.getLeftBracketFor(t);
                    if (closeBracketForUpperBlock) {
                        m.error("No matching closing brace found");
                        return true;
                    }
                    ParsingUtils.advanceWithError(b, ParsingErrors.UNEXPECTED_TOKEN);
                    continue;
                }
                boolean res = parseBracketsBlock(b, l + 1, ctx)
                        || parseEmptyStatement(b)
                        || parseComment(b);

                if (!res) { // if was not parsed -> just consume this element.
                    b.advanceLexer();
                }
            }
            m.error("No matching closing bracket");
            return false;
        } finally {
            ctx.bracketsStack.pop();
        }
    }

}
