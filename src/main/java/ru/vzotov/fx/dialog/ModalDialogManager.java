package ru.vzotov.fx.dialog;

public interface ModalDialogManager {
    void mount(ModalDialog<?> dialog);

    void unmount(ModalDialog<?> dialog);

}
