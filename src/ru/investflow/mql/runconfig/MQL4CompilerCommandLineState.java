package ru.investflow.mql.runconfig;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.KillableColoredProcessHandler;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.projectRoots.Sdk;
import org.jetbrains.annotations.NotNull;

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

    MQL4CompilerCommandLineState(@NotNull MQL4RunCompilerConfiguration runConfig, @NotNull ExecutionEnvironment environment) {
        super(environment);
        this.runConfig = runConfig;
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

        OSProcessHandler processHandler = new KillableColoredProcessHandler(cmd);
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