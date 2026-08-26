package com.certforge.i18n;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LanguageContext {
    public Language current() {
        return Language.fromLocale(LocaleContextHolder.getLocale());
    }
}
