package ru.investflow.mql.parser.parsing;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.parser.parsing.util.ParsingErrors;
import ru.investflow.mql.parser.parsing.util.ParsingScope;
import ru.investflow.mql.parser.parsing.util.ParsingUtils;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4TokenSets;

import static com.intellij.lang.parser.GeneratedParserUtilBase.recursion_guard_;
import static ru.investflow.mql.parser.parsing.BracketBlockParsing.parseBracketsBlock;
import static ru.investflow.mql.parser.parsing.CommentParsing.parseComment;
import static ru.investflow.mql.parser.parsing.FunctionsParsing.parseFunction;
import static ru.investflow.mql.parser.parsing.preprocessor.PreprocessorParsing.parsePreprocessorBlock;
import static ru.investflow.mql.parser.parsing.statement.EnumParsing.parseEnum;
import static ru.investflow.mql.parser.parsing.statement.StatementParsing.parseEmptyStatement;
import static ru.investflow.mql.parser.parsing.util.ParsingErrors.error;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.advanceLexerUntil;

public class ClassParsing implements MQL4Elements {

    public static final TokenSet ON_ERROR_CLASS_ADVANCE_TOKENS = TokenSet.create(R_CURLY_BRACKET);
    public static final TokenSet ON_ERROR_CLASS_DO_NOT_ADVANCE_TOKENS = TokenSet.create(SEMICOLON);

    /**
     * Form: (class|struct) name { }
     */
    public static boolean parseClassOrStruct(PsiBuilder b, int l) {
        IElementType t1 = b.getTokenType();
        if (t1 != CLASS_KEYWORD && t1 != STRUCT_KEYWORD && t1 != INTERFACE_KEYWORD) {
            return false;
        }
        Marker m = b.mark();
        try {
            b.advanceLexer(); // 'class|struct'
            IElementType t2 = b.getTokenType();
            if (t2 != IDENTIFIER) {
                error(b, (t1 == CLASS_KEYWORD ? "Class" : t1 == STRUCT_KEYWORD ? "Struct" : "Interface") + " name is expected");
                b.mark().done(MQL4Elements.CLASS_INNER_BLOCK);
                return true;
            }
            b.advanceLexer(); // name

            IElementType t3 = b.getTokenType();
            if (t3 == SEMICOLON) {
                return true; // class declaration;
            }
            if (t3 == COLON) {
                b.advanceLexer(); // ':'
                if (!parseInheritanceList(b, l)) {
                    advanceLexerUntil(b, ON_ERROR_CLASS_ADVANCE_TOKENS, ON_ERROR_CLASS_DO_NOT_ADVANCE_TOKENS);
                    return true;
                }
            }
            if (b.getTokenType() != L_CURLY_BRACKET) {
                error(b, ParsingErrors.UNEXPECTED_TOKEN);
                b.mark().done(MQL4Elements.CLASS_INNER_BLOCK);
                return true;
            }
            b.advanceLexer(); // '{'
            if (!parseClassInnerBlock(b, l)) {
                advanceLexerUntil(b, ON_ERROR_CLASS_ADVANCE_TOKENS, ON_ERROR_CLASS_DO_NOT_ADVANCE_TOKENS);
                return true;
            }
            b.advanceLexer(); // '}'
        } finally {
            m.done(CLASS_DEFINITION);
        }
        return true;
    }

    /**
     * List = [Element]* {
     * Element = [Access] Identifier [,]
     */
    private static boolean parseInheritanceList(PsiBuilder b, int l) {
        int nItems = 0;
        PsiBuilder.Marker list = b.mark();
        try {
            while (true) {
                PsiBuilder.Marker inArg = b.mark();
                try {
                    IElementType t1 = b.getTokenType();
                    if (t1 == PRIVATE_KEYWORD || t1 == PROTECTED_KEYWORD || t1 == PUBLIC_KEYWORD) {
                        b.advanceLexer(); // 'private'
                    }
                    if (!parseCustomTypeName(b, l)) {
                        error(b, ParsingErrors.UNEXPECTED_TOKEN);
                        return false;
                    }
                    nItems++;

                    IElementType t3 = b.getTokenType();
                    if (t3 == L_CURLY_BRACKET) {
                        break;
                    }
                    if (t3 != COMMA) {
                        error(b, ParsingErrors.UNEXPECTED_TOKEN);
                        return false;
                    }
                } finally {
                    inArg.done(MQL4Elements.CLASS_INHERITANCE_ITEM);
                }
                b.advanceLexer(); // ','
            }
        } finally {
            list.done(MQL4Elements.CLASS_INHERITANCE_LIST);
        }
        if (nItems == 0) {
            error(b, ParsingErrors.UNEXPECTED_TOKEN);
            return false;
        }
        return true;
    }

    /**
     * Form: as of today: set of anything until '}'
     */
    private static boolean parseClassInnerBlock(PsiBuilder b, int l) {
        Marker classInnerBlock = b.mark();
        ParsingScope.pushScope(b, ParsingScope.CLASS);
        try {
            while (b.getTokenType() != R_CURLY_BRACKET && !b.eof()) {
                boolean r = parseEmptyStatement(b)
                        || parseComment(b)
                        || parsePreprocessorBlock(b)
                        || parseFunction(b)
                        || parseEnum(b, l + 1)
                        || parseClassOrStruct(b, l + 1)
                        || parseBracketsBlock(b, l + 1);
                if (!r) {
                    b.advanceLexer();
                }
            }
            return !b.eof();
        } finally {
            classInnerBlock.done(CLASS_INNER_BLOCK);
            ParsingScope.popScope(b);
        }
    }

    public static boolean parseCustomTypeName(@NotNull PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "parseBracketsBlock")) {
            return false;
        }
        if (b.getTokenType() != IDENTIFIER) {
            return false;
        }
        b.advanceLexer(); // name

        int templateParamSize = parseTemplateList(b, 0, 0);
        if (templateParamSize > 0) {
            PsiBuilder.Marker m = b.mark();
            try {
                ParsingUtils.advanceLexer(b, templateParamSize);
            } finally {
                m.done(TYPE_TEMPLATE_BLOCK);
            }
        }
        return true;
    }

    //TODO: handle SH_LEFT, SH_RIGHT correctly
    private static int parseTemplateList(@NotNull PsiBuilder b, int offset, int depth) {
        if (depth > 10) {
            return -1;
        }
        int pos = offset;
        if (b.lookAhead(pos) != LT) {
            return -1;
        }
        pos++; // '<'
        while (true) {
            if (b.lookAhead(pos) == GT) {
                pos++; // '>'
                return pos - offset;
            }
            IElementType typeName = b.lookAhead(pos);
            if (typeName != IDENTIFIER && !MQL4TokenSets.DATA_TYPES.contains(typeName)) {
                return -1;
            }
            pos++; // identifier or raw type
            if (b.lookAhead(pos) == MUL) {
                pos++; // pointer type
            }
            if (b.lookAhead(pos) == LT) {
                int dPos = parseTemplateList(b, pos, depth + 1);
                if (dPos <= 0) {
                    return -1;
                }
                pos += dPos;
            }
            if (b.lookAhead(pos) == COMMA) {
                pos++; // ','
            }
        }
    }
}
