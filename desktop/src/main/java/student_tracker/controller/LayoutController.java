package student_tracker.controller;

import student_tracker.service.StudentService;
import student_tracker.model.Student;

import javafx.fxml.FXML;
import javafx.collections.ObservableList;
import javafx.scene.control.*;


public class LayoutController {

    private final StudentService service = new StudentService();

    @FXML public TextField input;
    // @FXML public ListView<Student> listView;
    @FXML public Label status;

    @FXML 
    public void initialize() {
        // listView.setItems(service.getStudents());
    }

    @FXML 
    public void onAdd() {
        String name = input.getText();

        service.addStudent(name);

        input.clear();
        status.setText("Added: " + name);
    }

    @FXML 
    public void onDelete() {
        // Student selected = listView.getSelectionModel().getSelectedItem();

        // service.deleteStudent(selected);
        // status.setText("Deleted: " + selected.getName());
    }
}