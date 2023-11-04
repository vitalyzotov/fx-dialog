package ru.vzotov.fx.dialog;

import java.util.Locale;
import java.util.ResourceBundle;

public class DialogMessagesControl extends ResourceBundle.Control {

    public static String RESOURCE_BUNDLE_NAME = "ru.vzotov.fx.dialog.DialogResources";

    @Override
    public String toBundleName(String baseName, Locale locale) {
        if (locale.getVariant() != null) {
            //todo: magic strings
            baseName = baseName.replace("ru.vzotov.hb.base", "ru.vzotov.hb.app.resources");
            locale = new Locale(locale.getLanguage(), locale.getCountry());

            return super.toBundleName(baseName, locale);
        }

        return super.toBundleName(baseName, locale);
    }
}
