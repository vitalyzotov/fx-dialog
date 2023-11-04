module ru.vzotov.fx.dialog {
    requires javafx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.material2;
    requires ru.vzotov.fx.utils;
    requires reactor.core;
    requires org.slf4j;
    requires fxtheme.dark;
    requires ru.vzotov.fx.reactor;

    uses ru.vzotov.fx.dialog.spi.DialogResourcesProvider;
    exports ru.vzotov.fx.dialog;
    exports ru.vzotov.fx.dialog.skin;
    exports ru.vzotov.fx.dialog.spi;
}
