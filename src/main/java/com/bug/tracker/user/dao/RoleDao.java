package com.bug.tracker.user.dao;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.user.entity.RoleBO;

import java.util.List;

public interface RoleDao {

    RoleBO addRole(RoleBO roleBO);

    RoleBO updateRole(RoleBO roleBO);

    CommonListTO<RoleBO> getRoleList(SearchCriteriaObj searchCriteriaObj);

    RoleBO getRoleById(Integer id);

    void deleteRole(List<Integer> id);

}
