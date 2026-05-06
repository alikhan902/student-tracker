package student_tracker.service;

import student_tracker.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StudentService {

    private final ObservableList<Student> students = FXCollections.observableArrayList();

    public ObservableList<Student> getStudents() {
        return students;
    }

    public void addStudent(String name) {
        if (name == null || name.isEmpty()) return;
        students.add(new Student(name));
    }

    public void deleteStudent(Student student) {
        students.remove(student);
    }
}