package ca.etsmtl.taf.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ca.etsmtl.taf.entity.Project;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {
  Optional<Project> findByName(String name);
}