package com.alok.studentTracker.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class GroupMembersId implements Serializable {
    private Long groupId;
    private Long userId;
}
