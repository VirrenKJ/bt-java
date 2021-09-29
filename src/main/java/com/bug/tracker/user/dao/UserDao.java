package com.bug.tracker.user.dao;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.user.entity.UserBO;

import java.util.List;

public interface UserDao {

    UserBO add(UserBO userBO);

    UserBO update(UserBO userBO);

    CommonListTO<UserBO> getList(SearchCriteriaObj searchCriteriaObj);

    UserBO getById(Integer id);

    UserBO getByUsername(String username) throws Exception;

    void delete(List<Integer> id);
}
