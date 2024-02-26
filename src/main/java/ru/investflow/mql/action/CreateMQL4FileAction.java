package ru.investflow.mql.action;

import com.intellij.ide.actions.CreateFileAction;
import ru.investflow.mql.MQL4FileType;
import ru.investflow.mql.MQL4Icons;
import ru.investflow.mql.MQL4PluginResources;

/**
 * The "New MQL4 File" action.
 */
public class CreateMQL4FileAction extends CreateFileAction {
    public CreateMQL4FileAction() {
        super(MQL4PluginResources.message("action.New-MQL-File.text"), MQL4PluginResources.message("action.New-MQL-File.description"), MQL4Icons.File);
    }

    @Override
    protected String getDefaultExtension() {
        return MQL4FileType.SOURCE_FILE_EXTENSION;
    }
}
