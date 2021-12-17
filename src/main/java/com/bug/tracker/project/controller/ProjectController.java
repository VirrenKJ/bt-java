package com.bug.tracker.project.controller;

import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.common.object.SearchResponseTO;
import com.bug.tracker.common.object.ValidationError;
import com.bug.tracker.master.dto.ResponseTO;
import com.bug.tracker.project.dto.ProjectTO;
import com.bug.tracker.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {

  @Autowired
  private Validator projectValidator;

  @Autowired
  private ProjectService projectService;

  private ResponseTO response;

  @InitBinder
  private void initBinder(WebDataBinder binder) {
    binder.setValidator(projectValidator);
  }

  @PostMapping("/add")
  public ResponseEntity<?> addProject(@Valid @RequestBody ProjectTO projectTO, Errors errors) {
    if (errors.hasErrors()) {
      ValidationError validationError = ValidationError.fromBindingErrors(errors);
      return new ResponseEntity<>(validationError, HttpStatus.OK);
    }
    ProjectTO projectTO_return = projectService.addProject(projectTO);
    response = ResponseTO.responseBuilder(200, "BT001", "/project", "project", projectTO_return);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/update")
  public ResponseEntity<?> updateProject(@Valid @RequestBody ProjectTO projectTO, Errors errors) {
    if (errors.hasErrors()) {
      ValidationError validationError = ValidationError.fromBindingErrors(errors);
      return new ResponseEntity<>(validationError, HttpStatus.OK);
    }
    ProjectTO projectTO_return = projectService.updateProject(projectTO);
    response = ResponseTO.responseBuilder(200, "BT002", "/project", "project", projectTO_return);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/list")
  public ResponseEntity<?> getProjectList(@RequestBody SearchCriteriaObj searchCriteriaObj) {
    SearchResponseTO searchResponseTO = projectService.getProjectList(searchCriteriaObj);
    if (searchResponseTO.getList() == null || searchResponseTO.getList().isEmpty()) {
      response = ResponseTO.responseBuilder(200, "BT006", "/project", "project", searchResponseTO);
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    response = ResponseTO.responseBuilder(200, "BT003", "/project", "project", searchResponseTO);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getProjectId(@PathVariable Integer id) {
    ProjectTO projectTO = projectService.getProjectId(id);
    if (projectTO == null) {
      response = ResponseTO.responseBuilder(200, "BT006", "/project", "project", null);
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    response = ResponseTO.responseBuilder(200, "BT004", "/project", "project", projectTO);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<?> deleteProject(@RequestParam(name = "id") List<Integer> id) {
    projectService.deleteProject(id);
    response = ResponseTO.responseBuilder(200, "BT005", "/project", "project", id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
