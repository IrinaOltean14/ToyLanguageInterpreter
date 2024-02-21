package GUI;
import controller.Controller;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.ADT.*;
import model.PrgState;
import model.statement.IStmt;
import model.value.StringValue;
import model.value.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainController {

    private Controller controller;

    @FXML
    private TextField numberOfProgramStatesTextField;
    @FXML
    private TableView<MyPair<Integer, Value>> heapTableView;

    @FXML
    private TableColumn<MyPair<Integer, Value>, Integer> addressColumn;

    @FXML
    private TableColumn<MyPair<Integer, Value>, String> valueColumn;

    @FXML
    private ListView<String> outputListView;

    @FXML
    private ListView<String> fileTableListView;

    @FXML
    private ListView<Integer> programStateIdentifiersListView;

    @FXML
    private TableView<MyPair<String, Value>> symbolTableView;

    @FXML
    private TableColumn<MyPair<String, Value>, String> variableNameColumn;

    @FXML
    private TableColumn<MyPair<String, Value>, String> variableValueColumn;

    @FXML
    private ListView<String> executionStackListView;

    @FXML
    private Button runOneStepButton;

    @FXML
    private TableView<MyPair<Integer, Integer>> lockTableView;
    @FXML
    private TableColumn<MyPair<Integer, Integer>, String> locationColumn;
    @FXML
    private TableColumn<MyPair<Integer, Integer>, String> lockValueColumn;

    public void setController(Controller controller){
        this.controller = controller;
        populate();
    }

    private void populate() {
        populateHeapTableView();
        populateOutputListView();
        populateFileTableListView();
        populateProgramStateIdentifiersListView();
        populateSymbolTableView();
        populateExecutionStackListView();
        populateLockTableView();
    }

    @FXML
    public void initialize(){
        programStateIdentifiersListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        addressColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().first).asObject());
        valueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().second.toString()));
        variableNameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().first));
        variableValueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().second.toString()));
        locationColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().first.toString()));
        lockValueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().second.toString()));
    }

    @FXML
    private void changeProgramState(MouseEvent event){
        populateExecutionStackListView();
        populateSymbolTableView();
    }

    private PrgState getCurrentProgramState(){
        if (controller.getProgramStates().size() == 0)
            return null;
        else{
            int currentId = programStateIdentifiersListView.getSelectionModel().getSelectedIndex();
            if (currentId == -1)
                return controller.getProgramStates().get(0);
            else
                return controller.getProgramStates().get(currentId);
        }
    }

    private void populateLockTableView() {
        PrgState programState = getCurrentProgramState();
        IMyLockTable lockTable = Objects.requireNonNull(programState).getLockTable();
        //System.out.println(lockTable);
        ArrayList<MyPair<Integer, Integer>> lockTableEntries = new ArrayList<>();
        for(Map.Entry<Integer, Integer> entry: lockTable.getContent().entrySet()) {
            lockTableEntries.add(new MyPair<>(entry.getKey(), entry.getValue()));
        }
        lockTableView.setItems(FXCollections.observableArrayList(lockTableEntries));
    }

    private void populateNumberOfProgramStatesTextField(){
        List<PrgState> programStates = controller.getProgramStates();
        numberOfProgramStatesTextField.setText(String.valueOf(programStates.size()));
    }

    private void populateHeapTableView(){
        PrgState programState = getCurrentProgramState();
        IMyHeap heap = Objects.requireNonNull(programState).getHeap();
        ArrayList<MyPair<Integer, Value>> heapEntries = new ArrayList<>();
        for(Map.Entry<Integer, Value> entry: heap.getContent().entrySet()) {
            heapEntries.add(new MyPair<>(entry.getKey(), entry.getValue()));
        }
        heapTableView.setItems(FXCollections.observableArrayList(heapEntries));
    }

    private void populateOutputListView(){
        PrgState programState = getCurrentProgramState();
        List<String> output = new ArrayList<>();
        List<Value> outputList = Objects.requireNonNull(programState).getList().getContent();
        int index;
        for (index = 0; index < outputList.size(); index++){
            output.add(outputList.get(index).toString());
        }
        outputListView.setItems(FXCollections.observableArrayList(output));
    }

    private void populateFileTableListView(){
        PrgState programState = getCurrentProgramState();
        List<String> files = new ArrayList<>(Objects.requireNonNull(programState).getFileTable().getContent().keySet()).stream()
                .map(StringValue::getValue).toList();
        fileTableListView.setItems(FXCollections.observableList(files));

    }

    private void populateProgramStateIdentifiersListView(){
        List<PrgState> programStates = controller.getProgramStates();
        List<Integer> idList = programStates.stream().map(PrgState::getId).collect(Collectors.toList());
        programStateIdentifiersListView.setItems(FXCollections.observableList(idList));
        populateNumberOfProgramStatesTextField();
    }

    private void populateSymbolTableView(){
        PrgState programState = getCurrentProgramState();
        IMyDictionary<String,Value> symbolTable = Objects.requireNonNull(programState).getDictionary();
        ArrayList<MyPair<String, Value>> symbolTableEntries = new ArrayList<>();
        for (Map.Entry<String, Value> entry: symbolTable.getContent().entrySet()) {
            symbolTableEntries.add(new MyPair<>(entry.getKey(), entry.getValue()));
        }
        symbolTableView.setItems(FXCollections.observableArrayList(symbolTableEntries));
    }

    private void populateExecutionStackListView(){
        PrgState programState = getCurrentProgramState();
        List<String> executionStackToString = new ArrayList<>();
        if (programState != null)
            for (IStmt statement: programState.getStack().getReversed()) {
                executionStackToString.add(statement.toString());
            }
        executionStackListView.setItems(FXCollections.observableList(executionStackToString));
    }

    @FXML
    private void runOneStep(MouseEvent mouseEvent){
        if (controller != null){
            try{
                List<PrgState> programStates = Objects.requireNonNull(controller.getProgramStates());
                if (programStates.size() > 0){
                    controller.oneStep();
                    //System.out.println("1");
                    populate();
                    programStates = controller.removeCompletedPrg(controller.getProgramStates());
                    controller.setProgramStates(programStates);
                    populateProgramStateIdentifiersListView();

                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText("An error has occured!");
                    alert.setContentText("There is nothing left to execute!");
                    alert.showAndWait();
                }
            }  catch (InterruptedException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Execution error!");
                alert.setHeaderText("An execution error has occured!");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }

        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("An error has occured!");
            alert.setContentText("No program selected!");
            alert.showAndWait();
        }
    }


}

class MyPair<T1, T2> {
    T1 first;
    T2 second;

    public MyPair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }
}

