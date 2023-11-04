package ru.vzotov.fx.dialog.skin;

import javafx.scene.control.SkinBase;
import ru.vzotov.fx.dialog.Scrim;

public class ScrimSkin<T extends Scrim> extends SkinBase<T> {

    public ScrimSkin(T control) {
        super(control);
        control.requestLayout();
    }

}
