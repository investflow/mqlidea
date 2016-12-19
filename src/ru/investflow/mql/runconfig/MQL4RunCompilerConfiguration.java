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
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.runconfig.ui.MQL4CompilerRunnerEditor;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Objects;

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
        // todo: check file exists

        if (sdkName == null || sdkName.isEmpty()) {
            throw new RuntimeConfigurationException("No SDK selected.");
        }
        //todo: check SDK exists
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) throws ExecutionException {
        return null;
    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {
        super.writeExternal(element);
        addElementWithValueAttribute(element, "sdk", sdkName);
        addElementWithValueAttribute(element, "file", fileToCompile);
        addElementWithValueAttribute(element, "encoding", buildEncoding);
    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {
        super.readExternal(element);
        sdkName = getFirstChildValueAttribute(element, "sdk");
        fileToCompile = Objects.toString(getFirstChildValueAttribute(element, "file"), "");
        buildEncoding = Objects.toString(getFirstChildValueAttribute(element, "encoding"), "");
    }

    @Override
    protected boolean isNewSerializationUsed() {
        return super.isNewSerializationUsed();
    }
}
