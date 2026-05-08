package student_tracker.model;

import java.nio.file.Path;

public record MaterialPayload(String title, String description, Path filePath) {
}
