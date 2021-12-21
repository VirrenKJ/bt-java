package com.bug.tracker.user.service;

import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.common.object.SearchResponseTO;
import com.bug.tracker.user.dto.UserTO;

import java.util.List;

public interface UserService {

  UserTO addUser(UserTO userTO);

  UserTO updateUser(UserTO userTO);

  SearchResponseTO getUserList(PaginationCriteria paginationCriteria);

  SearchResponseTO getEmployeeList(PaginationCriteria paginationCriteria);

  UserTO getUserById(Integer id);

  UserTO getUserByUsername(String username) throws Exception;

  void deleteUser(List<Integer> id);

}
