package com.bug.tracker.category.dao;

import com.bug.tracker.category.entity.GlobalCategoryBO;
import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.SearchCriteriaObj;

import java.util.List;

public interface GlobalCategoryDao {

    GlobalCategoryBO addGlobalCategory(GlobalCategoryBO globalCategoryBO);

    GlobalCategoryBO updateGlobalCategory(GlobalCategoryBO globalCategoryBO);

    CommonListTO<GlobalCategoryBO> getGlobalCategoryList(SearchCriteriaObj searchCriteriaObj);

    GlobalCategoryBO getGlobalCategoryById(Integer id);

    void deleteGlobalCategory(List<Integer> id);
}
