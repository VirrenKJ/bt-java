package com.bug.tracker.systemProfile.controller;

import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.common.object.SearchResponseTO;
import com.bug.tracker.common.object.ValidationError;
import com.bug.tracker.master.dto.ResponseTO;
import com.bug.tracker.systemProfile.dto.SystemProfileTO;
import com.bug.tracker.systemProfile.service.SystemProfileService;
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
@RequestMapping("/system-profile")
public class SystemProfileController {

  @Autowired
  private Validator systemProfileValidator;

  @Autowired
  private SystemProfileService systemProfileService;

  private ResponseTO response;

  @InitBinder
  private void initBinder(WebDataBinder binder) {
    binder.setValidator(systemProfileValidator);
  }

  @PostMapping("/add")
  public ResponseEntity<?> addSystemProfile(@Valid @RequestBody SystemProfileTO systemProfileTO, Errors errors) {
    if (errors.hasErrors()) {
      ValidationError validationError = ValidationError.fromBindingErrors(errors);
      return new ResponseEntity<>(validationError, HttpStatus.OK);
    }
    SystemProfileTO systemProfileTO_return = systemProfileService.addSystemProfile(systemProfileTO);
    response = ResponseTO.responseBuilder(200, "BT001", "/system-profile", "systemProfile", systemProfileTO_return);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/update")
  public ResponseEntity<?> updateSystemProfile(@Valid @RequestBody SystemProfileTO systemProfileTO, Errors errors) {
    if (errors.hasErrors()) {
      ValidationError validationError = ValidationError.fromBindingErrors(errors);
      return new ResponseEntity<>(validationError, HttpStatus.OK);
    }
    SystemProfileTO systemProfileTO_return = systemProfileService.updateSystemProfile(systemProfileTO);
    response = ResponseTO.responseBuilder(200, "BT002", "/system-profile", "systemProfile", systemProfileTO_return);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/list")
  public ResponseEntity<?> getSystemProfileList(@RequestBody PaginationCriteria paginationCriteria) {
    SearchResponseTO searchResponseTO = systemProfileService.getSystemProfileList(paginationCriteria);
    if (searchResponseTO.getList() == null || searchResponseTO.getList().isEmpty()) {
      response = ResponseTO.responseBuilder(200, "BT006", "/system-profile", "systemProfile", searchResponseTO);
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    response = ResponseTO.responseBuilder(200, "BT003", "/system-profile", "systemProfile", searchResponseTO);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getSystemProfileId(@PathVariable Integer id) {
    SystemProfileTO systemProfileTO = systemProfileService.getSystemProfileId(id);
    if (systemProfileTO == null) {
      response = ResponseTO.responseBuilder(200, "BT006", "/system-profile", "systemProfile", null);
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    response = ResponseTO.responseBuilder(200, "BT004", "/system-profile", "systemProfile", systemProfileTO);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<?> deleteSystemProfile(@RequestParam(name = "id") List<Integer> id) {
    systemProfileService.deleteSystemProfile(id);
    response = ResponseTO.responseBuilder(200, "BT005", "/system-profile", "systemProfile", id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
