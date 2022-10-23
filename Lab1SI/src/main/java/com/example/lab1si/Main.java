package com.example.lab1si;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Lab1");
        stage.setScene(scene);
        stage.show();
        Controller controller = fxmlLoader.getController();
        controller.setStage(stage);
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) {
        launch();
    }
}
