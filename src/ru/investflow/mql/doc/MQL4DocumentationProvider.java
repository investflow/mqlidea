package ru.investflow.mql.doc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.intellij.lang.documentation.DocumentationProviderEx;
import com.intellij.lang.documentation.ExternalDocumentationHandler;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.tree.IElementType;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.impl.MQL4DocLookupElement;
import ru.investflow.mql.settings.MQL4PluginSettings;

/**
 * Documentation provider for MQL4 language.
 */
public class MQL4DocumentationProvider extends DocumentationProviderEx implements ExternalDocumentationHandler {

    private static final Logger log = Logger.getInstance(MQL4DocumentationProvider.class);

    public static final String DOC_NOT_FOUND = "";

    private static final Map<String, DocEntry> docEntryByText = new HashMap<>();
    private static final Map<String, DocEntry> docEntryByLink = new HashMap<>();
    private static final ClassLoader loader = MQL4DocumentationProvider.class.getClassLoader();
    private static boolean resourcesLoadedFlag;


    public MQL4DocumentationProvider() {
        ensureResourcesAreLoaded();
    }

    private static void ensureResourcesAreLoaded() {
        if (resourcesLoadedFlag) {
            return;
        }
        loadResource("mql4-constants", DocEntryType.BuiltInConstant);
        loadResource("mql4-functions", DocEntryType.BuiltInFunction);
        loadResource("mql4-keywords", DocEntryType.Keyword);
        loadResource("mql4-preprocessor", DocEntryType.PreprocessorKeyword);
        resourcesLoadedFlag = true;
    }

    private static void loadResource(@NotNull String name, @NotNull DocEntryType type) {
        String resource = "/mql/doc/" + name + ".json";
        try (Reader reader = new InputStreamReader(loader.getResourceAsStream(resource), StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().create();
            JsonArray arr = gson.fromJson(reader, JsonArray.class);
            for (int i = 0; i < arr.size(); i++) {
                JsonArray doc = arr.get(i).getAsJsonArray();
                DocEntry entry = new DocEntry(doc.get(0).getAsString(), doc.get(1).getAsString(), type);
                docEntryByLink.put(entry.link, entry);
                docEntryByText.put(entry.text, entry);
            }
        } catch (Exception e) {
            log.error("Error loading resource with docs: " + resource, e);
        }
    }

    @Nullable
    public static DocEntry getEntryByText(@NotNull String text) {
        ensureResourcesAreLoaded();
        return docEntryByText.get(text);
    }

    @NotNull
    public static Collection<DocEntry> getEntries() {
        ensureResourcesAreLoaded();
        return docEntryByText.values();
    }

    @Nullable
    @Override
    public String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
        return null;
    }

    @Nullable
    @Override
    public List<String> getUrlFor(PsiElement element, PsiElement originalElement) {
        return null;
    }

    @Nullable
    @Override
    public String generateDoc(PsiElement element, @Nullable PsiElement originalElement) {
        String link = getLinkByElementText(element != null ? element : originalElement);
        return link == null ? null : generateDocByLink(link);
    }

    @NotNull
    private String generateDocByLink(@NotNull String link) {
        String resource = docLinkToResourcePath(link);
        if (loader.getResource(resource) == null) {
            DocEntry e = docEntryByLink.get(link);
            if (e != null) {
                log.warn("No docs found for " + link + "!");
                return "Resource not found: " + link;
            }
            return DOC_NOT_FOUND;
        }
        try (InputStream is = loader.getResourceAsStream(resource)) {
            return new Scanner(is, StandardCharsets.UTF_8).useDelimiter("\\A").next();
        } catch (Exception e) {
            log.error("Error loading resource with docs: " + link, e);
            return DOC_NOT_FOUND;
        }
    }

    /**
     * Returns file name for the given link.
     */
    @NotNull
    private String docLinkToResourcePath(@NotNull String link) {
        int anchorIdx = link.indexOf('#');
        String lang = getDocsLanguage();
        if (anchorIdx == -1) {
            return "/mql/doc/" + lang + "/" + link + ".html";
        }
        return "/mql/doc/" + lang + "/" + link.substring(0, anchorIdx) + ".html";
    }

    @Nullable
    private String getLinkByElementText(@Nullable PsiElement element) {
        if (element == null) {
            return null;
        }
        String text = element.getText();
        IElementType tt = element.getNode().getElementType();
        if (tt == MQL4Elements.L_ROUND_BRACKET) { // when positioned on '(' show doc for function name
            PsiElement bracketBlock = element.getParent();
            PsiElement functionNameEl = bracketBlock == null ? null : bracketBlock.getPrevSibling();
            if (functionNameEl != null) {
                text = functionNameEl.getText();
            }
        } else if (tt == MQL4Elements.WHITE_SPACE || tt == MQL4Elements.SEMICOLON) { // end of statement -> show docs for the statement itself.
            PsiElement prev = element.getPrevSibling();
            if (prev != null) {
                text = prev.getText();
            } else if (element.getParent().getNode().getElementType() == MQL4Elements.EMPTY_STATEMENT) {
                prev = element.getParent().getPrevSibling();
                if (prev != null) {
                    text = prev.getText();
                }
            }
        }
        DocEntry entry = docEntryByText.get(text);
        if (entry == null) {
            entry = docEntryByText.get("#" + text);
        }
        return entry == null ? null : entry.link;
    }

    @Override
    public PsiElement getDocumentationElementForLookupItem(PsiManager psiManager, Object object, PsiElement context) {
        return new MQL4DocLookupElement(object.toString(), context.getNode());
    }

    @Nullable
    @Override
    public PsiElement getCustomDocumentationElement(@NotNull Editor editor, @NotNull PsiFile file, @Nullable PsiElement contextElement) {
        return contextElement;
    }


    // ExternalDocumentationHandler methods

    @Override
    public boolean handleExternal(PsiElement element, PsiElement originalElement) {
        return getLinkByElementText(originalElement) != null;
    }

    @Override
    public boolean handleExternalLink(PsiManager psiManager, String link, PsiElement context) {
        return canFetchDocumentationLink(link);
    }

    @Override
    public boolean canFetchDocumentationLink(String link) {
        return docEntryByLink.containsKey(link) || loader.getResource(docLinkToResourcePath(link)) != null;
    }

    @NotNull
    @Override
    public String fetchExternalDocumentation(@NotNull String link, @Nullable PsiElement element) {
        return generateDocByLink(link);
    }

    public String getDocsLanguage() {
        return MQL4PluginSettings.getInstance().isUseEnDocs() ? "en" : "ru";
    }
}
