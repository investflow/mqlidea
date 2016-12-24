package ru.investflow.mql.runconfig;

import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.filters.Filter;
import com.intellij.execution.filters.HyperlinkInfo;
import com.intellij.execution.filters.OpenFileHyperlinkInfo;
import com.intellij.execution.process.KillableColoredProcessHandler;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static ru.investflow.mql.util.OSUtils.isWindowsOS;

class MQL4CompilerCommandLineState extends CommandLineState {

    @NotNull
    private final MQL4RunCompilerConfiguration runConfig;

    @Nullable
    private ConsoleView console;

    MQL4CompilerCommandLineState(@NotNull MQL4RunCompilerConfiguration runConfig, @NotNull ExecutionEnvironment environment) {
        super(environment);
        this.runConfig = runConfig;
    }

    @NotNull
    @Override
    public ExecutionResult execute(@NotNull Executor executor, @NotNull ProgramRunner runner) throws ExecutionException {
        DefaultExecutionResult result = (DefaultExecutionResult) super.execute(executor, runner);
        console = (ConsoleView) result.getExecutionConsole();
        console.addMessageFilter((line, entireLength) -> {
            Project p = MQL4CompilerCommandLineState.this.getEnvironment().getProject();
            VirtualFile fileToCompile = runConfig.getFileToCompileAsVirtualFile();
            if (fileToCompile != null) {
                String fileLocationPrefix = fileToCompile.getName() + "(";
                int fileLocationEndIdx = line.indexOf(')');
                if (line.startsWith(fileLocationPrefix) && fileLocationEndIdx > fileLocationPrefix.length()) {
                    String location = line.substring(fileLocationPrefix.length(), fileLocationEndIdx);
                    String[] rowAndCol = location.split(",");
                    if (rowAndCol.length == 2) {
                        int row = Integer.parseInt(rowAndCol[0]) - 1;
                        int col = Integer.parseInt(rowAndCol[1]) - 1;
                        HyperlinkInfo hyperlinkInfo = new OpenFileHyperlinkInfo(p, fileToCompile, row, col);
                        int startPos = entireLength - line.length();
                        return new Filter.Result(startPos, startPos + fileLocationEndIdx, hyperlinkInfo);
                    }
                }
            }
            return null;
        });
        return result;
    }

    @NotNull
    @Override
    protected ProcessHandler startProcess() throws ExecutionException {
        Sdk sdk = runConfig.getSdk();
        if (sdk == null) {
            throw new ExecutionException("SDK not found: " + runConfig.sdkName);
        }
        boolean useWine = !isWindowsOS();
        String metaeditorExePath = sdk.getHomePath() + "/metaeditor.exe";
        File buildDir = runConfig.getBuildDirAsFile();
        if (buildDir == null || !buildDir.isDirectory()) {
            throw new ExecutionException("Build Dir not found: " + runConfig.buildDir);
        }
        // Copy mql4 file to build folder. Replace original file. Re-code if needed.
        File fileToCompile = runConfig.getFileToCompileAsFile();
        if (fileToCompile == null || !fileToCompile.isFile()) {
            throw new ExecutionException("File not found: " + runConfig.fileToCompile);
        }
        File copiedFileToCompile = new File(buildDir, fileToCompile.getName());
        try {
            Charset fileToCompileCharset = StandardCharsets.UTF_8; //todo: get info from editor
            Charset copiedFileCharset = runConfig.buildEncoding.isEmpty() ? fileToCompileCharset : Charset.forName(runConfig.buildEncoding);
            copyFile(fileToCompile, fileToCompileCharset, copiedFileToCompile, copiedFileCharset);
        } catch (Exception e) {
            throw new ExecutionException("Failed to copy file [" + fileToCompile + "] to Build Dir: [" + buildDir + "]");
        }

        // Run MetaEditor compiler
        GeneralCommandLine cmd = new GeneralCommandLine();
        cmd.setExePath(useWine ? "wine" : metaeditorExePath);
        cmd.setWorkDirectory(buildDir);
        if (useWine) {
            cmd.addParameter(metaeditorExePath);
        }
        cmd.addParameter("/compile:" + copiedFileToCompile.getName());
        cmd.addParameter("/log");

        cmd.withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE);

        OSProcessHandler processHandler = new KillableColoredProcessHandler(cmd) {

            protected void onOSProcessTerminated(int exitCode) {
                super.onOSProcessTerminated(exitCode);
                String logFileName = copiedFileToCompile.getName().replace(".mq4", ".log");
                File logFile = new File(copiedFileToCompile.getParent(), logFileName);
                if (!logFile.exists() || console == null) {
                    return;
                }
                try {
                    String rawLog = new String(Files.readAllBytes(Paths.get(logFile.toURI())), StandardCharsets.UTF_16LE);
                    console.print(rawLog, ConsoleViewContentType.NORMAL_OUTPUT);
                } catch (IOException e) {
                    //todo: log
                }
            }
        };
        ProcessTerminatedListener.attach(processHandler, getEnvironment().getProject());

        return processHandler;
    }


    private static void copyFile(@NotNull File srcFile, @NotNull Charset srcCharset, @NotNull File dstFile, @NotNull Charset dstCharset) throws IOException {
        Path fromPath = Paths.get(srcFile.toURI());
        byte[] srcBytes = Files.readAllBytes(fromPath);
        byte[] dstBytes = srcBytes;
        if (dstCharset != srcCharset) {
            dstBytes = new String(srcBytes, srcCharset).getBytes(dstCharset);
        }
        Path toPath = Paths.get(dstFile.toURI());
        Files.write(toPath, dstBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}