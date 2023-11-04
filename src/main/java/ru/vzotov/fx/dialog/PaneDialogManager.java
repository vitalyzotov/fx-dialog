package ru.vzotov.fx.dialog;

import javafx.scene.layout.StackPane;

public class PaneDialogManager implements ModalDialogManager {

    private final StackPane container;

    public PaneDialogManager(StackPane container) {
        this.container = container;
    }

    @Override
    public void mount(ModalDialog<?> dialog) {
        container.getChildren().add(0, dialog);
        dialog.setMounted(true);

    }

    @Override
    public void unmount(ModalDialog<?> dialog) {
        dialog.setMounted(false);
        container.getChildren().remove(dialog);
    }
}
