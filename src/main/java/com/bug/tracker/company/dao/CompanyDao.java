package com.bug.tracker.company.dao;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.company.entity.CompanyBO;
import com.bug.tracker.user.entity.UserDetailBO;

import java.util.List;

public interface CompanyDao {

  CompanyBO add(CompanyBO companyBO);

  CompanyBO update(CompanyBO companyBO);

  CompanyBO copyCompanyToTenant(CompanyBO companyBO);

  CommonListTO<CompanyBO> getList(SearchCriteriaObj searchCriteriaObj);

  CompanyBO getById(Integer id);

  List<?> getListByEmployeeId(Integer id);

  void delete(List<Integer> id);
}
