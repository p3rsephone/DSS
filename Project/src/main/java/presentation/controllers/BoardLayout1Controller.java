package presentation.controllers;


import business.Engine;
import business.exceptions.InvalidPhaseException;
import business.utilities.parser.Parser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import presentation.Main;

import java.io.FileNotFoundException;
import java.io.IOException;

public class BoardLayout1Controller {
    private Main main;

    @FXML
    void alocar(ActionEvent event) throws InvalidPhaseException, IOException {
        Engine engine = main.getEngine();
        Parser parse = new Parser(engine);
        try {
            parse.parseRoom("src/main/java/business/utilities/parser/rooms.json");
            parse.parseCourse("src/main/java/business/utilities/parser/courses1.json");
            parse.parseCourse("src/main/java/business/utilities/parser/courses2.json");
            parse.parseShift("src/main/java/business/utilities/parser/shifts1.json");
            parse.parseShift("src/main/java/business/utilities/parser/shifts2.json");
            parse.parseStudent("src/main/java/business/utilities/parser/student.json");
            parse.parseTeacher("src/main/java/business/utilities/parser/teachers.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        engine.allocateStudents();
        engine.changePhase(2);
        FXMLLoader load = new FXMLLoader();
        load.setLocation((getClass().getResource("/presentation/views/boardLayout2.fxml")));
        Parent root =  load.load();
        BoardLayout2Controller controller = load.getController();
        controller.setMain(main);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        FXMLLoader load = new FXMLLoader();
        load.setLocation((getClass().getResource("/presentation/views/login.fxml")));
        Parent root =  load.load();
        LoginController controller = load.getController();
        controller.setMain(main);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void setMain(Main main){
        this.main = main;
    }

}

