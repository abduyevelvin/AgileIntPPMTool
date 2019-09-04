package io.agileintelligence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.agileintelligence.Exception.ProjectIDException;
import io.agileintelligence.domain.Backlog;
import io.agileintelligence.domain.Project;
import io.agileintelligence.repository.BacklogRepository;
import io.agileintelligence.repository.ProjectRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private BacklogRepository backlogRepository;

	public Project saveOrUpdate(Project p) {

		try {
			p.setProjectIdentifier(p.getProjectIdentifier().toUpperCase());
			
			if(p.getId() == null) {
				Backlog backlog = new Backlog();
				p.setBacklog(backlog);
				backlog.setProject(p);
				backlog.setProjectIdentifier(p.getProjectIdentifier().toUpperCase());
			}			
			else {
				p.setBacklog(backlogRepository.findByProjectIdentifier(p.getProjectIdentifier().toUpperCase()));
			}
			
			return projectRepository.save(p);
			
		} catch (Exception e) {
			throw new ProjectIDException("Project ID '" + p.getProjectIdentifier().toUpperCase() + "' already exists!");
		}

	}

	public Project findByProjectIdentifier(String projectID) {

		Project project = projectRepository.findByProjectIdentifier(projectID.toUpperCase());

		if (project == null) {
			throw new ProjectIDException("Project ID '" + projectID.toUpperCase() + "' doesn't exist!");
		}

		return project;
	}

	public Iterable<Project> findAllProjects() {

		return projectRepository.findAll();
	}

	public void deleteProjectByIdentifier(String projectID) {

		Project project = projectRepository.findByProjectIdentifier(projectID.toUpperCase());

		if (project == null) {
			throw new ProjectIDException(
					"Cann't delete project ID '" + projectID.toUpperCase() + "'. This project doesn't exist!");
		}
		
		projectRepository.delete(project);
	}
	
	public void deleteAllprojects() {
		projectRepository.deleteAll();
	}
}
