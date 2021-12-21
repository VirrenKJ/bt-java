package com.bug.tracker.user.service;

import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.user.dto.RoleTO;

import java.util.List;

public interface RoleService {

    RoleTO addRole(RoleTO roleTO);

    RoleTO updateRole(RoleTO roleTO);

    List<RoleTO> getRoleList(PaginationCriteria paginationCriteria);

    RoleTO getRoleById(Integer id);

    void deleteRole(List<Integer> id);

}
