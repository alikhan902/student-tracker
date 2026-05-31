package student_tracker.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileTest {
    @Test
    void userProfile_recordFields() {
        UserProfile u = new UserProfile(2L, "Ivan", "ivan", "i@x.com", "STUDENT", null);
        assertEquals(2L, u.id());
        assertEquals("Ivan", u.name());
        assertEquals("ivan", u.username());
        assertEquals("i@x.com", u.email());
        assertEquals("STUDENT", u.studentType());
        assertNull(u.groupId());
    }
}
