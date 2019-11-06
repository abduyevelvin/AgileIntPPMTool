package io.agileintelligence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.agileintelligence.domain.ProjectTask;

@Repository
//@Transactional
public interface ProjectTaskRepository extends CrudRepository<ProjectTask, Long> {

	List<ProjectTask> findByProjectIdentifierOrderByPriority(String id);
	
	ProjectTask findByProjectSequence(String seq);
	
	
	/*
	 * @Query("DELETE FROM ProjectTask pt WHERE pt =:pt")
	 * 
	 * @Modifying void delete(@Param("pt") ProjectTask projectTask);
	 */
	 
}
