package com.bug.tracker.user.dao;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.user.entity.RoleBO;

import java.util.List;

public interface RoleDao {

    RoleBO add(RoleBO roleBO);

    RoleBO update(RoleBO roleBO);

    CommonListTO<RoleBO> getList(SearchCriteriaObj searchCriteriaObj);

    RoleBO getById(Integer id);

    void delete(List<Integer> id);

}
