package com.bug.tracker.user.service;

import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.user.dto.RoleTO;

import java.util.List;

public interface RoleService {

    RoleTO add(RoleTO roleTO);

    RoleTO update(RoleTO roleTO);

    List<RoleTO> getList(SearchCriteriaObj searchCriteriaObj);

    RoleTO getById(Integer id);

    void delete(List<Integer> id);

}
