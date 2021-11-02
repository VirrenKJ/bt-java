package com.bug.tracker.company.dao;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.company.entity.CompanyBO;
import com.bug.tracker.company.entity.CompanyDetailsNewTenantBO;

import java.util.List;

public interface CompanyDao {

  CompanyBO add(CompanyBO companyBO);

  CompanyDetailsNewTenantBO add(CompanyDetailsNewTenantBO companyDetailsNewTenantBO);

  CompanyBO update(CompanyBO companyBO);

  CommonListTO<CompanyBO> getList(SearchCriteriaObj searchCriteriaObj);

  CommonListTO<CompanyBO> getBusinessList(SearchCriteriaObj searchCriteriaObj);

  CompanyBO getById(Integer id);

  void delete(List<Integer> id);
}
