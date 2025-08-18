import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent test = FXMLLoader.load(getClass().getResource("view/loginPage.fxml"));
        Scene s = new Scene(test);
        stage.setTitle("BMI");
        stage.setResizable(false);
        stage.setScene(s);
        stage.show();

        System.out.println("Hello World!");
    }
}
