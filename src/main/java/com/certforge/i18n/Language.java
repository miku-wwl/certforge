package com.certforge.i18n;

import java.util.Locale;

public enum Language {
    ZH("zh"),
    EN("en");

    private final String code;

    Language(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }

    public static Language fromLocale(Locale locale) {
        return locale != null && "en".equalsIgnoreCase(locale.getLanguage()) ? EN : ZH;
    }
}
