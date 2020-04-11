package ru.investflow.mql.runconfig.ui;

import com.intellij.execution.configuration.BrowseModuleValueActionListener;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileChooser.FileChooserDialog;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.VirtualFile;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.MQL4FileType;
import ru.investflow.mql.runconfig.MQL4RunCompilerConfiguration;
import ru.investflow.mql.sdk.MQL4SdkType;
import ru.investflow.mql.util.OSUtils;

public class MQL4CompilerRunnerEditor extends SettingsEditor<MQL4RunCompilerConfiguration> {
    private JPanel rootPanel;
    private TextFieldWithBrowseButton fileField;
    private MQL4SDKComboBoxWithBrowseButton sdkComboBox;
    private JTextField buildEncodingField;
    private TextFieldWithBrowseButton buildDirField;
    private JTextField buildLogEncodingField;

    public MQL4CompilerRunnerEditor(@NotNull Project project) {
        new MQL4FileSelector(project).setField(fileField);
        new MQL4BuildDirSelector(project).setField(buildDirField);
        sdkComboBox.project = project;
    }

    @Override
    protected void resetEditorFrom(@NotNull MQL4RunCompilerConfiguration configuration) {
        Sdk[] sdks = ProjectJdkTable.getInstance().getAllJdks();
        List<Sdk> mql4Sdks = Arrays.stream(sdks).filter(s -> s.getSdkType() instanceof MQL4SdkType).collect(Collectors.toList());
        sdkComboBox.comboBox.addItem(null);
        mql4Sdks.forEach(s -> sdkComboBox.comboBox.addItem(s));

        Sdk selectedSdk = configuration.getSdk();
        if (selectedSdk == null) {
            Project project = configuration.getProject();
            selectedSdk = ProjectRootManager.getInstance(project).getProjectSdk();
        }
        sdkComboBox.comboBox.setSelectedSdk(selectedSdk);
        fileField.setText(configuration.fileToCompile);
        buildEncodingField.setText(configuration.buildEncoding);
        buildDirField.setText(configuration.buildDir);
        buildLogEncodingField.setText(configuration.buildLogEncoding);
    }

    @Override
    protected void applyEditorTo(@NotNull MQL4RunCompilerConfiguration configuration) throws ConfigurationException {
        String selectedSdkName = sdkComboBox.comboBox.getSelectedSdkName();
        configuration.sdkName = selectedSdkName == null ? "" : selectedSdkName;
        configuration.fileToCompile = fileField.getText().replace('\\', '/'); // convert to presentable path
        configuration.buildEncoding = buildEncodingField.getText();
        configuration.buildDir = buildDirField.getText();
        configuration.buildLogEncoding = buildLogEncodingField.getText();
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return rootPanel;
    }

    private static class MQL4FileSelector extends BrowseModuleValueActionListener<JTextField> {
        @NotNull
        private final Project project;

        public MQL4FileSelector(@NotNull Project project) {
            super(project);
            this.project = project;
        }

        @Nullable
        @Override
        protected String showDialog() {
            FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFileDescriptor(MQL4FileType.INSTANCE);
            descriptor.setTitle("Select MQL4 File…");
            descriptor.setRoots(project.getBaseDir());

            FileChooserDialog chooser = FileChooserFactory.getInstance().createFileChooser(descriptor, project, null);
            VirtualFile[] selectedFiles = chooser.choose(project);
            if (selectedFiles.length == 0) {
                return null;
            }
            VirtualFile result = selectedFiles[0];
            String projectPath = project.getPresentableUrl();
            String resultPath = result.getPresentableUrl();
            if (projectPath != null && resultPath.startsWith(projectPath)) {
                return resultPath.substring(projectPath.length() + 1);
            }
            return resultPath;
        }
    }

    private class MQL4BuildDirSelector extends BrowseModuleValueActionListener<JTextField> {
        @NotNull
        private final Project project;

        public MQL4BuildDirSelector(@NotNull Project project) {
            super(project);
            this.project = project;
        }

        @Nullable
        @Override
        protected String showDialog() {
            FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor().withShowHiddenFiles(true);
            descriptor.setTitle("Select Build Folder …");
            Sdk sdk = sdkComboBox.comboBox.getSelectedSdk();
            FileChooserDialog chooser = FileChooserFactory.getInstance().createFileChooser(descriptor, project, null);

            // Default dir is ${SDK_ROOT} or ${SDK_ROOT}/MQL4/Experts if exists
            VirtualFile sdkHomeFile = sdk == null ? null : sdk.getHomeDirectory();
            String mqlDirName = "MQL4";
            VirtualFile mqlDir = sdkHomeFile == null ? null : sdkHomeFile.findChild(mqlDirName);
            if (mqlDir == null && sdkHomeFile != null) {
                mqlDir = sdkHomeFile.findChild("MQL5");
                if (mqlDir != null) {
                    mqlDirName = "MQL5";
                }
            }

            VirtualFile expertsDir = mqlDir == null ? null : mqlDir.findChild("Experts");
            // on Windows try make default build dir in the same location with 'Include' folder (user's AppData)
            if (OSUtils.isWindowsOS() && sdkHomeFile != null && sdkHomeFile.findChild(mqlDirName + "/Include") == null) {
                VirtualFile appDataSdkHome = OSUtils.findAppDataSDKHome(sdk);
                if (appDataSdkHome != null) {
                    mqlDir = appDataSdkHome.findChild(mqlDirName);
                    assert mqlDir != null;
                    expertsDir = mqlDir.findChild("Experts");
                }
            }
            VirtualFile defaultDir = Stream.of(expertsDir, mqlDir, sdkHomeFile).filter(Objects::nonNull).findFirst().orElse(null);

            VirtualFile[] selectedFolders = sdkHomeFile == null ? chooser.choose(null) : chooser.choose(null, defaultDir);
            if (selectedFolders.length == 0) {
                return null;
            }
            VirtualFile result = selectedFolders[0];
            String sdkPath = sdkHomeFile == null ? null : sdkHomeFile.getPresentableUrl();
            String resultPath = result.getPresentableUrl();
            if (sdkPath != null && resultPath.startsWith(sdkPath)) {
                return resultPath.substring(sdkPath.length() + 1);
            }
            return resultPath;
        }
    }
}
