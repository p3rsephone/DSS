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
        Course course = new Course("DSS", "Desenvolvimento de Sistemas de Software", 1, 1);
        Course course2 = new Course("SD", "Sistemas Distribuidos", 2, 1);
        Shift tp1 = new Shift("DSS-TP1", "DSS", 1, 1, 10, "DI109", "thu", "afternoon");
        Shift tp2 = new Shift("DSS-TP2", "DSS", 2, 1, 10, "DI109", "thu", "afternoon");
        Shift tp3 = new Shift("DSS-TP3", "DSS", 1, 1, 10, "DI109", "thu", "afternoon");
        Shift sd1 = new Shift("SD-TP1", "SD", 1, 1, 10, "DI109", "thu", "morning");
        Shift sd2 = new Shift("SD-TP2", "SD", 3, 1, 10, "DI109", "thu", "morning");
        Shift sd3 = new Shift("SD-TP3", "SD", 1, 1, 10, "DI109", "thu", "morning");
        Student a1 = new Student("joao", "joao@", "123", 1, false);
        Student a2 = new Student("maria", "maria@", "123", 2, false);
        Student a3 = new Student("jose", "jose@", "123", 3, false);
        Student a4 = new Student("marta", "marta@", "123", 4, false);
        Student a5 = new Student("david", "david@", "123", 5, false);
        engine.addCourse(course);
        engine.addCourse(course2);
        try {
            engine.addUser(a1);
            engine.addUser(a5);
            engine.addUser(a2);
            engine.addUser(a3);
            engine.addUser(a4);
        } catch (UserAlredyExistsException e) {
            e.printStackTrace();
        }

        try {
            course.addShift(tp1);
            course.addShift(tp2);
            course.addShift(tp3);
            course2.addShift(sd1);
            course2.addShift(sd2);
            course2.addShift(sd3);
        } catch (ShiftAlredyExistsException e) {
            e.printStackTrace();
        }

        engine.enrollStudent("DSS",  1);
        engine.enrollStudent("DSS",  2);
        engine.enrollStudent("DSS",  3);
        engine.enrollStudent("DSS",  4);
        engine.enrollStudent("DSS",  5);

        engine.enrollStudent("SD",  1);
        engine.enrollStudent("SD",  2);
        engine.enrollStudent("SD",  3);
        engine.enrollStudent("SD",  4);
        engine.enrollStudent("SD",  5);

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

