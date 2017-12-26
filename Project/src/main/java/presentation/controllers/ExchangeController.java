package presentation.controllers;

import business.Engine;
import business.exceptions.ShiftNotValidException;
import business.exceptions.TooManyRequestsException;
import business.users.Student;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ExchangeController {
    private Engine engine;
    private Student student;
    private String course;
    private String originShift;
    private String[] pedingRequest;
    private boolean trocaClicked = false;
    @FXML
    ChoiceBox<String> tps;

    public ExchangeController() {
        }

    public boolean isTrocaClicked(){
        return trocaClicked;
    }

    public void setTrocaClicked(boolean trocaClicked) {
        this.trocaClicked = trocaClicked;
    }

    @FXML
    void troca(ActionEvent event) {
        try {
            if(tps.getValue() != null){
                try {
                    engine.requestExchange(course,student,originShift,tps.getValue());
                } catch (ShiftNotValidException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Turno invalido !");
                    alert.showAndWait();
                }
                Stage dialog= (Stage) ((Node) event.getSource()).getScene().getWindow();
                dialog.close();
                setTrocaClicked(true);
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Turno nao selecionado !");
                alert.showAndWait();
            }
        } catch (TooManyRequestsException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Demasiados requestes !");
            alert.showAndWait();
        }

    }

    public void setInstances(Engine engine, Student student, String course, String originShift, String[] pedingRequest){
        this.engine = engine;
        this.student = student;
        this.course = course;
        this.originShift = originShift;
        this.pedingRequest = pedingRequest;
        List<String> ola = new ArrayList<>();
        Set<String> shifts = engine.getShiftsOfCourse(course);
        for(String shift : shifts){
            if(!Arrays.asList(this.pedingRequest).contains(shift) && !originShift.equals(shift)){
                ola.add(shift);
            }

        }
        tps.setItems(FXCollections.observableList(ola));
    }
}

