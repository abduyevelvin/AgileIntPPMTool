package io.agileintelligence.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.agileintelligence.domain.Project;
import io.agileintelligence.service.MapValidationErrorService;
import io.agileintelligence.service.ProjectService;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

	@Autowired
	private ProjectService projectService;

	@Autowired
	private MapValidationErrorService mapValidationService;

	@PostMapping("")
	public ResponseEntity<?> createNewProject(@Valid @RequestBody Project p, BindingResult result) {

		ResponseEntity<?> errorMap = mapValidationService.MapValidationService(result);

		if (errorMap != null)
			return errorMap;

		Project pro = projectService.saveOrUpdate(p);

		return new ResponseEntity<Project>(pro, HttpStatus.CREATED);
	}

	@GetMapping("/{projectId}")
	public ResponseEntity<?> getProjectById(@PathVariable String projectId) {

		Project project = projectService.findByProjectIdentifier(projectId);

		return new ResponseEntity<Project>(project, HttpStatus.OK);
	}

	@GetMapping("/all")
	public Iterable<Project> getAllProjects() {
		return projectService.findAllProjects();
	}

	@DeleteMapping("/{projectId}")
	public ResponseEntity<?> deleteProject(@PathVariable String projectId) {
		projectService.deleteProjectByIdentifier(projectId);

		return new ResponseEntity<String>("Project with ID: '" + projectId.toUpperCase() + "' is deleted",
				HttpStatus.OK);
	}

	@DeleteMapping("/all")
	public ResponseEntity<?> deleteAllProjects() {
		projectService.deleteAllprojects();

		return new ResponseEntity<String>("All projects have been deleted!", HttpStatus.OK);
	}
}
