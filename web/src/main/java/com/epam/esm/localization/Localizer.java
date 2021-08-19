package com.epam.esm.localization;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class Localizer {

    //runtime localization
    //switcher global
    //param in HTTP header (servlet filters)

    public Localizer() {

    }

    public String getLocalizedMessage(int errorCode) {
        Locale locale = LocaleContextHolder.getLocale();
        ResourceBundle localeBundle = ResourceBundle.getBundle("local", locale);
        return localeBundle.getString(String.valueOf(errorCode));
    }


}
