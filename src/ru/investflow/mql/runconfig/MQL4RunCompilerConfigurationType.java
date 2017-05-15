package ru.investflow.mql.runconfig;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.MQL4Icons;

/**
 * See http://www.jetbrains.org/intellij/sdk/docs/basics/run_configurations/run_configuration_management.html
 */
public class MQL4RunCompilerConfigurationType extends ConfigurationTypeBase {

    private static final String CONFIGURATION_TYPE_ID = "MQL4RunCompilerConfigurationTypeId";

    public MQL4RunCompilerConfigurationType() {
        super(CONFIGURATION_TYPE_ID, "MQL4 Build", "Build single MQL4 file", MQL4Icons.File);
        addFactory(new ConfigurationFactory(this) {
            @NotNull
            @Override
            public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
                return new MQL4RunCompilerConfiguration(project, this, "MQL4 Build");
            }
        });
    }

    @NotNull
    public static MQL4RunCompilerConfigurationType getInstance() {
        return Extensions.findExtension(CONFIGURATION_TYPE_EP, MQL4RunCompilerConfigurationType.class);
    }
}
