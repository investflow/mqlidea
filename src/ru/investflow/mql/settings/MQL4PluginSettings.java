package ru.investflow.mql.settings;

import com.intellij.openapi.application.ApplicationManager;

public interface MQL4PluginSettings {

    boolean isUseEnDocs();

    void setUseEnDocs(boolean v);

    static MQL4PluginSettings getInstance() {
        return ApplicationManager.getApplication().getComponent(MQL4PluginSettings.class);
    }
}
