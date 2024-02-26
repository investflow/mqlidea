package ru.investflow.mql.runconfig.ui;

import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.SimpleListCellRenderer;
import com.intellij.ui.SortedComboBoxModel;
import javax.swing.JList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.annotation.UsedIndirectly;
import ru.investflow.mql.util.SdkByNameComparator;

public class MQL4SDKComboBox extends ComboBox<Sdk> {

    @NotNull
    private final SortedComboBoxModel<Sdk> model;

    @UsedIndirectly
    public MQL4SDKComboBox() {
        this(new SortedComboBoxModel<>(SdkByNameComparator.INSTANCE));
    }

    public MQL4SDKComboBox(@NotNull SortedComboBoxModel<Sdk> model) {
        super(model);
        this.model = model;
        setRenderer(new SimpleListCellRenderer<>() {
            @Override
            public void customize(@NotNull JList<? extends Sdk> list, Sdk value, int index, boolean selected, boolean hasFocus) {
                setText(value == null ? "[none]" : value.getName());
            }
        });
    }

    @Override
    public void addItem(Sdk item) {
        model.add(item);
    }

    @Nullable
    public String getSelectedSdkName() {
        Sdk sdk = getSelectedSdk();
        return sdk == null ? null : sdk.getName();
    }

    @Nullable
    public Sdk getSelectedSdk() {
        return model.getSelectedItem();
    }

    public void setSelectedSdk(@Nullable Sdk sdk) {
        model.setSelectedItem(sdk);
    }
}
