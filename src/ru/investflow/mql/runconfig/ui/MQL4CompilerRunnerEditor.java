package ru.investflow.mql.runconfig.ui;

import com.intellij.application.options.ModulesComboBox;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.runconfig.MQL4RunCompilerConfiguration;
import ru.investflow.mql.sdk.MQL4SdkType;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MQL4CompilerRunnerEditor extends SettingsEditor<MQL4RunCompilerConfiguration> {
    private JPanel rootPanel;
    private TextFieldWithBrowseButton fileField;
    private MQL4SDKComboBox sdkComboBox;
    private ModulesComboBox scriptTypeComboBox;

    @Override
    protected void resetEditorFrom(@NotNull MQL4RunCompilerConfiguration configuration) {
        Sdk[] sdks = ProjectJdkTable.getInstance().getAllJdks();
        List<Sdk> mql4Sdks = Arrays.stream(sdks).filter(s -> s.getSdkType() instanceof MQL4SdkType).collect(Collectors.toList());
        sdkComboBox.addItem(null);
        mql4Sdks.forEach(s -> sdkComboBox.addItem(s));

        Sdk selectedSdk;
        if (configuration.sdkName == null) {
            Project project = configuration.getProject();
            selectedSdk = ProjectRootManager.getInstance(project).getProjectSdk();
        } else {
            selectedSdk = mql4Sdks.stream().filter(s -> s.getName().equals(configuration.sdkName)).findFirst().orElse(null);
        }
        sdkComboBox.setSelectedSdk(selectedSdk);
    }

    @Override
    protected void applyEditorTo(@NotNull MQL4RunCompilerConfiguration configuration) throws ConfigurationException {
        String selectedSdkName = sdkComboBox.getSelectedSdkName();
        configuration.sdkName = selectedSdkName == null ? "" : selectedSdkName;
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return rootPanel;
    }
}
