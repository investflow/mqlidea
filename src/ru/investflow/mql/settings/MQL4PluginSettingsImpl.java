package ru.investflow.mql.settings;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "MQL4.PluginSettings", storages = {@Storage("mql4-plugin.xml")})
public class MQL4PluginSettingsImpl implements MQL4PluginSettings, PersistentStateComponent<MQL4PluginSettingsImpl>, ApplicationComponent {

    public boolean enDocs;

    @Override
    public boolean isUseEnDocs() {
        return enDocs;
    }

    @Override
    public void setUseEnDocs(boolean v) {
        enDocs = v;
    }

    @Override
    public void initComponent() {

    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "MQL4.PluginSettings";
    }

    @Nullable
    @Override
    public MQL4PluginSettingsImpl getState() {
        return this;
    }

    @Override
    public void loadState(MQL4PluginSettingsImpl state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
