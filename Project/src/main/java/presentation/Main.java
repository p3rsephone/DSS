package presentation;

import business.Engine;
import business.courses.Course;
import business.courses.Room;
import business.courses.Shift;
import business.exceptions.InvalidPhaseException;
import business.exceptions.ShiftAlredyExistsException;
import business.exceptions.TooManyRequestsException;
import business.exceptions.UserAlredyExistsException;
import business.users.Student;
import business.users.Teacher;
import business.utilities.parser.Parser;
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
        Parser parse = new Parser(engine);
        parse.parseRoom("src/main/java/business/utilities/parser/rooms.json");
        parse.parseCourse("src/main/java/business/utilities/parser/courses1.json");
        parse.parseCourse("src/main/java/business/utilities/parser/courses2.json");
        parse.parseShift("src/main/java/business/utilities/parser/shifts1.json");
        parse.parseShift("src/main/java/business/utilities/parser/shifts2.json");
        parse.parseStudent("src/main/java/business/utilities/parser/student.json");
        parse.parseTeacher("src/main/java/business/utilities/parser/teachers.json");
        engine.allocateStudents();

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

