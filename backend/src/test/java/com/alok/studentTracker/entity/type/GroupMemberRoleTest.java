package com.alok.studentTracker.entity.type;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GroupMemberRoleTest {

    @Test
    void testGroupMemberRoleValues() {
        GroupMemberRole[] values = GroupMemberRole.values();
        assertEquals(2, values.length);
    }

    @Test
    void testGroupMemberRoleStandard() {
        GroupMemberRole standard = GroupMemberRole.STANDARD;
        assertEquals("STANDARD", standard.name());
    }

    @Test
    void testGroupMemberRoleHeadman() {
        GroupMemberRole headman = GroupMemberRole.HEADMAN;
        assertEquals("HEADMAN", headman.name());
    }

    @Test
    void testGroupMemberRoleValueOf() {
        GroupMemberRole role = GroupMemberRole.valueOf("STANDARD");
        assertEquals(GroupMemberRole.STANDARD, role);
    }

    @Test
    void testGroupMemberRoleValueOfHeadman() {
        GroupMemberRole role = GroupMemberRole.valueOf("HEADMAN");
        assertEquals(GroupMemberRole.HEADMAN, role);
    }

    @Test
    void testGroupMemberRoleComparison() {
        assertTrue(GroupMemberRole.STANDARD == GroupMemberRole.STANDARD);
        assertNotEquals(GroupMemberRole.STANDARD, GroupMemberRole.HEADMAN);
    }

    @Test
    void testGroupMemberRoleOrdinal() {
        assertEquals(0, GroupMemberRole.STANDARD.ordinal());
        assertEquals(1, GroupMemberRole.HEADMAN.ordinal());
    }

    @Test
    void testAllGroupMemberRolesExist() {
        boolean hasStandard = false;
        boolean hasHeadman = false;

        for (GroupMemberRole role : GroupMemberRole.values()) {
            if (role == GroupMemberRole.STANDARD) hasStandard = true;
            if (role == GroupMemberRole.HEADMAN) hasHeadman = true;
        }

        assertTrue(hasStandard && hasHeadman);
    }
}
