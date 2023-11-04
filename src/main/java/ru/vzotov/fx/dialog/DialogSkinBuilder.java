package ru.vzotov.fx.dialog;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static ru.vzotov.fx.dialog.ModalDialog.STYLE_DIALOG_CONTENT;
import static ru.vzotov.fx.dialog.ModalDialog.STYLE_DIALOG_TITLE;
import static ru.vzotov.fx.utils.LayoutUtils.hgrow;
import static ru.vzotov.fx.utils.LayoutUtils.stretch;
import static ru.vzotov.fx.utils.LayoutUtils.styled;
import static ru.vzotov.fx.utils.LayoutUtils.vgrow;

public class DialogSkinBuilder {

    public static DialogDsl start() {
        return new DialogDslBuilder();
    }

    private static class DialogDslBuilder implements DialogDsl {
        private TitleDslBuilder title;
        private BodyDslBuilder body;
        private ButtonsDslBuilder footer;

        @Override
        public TitleDsl title(Region title) {
            return this.title = new TitleDslBuilder(this, title);
        }

        @Override
        public BodyDsl body(Region body) {
            return this.body = new BodyDslBuilder(this, body);
        }

        @Override
        public ButtonsDsl footer() {
            return this.footer = new ButtonsDslBuilder(this);
        }

        @Override
        public Node build() {
            final List<Region> parts = Stream.<Optional<Region>>builder()
                    .add(Optional.ofNullable(title).map(PartDslBuilder::build))
                    .add(Optional.ofNullable(body).map(PartDslBuilder::build))
                    .add(Optional.ofNullable(footer).map(PartDslBuilder::build))
                    .build()
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
            final VBox box = new VBox();
            box.getChildren().setAll(parts);
            box.setFillWidth(true);
            return styled(STYLE_DIALOG_CONTENT, new StackPane(box));
        }
    }

    private static class BodyDslBuilder extends PartDslBuilder implements BodyDsl {

        private final Region body;

        private BodyDslBuilder(DialogDslBuilder parent, Region body) {
            super(parent);
            this.body = body;
        }

        @Override
        public Region build() {
            return vgrow(Priority.ALWAYS, body);
        }
    }

    private static class TitleDslBuilder extends PartDslBuilder implements TitleDsl {
        private final Region title;
        private Region titleControls;

        private TitleDslBuilder(DialogDslBuilder parent, Region title) {
            super(parent);
            this.title = title;
        }

        @Override
        public TitleDsl controls(Region controls) {
            this.titleControls = controls;
            return this;
        }

        @Override
        public Region build() {
            return styled(STYLE_DIALOG_TITLE, new HBox(hgrow(Priority.ALWAYS, stretch(title)), titleControls));
        }
    }

    private static class ButtonsDslBuilder extends PartDslBuilder implements ButtonsDsl {
        private final List<Node> buttons = new ArrayList<>();

        private ButtonsDslBuilder(DialogDslBuilder parent) {
            super(parent);
        }

        @Override
        public ButtonsDsl button(Button button) {
            buttons.add(button);
            return this;
        }

        @Override
        public ButtonsDsl child(Node child) {
            buttons.add(child);
            return this;
        }

        @Override
        public Region build() {
            final ButtonBar footer = new ButtonBar();
            footer.getButtons().setAll(buttons);
            return footer;
        }
    }

    private static abstract class PartDslBuilder implements PartDsl {

        final DialogDslBuilder parent;

        private PartDslBuilder(DialogDslBuilder parent) {
            this.parent = parent;
        }

        @Override
        public DialogDsl and() {
            return parent;
        }

        public abstract Region build();
    }

    public interface DialogDsl {
        TitleDsl title(Region title);

        BodyDsl body(Region body);

        ButtonsDsl footer();

        Node build();
    }

    public interface BodyDsl extends PartDsl {

    }

    public interface ButtonsDsl extends PartDsl {
        ButtonsDsl button(Button button);

        ButtonsDsl child(Node child);
    }

    public interface TitleDsl extends PartDsl {
        TitleDsl controls(Region controls);
    }

    public interface PartDsl {
        DialogDsl and();
    }
}
