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
import com.intellij.ui.SimpleTextAttributes;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.util.TextUtils;

import static ru.investflow.mql.util.OSUtils.isWine;

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
            int startPos = entireLength - line.length();
            Project p = MQL4CompilerCommandLineState.this.getEnvironment().getProject();
            VirtualFile fileToCompile = runConfig.getFileToCompileAsVirtualFile();
            if (fileToCompile == null) {
                return null;
            }
            if (line.startsWith("Result: ")) {
                return new Filter.Result(startPos, entireLength, null, SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES.toTextAttributes());
            }
            if (line.startsWith(fileToCompile.getName() + " : information: ")) {
                return new Filter.Result(startPos, entireLength, null, SimpleTextAttributes.GRAYED_BOLD_ATTRIBUTES.toTextAttributes());
            }
            String fileLocationPrefix = fileToCompile.getName() + "(";
            int fileLocationEndIdx = line.indexOf(')');
            if (line.startsWith(fileLocationPrefix) && fileLocationEndIdx > fileLocationPrefix.length()) {
                String location = line.substring(fileLocationPrefix.length(), fileLocationEndIdx);
                String[] rowAndCol = location.split(",");
                if (rowAndCol.length == 2) {
                    int row = Integer.parseInt(rowAndCol[0]) - 1;
                    int col = Integer.parseInt(rowAndCol[1]) - 1;
                    HyperlinkInfo hyperlinkInfo = new OpenFileHyperlinkInfo(p, fileToCompile, row, col);
                    return new Filter.Result(startPos, startPos + fileLocationEndIdx, hyperlinkInfo);
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
        String metaeditorExePath = sdk.getHomePath() + "/metaeditor64.exe";
        if (!new File(metaeditorExePath).exists()) {
            metaeditorExePath = sdk.getHomePath() + "/metaeditor.exe";
        }
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

        if (!fileToCompile.toPath().equals(copiedFileToCompile.toPath())) { // copy file only if build dir is different from current dir.
            VirtualFile virtualFileToCompile = runConfig.getFileToCompileAsVirtualFile();
            try {
                Charset fileToCompileCharset = virtualFileToCompile != null ? virtualFileToCompile.getCharset() : StandardCharsets.UTF_8;
                Charset copiedFileCharset = runConfig.buildEncoding.isEmpty() ? fileToCompileCharset : Charset.forName(runConfig.buildEncoding);
                copyFile(fileToCompile, fileToCompileCharset, copiedFileToCompile, copiedFileCharset);
            } catch (Exception e) {
                throw new ExecutionException("Failed to copy file [" + fileToCompile + "] to Build Dir: [" + buildDir + "]");
            }
        }

        // Run MetaEditor compiler
        GeneralCommandLine cmd = new GeneralCommandLine();
        cmd.setExePath(isWine() ? "wine" : metaeditorExePath);
        cmd.setWorkDirectory(buildDir);
        if (isWine()) {
            cmd.addParameter(metaeditorExePath);
            cmd.getEnvironment().put("WINEDEBUG", "-all"); // disable wine related warnings and debug logs.
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
                    byte[] outputBytes = Files.readAllBytes(Paths.get(logFile.toURI()));
                    String rawLogInCp1252 = new String(outputBytes, StandardCharsets.UTF_16);
                    String rawLog = rawLogInCp1252;
                    if (!TextUtils.isEmpty(runConfig.buildLogEncoding)) {
                        try {
                            rawLog = new String(rawLogInCp1252.getBytes("Cp1252"), runConfig.buildLogEncoding);
                        } catch (Exception ignored) {
                            //todo: use logger
                        }
                    }
                    console.print(rawLog, ConsoleViewContentType.NORMAL_OUTPUT);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to read log file: " + logFile);
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
