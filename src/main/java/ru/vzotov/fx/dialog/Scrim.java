package ru.vzotov.fx.dialog;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import ru.vzotov.fx.dialog.skin.ScrimSkin;

/**
 * Затемнение экрана, защищает от нажатий
 */
public class Scrim extends Control {

    private static final String DEFAULT_STYLE_CLASS = "scrim";

    public Scrim() {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        setFocusTraversable(false);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ScrimSkin<>(this);
    }
}
