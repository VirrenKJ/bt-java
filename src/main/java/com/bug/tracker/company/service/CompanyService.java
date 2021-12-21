package com.bug.tracker.company.service;

import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.common.object.SearchResponseTO;
import com.bug.tracker.company.dto.CompanyTO;

import java.sql.SQLException;
import java.util.List;

public interface CompanyService {

  CompanyTO add(CompanyTO companyTO) throws SQLException;

  CompanyTO update(CompanyTO companyTO);

  CompanyTO copyCompanyToTenant(CompanyTO companyTO) throws SQLException;

  SearchResponseTO getList(PaginationCriteria paginationCriteria);

  List<CompanyTO> getListByEmployeeId(Integer id);

  CompanyTO getById(Integer id);

  void delete(List<Integer> id);

}
