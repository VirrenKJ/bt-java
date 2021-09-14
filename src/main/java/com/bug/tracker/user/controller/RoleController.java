package com.bug.tracker.user.controller;

import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.common.object.ValidationError;
import com.bug.tracker.common.object.ValidationErrorBuilder;
import com.bug.tracker.master.dto.ResponseTO;
import com.bug.tracker.user.dto.RoleTO;
import com.bug.tracker.user.service.RoleService;
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
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private Validator roleValidator;

    @Autowired
    private RoleService roleService;

    private ResponseTO response;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(roleValidator);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody RoleTO roleTO, Errors errors) {
        if (errors.hasErrors()) {
            ValidationError validationError = ValidationErrorBuilder.fromBindingErrors(errors);
            return new ResponseEntity<>(validationError, HttpStatus.OK);
        }
        RoleTO roleTO_return = roleService.add(roleTO);
        response = ResponseTO.responseBuilder(200, "BT001", "/role", "role", roleTO_return);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody RoleTO roleTO, Errors errors) {
        if (errors.hasErrors()) {
            ValidationError validationError = ValidationErrorBuilder.fromBindingErrors(errors);
            return new ResponseEntity<>(validationError, HttpStatus.OK);
        }
        RoleTO roleTO_return = roleService.update(roleTO);
        response = ResponseTO.responseBuilder(200, "BT002", "/role", "role", roleTO_return);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/list")
    public ResponseEntity<?> getList(@RequestBody SearchCriteriaObj searchCriteriaObj) {
        List<RoleTO> roleTOS = roleService.getList(searchCriteriaObj);
        if (roleTOS == null || roleTOS.isEmpty()) {
            response = ResponseTO.responseBuilder(200, "BT006", "/role", "role", roleTOS);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response = ResponseTO.responseBuilder(200, "BT003", "/role", "role", roleTOS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        RoleTO roleTO = roleService.getById(id);
        if (roleTO == null) {
            response = ResponseTO.responseBuilder(200, "BT006", "/role", "role", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response = ResponseTO.responseBuilder(200, "BT004", "/role", "role", roleTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam(name = "id") List<Integer> id) {
        roleService.delete(id);
        response = ResponseTO.responseBuilder(200, "BT005", "/role", "role", id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
