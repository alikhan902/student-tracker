package com.alok.studentTracker.Repository;

import com.alok.studentTracker.entity.GroupMembers;
import com.alok.studentTracker.entity.GroupMembersId;
import com.alok.studentTracker.entity.type.GroupMemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMembersRepository extends JpaRepository<GroupMembers, GroupMembersId> {
    
    @Query("SELECT gm FROM GroupMembers gm WHERE gm.group.id = :groupId")
    List<GroupMembers> findByGroupId(@Param("groupId") Long groupId);

    @Query("SELECT gm FROM GroupMembers gm WHERE gm.user.id = :userId")
    List<GroupMembers> findByUserId(@Param("userId") Long userId);

    @Query("SELECT gm FROM GroupMembers gm WHERE gm.group.id = :groupId AND gm.role = :role")
    List<GroupMembers> findByGroupIdAndRole(@Param("groupId") Long groupId, @Param("role") GroupMemberRole role);

    @Query("SELECT gm FROM GroupMembers gm WHERE gm.group.id = :groupId AND gm.user.id = :userId")
    Optional<GroupMembers> findByGroupIdAndUserId(@Param("groupId") Long groupId, @Param("userId") Long userId);
}
