package ru.investflow.mql.index;

import com.intellij.psi.stubs.StubIndexKey;
import ru.investflow.mql.psi.impl.MQL4ClassElement;
import ru.investflow.mql.psi.impl.MQL4FunctionElement;

public class MQL4IndexKeys {

    public static final StubIndexKey<String, MQL4ClassElement> CLASS_NAME_INDEX_KEY = StubIndexKey.createIndexKey("mql4.className.index");
    public static final StubIndexKey<String, MQL4FunctionElement> FUNCTION_NAME_INDEX_KEY = StubIndexKey.createIndexKey("mql4.functionName.index");


}
