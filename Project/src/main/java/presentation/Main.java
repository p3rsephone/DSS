package presentation;

import business.Engine;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import presentation.controllers.LoginController;

import java.io.IOException;

public class Main extends Application {
    private Engine engine ;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        engine = new Engine();
        this.primaryStage = primaryStage;
        primaryStage.setTitle("UPS!");
        showLoginView();
    }

    public void resetEngine(){
        engine.reset();
    }

    private void showLoginView() throws IOException {
        FXMLLoader ola = new FXMLLoader();
        ola.setLocation((getClass().getResource("/presentation/views/login.fxml")));
        Parent root =  ola.load();
        LoginController controller = ola.getController();
        controller.setMain(this);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public Engine getEngine() {
        return engine;
    }
}

