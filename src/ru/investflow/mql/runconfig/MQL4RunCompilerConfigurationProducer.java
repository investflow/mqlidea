
package ru.investflow.mql.runconfig;

import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.RunConfigurationProducer;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiElement;

public class MQL4RunCompilerConfigurationProducer extends RunConfigurationProducer<MQL4RunCompilerConfiguration> {

    protected MQL4RunCompilerConfigurationProducer() {
        super(MQL4RunCompilerConfigurationType.getInstance());
    }

    @Override
    protected boolean setupConfigurationFromContext(MQL4RunCompilerConfiguration configuration, ConfigurationContext context, Ref<PsiElement> sourceElement) {
        return false;
    }

    @Override
    public boolean isConfigurationFromContext(MQL4RunCompilerConfiguration configuration, ConfigurationContext context) {
        return false;
    }
}
