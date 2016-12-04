package ru.investflow.mql.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

//todo: SearchableConfigurable,
public class MQL4PluginSettingsPanel extends JPanel implements Configurable {

    @NotNull
    private final JCheckBox useEnDocsCheckbox;

    @NotNull
    private final MQL4PluginSettings settings;

    public MQL4PluginSettingsPanel(@NotNull MQL4PluginSettings settings) {
        this.settings = settings;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        useEnDocsCheckbox = new JCheckBox("Show documentation in English");
        useEnDocsCheckbox.setSelected(settings.isUseEnDocs());
        add(useEnDocsCheckbox);
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "MQL4";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    /**
     * Returns the user interface component for editing the configuration.
     *
     * @return the component instance.
     */
    @Nullable
    @Override
    public JComponent createComponent() {
        return this;
    }

    /**
     * Checks if the settings in the user interface component were modified by the user and
     * need to be saved.
     *
     * @return true if the settings were modified, false otherwise.
     */
    @Override
    public boolean isModified() {
        return settings.isUseEnDocs() != useEnDocsCheckbox.isSelected();
    }

    /**
     * Store the settings from configurable to other components.
     */
    @Override
    public void apply() throws ConfigurationException {
        settings.setUseEnDocs(useEnDocsCheckbox.isSelected());
    }

    /**
     * Load settings from other components to configurable.
     */
    @Override
    public void reset() {
        useEnDocsCheckbox.setSelected(settings.isUseEnDocs());
    }

}
