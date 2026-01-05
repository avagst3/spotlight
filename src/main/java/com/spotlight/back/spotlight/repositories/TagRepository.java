package com.spotlight.back.spotlight.repositories;

import com.spotlight.back.spotlight.models.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {
}