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
import student_tracker.model.Material;
import student_tracker.model.MaterialPayload;

import java.nio.file.Path;

public class MaterialDialog extends Dialog<MaterialPayload> {
    public MaterialDialog(Stage stage, boolean editMode, Material material) {
        setTitle(editMode ? "Изменить материал" : "Создать материал");
        ButtonType submit = new ButtonType(editMode ? "Сохранить" : "Создать", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(submit, ButtonType.CANCEL);
        TextField title = new TextField(material == null ? "" : material.title());
        TextArea description = new TextArea(material == null ? "" : material.description());
        Button pickFile = new Button("Выбрать файл");
        Label fileLabel = new Label("Файл не выбран");
        final Path[] selected = {null};
        pickFile.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            java.io.File file = chooser.showOpenDialog(stage);
            if (file != null) {
                selected[0] = file.toPath();
                fileLabel.setText(file.getName());
            }
        });
        getDialogPane().setContent(new VBox(8, title, description, pickFile, fileLabel));
        setResultConverter(bt -> bt == submit ? new MaterialPayload(title.getText(), description.getText(), selected[0]) : null);
    }
}
