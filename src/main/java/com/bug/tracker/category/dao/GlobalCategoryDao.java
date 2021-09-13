package com.bug.tracker.category.dao;

import com.bug.tracker.category.entity.GlobalCategoryBO;
import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.SearchCriteriaObj;

import java.util.List;

public interface GlobalCategoryDao {

    GlobalCategoryBO add(GlobalCategoryBO globalCategoryBO);

    GlobalCategoryBO update(GlobalCategoryBO globalCategoryBO);

    CommonListTO<GlobalCategoryBO> getList(SearchCriteriaObj searchCriteriaObj);

    GlobalCategoryBO getById(Integer id);

    void delete(List<Integer> id);
}
