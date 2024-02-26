package ru.investflow.mql.runconfig;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RunnerSettings;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.AsyncProgramRunner;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.RunContentBuilder;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.concurrency.Promise;
import org.jetbrains.concurrency.Promises;

public class MQL4CompilerRunner extends AsyncProgramRunner<RunnerSettings> {

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
    protected Promise<RunContentDescriptor> execute(@NotNull ExecutionEnvironment executionEnvironment,
                                                    @NotNull RunProfileState runProfileState) throws ExecutionException {
        FileDocumentManager.getInstance().saveAllDocuments();
        Executor executor = executionEnvironment.getExecutor();
        ExecutionResult executionResult = runProfileState.execute(executor, MQL4CompilerRunner.this);

        if (executionResult == null) {
            return Promises.resolvedPromise(null);
        }
        RunContentBuilder contentBuilder = new RunContentBuilder(executionResult, executionEnvironment);
        RunContentDescriptor contentToReuse = executionEnvironment.getContentToReuse();
        return Promises.resolvedPromise(contentBuilder.showRunContent(contentToReuse));
    }

}
