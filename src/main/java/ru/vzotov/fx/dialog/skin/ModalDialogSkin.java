package ru.vzotov.fx.dialog.skin;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.SkinBase;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2OutlinedAL;
import ru.vzotov.fx.dialog.ModalDialog;
import ru.vzotov.fx.dialog.Scrim;

import java.util.Locale;
import java.util.ResourceBundle;

import static ru.vzotov.fx.dialog.DialogMessagesControl.RESOURCE_BUNDLE_NAME;
import static ru.vzotov.fx.dialog.ModalDialog.ModalDialogEvent.MODAL_DIALOG_CANCEL;
import static ru.vzotov.fx.dialog.ModalDialog.ModalDialogEvent.MODAL_DIALOG_OK;

public class ModalDialogSkin<T extends ModalDialog<?>> extends SkinBase<T> {

    private final Scrim scrim;

    protected static ResourceBundle R = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME, new Locale(Locale.getDefault().getLanguage()));

    public ModalDialogSkin(T control) {
        super(control);

        control.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (KeyCode.ESCAPE == keyEvent.getCode()) {
                getSkinnable().hide();
            }
        });

        scrim = new Scrim();
        scrim.setManaged(false);
        getChildren().add(0, scrim);

        control.requestLayout();
    }

    protected Hyperlink createCloseButton() {
        final Hyperlink btnClose = new Hyperlink(null, FontIcon.of(Material2OutlinedAL.CLOSE));
        btnClose.setOnAction(action -> {
            getSkinnable().hide();
            action.consume();
        });
        return btnClose;
    }

    protected Button createConfirmButton() {
        final Button btnOk = new Button(R.getString("dialog.ok"));
        btnOk.setOnAction(this::okButtonPressed);
        return btnOk;
    }

    protected void okButtonPressed(ActionEvent action) {
        getSkinnable().fireEvent(new ModalDialog.ModalDialogEvent(MODAL_DIALOG_OK));
        action.consume();
    }

    protected Button createCancelButton() {
        final Button btnCancel = new Button(R.getString("dialog.cancel"));
        btnCancel.setOnAction(this::cancelButtonPressed);
        return btnCancel;
    }

    protected void cancelButtonPressed(ActionEvent action) {
        getSkinnable().fireEvent(new ModalDialog.ModalDialogEvent(MODAL_DIALOG_CANCEL));
        action.consume();
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        super.layoutChildren(contentX, contentY, contentWidth, contentHeight);
        scrim.resizeRelocate(contentX, contentY, contentWidth, contentHeight);
        //scrim.toBack();
    }

    @Override
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return Double.MAX_VALUE;
    }
}
