package presentation.controllers;

import business.Engine;
import business.exceptions.TooManyRequestsException;
import business.users.Student;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            engine.requestExchange(course,student,originShift,tps.getValue());
        } catch (TooManyRequestsException e) {
            e.printStackTrace();
        }

        Stage dialog= (Stage) ((Node) event.getSource()).getScene().getWindow();
        dialog.close();
        setTrocaClicked(true);
        }

    public void setInstances(Engine engine, Student student, String course, String originShift, String[] pedingRequest){
        this.engine = engine;
        this.student = student;
        this.course = course;
        this.originShift = originShift;
        this.pedingRequest = pedingRequest;
        List<String> ola = new ArrayList<>();
        for(String shift : engine.getShiftsOfCourse(course)){
            if(!Arrays.asList(pedingRequest).contains(shift) && !originShift.equals(shift)){
                ola.add(shift);
            }

        }
        tps.setItems(FXCollections.observableList(ola));
    }
}

