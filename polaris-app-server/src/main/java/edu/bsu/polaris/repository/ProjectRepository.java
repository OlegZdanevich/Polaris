package edu.bsu.polaris.repository;

import edu.bsu.polaris.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findById(Long pollId);

    Page<Project> findByCreatedBy(Long userId, Pageable pageable);

    long countByCreatedBy(Long userId);

    List<Project> findByIdIn(List<Long> pollIds);

    List<Project> findByIdIn(List<Long> pollIds, Sort sort);

}
