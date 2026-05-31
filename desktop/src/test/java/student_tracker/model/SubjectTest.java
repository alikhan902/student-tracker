package student_tracker.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubjectTest {
    @Test
    void subjectRecord_hasValues() {
        Subject s = new Subject(1L, "Title", "Desc", "/img.png");
        assertEquals(1L, s.id());
        assertEquals("Title", s.title());
        assertEquals("Desc", s.description());
        assertEquals("/img.png", s.photoUrl());
    }
}
