package io.agileintelligence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.agileintelligence.domain.Project;
import java.lang.String;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

	Project findByProjectIdentifier(String projectidentifier);
	
	@Override
	Iterable<Project> findAll();
}
