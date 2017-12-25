package presentation.controllers;

import business.Engine;
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
import presentation.controllers.utilities.ExchangeTable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TeacherBossLayoutController {
    private Main main;
    private Engine engine;
    private Teacher teacher;
    @FXML
    private TableView<ExchangeTable> table;

    @FXML
    private TableColumn<ExchangeTable, String> codigo;

    @FXML
    private TableColumn<ExchangeTable, String> aluno1;

    @FXML
    private TableColumn<ExchangeTable, String> aluno2;

    @FXML
    private TableColumn<ExchangeTable, String> troca1;

    @FXML
    private TableColumn<ExchangeTable, String> troca2;

    @FXML
    private TableColumn<ExchangeTable, String> cancelada;

    @FXML
    private void initialize() {
        codigo.setCellValueFactory(new PropertyValueFactory<ExchangeTable,String>("codigo"));
        aluno1.setCellValueFactory(new PropertyValueFactory<ExchangeTable,String>("aluno1"));
        aluno2.setCellValueFactory(new PropertyValueFactory<ExchangeTable,String>("aluno2") );
        troca1.setCellValueFactory(new PropertyValueFactory<ExchangeTable,String>("troca1") );
        troca2.setCellValueFactory(new PropertyValueFactory<ExchangeTable,String>("troca2") );
        cancelada.setCellValueFactory(new PropertyValueFactory<ExchangeTable,String>("cancelada") );
    }

    private void loadTable(){
        List<ExchangeTable> list = new ArrayList<>();
        ObservableList<ExchangeTable> obList = FXCollections.observableList(list);

         Set<Exchange> exchanges = engine.getAllExchangesOfCourse(teacher.getCourse());
        for(Exchange exchange :  exchanges){
            obList.add(new ExchangeTable(exchange.getOriginStudent().toString()
                    ,exchange.getDestStudent().toString(),exchange.getOriginShift()
                    ,exchange.getDestShift(),exchange.getCode(), exchange.isCancelled()));
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
    void cancelarTroca(ActionEvent event) {

        int numberSelected= table.getSelectionModel().getSelectedIndex();
        if(numberSelected >= 0){
            ExchangeTable selectedIndex = table.getSelectionModel().getSelectedItem();
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

    public void setInstances(Main main, Engine engine, Teacher teacher){
        this.main = main;
        this.engine = engine;
        this.teacher = teacher;
        loadTable();
    }

}

