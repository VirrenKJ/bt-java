package com.bug.tracker.company.service;

import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.company.dto.CompanyTO;

import java.util.List;

public interface CompanyService {

  CompanyTO add(CompanyTO companyTO);

  CompanyTO update(CompanyTO companyTO);

  List<CompanyTO> getList(SearchCriteriaObj searchCriteriaObj);

  CompanyTO getById(Integer id);

  void delete(List<Integer> id);

}
