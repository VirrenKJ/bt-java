package com.bug.tracker.company.dao;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.company.entity.CompanyBO;
import com.bug.tracker.company.entity.CompanyCustomBO;

import java.util.List;

public interface CompanyDao {

  CompanyBO add(CompanyBO companyBO);

  CompanyBO update(CompanyBO companyBO);

  CompanyCustomBO copyCompanyToTenant(CompanyCustomBO companyBO);

  CommonListTO<CompanyBO> getList(PaginationCriteria paginationCriteria);

  CompanyBO getById(Integer id);

  List<?> getListByEmployeeId(Integer id);

  void delete(List<Integer> id);
}
