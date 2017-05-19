package ru.investflow.mql.index;

import com.intellij.psi.stubs.StubIndexKey;
import ru.investflow.mql.psi.impl.MQL4ClassElement;

public class MQL4IndexKeys {

    public static final StubIndexKey<String, MQL4ClassElement> CLASS_NAME_INDEX_KEY = StubIndexKey.createIndexKey("mql4.className.index");
}
