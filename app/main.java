package app;

import app.db.Db;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;

public class main extends Application {

    private static Stage primaryStage;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        // حمّلي شاشة البداية (مثلاً: loginPage.fxml)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/loginPage.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Project");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);

        }

}
