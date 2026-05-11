package com.alok.studentTracker.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class GroupTest {

    private Group group;

    @BeforeEach
    void setUp() {
        group = Group.builder()
                .id(1L)
                .name("Computer Science")
                .users(new HashSet<>())
                .trainingSubjects(new HashSet<>())
                .build();
    }

    @Test
    void testGroupCreation() {
        assertNotNull(group);
        assertEquals(1L, group.getId());
        assertEquals("Computer Science", group.getName());
    }

    @Test
    void testGroupUsers() {
        assertTrue(group.getUsers().isEmpty());
        
        User user = User.builder()
                .id(1L)
                .username("user1")
                .password("pass")
                .build();
        
        group.getUsers().add(user);
        assertEquals(1, group.getUsers().size());
        assertTrue(group.getUsers().contains(user));
    }

    @Test
    void testGroupTrainingSubjects() {
        assertTrue(group.getTrainingSubjects().isEmpty());
        
        TrainingSubject subject = TrainingSubject.builder()
                .id(1L)
                .title("Java Basics")
                .group(group)
                .build();
        
        group.getTrainingSubjects().add(subject);
        assertEquals(1, group.getTrainingSubjects().size());
        assertTrue(group.getTrainingSubjects().contains(subject));
    }

    @Test
    void testGroupBuilder() {
        Group builtGroup = Group.builder()
                .id(2L)
                .name("Mathematics")
                .build();
        
        assertEquals(2L, builtGroup.getId());
        assertEquals("Mathematics", builtGroup.getName());
    }

    @Test
    void testGroupNoArgsConstructor() {
        Group newGroup = new Group();
        assertNull(newGroup.getId());
        assertNull(newGroup.getName());
        assertNotNull(newGroup.getUsers());
        assertTrue(newGroup.getUsers().isEmpty());
        assertTrue(newGroup.getTrainingSubjects().isEmpty());
    }

    @Test
    void testGroupAllArgsConstructor() {
        Group newGroup = new Group(3L, "Physics", new HashSet<>(), new HashSet<>());
        assertEquals(3L, newGroup.getId());
        assertEquals("Physics", newGroup.getName());
    }

    @Test
    void testGroupSetters() {
        Group newGroup = new Group();
        newGroup.setId(4L);
        newGroup.setName("Chemistry");
        
        assertEquals(4L, newGroup.getId());
        assertEquals("Chemistry", newGroup.getName());
    }

    @Test
    void testGroupMultipleSubjects() {
        TrainingSubject subject1 = TrainingSubject.builder()
                .id(1L)
                .title("Subject 1")
                .group(group)
                .build();
        
        TrainingSubject subject2 = TrainingSubject.builder()
                .id(2L)
                .title("Subject 2")
                .group(group)
                .build();
        
        group.getTrainingSubjects().add(subject1);
        group.getTrainingSubjects().add(subject2);
        
        assertEquals(2, group.getTrainingSubjects().size());
    }
}
