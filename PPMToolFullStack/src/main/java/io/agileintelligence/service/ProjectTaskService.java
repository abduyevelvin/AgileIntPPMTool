package io.agileintelligence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.agileintelligence.domain.Backlog;
import io.agileintelligence.domain.ProjectTask;
import io.agileintelligence.repository.BacklogRepository;
import io.agileintelligence.repository.ProjectTaskRepository;

@Service
public class ProjectTaskService {

	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
		
		// Exception: Project not found
		
		// PTs to be added to a specific project, project != null, BL exists
		Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
		// set the BL to PT
		projectTask.setBacklog(backlog);
		// we want our project sequence to be like this: IDPRO-1, IDPRO-2, ...100, 101
		Integer backlogSequence = backlog.getPTSequence();
		// Update the BL sequence
		backlogSequence++;
		backlog.setPTSequence(backlogSequence);
		
		// Add sequence to Project Task
		projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
		projectTask.setProjectIdentifier(projectIdentifier);
		
		// INITIAL priority when priority null
		if(projectTask.getPriority() == null) { // in the future projectTask.getPriority() == 0 || 
			projectTask.setPriority(3);
		}
		// INITIAL status when status null
		if(projectTask.getStatus() == "" || projectTask.getStatus() == null) {
			projectTask.setStatus("TO_DO");
		}
		
		return projectTaskRepository.save(projectTask);
	}
	
	public Iterable<ProjectTask> findBacklogById(String id) {
		
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
	}
}
