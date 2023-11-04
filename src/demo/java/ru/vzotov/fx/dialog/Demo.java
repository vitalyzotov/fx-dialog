package ru.vzotov.fx.dialog;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vzotov.fx.reactor.JavaFxScheduler;
import ru.vzotov.fxtheme.FxTheme;

import java.util.ArrayList;

import static ru.vzotov.fx.utils.LayoutUtils.styled;

public class Demo extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(Demo.class);

    public static void main(String[] args) {
        FxTheme.loadFonts();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LOG.debug("Application started");
        final Button btnCreateDocument;
        final Button btnEditDocument;
        final ToolBar toolBar = new ToolBar(
                btnCreateDocument = new Button("Create"),
                btnEditDocument = new Button("Edit")
        );
        final BorderPane borderPane = styled("demo", new BorderPane(new Label("page content"), toolBar, null, null, null));
        final StackPane root = new StackPane(borderPane);

        final ModalDialogManager manager = new PaneDialogManager(root);


        btnCreateDocument.setOnAction(action -> {
            new SampleDialog().asMono(manager)
                    .map(d -> new SampleDocument(d.getName()))
                    .publishOn(JavaFxScheduler.instance())
                    .subscribe(
                            result -> LOG.debug("Result: {}", result),
                            error -> LOG.error("Error", error)
                    );
        });
        btnEditDocument.setOnAction(action -> {
        });

        final Scene scene = new Scene(root, 1024, 800);
        scene.focusOwnerProperty().addListener((prop, oldNode, newNode) -> LOG.debug("focused {}", newNode));
        scene.getStylesheets().addAll(FxTheme.stylesheet(), "dialog.css");
        scene.setOnKeyPressed(t -> {
            if (t.getCode() == KeyCode.F5) {
                scene.getStylesheets().setAll(new ArrayList<>(scene.getStylesheets()));
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public record SampleDocument(String name) {
    }

}
