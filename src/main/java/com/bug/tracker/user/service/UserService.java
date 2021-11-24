package com.bug.tracker.user.service;

import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.common.object.SearchResponseTO;
import com.bug.tracker.user.dto.UserTO;

import java.util.List;

public interface UserService {

  UserTO add(UserTO userTO);

  UserTO update(UserTO userTO);

  SearchResponseTO getList(SearchCriteriaObj searchCriteriaObj);

  List<?> getEmployeeList(SearchCriteriaObj searchCriteriaObj);

  UserTO getById(Integer id);

  UserTO getByUsername(String username) throws Exception;

  void delete(List<Integer> id);

}
