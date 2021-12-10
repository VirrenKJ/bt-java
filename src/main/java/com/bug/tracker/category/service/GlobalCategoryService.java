package com.bug.tracker.category.service;

import com.bug.tracker.category.dto.GlobalCategoryTO;
import com.bug.tracker.common.object.SearchCriteriaObj;

import java.util.List;

public interface GlobalCategoryService {

    GlobalCategoryTO addGlobalCategory(GlobalCategoryTO globalCategoryTO);

    GlobalCategoryTO updateGlobalCategory(GlobalCategoryTO globalCategoryTO);

    List<GlobalCategoryTO> getGlobalCategoryList(SearchCriteriaObj searchCriteriaObj);

    GlobalCategoryTO getGlobalCategoryById(Integer id);

    void deleteGlobalCategory(List<Integer> id);
}
