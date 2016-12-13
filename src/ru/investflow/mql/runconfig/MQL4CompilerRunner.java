package ru.investflow.mql.runconfig;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.RunProfileStarter;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.AsyncGenericProgramRunner;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.concurrency.AsyncPromise;
import org.jetbrains.concurrency.Promise;

public class MQL4CompilerRunner extends AsyncGenericProgramRunner {

    private static final String RUNNER_ID = "MQL4CompilerRunnerId";

    @NotNull
    @Override
    public String getRunnerId() {
        return RUNNER_ID;
    }

    @Override
    public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
        return profile instanceof MQL4RunCompilerConfiguration
                && (DefaultRunExecutor.EXECUTOR_ID.equals(executorId) || DefaultDebugExecutor.EXECUTOR_ID.equals(executorId));
    }

    @NotNull
    @Override
    protected Promise<RunProfileStarter> prepare(@NotNull ExecutionEnvironment environment, @NotNull RunProfileState state) throws ExecutionException {
        FileDocumentManager.getInstance().saveAllDocuments();

        //TODO:
        return new AsyncPromise<>();
    }

}
