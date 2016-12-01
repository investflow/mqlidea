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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MQL4DocumentationProvider extends DocumentationProviderEx implements ExternalDocumentationHandler {

    private static final Logger log = Logger.getInstance(MQL4DocumentationProvider.class);

    public static final String DOC_NOT_FOUND = "";

    private final Map<String, String> docLinkByText = new HashMap<>();
    private final Map<String, DocEntry> docEntryMapByLink = new HashMap<>();
    private final ClassLoader loader = MQL4DocumentationProvider.class.getClassLoader();

    public MQL4DocumentationProvider() {
        loadResource("mql4-constants", DocEntryType.Constant);
        loadResource("mql4-functions", DocEntryType.BuiltInFunction);
        loadResource("mql4-keywords", DocEntryType.Keyword);
        loadResource("mql4-preprocessor", DocEntryType.PreprocessorKeyword);
    }

    private void loadResource(@NotNull String name, @NotNull DocEntryType type) {
        String resource = "/mql/doc/" + name + "-" + getDocsLanguage() + ".json";
        try (Reader reader = new InputStreamReader(getClass().getResourceAsStream(resource), StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().create();
            JsonArray arr = gson.fromJson(reader, JsonArray.class);
            for (int i = 0; i < arr.size(); i++) {
                JsonArray doc = arr.get(i).getAsJsonArray();
                DocEntry entry = new DocEntry(doc.get(0).getAsString(), doc.get(1).getAsString(), doc.get(2).getAsString(), type);
                docEntryMapByLink.put(entry.link, entry);
                docLinkByText.put(entry.text, entry.link);
            }
        } catch (Exception e) {
            log.error("Error loading resource with docs: " + resource, e);
        }
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
        String link = getLinkByElementText(originalElement);
        return link == null ? null : generateDocByLink(link);

    }

    @NotNull
    private String generateDocByLink(@NotNull String link) {
        String resource = docLinkToResource(link);
        if (loader.getResource(resource) == null) {
            DocEntry e = docEntryMapByLink.get(link);
            if (e != null) {
                log.warn("No docs found for " + link + ", using quick nav info");
                return e.quickNavigateInfo;
            }
            return DOC_NOT_FOUND;
        }
        try (InputStream is = loader.getResourceAsStream(resource)) {
            return new Scanner(is, "UTF-8").useDelimiter("\\A").next();
        } catch (Exception e) {
            log.error("Error loading resource with docs: " + link, e);
            return DOC_NOT_FOUND;
        }
    }

    @NotNull
    private String docLinkToResource(@Nullable String link) {
        return "/mql/doc/" + getDocsLanguage() + "/" + link + ".html";
    }

    @Nullable
    private String getLinkByElementText(@Nullable PsiElement originalElement) {
        if (originalElement == null) {
            return null;
        }
        return docLinkByText.get(originalElement.getText());
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
        return docEntryMapByLink.containsKey(link) || loader.getResource(docLinkToResource(link)) != null;
    }

    @NotNull
    @Override
    public String fetchExternalDocumentation(@NotNull String link, @Nullable PsiElement element) {
        return generateDocByLink(link);
    }

    public String getDocsLanguage() {
        return "ru";
    }
}
