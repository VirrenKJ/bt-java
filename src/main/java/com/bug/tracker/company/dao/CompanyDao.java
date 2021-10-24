package com.bug.tracker.company.dao;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.company.entity.CompanyBO;

import java.util.List;

public interface CompanyDao {

  CompanyBO add(CompanyBO companyBO);

  CompanyBO update(CompanyBO companyBO);

  CommonListTO<CompanyBO> getList(SearchCriteriaObj searchCriteriaObj);

  CompanyBO getById(Integer id);

  void delete(List<Integer> id);
}
