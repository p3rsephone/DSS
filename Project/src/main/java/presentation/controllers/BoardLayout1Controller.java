package presentation.controllers;


import business.Engine;
import business.exceptions.InvalidPhaseException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import presentation.Main;

import java.io.IOException;

public class BoardLayout1Controller {
    private Main main;

    @FXML
    void alocar(ActionEvent event) throws InvalidPhaseException {
        Engine engine = main.getEngine();
        engine.allocateStudents();
        engine.changePhase(2);
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

