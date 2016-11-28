package ru.investflow.mql.doc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.intellij.lang.documentation.DocumentationProviderEx;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MQL4DocumentationProvider extends DocumentationProviderEx {

    private static final Logger log = Logger.getInstance(MQL4DocumentationProvider.class);

    private final Map<String, DocEntry> docEntryMap = new HashMap<>();

    public MQL4DocumentationProvider() {
        loadResource("mql4-constants");
        loadResource("mql4-functions");
        loadResource("mql4-keywords");
        loadResource("mql4-preprocessor");
    }

    private void loadResource(@NotNull String name) {
        String resource = "/mql/doc/" + name + "-ru.json";
        try (Reader reader = new InputStreamReader(getClass().getResourceAsStream(resource), StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().create();
            JsonArray arr = gson.fromJson(reader, JsonArray.class);
            for (int i = 0; i < arr.size(); i++) {
                JsonArray doc = arr.get(i).getAsJsonArray();
                String token = doc.get(0).getAsString();
                docEntryMap.put(token, new DocEntry(token, doc.get(1).getAsString(), doc.get(2).getAsString()));
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
        DocEntry e = getEntryByPsiElement(originalElement);
        return e == null ? null : e.quickNavigateInfo;
    }

    @Nullable
    private DocEntry getEntryByPsiElement(@Nullable PsiElement originalElement) {
        if (originalElement == null) {
            return null;
        }
        return docEntryMap.get(originalElement.getText());
    }

    @Nullable
    @Override
    public PsiElement getDocumentationElementForLookupItem(PsiManager psiManager, Object object, PsiElement element) {
        return null;
    }

    @Nullable
    @Override
    public PsiElement getDocumentationElementForLink(PsiManager psiManager, String link, PsiElement context) {
        return null;
    }

    @Nullable
    @Override
    public PsiElement getCustomDocumentationElement(@NotNull Editor editor, @NotNull PsiFile file, @Nullable PsiElement contextElement) {
        return contextElement;
    }

}
