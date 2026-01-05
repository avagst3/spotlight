package com.spotlight.back.spotlight.repositories;

import com.spotlight.back.spotlight.models.entities.Project;
import com.spotlight.back.spotlight.models.entities.Tag;
import com.spotlight.back.spotlight.models.entities.TagProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TagProjectRepository extends JpaRepository<TagProject, UUID> {
    List<TagProject> findByProject(Project project);
    List<TagProject> findByTag(Tag tag);
}