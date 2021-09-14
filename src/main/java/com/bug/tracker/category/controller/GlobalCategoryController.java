package com.bug.tracker.category.controller;

import com.bug.tracker.category.dto.GlobalCategoryTO;
import com.bug.tracker.category.service.GlobalCategoryService;
import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.common.object.ValidationError;
import com.bug.tracker.common.object.ValidationErrorBuilder;
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
    public ResponseEntity<?> add(@Valid @RequestBody GlobalCategoryTO globalCategoryTO, Errors errors) {
        if (errors.hasErrors()) {
            ValidationError validationError = ValidationErrorBuilder.fromBindingErrors(errors);
            return new ResponseEntity<>(validationError, HttpStatus.OK);
        }
        GlobalCategoryTO globalCategoryTO2 = globalCategoryService.add(globalCategoryTO);
        response = ResponseTO.responseBuilder(200, "BT001", "/category", "globalCategory", globalCategoryTO2);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody GlobalCategoryTO globalCategoryTO, Errors errors) {
        if (errors.hasErrors()) {
            ValidationError validationError = ValidationErrorBuilder.fromBindingErrors(errors);
            return new ResponseEntity<>(validationError, HttpStatus.OK);
        }
        GlobalCategoryTO globalCategoryTO2 = globalCategoryService.update(globalCategoryTO);
        response = ResponseTO.responseBuilder(200, "BT002", "/category", "globalCategory", globalCategoryTO2);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/list")
    public ResponseEntity<?> getList(@RequestBody SearchCriteriaObj searchCriteriaObj) {
        List<GlobalCategoryTO> globalCategoryTOs = globalCategoryService.getList(searchCriteriaObj);
        if (globalCategoryTOs == null || globalCategoryTOs.isEmpty()) {
            response = ResponseTO.responseBuilder(200, "BT006", "/category", "globalCategory", globalCategoryTOs);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response = ResponseTO.responseBuilder(200, "BT003", "/category", "globalCategory", globalCategoryTOs);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        GlobalCategoryTO globalCategoryTO = globalCategoryService.getById(id);
        if (globalCategoryTO == null) {
            response = ResponseTO.responseBuilder(200, "BT006", "/category", "globalCategory", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response = ResponseTO.responseBuilder(200, "BT004", "/category", "globalCategory", globalCategoryTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam(name = "id") List<Integer> id) {
        globalCategoryService.delete(id);
        response = ResponseTO.responseBuilder(200, "BT005", "/category", "globalCategory", id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
