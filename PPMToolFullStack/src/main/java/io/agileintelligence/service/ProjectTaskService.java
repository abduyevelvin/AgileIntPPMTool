package io.agileintelligence.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.agileintelligence.Exception.ProjectNotFoundException;
import io.agileintelligence.domain.Backlog;
import io.agileintelligence.domain.Project;
import io.agileintelligence.domain.ProjectTask;
import io.agileintelligence.repository.BacklogRepository;
import io.agileintelligence.repository.ProjectRepository;
import io.agileintelligence.repository.ProjectTaskRepository;

@Service
public class ProjectTaskService {

	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
		
		try {
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
		} catch (Exception e) {
			// Exception: Project not found
			throw new ProjectNotFoundException("Project Not Found by ID: " + projectIdentifier);
		}
	}
	
	public Iterable<ProjectTask> findBacklogById(String id) {
		
		Project project = projectRepository.findByProjectIdentifier(id);
		
		if(project == null) {
			throw new ProjectNotFoundException("Project Not Found by ID: " + id);
		}
		
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
	}
	
	public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id) {
		
//		Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
//		if(backlog == null) {
//			throw new ProjectNotFoundException("Project Not Found by ID: " + backlog_id);
//		}
		
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
		if(projectTask == null) {
			throw new ProjectNotFoundException("Project Task '" + pt_id + "' not found");
		}
		
		if(!projectTask.getProjectIdentifier().equals(backlog_id)) {
			throw new ProjectNotFoundException("Project Task '" + pt_id + "' doesn't exist in project: " + backlog_id);
		}
		
		return projectTask;
	}
	
	public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id) {
		//ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
		
		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);
		
		projectTask = updatedTask;
		
		return projectTaskRepository.save(projectTask);
	}
	
	@Transactional
	public void deletePTByProjectSequence(String backlog_id, String pt_id) {
		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);
		
		/*
		 * Backlog backlog = projectTask.getBacklog(); List<ProjectTask> pts =
		 * backlog.getProjectTasks(); pts.remove(projectTask);
		 * 
		 * backlogRepository.save(backlog);
		 */
		
		projectTaskRepository.delete(projectTask);
	}
}
