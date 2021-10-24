package com.bug.tracker.company.service;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.common.service.ModelConvertorService;
import com.bug.tracker.company.dao.CompanyDao;
import com.bug.tracker.company.dto.CompanyTO;
import com.bug.tracker.company.entity.CompanyBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

  @Autowired
  private CompanyDao companyDao;

  @Autowired
  private ModelConvertorService modelConvertorService;

  @Override
  public CompanyTO add(CompanyTO companyTO) {
    companyTO.setDbName(companyTO.getName().toLowerCase().replace(" ", "_"));
    companyTO.setDbUuid(UUID.randomUUID().toString());
    CompanyBO companyBO = modelConvertorService.map(companyTO, CompanyBO.class);
    return modelConvertorService.map(companyDao.add(companyBO), CompanyTO.class);
  }

  @Override
  public CompanyTO update(CompanyTO companyTO) {
    CompanyBO companyBO = modelConvertorService.map(companyTO, CompanyBO.class);
    return modelConvertorService.map(companyDao.update(companyBO), CompanyTO.class);
  }

  @Override
  public List<CompanyTO> getList(SearchCriteriaObj searchCriteriaObj) {
    CommonListTO<CompanyBO> commonListTO = companyDao.getList(searchCriteriaObj);
    List<CompanyBO> companyBOS = commonListTO.getDataList();
    List<CompanyTO> companyTOS = modelConvertorService.map(companyBOS, CompanyTO.class);
    return companyTOS;
  }

  @Override
  public CompanyTO getById(Integer id) {
    CompanyTO companyTO = modelConvertorService.map(companyDao.getById(id), CompanyTO.class);
    return companyTO;
  }

  @Override
  public void delete(List<Integer> id) {
    companyDao.delete(id);
  }
}
