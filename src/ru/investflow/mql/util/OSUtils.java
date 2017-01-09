package ru.investflow.mql.util;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.vfs.StandardFileSystems;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class OSUtils {

    protected static final Logger log = Logger.getInstance(OSUtils.class);

    public static boolean isWine() {
        return !isWindowsOS();
    }

    public static boolean isWindowsOS() {
        String prop = System.getProperty("os.name");
        return prop == null || prop.startsWith("Windows");
    }

    @Nullable
    public static VirtualFile findAppDataSDKHome(@NotNull Sdk sdk) {
        String userHome = System.getProperty("user.home");
        if (userHome == null) {
            return null;
        }
        File terminalDir = new File(userHome, "Application Data/MetaQuotes/Terminal");
        if (!terminalDir.isDirectory()) {
            return null;
        }
        File[] files = terminalDir.listFiles();
        if (files == null) {
            return null;
        }
        VirtualFileSystem localFS = StandardFileSystems.local();
        for (File f : files) {
            if (!f.isDirectory()) {
                continue;
            }
            File originTxtFile = new File(f, "origin.txt");
            if (!originTxtFile.isFile()) {
                continue;
            }
            try {
                String origin = Files.readAllLines(originTxtFile.toPath(), StandardCharsets.UTF_16).stream().findFirst().orElse("");
                if (origin.isEmpty()) {
                    continue;
                }
                File originDir = new File(origin);
                if (!originDir.isDirectory()) {
                    continue;
                }
                VirtualFile res = localFS.findFileByPath(originDir.getAbsolutePath());
                if (res != null && res.equals(sdk.getHomeDirectory())) {
                    return localFS.findFileByPath(f.getAbsolutePath());
                }
            } catch (IOException e) {
                log.error("Error reading file: " + originTxtFile);
            }
        }
        return null;
    }
}
