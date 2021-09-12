package com.bug.tracker.category.controller;

import com.bug.tracker.category.dto.GlobalCategoryTO;
import com.bug.tracker.category.service.GlobalCategoryService;
import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.common.object.ValidationError;
import com.bug.tracker.common.object.ValidationErrorBuilder;
import com.bug.tracker.common.service.APP_MSG;
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
            return new ResponseEntity<ValidationError>(validationError, HttpStatus.OK);
        }
        GlobalCategoryTO globalCategoryTO2 = globalCategoryService.add(globalCategoryTO);
        responseBuilder(200, "BT001", APP_MSG.MESSAGE.get("BT001"), "globalCategory", globalCategoryTO2);
        return new ResponseEntity<ResponseTO>(response, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody GlobalCategoryTO globalCategoryTO, Errors errors) {
        if (errors.hasErrors()) {
            ValidationError validationError = ValidationErrorBuilder.fromBindingErrors(errors);
            return new ResponseEntity<ValidationError>(validationError, HttpStatus.OK);
        }
        GlobalCategoryTO globalCategoryTO2 = globalCategoryService.update(globalCategoryTO);
        responseBuilder(200, "BT002", APP_MSG.MESSAGE.get("BT002"), "globalCategory", globalCategoryTO2);
        return new ResponseEntity<ResponseTO>(response, HttpStatus.OK);
    }

    @PostMapping("/list")
    public ResponseEntity<?> getList(@RequestBody SearchCriteriaObj searchCriteriaObj) {
        List<GlobalCategoryTO> globalCategoryTOs = globalCategoryService.getList(searchCriteriaObj);
        if (globalCategoryTOs.isEmpty()) {
            responseBuilder(200, "BT006", APP_MSG.MESSAGE.get("BT006"), "globalCategory", globalCategoryTOs);
            return new ResponseEntity<ResponseTO>(response, HttpStatus.OK);
        }
        responseBuilder(200, "BT003", APP_MSG.MESSAGE.get("BT003"), "globalCategory", globalCategoryTOs);
        return new ResponseEntity<ResponseTO>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        GlobalCategoryTO globalCategoryTO = globalCategoryService.getById(id);
        responseBuilder(200, "BT004", APP_MSG.MESSAGE.get("BT004"), "globalCategory", globalCategoryTO);
        return new ResponseEntity<ResponseTO>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam(name = "id") List<Integer> id) {
        globalCategoryService.delete(id);
        responseBuilder(200, "BT005", APP_MSG.MESSAGE.get("BT005"), "globalCategory", id);
        return new ResponseEntity<ResponseTO>(response, HttpStatus.OK);
    }

    private void responseBuilder(int status, String code, String message, String key, Object data) {
        response = new ResponseTO();
        response.setStatus(status);
        response.setPath("/project/globalCategory");
        response.setMessage(message);
        response.setCode(code);
        response.putData(key, data);
    }
}
