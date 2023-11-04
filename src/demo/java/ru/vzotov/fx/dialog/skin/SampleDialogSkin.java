package ru.vzotov.fx.dialog.skin;

import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ru.vzotov.fx.dialog.DialogSkinBuilder;
import ru.vzotov.fx.dialog.SampleDialog;

public class SampleDialogSkin<T extends SampleDialog> extends ModalDialogSkin<T> {
    public SampleDialogSkin(T control) {
        super(control);

        final Hyperlink btnClose;
        final Label lblDialogTitle;
        final VBox body;
        final Button btnOk;
        final Button btnCancel;

        final TextField name = new TextField();
        control.nameProperty().bindBidirectional(name.textProperty());

        final Node root = DialogSkinBuilder.start()
                .title(lblDialogTitle = new Label("Sample document"))
                .controls(btnClose = createCloseButton())
                .and().body(body = new VBox(name))
                .and().footer()
                .button(btnOk = createConfirmButton())
                .button(btnCancel = createCancelButton())
                .and().build();
        btnOk.disableProperty().bind(Bindings.not(control.validProperty()));

        getChildren().add(root);
        control.setOnShow(event -> {
            name.requestFocus();
        });


    }
}
