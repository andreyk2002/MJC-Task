package com.epam.esm.localization;

import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class Localizer {

    //runtime localization
    //switcher global
    //param in HTTP header (servlet filters)
    private final ResourceBundle localeBundle;

    public Localizer() {
        Locale current = new Locale("ru", "RU");
        localeBundle = ResourceBundle.getBundle("local", current);
    }

    public String getLocalizedMessage(String key) {
        return localeBundle.getString(key);
    }


}
