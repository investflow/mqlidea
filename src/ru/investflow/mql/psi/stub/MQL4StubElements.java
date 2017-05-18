package ru.investflow.mql.psi.stub;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lang.LighterASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilderFactory;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.ILightStubFileElementType;
import com.intellij.util.diff.FlyweightCapableTreeStructure;
import ru.investflow.mql.MQL4Language;
import ru.investflow.mql.parser.MQL4Parser;

public interface MQL4StubElements {

    ILightStubFileElementType FILE = new ILightStubFileElementType(MQL4Language.INSTANCE) {
        public FlyweightCapableTreeStructure<LighterASTNode> parseContentsLight(ASTNode chameleon) {
            PsiElement psi = chameleon.getPsi();
            assert (psi != null) : ("Bad chameleon: " + chameleon);

            Project project = psi.getProject();
            PsiBuilderFactory factory = PsiBuilderFactory.getInstance();
            PsiBuilder builder = factory.createBuilder(project, chameleon);
            ParserDefinition parserDefinition = LanguageParserDefinitions.INSTANCE.forLanguage(getLanguage());
            assert (parserDefinition != null) : this;
            MQL4Parser parser = new MQL4Parser();
            return parser.parseLight(this, builder);
        }
    };

    IStubElementType CLASS = new MQL4ClassElementStubType();
}
