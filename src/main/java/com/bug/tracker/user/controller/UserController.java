package com.bug.tracker.user.controller;

import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.common.object.SearchResponseTO;
import com.bug.tracker.common.object.ValidationError;
import com.bug.tracker.master.dto.ResponseTO;
import com.bug.tracker.user.dto.UserTO;
import com.bug.tracker.user.service.UserService;
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
@RequestMapping("/user")
public class UserController {

  @Autowired
  private Validator userValidator;

  @Autowired
  private UserService userService;

  private ResponseTO response;

  @InitBinder
  private void initBinder(WebDataBinder binder) {
    binder.setValidator(userValidator);
  }

  @PostMapping("/add")
  public ResponseEntity<?> addUser(@Valid @RequestBody UserTO userTO, Errors errors) {
    if (errors.hasErrors()) {
      ValidationError validationError = ValidationError.fromBindingErrors(errors);
      return new ResponseEntity<>(validationError, HttpStatus.OK);
    }
    UserTO userTO_return = userService.addUser(userTO);
    response = ResponseTO.responseBuilder(200, "BT001", "/user", "user", userTO_return);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/update")
  public ResponseEntity<?> updateUser(@Valid @RequestBody UserTO userTO, Errors errors) {
    if (errors.hasErrors()) {
      ValidationError validationError = ValidationError.fromBindingErrors(errors);
      return new ResponseEntity<>(validationError, HttpStatus.OK);
    }
    UserTO userTO_return = userService.updateUser(userTO);
    response = ResponseTO.responseBuilder(200, "BT002", "/user", "user", userTO_return);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/list")
  public ResponseEntity<?> getUserList(@RequestBody PaginationCriteria paginationCriteria) {
    SearchResponseTO searchResponseTO = userService.getUserList(paginationCriteria);
    if (searchResponseTO.getList() == null || searchResponseTO.getList().isEmpty()) {
      response = ResponseTO.responseBuilder(200, "BT006", "/user", "user", searchResponseTO);
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    response = ResponseTO.responseBuilder(200, "BT003", "/user", "user", searchResponseTO);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/employee-list")
  public ResponseEntity<?> getEmployeeList(@RequestBody PaginationCriteria paginationCriteria) {
    SearchResponseTO searchResponseTO = userService.getEmployeeList(paginationCriteria);
    if (searchResponseTO.getList() == null || searchResponseTO.getList().isEmpty()) {
      response = ResponseTO.responseBuilder(200, "BT006", "/employee-list", "user", searchResponseTO);
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    response = ResponseTO.responseBuilder(200, "BT003", "/employee-list", "user", searchResponseTO);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/company-employee")
  public ResponseEntity<?> getEmployeeListByCompany(@RequestBody PaginationCriteria paginationCriteria) {
    SearchResponseTO searchResponseTO = userService.getEmployeeListByCompany(paginationCriteria);
    if (searchResponseTO.getList() == null || searchResponseTO.getList().isEmpty()) {
      response = ResponseTO.responseBuilder(200, "BT006", "/employee-list", "user", searchResponseTO);
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    response = ResponseTO.responseBuilder(200, "BT003", "/employee-list", "user", searchResponseTO);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getUserById(@PathVariable Integer id) {
    UserTO userTO = userService.getUserById(id);
    if (userTO == null) {
      response = ResponseTO.responseBuilder(200, "BT006", "/user", "user", null);
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    response = ResponseTO.responseBuilder(200, "BT004", "/user", "user", userTO);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/username")
  public ResponseEntity<?> getUserByUsername(@RequestParam(name = "username") String username) throws Exception {
    UserTO userTO = userService.getUserByUsername(username);
    if (userTO == null) {
      response = ResponseTO.responseBuilder(200, "BT006", "/user", "user", null);
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    response = ResponseTO.responseBuilder(200, "BT004", "/user", "user", userTO);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<?> deleteUser(@RequestParam(name = "id") List<Integer> id) {
    userService.deleteUser(id);
    response = ResponseTO.responseBuilder(200, "BT005", "/user", "user", id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
