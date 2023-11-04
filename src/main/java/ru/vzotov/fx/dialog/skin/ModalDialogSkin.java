package ru.vzotov.fx.dialog.skin;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Control;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2OutlinedAL;
import ru.vzotov.fx.dialog.ModalDialog;
import ru.vzotov.fx.dialog.Scrim;

import java.util.Locale;
import java.util.ResourceBundle;

import static javafx.scene.layout.Priority.ALWAYS;
import static ru.vzotov.fx.dialog.DialogMessagesControl.RESOURCE_BUNDLE_NAME;
import static ru.vzotov.fx.dialog.ModalDialog.ModalDialogEvent.MODAL_DIALOG_CANCEL;
import static ru.vzotov.fx.dialog.ModalDialog.ModalDialogEvent.MODAL_DIALOG_OK;
import static ru.vzotov.fx.dialog.ModalDialog.STYLE_DIALOG_BODY;
import static ru.vzotov.fx.dialog.ModalDialog.STYLE_DIALOG_CONTENT;
import static ru.vzotov.fx.dialog.ModalDialog.STYLE_DIALOG_TITLE;
import static ru.vzotov.fx.utils.LayoutUtils.restyled;
import static ru.vzotov.fx.utils.LayoutUtils.styled;

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
        btnOk.setOnAction(action -> {
            getSkinnable().fireEvent(new ModalDialog.ModalDialogEvent(MODAL_DIALOG_OK));
            action.consume();
        });
        return btnOk;
    }

    protected Button createCancelButton() {
        final Button btnCancel = new Button(R.getString("dialog.cancel"));
        btnCancel.setOnAction(action -> {
            getSkinnable().fireEvent(new ModalDialog.ModalDialogEvent(MODAL_DIALOG_CANCEL));
            action.consume();
        });
        return btnCancel;
    }

    protected void build(String title, Control closeButton, Region content, ButtonBar buttons, String... dialogClasses) {
        final StackPane root = styled(restyled(new StackPane(), STYLE_DIALOG_CONTENT), dialogClasses);

        final Label lblDialogTitle = new Label(title, closeButton);

        final Region spacer = new Region();
        final HBox titleBox = styled(new HBox(lblDialogTitle, spacer), STYLE_DIALOG_TITLE);
        HBox.setHgrow(lblDialogTitle, ALWAYS);
        HBox.setHgrow(spacer, ALWAYS);

        final VBox box = styled(new VBox(titleBox, content), STYLE_DIALOG_BODY);
        if (buttons != null) {
            box.getChildren().add(buttons);
        }
        box.setFillWidth(true);
        VBox.setVgrow(content, Priority.ALWAYS);
        root.getChildren().addAll(box);

        getChildren().add(root);
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
