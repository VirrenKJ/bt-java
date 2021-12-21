package com.bug.tracker.issue.controller;

import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.common.object.SearchResponseTO;
import com.bug.tracker.common.object.ValidationError;
import com.bug.tracker.issue.dto.IssueTO;
import com.bug.tracker.issue.service.IssueService;
import com.bug.tracker.master.dto.ResponseTO;
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
@RequestMapping("/issue")
public class IssueController {

  @Autowired
  private Validator issueValidator;

  @Autowired
  private IssueService issueService;

  private ResponseTO response;

  @InitBinder
  private void initBinder(WebDataBinder binder) {
    binder.setValidator(issueValidator);
  }

  @PostMapping("/add")
  public ResponseEntity<?> addIssue(@Valid @RequestBody IssueTO issueTO, Errors errors) {
    if (errors.hasErrors()) {
      ValidationError validationError = ValidationError.fromBindingErrors(errors);
      return new ResponseEntity<>(validationError, HttpStatus.OK);
    }
    IssueTO issueTO_return = issueService.addIssue(issueTO);
    response = ResponseTO.responseBuilder(200, "BT001", "/issue", "issue", issueTO_return);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/update")
  public ResponseEntity<?> updateIssue(@Valid @RequestBody IssueTO issueTO, Errors errors) {
    if (errors.hasErrors()) {
      ValidationError validationError = ValidationError.fromBindingErrors(errors);
      return new ResponseEntity<>(validationError, HttpStatus.OK);
    }
    IssueTO issueTO_return = issueService.updateIssue(issueTO);
    response = ResponseTO.responseBuilder(200, "BT002", "/issue", "issue", issueTO_return);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/list")
  public ResponseEntity<?> getIssueList(@RequestBody PaginationCriteria paginationCriteria) {
    SearchResponseTO searchResponseTO = issueService.getIssueList(paginationCriteria);
    if (searchResponseTO.getList() == null || searchResponseTO.getList().isEmpty()) {
      response = ResponseTO.responseBuilder(200, "BT006", "/issue", "issue", searchResponseTO);
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    response = ResponseTO.responseBuilder(200, "BT003", "/issue", "issue", searchResponseTO);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getIssueId(@PathVariable Integer id) {
    IssueTO issueTO = issueService.getIssueId(id);
    if (issueTO == null) {
      response = ResponseTO.responseBuilder(200, "BT006", "/issue", "issue", null);
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    response = ResponseTO.responseBuilder(200, "BT004", "/issue", "issue", issueTO);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<?> deleteIssue(@RequestParam(name = "id") List<Integer> id) {
    issueService.deleteIssue(id);
    response = ResponseTO.responseBuilder(200, "BT005", "/issue", "issue", id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
