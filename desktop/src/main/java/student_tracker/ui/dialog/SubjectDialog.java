package student_tracker.ui.dialog;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import student_tracker.model.Subject;
import student_tracker.model.SubjectPayload;

import java.nio.file.Path;

public class SubjectDialog extends Dialog<SubjectPayload> {
    public SubjectDialog(Stage stage, boolean editMode, Subject subject) {
        setTitle(editMode ? "Изменить предмет" : "Создать предмет");
        ButtonType submit = new ButtonType(editMode ? "Сохранить" : "Создать", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(submit, ButtonType.CANCEL);
        TextField title = new TextField(subject == null ? "" : subject.title());
        TextArea description = new TextArea(subject == null ? "" : subject.description());
        Button pickFile = new Button("Выбрать фото");
        Label fileLabel = new Label("Файл не выбран");
        final Path[] selected = {null};
        pickFile.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
            java.io.File file = chooser.showOpenDialog(stage);
            if (file != null) {
                selected[0] = file.toPath();
                fileLabel.setText(file.getName());
            }
        });
        getDialogPane().setContent(new VBox(8, title, description, pickFile, fileLabel));
        setResultConverter(bt -> bt == submit ? new SubjectPayload(title.getText(), description.getText(), selected[0]) : null);
    }
}
