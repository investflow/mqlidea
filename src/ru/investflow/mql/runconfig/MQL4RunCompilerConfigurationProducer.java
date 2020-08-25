
package ru.investflow.mql.runconfig;

import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.RunConfigurationProducer;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class MQL4RunCompilerConfigurationProducer extends RunConfigurationProducer<MQL4RunCompilerConfiguration> {

    protected MQL4RunCompilerConfigurationProducer() {
        super(MQL4RunCompilerConfigurationType.getInstance());
    }

    @Override
    protected boolean setupConfigurationFromContext(@NotNull MQL4RunCompilerConfiguration configuration, @NotNull ConfigurationContext context, @NotNull Ref<PsiElement> sourceElement) {
        return false;
    }

    @Override
    public boolean isConfigurationFromContext(@NotNull MQL4RunCompilerConfiguration configuration, @NotNull ConfigurationContext context) {
        return false;
    }
}
