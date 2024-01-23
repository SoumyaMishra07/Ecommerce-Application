package com.example.ecommerce;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class ECommerce extends Application {

UserInterface userInterface = new UserInterface();
    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(userInterface.createContent());
        stage.setTitle("ECommerce!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}