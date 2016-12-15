package ru.investflow.mql.runconfig.ui;

import com.intellij.application.options.ModulesComboBox;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.runconfig.MQL4RunCompilerConfiguration;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicComboBoxEditor;

public class MQL4CompilerRunnerEditor extends SettingsEditor<MQL4RunCompilerConfiguration> {
    private JPanel rootPanel;
    private TextFieldWithBrowseButton fileField;
    private MQL4SDKComboBox sdkComboBox;
    private ModulesComboBox scriptTypeComboBox;

    @NotNull
    private final MQL4RunCompilerConfiguration configuration;

    public MQL4CompilerRunnerEditor(@NotNull MQL4RunCompilerConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void resetEditorFrom(@NotNull MQL4RunCompilerConfiguration configuration) {
        //TODO:
    }

    @Override
    protected void applyEditorTo(@NotNull MQL4RunCompilerConfiguration configuration) throws ConfigurationException {
        //TODO:
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return rootPanel;
    }
}
