package com.bug.tracker.user.service;

import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.common.object.SearchResponseTO;
import com.bug.tracker.user.dto.*;

import java.util.List;

public interface UserService {

  UserTO addUser(UserTO userTO);

  UserBasicTO copyUserToTenant(UserBasicTO userBasicTO);

  UserBasicTO updateUserToTenant(UserBasicTO userBasicTO);

  UserTO updateUser(UserTO userTO);

  boolean changePassword(PasswordChangeTO passwordChangeTO);

  void sendToken(String userEmail) throws Exception;

  String validatePasswordResetToken(String token);

  UserTO resetPassword(ForgotPasswordTO forgotPasswordTO);

  SearchResponseTO getUserList(PaginationCriteria paginationCriteria);

  SearchResponseTO getEmployeeList(PaginationCriteria paginationCriteria);

  SearchResponseTO getEmployeeListByCompany(PaginationCriteria paginationCriteria);

  UserTO getUserById(Integer id);

  UserTO getUserByUsername(String username) throws Exception;

  UserTO getUserByEmail(String username) throws Exception;

  void deleteUser(List<Integer> id);

}
