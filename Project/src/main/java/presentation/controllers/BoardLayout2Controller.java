package presentation.controllers;

import business.Engine;
import business.courses.Course;
import business.courses.Exchange;
import business.exceptions.ExchangeAlreadyCancelledException;
import business.exceptions.ExchangeDoesNotExistException;
import business.exceptions.StudentNotInShiftException;
import business.users.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import presentation.Main;
import presentation.controllers.utilities.DCTable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class BoardLayout2Controller {
    private Main main;
    private Engine engine;
    @FXML
    private TableView<DCTable> table;

    @FXML
    private TableColumn<DCTable, String> codigo;

    @FXML
    private TableColumn<DCTable,String> uc;

    @FXML
    private TableColumn<DCTable, String> aluno1;

    @FXML
    private TableColumn<DCTable, String> aluno2;

    @FXML
    private TableColumn<DCTable, String> troca1;

    @FXML
    private TableColumn<DCTable, String> troca2;

    @FXML
    private TableColumn<DCTable, String> cancelada;

    @FXML
    private void initialize() {
        codigo.setCellValueFactory(new PropertyValueFactory<DCTable,String>("codigo"));
        uc.setCellValueFactory(new PropertyValueFactory<DCTable,String>("uc") );
        aluno1.setCellValueFactory(new PropertyValueFactory<DCTable,String>("aluno1"));
        aluno2.setCellValueFactory(new PropertyValueFactory<DCTable,String>("aluno2") );
        troca1.setCellValueFactory(new PropertyValueFactory<DCTable,String>("troca1") );
        troca2.setCellValueFactory(new PropertyValueFactory<DCTable,String>("troca2") );
        cancelada.setCellValueFactory(new PropertyValueFactory<DCTable,String>("cancelada") );
    }

    private void loadTable(){
        List<DCTable> list = new ArrayList<>();
        ObservableList<DCTable> obList = FXCollections.observableList(list);
        Engine engine = main.getEngine();

         HashMap<String, Course> courses = engine.getCourses();
         courses.forEach((k,v)->{
             Set<Exchange> exchanges = engine.getAllExchangesOfCourse(v.getCode());
             for(Exchange exchange :  exchanges){
                 obList.add(new DCTable(exchange.getOriginStudent().toString()
                         ,exchange.getDestStudent().toString(),exchange.getOriginShift()
                         ,exchange.getDestShift(),exchange.getCode(),exchange.getCourse(), exchange.isCancelled()));
             }
             table.getItems().setAll(obList);
         });

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
    void cancelarTroca(ActionEvent event) {

        int numberSelected= table.getSelectionModel().getSelectedIndex();
        if(numberSelected >= 0){
            DCTable selectedIndex = table.getSelectionModel().getSelectedItem();
            try {
                engine.cancelExchange(selectedIndex.getCodigo());
                loadTable();
            } catch (ExchangeDoesNotExistException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Troca inexistente !");
                alert.showAndWait();
            } catch (StudentNotInShiftException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Estudante/s nao se encontra/m nesse turno !");
                alert.showAndWait();
            } catch (ExchangeAlreadyCancelledException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Troca ja foi cancelada !");
                alert.showAndWait();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Nenhuma troca selecrionada !");
            alert.showAndWait();
        }
    }

    @FXML
    void mudarFase(ActionEvent event) {

    }

    public void setMain(Main main){
        this.main = main;
        this.engine = main.getEngine();
        loadTable();
    }

}

