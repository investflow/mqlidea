package ru.investflow.mql.sdk;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.projectRoots.AdditionalDataConfigurable;
import com.intellij.openapi.projectRoots.SdkAdditionalData;
import com.intellij.openapi.projectRoots.SdkModel;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.openapi.projectRoots.SdkType;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.MQL4Icons;
import ru.investflow.mql.util.OSUtils;

import javax.swing.Icon;
import java.io.File;

public class MQL4SdkType extends SdkType {

    protected static final Logger log = Logger.getInstance(MQL4SdkType.class);

    public static final String MQL4_SDK_TYPE_ID = "MQL4 SDK";

    /**
     * Note: this is not singleton in runtime. Intellij IDEA creates many instance of SdkType
     */
    @NotNull
    public final static MQL4SdkType INSTANCE = new MQL4SdkType();

    public MQL4SdkType() {
        super(MQL4_SDK_TYPE_ID);
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return MQL4Icons.File;
    }

    @NotNull
    @Override
    public Icon getIconForAddAction() {
        return getIcon();
    }

    @Nullable
    @Override
    public String suggestHomePath() {
        if (OSUtils.isWindowsOS()) {
            File res = new File("c:/Program Files (x86)");
            if (res.isDirectory()) {
                return res.getAbsolutePath();
            }
            res = new File("c:/Program Files");
            if (res.isDirectory()) {
                return res.getAbsolutePath();
            }
            return new File("C:/").getAbsolutePath();
        }
        String home = System.getProperty("user.home");
        if (home == null) {
            return null;
        }
        return home + "/.wine/drive_c/Program Files (x86)/";
    }

    @Override
    public boolean isValidSdkHome(@NotNull String path) {
        log.debug("Validating sdk path: " + path);
        if (!new File(path, "metaeditor.exe").exists()) {
            log.debug("metaeditor.exe not found!");
            return false;
        }
        return true;
    }

    @NotNull
    @Override
    public String suggestSdkName(@Nullable String currentSdkName, @NotNull String sdkHome) {
        return "MQL4 SDK " + new File(sdkHome).getName();
    }

    @Nullable
    @Override
    public String getVersionString(String sdkHome) {
        return "MQL4";
    }

    @NotNull
    @Override
    public String getPresentableName() {
        return "MQL4 SDK";
    }


    @Nullable
    @Override
    public AdditionalDataConfigurable createAdditionalDataConfigurable(@NotNull SdkModel sdkModel, @NotNull SdkModificator sdkModificator) {
        return null;
    }

    @Override
    public void saveAdditionalData(@NotNull SdkAdditionalData sdkAdditionalData, @NotNull Element element) {
    }
}
