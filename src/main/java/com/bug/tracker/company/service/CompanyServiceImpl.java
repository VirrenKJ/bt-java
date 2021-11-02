package com.bug.tracker.company.service;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.common.service.ModelConvertorService;
import com.bug.tracker.company.dao.CompanyDao;
import com.bug.tracker.company.dto.CompanyDbDetailTO;
import com.bug.tracker.company.dto.CompanyTO;
import com.bug.tracker.company.entity.CompanyBO;
import com.bug.tracker.company.entity.CompanyDetailsNewTenantBO;
import com.bug.tracker.config.ClientDBCache;
import com.bug.tracker.config.MultiLocationDBSource;
import com.bug.tracker.config.UserSessionContext;
import com.bug.tracker.config.tenantConfig.MultiTenantConnectionProviderImpl;
import com.bug.tracker.config.tenantConfig.TenantContext;
import com.bug.tracker.user.dao.UserDao;
import com.bug.tracker.user.dto.UserTO;
import com.bug.tracker.user.entity.RoleBO;
import com.bug.tracker.user.entity.UserBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

  @Autowired
  private CompanyDao companyDao;

  @Autowired
  private UserDao userDao;

  @Autowired
  private ModelConvertorService modelConvertorService;

  @Override
  public CompanyTO add(CompanyTO companyTO) throws SQLException {
    companyTO.setDbName(companyTO.getName().toLowerCase().replace(" ", "_"));
    companyTO.setDbUuid(UUID.randomUUID().toString());
    companyTO.setUserId(UserSessionContext.getCurrentTenant().getId());
    companyDbDetail(companyTO);
    createCompanyDb(companyTO.getDbName());
    runCompanyDbScript(companyTO.getDbName());
//    addUserAndCompanyData(companyTO);
    CompanyBO companyBO = modelConvertorService.map(companyTO, CompanyBO.class);
    CompanyTO companyTO_return = modelConvertorService.map(companyDao.add(companyBO), CompanyTO.class);
    return companyTO_return;
  }

  private void companyDbDetail(CompanyTO companyTO) {
    if (getProperties() != null) {
      companyTO.setCompanyDbDetail(new CompanyDbDetailTO());
      companyTO.getCompanyDbDetail().setDbUrl("jdbc:mysql://localhost:3306/" + companyTO.getDbName());
      companyTO.getCompanyDbDetail().setDbUsername(getProperties().getProperty("spring.datasource.username"));
      companyTO.getCompanyDbDetail().setDbPassword(getProperties().getProperty("spring.datasource.password"));
    }
  }

  private void createCompanyDb(String dbName) {
    if (getProperties() != null) {
      DriverManagerDataSource dataSource = new DriverManagerDataSource("jdbc:mysql://localhost:3306",
              getProperties().getProperty("spring.datasource.username"), getProperties().getProperty("spring.datasource.password"));
      dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
      JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
      jdbcTemplate.execute("DROP DATABASE IF EXISTS " + dbName);
      jdbcTemplate.execute("CREATE DATABASE " + dbName);
    }
  }

  private void runCompanyDbScript(String dbName) {
    if (getProperties() != null) {
      ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(
              false, false, "UTF-8", new ClassPathResource("company-setup-script.sql"));
      DriverManagerDataSource dataSource = new DriverManagerDataSource("jdbc:mysql://localhost:3306/" + dbName,
              getProperties().getProperty("spring.datasource.username"), getProperties().getProperty("spring.datasource.password"));
      dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
      resourceDatabasePopulator.execute(dataSource);
    }
  }

  private void addUserAndCompanyData(CompanyTO companyTO) throws SQLException {
    getDataSource(companyTO.getCompanyDbDetail().getDbUrl(),
            companyTO.getCompanyDbDetail().getDbUsername(), companyTO.getCompanyDbDetail().getDbPassword()).getConnection();
    UserBO userBO = UserSessionContext.getCurrentTenant();
    List<RoleBO> roles = new ArrayList<>();
    RoleBO roleBO = new RoleBO();
    roleBO.setRoleId(userBO.getRoles().get(0).getRoleId());
    roles.add(roleBO);
    userBO.setRoles(roles);
    CompanyDetailsNewTenantBO companyDetailsNewTenantBO = modelConvertorService.map(companyTO, CompanyDetailsNewTenantBO.class);
    modelConvertorService.map(userDao.update(userBO), UserTO.class);
    modelConvertorService.map(companyDao.add(companyDetailsNewTenantBO), CompanyTO.class);
  }

  private Properties getProperties() {
    Properties props = null;
    try {
      props = PropertiesLoaderUtils.loadProperties(new ClassPathResource("/application.properties"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return props;
  }

  private DriverManagerDataSource getDataSource(String url, String userName, String password) {
    DriverManagerDataSource dataSource = new DriverManagerDataSource(url, userName, password);
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
    return modelConvertorService.map(companyBOS, CompanyTO.class);
  }

  @Override
  public List<CompanyTO> getBusinessList(SearchCriteriaObj searchCriteriaObj) {
    CommonListTO<CompanyBO> commonListTO = companyDao.getBusinessList(searchCriteriaObj);
    List<CompanyBO> companyBOS = commonListTO.getDataList();
    return modelConvertorService.map(companyBOS, CompanyTO.class);
  }

  @Override
  public CompanyTO getById(Integer id) {
    return modelConvertorService.map(companyDao.getById(id), CompanyTO.class);
  }

  @Override
  public void delete(List<Integer> id) {
    companyDao.delete(id);
  }
}
