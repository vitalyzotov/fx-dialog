package ru.vzotov.fx.dialog;

import java.util.ResourceBundle;
import java.util.spi.ResourceBundleControlProvider;

import static ru.vzotov.fx.dialog.DialogMessagesControl.RESOURCE_BUNDLE_NAME;

public class DialogMessagesControlProvider implements ResourceBundleControlProvider {

    @Override
    public ResourceBundle.Control getControl(String baseName) {
        if (baseName.equals(RESOURCE_BUNDLE_NAME)) {
            return new DialogMessagesControl();
        }

        return null;
    }

}
