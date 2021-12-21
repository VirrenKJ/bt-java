package com.bug.tracker.category.controller;

import com.bug.tracker.category.dto.GlobalCategoryTO;
import com.bug.tracker.category.service.GlobalCategoryService;
import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.common.object.SearchResponseTO;
import com.bug.tracker.common.object.ValidationError;
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
@RequestMapping("/category")
public class GlobalCategoryController {

  @Autowired
  private Validator globalCategoryValidator;

  @Autowired
  private GlobalCategoryService globalCategoryService;

  private ResponseTO response;

  @InitBinder
  private void initBinder(WebDataBinder binder) {
    binder.setValidator(globalCategoryValidator);
  }

  @PostMapping("/add")
  public ResponseEntity<?> addGlobalCategory(@Valid @RequestBody GlobalCategoryTO globalCategoryTO, Errors errors) {
    if (errors.hasErrors()) {
      ValidationError validationError = ValidationError.fromBindingErrors(errors);
      return new ResponseEntity<>(validationError, HttpStatus.OK);
    }
    GlobalCategoryTO globalCategoryTO_return = globalCategoryService.addGlobalCategory(globalCategoryTO);
    response = ResponseTO.responseBuilder(200, "BT001", "/category", "globalCategory", globalCategoryTO_return);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/update")
  public ResponseEntity<?> updateGlobalCategory(@Valid @RequestBody GlobalCategoryTO globalCategoryTO, Errors errors) {
    if (errors.hasErrors()) {
      ValidationError validationError = ValidationError.fromBindingErrors(errors);
      return new ResponseEntity<>(validationError, HttpStatus.OK);
    }
    GlobalCategoryTO globalCategoryTO_return = globalCategoryService.updateGlobalCategory(globalCategoryTO);
    response = ResponseTO.responseBuilder(200, "BT002", "/category", "globalCategory", globalCategoryTO_return);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/list")
  public ResponseEntity<?> getGlobalCategoryList(@RequestBody PaginationCriteria paginationCriteria) {
    SearchResponseTO searchResponseTO = globalCategoryService.getGlobalCategoryList(paginationCriteria);
    if (searchResponseTO.getList() == null || searchResponseTO.getList().isEmpty()) {
      response = ResponseTO.responseBuilder(200, "BT006", "/category", "globalCategory", searchResponseTO);
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    response = ResponseTO.responseBuilder(200, "BT003", "/category", "globalCategory", searchResponseTO);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getGlobalCategoryById(@PathVariable Integer id) {
    GlobalCategoryTO globalCategoryTO = globalCategoryService.getGlobalCategoryById(id);
    if (globalCategoryTO == null) {
      response = ResponseTO.responseBuilder(200, "BT006", "/category", "globalCategory", null);
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    response = ResponseTO.responseBuilder(200, "BT004", "/category", "globalCategory", globalCategoryTO);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<?> deleteGlobalCategory(@RequestParam(name = "id") List<Integer> id) {
    globalCategoryService.deleteGlobalCategory(id);
    response = ResponseTO.responseBuilder(200, "BT005", "/category", "globalCategory", id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
