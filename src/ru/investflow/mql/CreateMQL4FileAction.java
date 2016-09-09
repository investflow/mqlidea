package ru.investflow.mql;

import com.intellij.ide.actions.CreateFileAction;

/**
 * The "New MQL4 File" action.
 */
public class CreateMQL4FileAction extends CreateFileAction {
    public CreateMQL4FileAction() {
        super(MQL4PluginResources.message("action.New-MQL-File.text"), MQL4PluginResources.message("action.New-MQL-File.text"), MQL4Icons.FILE);
    }

    @Override
    protected String getDefaultExtension() {
        return MQL4FileType.DEFAULT_EXTENSION;
    }
}
