package com.bug.tracker.user.service;

import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.user.dto.UserTO;

import java.util.List;

public interface UserService {

    UserTO add(UserTO userTO);

    UserTO update(UserTO userTO);

    List<UserTO> getList(SearchCriteriaObj searchCriteriaObj);

    UserTO getById(Integer id);

    void delete(List<Integer> id);

}
