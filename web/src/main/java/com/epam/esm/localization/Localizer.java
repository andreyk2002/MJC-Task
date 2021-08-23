package com.epam.esm.localization;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class Localizer {

    public String getLocalizedMessage(int errorCode, Object... params) {
        Locale locale = LocaleContextHolder.getLocale();
        ResourceBundle localeBundle = ResourceBundle.getBundle("local", locale);
        String bundleString = localeBundle.getString(String.valueOf(errorCode));
        return MessageFormat.format(bundleString, params);
    }


}
