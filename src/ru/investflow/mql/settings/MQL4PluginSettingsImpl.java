package ru.investflow.mql.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "MQL4.PluginSettings", storages = {@Storage("mql4-plugin.xml")})
public class MQL4PluginSettingsImpl implements MQL4PluginSettings, PersistentStateComponent<MQL4PluginSettingsImpl> {

    public boolean enDocs;
    public boolean errorAnalysis = true;

    @Override
    public boolean isUseEnDocs() {
        return enDocs;
    }

    @Override
    public void setUseEnDocs(boolean v) {
        enDocs = v;
    }

    @Override
    public boolean performErrorAnalysis() {
        return errorAnalysis;
    }

    @Override
    public void setPerformErrorAnalysis(boolean v) {
        errorAnalysis = v;
    }

    @Nullable
    @Override
    public MQL4PluginSettingsImpl getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull MQL4PluginSettingsImpl state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
