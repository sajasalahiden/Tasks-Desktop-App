package app;

import app.db.Db;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;


public class main extends Application {



    @Override
    public void start(Stage stage) throws Exception {

        Parent loader =  FXMLLoader.load(getClass().getResource("view/loginPage.fxml"));
        Scene s = new Scene(loader);

        stage.setScene(s);
        stage.setTitle("Task Application");
        stage.show();
        s.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                Dialog dialog = new Dialog();
                dialog.setTitle("Task Application");
                dialog.setHeaderText("Exit");
                dialog.setContentText("Are you sure you want to exit?");
                ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                ButtonType no = new ButtonType("No", ButtonBar.ButtonData.NO);
                dialog.getDialogPane().getButtonTypes().addAll(yes, no);
                if (dialog.showAndWait().get() == yes) {
                    stage.close();
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);

        }

}
