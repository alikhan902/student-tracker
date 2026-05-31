package student_tracker.ui.dialog;

import javafx.scene.control.TextInputDialog;

public class TextInputDialogEx extends TextInputDialog {
    public TextInputDialogEx(String prompt, String initialValue) {
        super(initialValue);
        setTitle(prompt);
        setHeaderText(prompt);
    }
}
