package com.bug.tracker.company.controller;

import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.common.object.ValidationError;
import com.bug.tracker.common.object.ValidationErrorBuilder;
import com.bug.tracker.company.dto.CompanyTO;
import com.bug.tracker.company.service.CompanyService;
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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/company")
public class CompanyController {

  @Autowired
  private Validator companyValidator;

  @Autowired
  private CompanyService companyService;

  private ResponseTO response;

  @InitBinder
  private void initBinder(WebDataBinder binder) {
    binder.setValidator(companyValidator);
  }

  @PostMapping("/add")
  public ResponseEntity<?> add(@Valid @RequestBody CompanyTO companyTO, Errors errors) {
    if (errors.hasErrors()) {
      ValidationError validationError = ValidationErrorBuilder.fromBindingErrors(errors);
      return new ResponseEntity<>(validationError, HttpStatus.OK);
    }
    CompanyTO companyTO_return = companyService.add(companyTO);
    response = ResponseTO.responseBuilder(200, "BT001", "/company", "company", companyTO_return);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/update")
  public ResponseEntity<?> update(@Valid @RequestBody CompanyTO companyTO, Errors errors) {
    if (errors.hasErrors()) {
      ValidationError validationError = ValidationErrorBuilder.fromBindingErrors(errors);
      return new ResponseEntity<>(validationError, HttpStatus.OK);
    }
    CompanyTO companyTO_return = companyService.update(companyTO);
    response = ResponseTO.responseBuilder(200, "BT002", "/company", "company", companyTO_return);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/list")
  public ResponseEntity<?> getList(@RequestBody SearchCriteriaObj searchCriteriaObj) {
    List<CompanyTO> companyTOs = companyService.getList(searchCriteriaObj);
    if (companyTOs == null || companyTOs.isEmpty()) {
      response = ResponseTO.responseBuilder(200, "BT006", "/company", "company", companyTOs);
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    response = ResponseTO.responseBuilder(200, "BT003", "/company", "company", companyTOs);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable Integer id) {
    CompanyTO companyTO = companyService.getById(id);
    if (companyTO == null) {
      response = ResponseTO.responseBuilder(200, "BT006", "/company", "company", null);
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    response = ResponseTO.responseBuilder(200, "BT004", "/company", "company", companyTO);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<?> delete(@RequestParam(name = "id") List<Integer> id) {
    companyService.delete(id);
    response = ResponseTO.responseBuilder(200, "BT005", "/company", "company", id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
