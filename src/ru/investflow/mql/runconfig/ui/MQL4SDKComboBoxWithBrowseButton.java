package ru.investflow.mql.runconfig.ui;

import com.intellij.openapi.application.Result;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.SdkConfigurationUtil;
import com.intellij.ui.ComboboxWithBrowseButton;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.sdk.MQL4SdkType;

public class MQL4SDKComboBoxWithBrowseButton extends ComboboxWithBrowseButton {

    @NotNull
    public final MQL4SDKComboBox comboBox;

    public Project project;

    public MQL4SDKComboBoxWithBrowseButton() {
        super(new MQL4SDKComboBox());
        this.comboBox = (MQL4SDKComboBox) getComboBox();

        addActionListener(e -> SdkConfigurationUtil.selectSdkHome(MQL4SdkType.INSTANCE, sdkHome -> {
            Sdk sdk = SdkConfigurationUtil.createAndAddSDK(sdkHome, MQL4SdkType.INSTANCE);
            if (sdk == null) {
                return;
            }
            new WriteAction<Void>() {
                @Override
                protected void run(@NotNull Result result) throws Throwable {
                    ProjectJdkTable.getInstance().addJdk(sdk);
                }
            }.execute();
            comboBox.addItem(sdk);
            comboBox.setSelectedSdk(sdk);
        }));
    }
}
