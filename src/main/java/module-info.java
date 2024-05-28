module showdomilhao {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;
    requires org.slf4j;
    requires com.jfoenix;

    exports br.com.showdomilhao.application to javafx.graphics;
    exports br.com.showdomilhao.controller to javafx.fxml;

    opens br.com.showdomilhao.controller to javafx.fxml;
    opens br.com.showdomilhao.model to javafx.base;
}