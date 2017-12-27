package presentation.controllers;


import business.Engine;
import business.courses.Course;
import business.courses.Exchange;
import business.courses.Shift;
import business.exceptions.ShiftNotValidException;
import business.users.Student;
import business.utilities.CourseTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import presentation.Main;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class StudentLayoutController {

    private Main main;
    private Student student;

    @FXML
    private TableView<CourseTable> table;

    @FXML
    private TableColumn<CourseTable,String > uc;

    @FXML
    private TableColumn<CourseTable, String> tp;

    @FXML
    private TableColumn<CourseTable, String> trocaPendente;

    @FXML
    private Button pedidoTroca;

    @FXML
    private void initialize() {

        uc.setCellValueFactory(new PropertyValueFactory<CourseTable,String>("uc"));
        tp.setCellValueFactory(new PropertyValueFactory<CourseTable,String>("tp") );
        trocaPendente.setCellValueFactory(new PropertyValueFactory<CourseTable,String>("trocaPendente") );
    }

    private void loadTable(){
        List<CourseTable> list = new ArrayList<>();
        ObservableList<CourseTable> obList = FXCollections.observableList(list);

        Set<String> courses = student.getEnrollments();
        for(String course :  courses){
            String shift = main.getEngine().getShift(course ,student.getShifts());
            obList.add(new CourseTable(course,shift,main.getEngine().concatRequests(student.getNumber(),course,shift)));
        }
        table.getItems().setAll(obList);
    }

    @FXML
    void Logout(ActionEvent event) throws IOException {
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

    @FXML
    void pedidoDeTroca(ActionEvent event) throws IOException {
        int numberSelected= table.getSelectionModel().getSelectedIndex();
        if(numberSelected >= 0){

            CourseTable selectedIndex = table.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/presentation/views/exchange.fxml"));
            AnchorPane page =  loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ExchangeController exchangeController = loader.getController();
            exchangeController.setInstances(main.getEngine(),student,selectedIndex.getUc(),selectedIndex.getTp(),selectedIndex.getTrocaPendente().split(", "));
            dialogStage.showAndWait();

            if(exchangeController.isTrocaClicked()){
                loadTable();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Nenhuma uc selecrionada para troca !");
            alert.showAndWait();
        }

    }

    @FXML
    void cancelRequest(ActionEvent event) {
        int numberSelected= table.getSelectionModel().getSelectedIndex();
        if(numberSelected>=0){
            CourseTable selectedIndex = table.getSelectionModel().getSelectedItem();
            List<String> pedingRequest = new ArrayList<>(main.getEngine().getRequests(student.getNumber(), selectedIndex.getUc(), selectedIndex.getTp()));
            ChoiceDialog<String> dialog = new ChoiceDialog<>(null,pedingRequest);
            dialog.setTitle("Choice Dialog");
            dialog.setContentText("Cancelar troca:");

            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()){

                try {
                    main.getEngine().cancelRequest(student.getNumber(),main.getEngine().getRequest(student.getNumber(),selectedIndex.getUc(),selectedIndex.getTp(),result.get()));
                    loadTable();
                } catch (ShiftNotValidException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Turno invalido!");
                    alert.showAndWait();
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Turno nao selecionado!");
            alert.showAndWait();
        }
    }

    void setInstances(Main main, Student student){
        this.main = main;
        this.student = student;
        if(main.getEngine().getPhase().equals(3)){
            pedidoTroca.setVisible(false);
            pedidoTroca.setManaged(false);
            trocaPendente.setVisible(false);
        }
        this.loadTable();
    }
}

