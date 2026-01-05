package com.spotlight.back.spotlight.repositories;

import com.spotlight.back.spotlight.models.entities.Team;
import com.spotlight.back.spotlight.models.entities.User;
import com.spotlight.back.spotlight.models.entities.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserTeamRepository extends JpaRepository<UserTeam, UUID> {
    List<UserTeam> findByUser(User user);
    List<UserTeam> findByTeam(Team team);
    Optional<UserTeam> findByUserAndTeam(User user, Team team);
}