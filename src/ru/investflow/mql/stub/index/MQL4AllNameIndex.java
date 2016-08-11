package ru.investflow.mql.stub.index;

import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.MQL4NamedElement;

/**
 * Stub index to store all names defined in the project; specifically for the "go to symbol" feature.
 */
public class MQL4AllNameIndex extends StringStubIndexExtension<MQL4NamedElement> {
    public static final StubIndexKey<String, MQL4NamedElement> KEY = StubIndexKey.createIndexKey("mql4.all.name");
    public static final int VERSION = 0;

    @Override
    public int getVersion() {
        return super.getVersion() + VERSION;
    }

    @NotNull
    @Override
    public StubIndexKey<String, MQL4NamedElement> getKey() {
        return KEY;
    }
}