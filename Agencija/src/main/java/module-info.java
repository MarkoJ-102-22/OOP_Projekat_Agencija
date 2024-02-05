module com.example.agencija {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;


    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;



//    opens com.example.agencija to javafx.fxml;
//    exports com.example.agencija;


    opens com.example.agencija.app to javafx.fxml;
    exports com.example.agencija.app;

    opens com.example.agencija.model to javafx.fxml;
    exports com.example.agencija.model;

    opens com.example.agencija.view to javafx.fxml;
    exports com.example.agencija.view;
}