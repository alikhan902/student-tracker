package student_tracker.ui.dialog;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import student_tracker.model.PasswordChange;

public class PasswordDialog extends Dialog<PasswordChange> {
    public PasswordDialog() {
        setTitle("Сменить пароль");
        ButtonType submit = new ButtonType("Обновить", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(submit, ButtonType.CANCEL);

        PasswordField current = new PasswordField();
        PasswordField next = new PasswordField();
        PasswordField confirm = new PasswordField();
        current.setPromptText("Текущий пароль");
        next.setPromptText("Новый пароль");
        confirm.setPromptText("Подтвердите пароль");
        VBox content = new VBox(8, current, next, confirm);
        getDialogPane().setContent(content);
        setResultConverter(bt -> {
            if (bt != submit) return null;
            if (!next.getText().equals(confirm.getText())) return null;
            return new PasswordChange(current.getText(), next.getText());
        });
    }
}
