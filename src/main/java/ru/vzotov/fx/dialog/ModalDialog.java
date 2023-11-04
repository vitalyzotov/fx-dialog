package ru.vzotov.fx.dialog;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import reactor.core.publisher.Mono;
import ru.vzotov.fx.dialog.skin.ModalDialogSkin;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class ModalDialog<T extends ModalDialog<T>> extends Control {
    private static final String DEFAULT_STYLE_CLASS = "modal-dialog";
    public static final String STYLE_DIALOG_TITLE = "dialog-title";
    public static final String STYLE_DIALOG_CONTENT = "dialog-content";
    public static final String STYLE_DIALOG_BODY = "dialog-body";

    public ModalDialog() {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        setFocusTraversable(false);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ModalDialogSkin<>(this);
    }

    private volatile boolean mounted;

    public boolean isMounted() {
        return mounted;
    }

    public void setMounted(boolean mounted) {
        this.mounted = mounted;
    }

    /**
     * Показывает диалог
     */
    public void show() {
        toFront();
        fireEvent(new ModalDialogEvent(ModalDialogEvent.MODAL_DIALOG_SHOW));
    }

    public void hide() {
        toBack();
        fireEvent(new ModalDialogEvent(ModalDialogEvent.MODAL_DIALOG_HIDE));
    }

    public Mono<T> asMono(ModalDialogManager manager) {
        return asMono(manager, () -> (T)this);
    }

    public <V> Mono<V> asMono(ModalDialogManager manager, Supplier<V> value) {
        return asMono(manager, false, value);
    }

    public <V> Mono<V> asMono(ModalDialogManager manager, boolean keepMounted, Supplier<V> value) {
        return asMono(manager, keepMounted, value, null);
    }

    public <V> Mono<V> asMono(ModalDialogManager manager, boolean keepMounted, Supplier<V> value,
                              Consumer<T> onShow) {
        return Mono.create(sink -> {
            final EventHandler<ModalDialogEvent> okPressed = event -> {
                sink.success(value.get()); // call success before hide
                hide();
            };
            final EventHandler<ModalDialogEvent> cancelPressed = event -> {
                hide();
            };
            final EventHandler<ModalDialogEvent> dialogHide = event -> {
                sink.success();
            };
            final EventHandler<ModalDialogEvent> dialogShow = event -> {
                if (onShow != null) {
                        //noinspection unchecked
                        onShow.accept((T) ModalDialog.this);
                }
            };

            addEventHandler(ModalDialogEvent.MODAL_DIALOG_OK, okPressed);
            addEventHandler(ModalDialogEvent.MODAL_DIALOG_CANCEL, cancelPressed);
            addEventHandler(ModalDialogEvent.MODAL_DIALOG_HIDE, dialogHide);
            addEventHandler(ModalDialogEvent.MODAL_DIALOG_SHOW, dialogShow);

            if (!isMounted()) {
                manager.mount(ModalDialog.this);
            }

            sink.onRequest(i -> show());

            sink.onCancel(this::hide);

            sink.onDispose(() -> {
                removeEventHandler(ModalDialogEvent.MODAL_DIALOG_OK, okPressed);
                removeEventHandler(ModalDialogEvent.MODAL_DIALOG_CANCEL, cancelPressed);
                removeEventHandler(ModalDialogEvent.MODAL_DIALOG_HIDE, dialogHide);
                removeEventHandler(ModalDialogEvent.MODAL_DIALOG_SHOW, dialogShow);
                if (!keepMounted) {
                    manager.unmount(this);
                }
            });
        });
    }

    /**
     * OnHide
     */
    private ModalDialogEventHandlerProperty onHide;

    public final ObjectProperty<EventHandler<ModalDialogEvent>> onHideProperty() {
        if (onHide == null) {
            onHide = new ModalDialogEventHandlerProperty("onHide", ModalDialogEvent.MODAL_DIALOG_HIDE);
        }

        return onHide;
    }

    public final void setOnHide(EventHandler<ModalDialogEvent> value) {
        onHideProperty().set(value);
    }

    public final EventHandler<ModalDialogEvent> getOnHide() {
        return onHide == null ? null : onHideProperty().get();
    }


    /**
     * OnShow
     */
    private ModalDialogEventHandlerProperty onShow;

    public final ObjectProperty<EventHandler<ModalDialogEvent>> onShowProperty() {
        if (onShow == null) {
            onShow = new ModalDialogEventHandlerProperty("onShow", ModalDialogEvent.MODAL_DIALOG_SHOW);
        }

        return onShow;
    }

    public final void setOnShow(EventHandler<ModalDialogEvent> value) {
        onShowProperty().set(value);
    }

    public final EventHandler<ModalDialogEvent> getOnShow() {
        return onShow == null ? null : onShowProperty().get();
    }

    /**
     * OK button event handler
     */
    private ModalDialogEventHandlerProperty onOkButtonPressed;

    public final ObjectProperty<EventHandler<ModalDialogEvent>> onOkButtonPressedProperty() {
        if (onOkButtonPressed == null) {
            onOkButtonPressed = new ModalDialogEventHandlerProperty("onOkButtonPressed", ModalDialogEvent.MODAL_DIALOG_OK);
        }

        return onOkButtonPressed;
    }

    public final void setOnOkButtonPressed(EventHandler<ModalDialogEvent> value) {
        onOkButtonPressedProperty().set(value);
    }

    public final EventHandler<ModalDialogEvent> getOnOkButtonPressed() {
        return onOkButtonPressed == null ? null : onOkButtonPressedProperty().get();
    }

    /**
     * Cancel button event handler
     */
    private ModalDialogEventHandlerProperty onCancelButtonPressed;

    public final ObjectProperty<EventHandler<ModalDialogEvent>> onCancelButtonPressedProperty() {
        if (onCancelButtonPressed == null) {
            onCancelButtonPressed = new ModalDialogEventHandlerProperty("onCancelButtonPressed", ModalDialogEvent.MODAL_DIALOG_CANCEL);
        }

        return onCancelButtonPressed;
    }

    public final void setOnCancelButtonPressed(EventHandler<ModalDialogEvent> value) {
        onCancelButtonPressedProperty().set(value);
    }

    public final EventHandler<ModalDialogEvent> getOnCancelButtonPressed() {
        return onCancelButtonPressed == null ? null : onCancelButtonPressedProperty().get();
    }

    /**
     * Events
     */
    public static class ModalDialogEvent extends Event {

        public static final EventType<ModalDialogEvent> MODAL_DIALOG_EVENT = new EventType<>(
                Event.ANY, "MODAL_DIALOG_EVENT");
        public static final EventType<ModalDialogEvent> MODAL_DIALOG_SHOW = new EventType<>(
                MODAL_DIALOG_EVENT, "MODAL_DIALOG_SHOW_EVENT");
        public static final EventType<ModalDialogEvent> MODAL_DIALOG_HIDE = new EventType<>(
                MODAL_DIALOG_EVENT, "MODAL_DIALOG_HIDE_EVENT");
        public static final EventType<ModalDialogEvent> MODAL_DIALOG_OK = new EventType<>(
                MODAL_DIALOG_EVENT, "MODAL_DIALOG_OK_EVENT");
        public static final EventType<ModalDialogEvent> MODAL_DIALOG_CANCEL = new EventType<>(
                MODAL_DIALOG_EVENT, "MODAL_DIALOG_CANCEL_EVENT");

        public ModalDialogEvent(EventType<? extends Event> eventType) {
            super(eventType);
        }

    }

    /**
     * Event handler class
     */
    protected class ModalDialogEventHandlerProperty extends SimpleObjectProperty<EventHandler<ModalDialogEvent>> {

        private final EventType<ModalDialogEvent> eventType;

        public ModalDialogEventHandlerProperty(final String name, final EventType<ModalDialogEvent> eventType) {
            super(ModalDialog.this, name);
            this.eventType = eventType;
        }

        @Override
        protected void invalidated() {
            setEventHandler(eventType, get());
        }
    }

    public Reactor<T, T> reactive() {
        return reactive(Function.identity());
    }

    /**
     *
     * @param result a function that accepts the dialog instance, and returns a result object
     * @return
     * @param <R> a type of result object
     */
    public <R> Reactor<T, R> reactive(Function<T, R> result) {
        return new DefaultReactor<R>().with(result);
    }

    public interface Reactor<D extends ModalDialog<D>, R> {
        Reactor<D, R> keepMounted();

        Reactor<D, R> managedBy(ModalDialogManager manager);

        Reactor<D, R> with(Function<D, R> result);

        Reactor<D, R> onShow(Consumer<D> onShow);

        Mono<R> show();
    }

    protected class DefaultReactor<R> implements Reactor<T, R> {

        private boolean keepMounted = false;
        private ModalDialogManager manager;
        private Function<T, R> result;
        private Consumer<T> onShow;

        private T dialog() {
            //noinspection unchecked
            return (T) ModalDialog.this;
        }

        @Override
        public Reactor<T, R> keepMounted() {
            this.keepMounted = true;
            return this;
        }

        @Override
        public Reactor<T, R> managedBy(ModalDialogManager manager) {
            this.manager = manager;
            return this;
        }

        @Override
        public Reactor<T, R> with(Function<T, R> result) {
            this.result = result;
            return this;
        }

        @Override
        public Reactor<T, R> onShow(Consumer<T> onShow) {
            this.onShow = onShow;
            return this;
        }

        @Override
        public Mono<R> show() {
            Objects.requireNonNull(manager);
            Objects.requireNonNull(result);
            return asMono(manager, keepMounted, () -> result.apply(dialog()), onShow);
        }
    }
}
