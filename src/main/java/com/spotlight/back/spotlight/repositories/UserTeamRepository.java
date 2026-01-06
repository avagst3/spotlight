package com.spotlight.back.spotlight.repositories;

import com.spotlight.back.spotlight.models.entities.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserTeamRepository extends JpaRepository<UserTeam, UUID> {
    List<UserTeam> findByUserId(UUID userId);
    List<UserTeam> findByTeamId(UUID teamId);
    Optional<UserTeam> findByUserIdAndTeamId(UUID userId, UUID teamId);
}