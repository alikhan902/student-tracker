package student_tracker.model;

import java.nio.file.Path;

public record SubjectPayload(String title, String description, Path photoPath) {
}
