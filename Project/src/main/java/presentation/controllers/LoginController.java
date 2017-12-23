package presentation.controllers;


import business.Engine;
import business.exceptions.InvalidPhaseException;
import business.users.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import presentation.Main;
import java.io.IOException;



public class LoginController {
    @FXML
    private TextField number ;

    @FXML
    private PasswordField password;

    private Main main;

    public LoginController() {
    }

    @FXML
    public void login(ActionEvent event) throws IOException {
        Integer number = Integer.parseInt(this.number.getText());
        String password = this.password.getText();
        Engine engine = main.getEngine();
        /*
        try {
            engine.changePhase(2);
        } catch (InvalidPhaseException e) {
            e.printStackTrace();
        }
        */
        User u;
        if((u=engine.login(number,password)) != null){
            switch (engine.getPhase()){
                case 1:
                    if( u instanceof Student || u instanceof Teacher){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Denied acess: Phase 1 in progress");
                        alert.showAndWait();
                    }
                    if(u instanceof DC){
                         break;
                    }
                        break;
                case 2:
                    if(u instanceof Student){

                        FXMLLoader load = new FXMLLoader();
                        load.setLocation(getClass().getResource("/presentation/views/studentLayout.fxml"));
                        Parent student_parent = load.load();
                        StudentLayoutController slc = load.getController();
                        slc.setInstances(main,(Student) u);
                        Scene student_scene = new Scene(student_parent);
                        Stage ups_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        ups_stage.setScene(student_scene);
                        ups_stage.show();
                    }
                    if(u instanceof Teacher){
                        break;
                    }
                    if(u instanceof DC){
                        break;
                    }
                    break;
                case 3:
                    if(u instanceof Student){
                        break;
                    }
                    if(u instanceof DC){
                        break;
                    }
                    if(u instanceof Teacher){
                        break;
                    }
                    break;
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Numero e/ou password invalidos");
            alert.showAndWait();
        }
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
