package ru.investflow.mql.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.ComboBox;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;

//todo: SearchableConfigurable,
public class MQL4PluginSettingsPanel extends JPanel implements Configurable {

    @NotNull
    private final JComboBox<String> langDocsSelector;

    @NotNull
    private final MQL4PluginSettings settings;

    public MQL4PluginSettingsPanel(@NotNull MQL4PluginSettings settings) {
        this.settings = settings;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        comboPanel.add(new JLabel("Docs language: "));
        langDocsSelector = new ComboBox<>(new String[]{"Russian", "English"});
        langDocsSelector.setSelectedIndex(settings.isUseEnDocs() ? 1 : 0);
        comboPanel.add(langDocsSelector);
        add(comboPanel);

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
        return langDocsSelector.getSelectedIndex() != (settings.isUseEnDocs() ? 1 : 0);
    }

    /**
     * Store the settings from configurable to other components.
     */
    @Override
    public void apply() throws ConfigurationException {
        settings.setUseEnDocs(langDocsSelector.getSelectedIndex() == 1);
    }

    /**
     * Load settings from other components to configurable.
     */
    @Override
    public void reset() {
        langDocsSelector.setSelectedIndex(settings.isUseEnDocs() ? 1 : 0);
    }

}
