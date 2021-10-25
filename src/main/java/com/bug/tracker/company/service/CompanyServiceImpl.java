package com.bug.tracker.company.service;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.common.service.ModelConvertorService;
import com.bug.tracker.company.dao.CompanyDao;
import com.bug.tracker.company.dto.CompanyDbDetailTO;
import com.bug.tracker.company.dto.CompanyTO;
import com.bug.tracker.company.entity.CompanyBO;
import com.bug.tracker.config.ClientDBCache;
import com.bug.tracker.config.MultiLocationDBSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
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
//    companyDetail(companyTO);
    createCompanyDb(companyTO.getDbName());
    CompanyBO companyBO = modelConvertorService.map(companyTO, CompanyBO.class);
    CompanyTO companyTO_return = modelConvertorService.map(companyDao.add(companyBO), CompanyTO.class);
    ClientDBCache.getAllKey().put(companyTO_return.getDbUuid(), companyTO_return.getDbName());
    new MultiLocationDBSource().updateDriverManagerForNewCompany(companyTO_return.getDbUuid());
    return companyTO_return;
  }

//  public void companyDetail(CompanyTO companyTO) {
//    companyTO.setCompanyDbDetail(new CompanyDbDetailTO());
//    companyTO.getCompanyDbDetail().setDbUrl("jdbc:mysql://localhost:3306/" + companyTO.getDbName());
//    Properties props;
//    try {
//      props = PropertiesLoaderUtils.loadProperties(new ClassPathResource("/application.properties"));
//      companyTO.getCompanyDbDetail().setDbUsername(props.getProperty("spring.datasource.username"));
//      companyTO.getCompanyDbDetail().setDbPassword(props.getProperty("spring.datasource.password"));
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }

  private void createCompanyDb(String dbName) {
    JdbcTemplate jdbcTemplate;
    Properties props;
    try {
      props = PropertiesLoaderUtils.loadProperties(new ClassPathResource("/application.properties"));
      jdbcTemplate = new JdbcTemplate(getDataSource(props.getProperty("spring.datasource.username"),
              props.getProperty("spring.datasource.password")));
      jdbcTemplate.execute("CREATE DATABASE IF NOT EXISTS " + dbName);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private DriverManagerDataSource getDataSource(String userName, String password) {
    DriverManagerDataSource dataSource = new DriverManagerDataSource("jdbc:mysql://localhost:3306", userName, password);
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    return dataSource;
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
