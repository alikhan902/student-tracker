package student_tracker.model;

public record UserProfile(long id, String name, String username, String email, String studentType, Long groupId) {
}
