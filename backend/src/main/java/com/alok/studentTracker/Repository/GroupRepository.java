package com.alok.studentTracker.Repository;

import com.alok.studentTracker.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByName(String name);

    @Query("SELECT g FROM Group g LEFT JOIN FETCH g.users WHERE g.id = :id")
    Optional<Group> findByIdWithUsers(Long id);
}
