package presentation;

import business.Engine;
import business.exceptions.UserAlredyExistsException;
import business.users.Student;
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
        this.engine = new Engine();
        try {
            engine.addUser(new Student("luis","lol@gmail.com","123",77,false));
            engine.addUser(new Student("andre","xd@gmail.com","123",3,false));
            engine.addUser(new Student("carlos","email@fixe.pt","123",9090,true));
        } catch (UserAlredyExistsException e) {
            e.printStackTrace();
        }

        this.primaryStage = primaryStage;
        primaryStage.setTitle("UPS!");
        showLoginView();
    }

    public void showLoginView() throws IOException {
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

