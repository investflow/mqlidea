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
import ru.investflow.mql.settings.MQL4PluginSettings;

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

    private static final Map<String, String> docLinkByText = new HashMap<>();
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
        loadResource("mql4-constants", DocEntryType.Constant);
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
                docLinkByText.put(entry.text, entry.link);
            }
        } catch (Exception e) {
            log.error("Error loading resource with docs: " + resource, e);
        }
    }

    @Nullable
    public static DocEntry getEntryForElement(@NotNull PsiElement e) {
        ensureResourcesAreLoaded();
        String link = docLinkByText.get(e.getText());
        return link == null ? null : docEntryByLink.get(link);
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
            DocEntry e = docEntryByLink.get(link);
            if (e != null) {
                log.warn("No docs found for " + link + "!");
                return "Resource not found: " + link;
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
        return docEntryByLink.containsKey(link) || loader.getResource(docLinkToResource(link)) != null;
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
