package com.bug.tracker.category.service;

import com.bug.tracker.category.dto.GlobalCategoryTO;
import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.common.object.SearchResponseTO;

import java.util.List;

public interface GlobalCategoryService {

    GlobalCategoryTO addGlobalCategory(GlobalCategoryTO globalCategoryTO);

    GlobalCategoryTO updateGlobalCategory(GlobalCategoryTO globalCategoryTO);

    SearchResponseTO getGlobalCategoryList(PaginationCriteria paginationCriteria);

    GlobalCategoryTO getGlobalCategoryById(Integer id);

    void deleteGlobalCategory(List<Integer> id);
}
