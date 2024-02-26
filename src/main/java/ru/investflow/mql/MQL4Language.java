package ru.investflow.mql;

import com.intellij.lang.Language;

public class MQL4Language extends Language {

    public static final MQL4Language INSTANCE = new MQL4Language();

    private MQL4Language() {
        super("MQL4");
    }
}
