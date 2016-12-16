package ru.investflow.mql.runconfig.ui;

import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.ListCellRendererWrapper;
import com.intellij.ui.SortedComboBoxModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.annotation.UsedIndirectly;
import ru.investflow.mql.util.SdkByNameComparator;

import javax.swing.JList;

public class MQL4SDKComboBox extends ComboBox<Sdk> {

    @NotNull
    private final SortedComboBoxModel<Sdk> model;
    private String selectedSdkName;

    @UsedIndirectly
    public MQL4SDKComboBox() {
        this(new SortedComboBoxModel<>(SdkByNameComparator.INSTANCE));
    }

    public MQL4SDKComboBox(@NotNull SortedComboBoxModel<Sdk> model) {
        super(model);
        this.model = model;
        setRenderer(new ListCellRendererWrapper<Sdk>() {
            @Override
            public void customize(JList list, Sdk sdk, int index, boolean selected, boolean hasFocus) {
                setText(sdk == null ? "[none]" : sdk.getName());
            }
        });
    }

    @Override
    public void addItem(Sdk item) {
        model.add(item);
    }

    @Nullable
    public String getSelectedSdkName() {
        Sdk sdk = model.getSelectedItem();
        return sdk == null ? null : sdk.getName();
    }

    public void setSelectedSdk(@Nullable Sdk sdk) {
        model.setSelectedItem(sdk);
    }
}
