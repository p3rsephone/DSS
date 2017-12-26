package presentation.controllers;

import business.Engine;
import business.exceptions.RoomCapacityExceededException;
import business.exceptions.StudentAlreadyInShiftException;
import business.exceptions.StudentNotInShiftException;
import business.exceptions.StudentsDoNotFitInShiftException;
import business.users.Student;
import business.users.Teacher;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import presentation.Main;
import presentation.controllers.utilities.StudentTable;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TeacherLayoutController {
    private Main main;
    private Engine engine;
    private Teacher teacher;

    @FXML
    private ChoiceBox<String> shift;

    @FXML
    private TableView<StudentTable> table;

    @FXML
    private TableColumn<StudentTable, String> numero;

    @FXML
    private TableColumn<StudentTable, String> nome;

    @FXML
    private TableColumn<StudentTable, Integer> faltas;

    @FXML
    private VBox bossControl;


    @FXML
    private void initialize() {
        numero.setCellValueFactory(new PropertyValueFactory<StudentTable,String>("numero"));
        nome.setCellValueFactory(new PropertyValueFactory<StudentTable,String>("nome"));
        faltas.setCellValueFactory(new PropertyValueFactory<StudentTable,Integer>("faltas") );
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);



        shift.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> selected, String antigoTurno, String novoTurno) {
                loadTable();
            }
        });

    }

    private void loadTable(){
        List<StudentTable> list = new ArrayList<>();
        ObservableList<StudentTable> obList = FXCollections.observableList(list);

        Set<Student> alunos = engine.getStudentOfShift(teacher.getCourse(),shift.getValue()) ;
        for(Student aluno :  alunos){
            try {
                obList.add(
                        new StudentTable(aluno.getNumber().toString(),aluno.getName(),
                                engine.getAbsentment(teacher.getCourse(),
                                        engine.getShift(teacher.getCourse(),aluno.getShifts()),aluno.getNumber()
                                )
                        )
                );
            } catch (StudentNotInShiftException e) {
                e.printStackTrace();
            }
        }
        table.getItems().setAll(obList);
    }

    private void loadShifts(){
            Set<String> turnos;
            List<String> shifts = new ArrayList<>();
            if(teacher.isBoss()) {
                turnos = engine.getShiftsOfCourse(teacher.getCourse());
            }
            else {
                turnos = teacher.getShifts();
            }

            for(String s: turnos ){
                    shifts.add(s);
                }
            shift.setItems(FXCollections.observableList(shifts));
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
    void adcionarAluno(ActionEvent event) {
        Set<Student> alunos =engine.getStudentsWithoutShift(teacher.getCourse());
        ArrayList<String> choices =
                alunos.stream()
                        .map(a-> a.getNumber().toString())
                        .collect(Collectors.toCollection(ArrayList::new));
        ChoiceDialog<String> dialog = new ChoiceDialog<>(null,choices);
        dialog.setTitle("Choice Dialog");
        dialog.setContentText("Alunos:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()){
            try {
                //HELLO RESENDE
                engine.addStudentToShift(teacher.getCourse(),shift.getValue(),Integer.parseInt(result.get()) );
            } catch (StudentAlreadyInShiftException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Aluno ja se encontra no turno");
                alert.showAndWait();
            } catch (RoomCapacityExceededException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Capacidade da sala ultrapassada impossivel adicionar aluno!");
                alert.showAndWait();
            }
            loadTable();
        }
    }

    @FXML
    void presenca(ActionEvent event) {
        int numberSelected= table.getSelectionModel().getSelectedIndex();
        if(numberSelected >= 0){
            List<StudentTable> selectedIndex = table.getSelectionModel().getSelectedItems();
            ArrayList<Integer> alunos =
                    selectedIndex.stream()
                            .map(index -> Integer.parseInt(index.getNumero()))
                            .collect(Collectors.toCollection(ArrayList::new));
            engine.markAbsent(teacher.getCourse(),shift.getValue(),alunos);
            loadTable();
            }
        else{
        }
    }

    @FXML
    void removerAluno(ActionEvent event) {
        int numberSelected= table.getSelectionModel().getSelectedIndex();
        if(numberSelected >= 1){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Selecione apenas um unico aluno!");
            alert.showAndWait();
        }
        else if(numberSelected >= 0){
            StudentTable selectedIndex = table.getSelectionModel().getSelectedItem();
            engine.expellStudent(teacher.getCourse(),shift.getValue(),Integer.parseInt(selectedIndex.getNumero()));
            loadTable();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Nenhum aluno selecionado !");
            alert.showAndWait();
        }
    }

    @FXML
    void tamanhoTP(ActionEvent event) {
        if(shift.getValue() != null){
            Set<String> s = new TreeSet<>();
            s.add(shift.getValue());
            String idS =  engine.getShift(teacher.getCourse(),s);
            TextInputDialog dialog = new TextInputDialog("30");
            dialog.setTitle("limit");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()){
                if(isNumeric(result.get())){
                    try {
                        engine.defineShiftLimit(teacher.getCourse(),idS,Integer.parseInt(result.get()));
                    } catch (RoomCapacityExceededException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Limite da sala ultrapassado !");
                        alert.showAndWait();
                    } catch (StudentsDoNotFitInShiftException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Demasiados alunos no turno para este limite !");
                        alert.showAndWait();
                    }
                }
            }

        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Nenhum turno selecionado!");
            alert.showAndWait();
        }
    }

    public boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    public void setInstances(Main main, Teacher teacher){
        this.main = main;
        this.engine = main.getEngine();
        this.teacher = teacher;
        bossControl.setVisible(teacher.isBoss());
        bossControl.setManaged(teacher.isBoss());
        loadShifts();
    }

}
