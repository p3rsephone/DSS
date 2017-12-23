package presentation.controllers;

import business.Engine;
import business.exceptions.TooManyRequestsException;
import business.users.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ExchangeController {
    private Engine engine;
    private Student student;
    private String course;
    private String originShift;

    public ExchangeController() {
        }

    @FXML
    void troca(ActionEvent event) {
        try {
            engine.requestExchange(course,student,originShift,"");
        } catch (TooManyRequestsException e) {
            e.printStackTrace();
        }
    }

    public void setInstances(){

        }
}

