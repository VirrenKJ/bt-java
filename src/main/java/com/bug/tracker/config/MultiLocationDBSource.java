package com.bug.tracker.config;

import com.mysql.cj.util.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Component
public class MultiLocationDBSource {

  private DriverManagerDataSource getDataSource(String url, String userName, String password) {
    DriverManagerDataSource dataSource = new DriverManagerDataSource(url, userName, password);
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    return dataSource;
  }

  public void databaseDetail() {
    JdbcTemplate jdbcDatabaseDetail;
    Properties props;
    try {
      props = PropertiesLoaderUtils.loadProperties(new ClassPathResource("/application.properties"));
      jdbcDatabaseDetail = new JdbcTemplate(getDataSource(props.getProperty("spring.datasource.url"),
              props.getProperty("spring.datasource.username"), props.getProperty("spring.datasource.password")));
      List<Map<String, Object>> rows = jdbcDatabaseDetail.queryForList(this.getDatabaseDetailQuery(true, null));
      for (Map<String, Object> row : rows) {
        ClientDBCache.driverManagerMap.put(row.get("uuid").toString(), getDataSource(row.get("url").toString(),
                row.get("userName").toString(), row.get("password").toString()));
      }
      this.setDbMap(jdbcDatabaseDetail.queryForList(this.getDatabaseMappingQuery()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String getDatabaseDetailQuery(boolean isAll, String dbUUID) {
    String dbQuery = "SELECT cdb.db_url as url, cdb.db_username as userName, cdb.db_password as password, comp.db_uuid as uuid FROM company_db_detail cdb INNER join company comp on cdb.company_id = comp.id";
    if (!isAll && !StringUtils.isNullOrEmpty(dbUUID)) {
      dbQuery += " where comp.db_uuid='" + dbUUID + "'";
    }
    return dbQuery;
  }

  private String getDatabaseMappingQuery() {
    return "SELECT db_uuid, db_name FROM company";
  }

  private void setDbMap(List<Map<String, Object>> mapData) {
    if (ClientDBCache.getAllKey().size() == 1 && mapData != null && !mapData.isEmpty()) {
      for (Map<String, Object> map : mapData) {
        ClientDBCache.getAllKey().put(map.get("db_uuid").toString(), map.get("db_name").toString());
      }
    }
  }

  public void updateDriverManagerForNewCompany(String dbUUID) {
    Properties props;
    try {
      props = PropertiesLoaderUtils.loadProperties(new ClassPathResource("/application.properties"));
      JdbcTemplate jdbcDatabaseDetail = new JdbcTemplate(getDataSource(props.getProperty("spring.datasource.url"),
              props.getProperty("spring.datasource.username"), props.getProperty("spring.datasource.password")));
      List<Map<String, Object>> rows = jdbcDatabaseDetail
              .queryForList(this.getDatabaseDetailQuery(false, dbUUID));
      for (Map<String, Object> row : rows) {
        ClientDBCache.driverManagerMap.put(row.get("uuid").toString(), getDataSource(row.get("url").toString(),
                row.get("userName").toString(), row.get("password").toString()));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
