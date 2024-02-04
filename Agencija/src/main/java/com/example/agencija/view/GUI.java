package com.example.agencija.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
public class GUI extends Application implements View {

    @Override
    public void drawView() {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("log-in.fxml"));
        System.out.println();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Agencija || Prijava");
        stage.setScene(scene);
        stage.show();
    }
}
