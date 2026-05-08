package student_tracker.model;

import java.util.List;

public record Group(String name, List<GroupMember> members) {
}
