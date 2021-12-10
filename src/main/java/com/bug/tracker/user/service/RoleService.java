package com.bug.tracker.user.service;

import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.user.dto.RoleTO;

import java.util.List;

public interface RoleService {

    RoleTO addRole(RoleTO roleTO);

    RoleTO updateRole(RoleTO roleTO);

    List<RoleTO> getRoleList(SearchCriteriaObj searchCriteriaObj);

    RoleTO getRoleById(Integer id);

    void deleteRole(List<Integer> id);

}
