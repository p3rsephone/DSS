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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import presentation.Main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

public class BoardLayout1Controller {
    private Main main;
    @FXML
    private Button reniniciar;

    @FXML
    private Button alocarAlunos;

    @FXML
    void reset(ActionEvent event) throws InvalidPhaseException, IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setContentText("Quer reiniciar o processo de alocação?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            main.resetEngine();
            FXMLLoader load = new FXMLLoader();
            load.setLocation((getClass().getResource("/presentation/views/boardLayout1.fxml")));
            Parent root =  load.load();
            BoardLayout1Controller controller = load.getController();
            controller.setMain(main);
            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();
        }

    }


    @FXML
    void alocar(ActionEvent event) throws InvalidPhaseException, IOException {

        Engine engine = main.getEngine();
        Parser parse = new Parser(engine);
        try {
            parse.parseRoom("src/main/resources/rooms.json");
            parse.parseCourse("src/main/resources/courses1.json");
            parse.parseCourse("src/main/resources/courses2.json");
            parse.parseTeacher("src/main/resources/teachers.json");
            parse.parseShift("src/main/resources/shifts1.json");
            parse.parseShift("src/main/resources/shifts2.json");
            parse.parseStudent("src/main/resources/student.json");
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
        if(main.getEngine().getPhase().equals(1)){
            reniniciar.setVisible(false);
            reniniciar.setManaged(false);
        }
        else {
            alocarAlunos.setVisible(false);
            alocarAlunos.setManaged(false);
        }
    }

}
