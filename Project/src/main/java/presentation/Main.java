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
        Student s1  = new Student("luis","lol@gmail.com","123",77,false);

        Student s2;
        this.engine = new Engine();
            engine.addRoom(new Room("CP1201",30));
        try {
            engine.changePhase(2);
        } catch (InvalidPhaseException e) {
            e.printStackTrace();
        }
        try {

            s2 =new Student("andre","xd@gmail.com","123",3,false);
             Student s3 =new Student("carlos","email@fixe.pt","123",9090,true);
             Teacher t1 =new Teacher("lol",1,"stor@edut.edu","123",true);
            s1.addEnrollment("UC1");
            s1.addEnrollment("UC2");
            s2.addEnrollment("UC1");
            s1.addShift("TP1");
            s1.addShift("TP1");
            s2.addShift("TP2");

            engine.addUser(s1);
            engine.addUser(s2);
            engine.addUser(s3);
            engine.addUser(t1);

        } catch (UserAlredyExistsException e) {
            e.printStackTrace();
        }
            Course uc1 = new Course("UC1","uc1","1",1,"sex");
            Shift uc1Shift1 = new Shift("TP1","UC1",30,1,20,"CP1201");
            Shift uc1Shift2 = new Shift("TP2","UC1",30,1,20,"CP1201");
            Course uc2 = new Course("UC2","uc2","1",1,"sex");
            Shift uc2Shift = new Shift("TP1","UC2",30,1,20,"CP1201");
        try {
            uc1.addShift(uc1Shift1);
            uc1.addShift(uc1Shift2);
            uc2.addShift(uc2Shift);
        } catch (ShiftAlredyExistsException e) {
            e.printStackTrace();
        }
        engine.addCourse(uc1);
        engine.addCourse(uc2);
        engine.enrollStudent("UC1","TP1",77);
        engine.enrollStudent("UC2","TP1", 77);
        engine.enrollStudent("UC1","TP2",3);
        try {
            engine.requestExchange("UC1",s1,"TP1","TP2");
        } catch (TooManyRequestsException e) {
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

