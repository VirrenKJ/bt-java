package com.bug.tracker.user.dao;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.user.entity.UserBO;
import com.bug.tracker.user.entity.UserBasicBO;
import com.bug.tracker.user.entity.UserDetailBO;

import java.util.List;

public interface UserDao {

  UserBO addUser(UserBO userBO);

  UserBasicBO copyUserToTenant(UserBasicBO userBasicBO);

  UserBO updateUser(UserBO userBO);

  CommonListTO<UserBO> getUserList(SearchCriteriaObj searchCriteriaObj);

  CommonListTO<UserDetailBO> getEmployeeList(SearchCriteriaObj searchCriteriaObj);

  UserBO getUserById(Integer id);

  UserBO getUserByUsername(String username) throws Exception;

  void deleteUser(List<Integer> id);
}
