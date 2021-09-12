package com.bug.tracker.category.service;

import com.bug.tracker.category.dto.GlobalCategoryTO;
import com.bug.tracker.common.object.SearchCriteriaObj;

import java.util.List;

public interface GlobalCategoryService {

    GlobalCategoryTO add(GlobalCategoryTO globalCategoryTO);

    GlobalCategoryTO update(GlobalCategoryTO globalCategoryTO);

    List<GlobalCategoryTO> getList(SearchCriteriaObj searchCriteriaObj);

    GlobalCategoryTO getById(Integer id);

    void delete(List<Integer> id);
}
