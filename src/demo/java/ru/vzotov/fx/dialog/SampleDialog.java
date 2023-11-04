package ru.vzotov.fx.dialog;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Skin;
import ru.vzotov.fx.dialog.skin.SampleDialogSkin;

public class SampleDialog extends ModalDialog<SampleDialog> {

    private static final String DEFAULT_STYLE_CLASS = "sample-dialog";

    public SampleDialog() {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        valid.bind(name.isNotEmpty());
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new SampleDialogSkin<>(this);
    }

    /**
     * Is valid
     */
    private final ReadOnlyBooleanWrapper valid = new ReadOnlyBooleanWrapper();

    public boolean isValid() {
        return valid.get();
    }

    public ReadOnlyBooleanProperty validProperty() {
        return valid.getReadOnlyProperty();
    }

    private void setValid(boolean valid) {
        this.valid.set(valid);
    }

    /**
     * Name
     */
    private final StringProperty name = new SimpleStringProperty();

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
