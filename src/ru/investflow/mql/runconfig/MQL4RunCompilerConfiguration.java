package ru.investflow.mql.runconfig;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.vfs.VirtualFile;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.runconfig.ui.MQL4CompilerRunnerEditor;
import ru.investflow.mql.sdk.MQL4SdkType;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.intellij.openapi.util.JDOMExternalizerUtil.addElementWithValueAttribute;
import static com.intellij.openapi.util.JDOMExternalizerUtil.getFirstChildValueAttribute;

public class MQL4RunCompilerConfiguration extends RunConfigurationBase {

    /**
     * Selected SDK name.
     * There is a difference between 'null' and empty string values:
     * null -> the value was never set
     * empty -> the value was manually set to 'none'
     */
    @Nullable
    public String sdkName;

    /**
     * File to compile. Either full path of sub-path in SDK dir.
     */
    @NotNull
    public String fileToCompile = "";

    /**
     * Build dir - a dir to copy file to before compilation start..
     */
    @NotNull
    public String buildDir = "";

    /**
     * File encoding to convert MQL4 file before passing it to compiler.
     */
    @SuppressWarnings("InjectedReferences")
    @NotNull
    public String buildEncoding = "";

    protected MQL4RunCompilerConfiguration(@NotNull Project project, @NotNull ConfigurationFactory factory, String name) {
        super(project, factory, name);
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new MQL4CompilerRunnerEditor(getProject());
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
        if (!buildEncoding.isEmpty()) {
            try {
                Charset.forName(buildEncoding);
            } catch (UnsupportedCharsetException ignored) {
                throw new RuntimeConfigurationException("Encoding is not supported: " + buildEncoding);
            }
        }
        if (fileToCompile.isEmpty()) {
            throw new RuntimeConfigurationException("No MQL4 file selected.");
        }
        VirtualFile file = getFileToCompileAsVirtualFile();
        if (file == null || !file.exists()) {
            throw new RuntimeConfigurationException("File not found: " + fileToCompile);
        }

        if (sdkName == null || sdkName.isEmpty()) {
            throw new RuntimeConfigurationException("No SDK selected.");
        }
        Sdk sdk = getSdk();
        if (sdk == null) {
            throw new RuntimeConfigurationException("SDK not found: " + sdkName);
        }

        if (buildDir.isEmpty()) {
            throw new RuntimeConfigurationException("No 'Build Dir' specified.");
        }
        File buildDirFile = getBuildDirAsFile();
        if (buildDirFile == null || !buildDirFile.isDirectory()) {
            throw new RuntimeConfigurationException("'Build Dir' is not a valid directory: " + buildDirFile);
        }
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) throws ExecutionException {
        return new MQL4CompilerCommandLineState(this, environment);
    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {
        super.writeExternal(element);
        addElementWithValueAttribute(element, "sdk", sdkName);
        addElementWithValueAttribute(element, "file", fileToCompile);
        addElementWithValueAttribute(element, "encoding", buildEncoding);
        addElementWithValueAttribute(element, "buildDir", buildDir);
    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {
        super.readExternal(element);
        sdkName = getFirstChildValueAttribute(element, "sdk");
        fileToCompile = Objects.toString(getFirstChildValueAttribute(element, "file"), "");
        buildEncoding = Objects.toString(getFirstChildValueAttribute(element, "encoding"), "");
        buildDir = Objects.toString(getFirstChildValueAttribute(element, "buildDir"), "");
    }

    @Nullable
    public Sdk getSdk() {
        if (sdkName == null || sdkName.isEmpty()) {
            return null;
        }
        Sdk[] sdks = ProjectJdkTable.getInstance().getAllJdks();
        List<Sdk> mql4Sdks = Arrays.stream(sdks).filter(s -> s.getSdkType() instanceof MQL4SdkType).collect(Collectors.toList());

        return mql4Sdks.stream()
                .filter(s -> s.getName().equals(sdkName))
                .findFirst()
                .orElse(null);
    }

    @Nullable
    public File getBuildDirAsFile() {
        if (buildDir.isEmpty()) {
            return null;
        }
        File res = new File(buildDir);
        if (res.isDirectory()) {
            return res;
        }
        Sdk sdk = getSdk();
        if (sdk == null) {
            throw new IllegalStateException("SDK not found: " + sdkName);
        }
        return new File(sdk.getHomePath() + "/" + buildDir);
    }

    @Nullable
    public File getFileToCompileAsFile() {
        if (fileToCompile.isEmpty()) {
            return null;
        }
        String projectPath = getProject().getPresentableUrl();
        if (projectPath != null && new File(projectPath, fileToCompile).exists()) {
            return new File(projectPath, fileToCompile);
        }
        return new File(fileToCompile);
    }

    @Nullable
    public VirtualFile getFileToCompileAsVirtualFile() {
        return getProject().getBaseDir().findFileByRelativePath(fileToCompile);
    }
}
