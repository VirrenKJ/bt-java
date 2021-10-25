package com.bug.tracker.config.tenantConfig;

import com.bug.tracker.config.ClientDBCache;
import com.bug.tracker.config.MultiLocationDBSource;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

@Component
public class MultiTenantConnectionProviderImpl implements MultiTenantConnectionProvider {

  private static final long serialVersionUID = 4594035686849152415L;
  private static final Logger logger = LoggerFactory.getLogger(MultiTenantConnectionProviderImpl.class);
  private final String DEFAULT_TENANT = "default";
  private final DataSource datasource;

  public MultiTenantConnectionProviderImpl(DataSource dataSource) {
    this.datasource = dataSource;
  }

  @Override
  public Connection getAnyConnection() throws SQLException {
    return datasource.getConnection();
  }

  @Override
  public void releaseAnyConnection(Connection connection) throws SQLException {
    connection.close();
  }

  @Override
  public Connection getConnection(String tenantIdentifier) throws SQLException {
    final Connection connection;
    logger.info("*************************" + tenantIdentifier + "*********************");
    if (DEFAULT_TENANT.equals(tenantIdentifier)) {
      if (ClientDBCache.driverManagerMap.isEmpty()) {
        new MultiLocationDBSource().databaseDetail();
      }
      connection = getAnyConnection();
      logger.info("**********Fetching default connection***********");
    } else if (tenantIdentifier != null) {
      if (ClientDBCache.driverManagerMap.isEmpty()) {
        new MultiLocationDBSource().databaseDetail();
      }
      logger.info("**********Fetching Client DB connection***********");
      if (ClientDBCache.driverManagerMap.get(tenantIdentifier) != null) {
        connection = ClientDBCache.driverManagerMap.get(tenantIdentifier).getConnection();
      } else {
        connection = getAnyConnection();
      }
    } else {
      connection = getAnyConnection();
    }
    return connection;
  }

  @Override
  public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
    logger.info("Release connection for tenant {}", tenantIdentifier);
    connection.setSchema(DEFAULT_TENANT);
    releaseAnyConnection(connection);
  }

  @Override
  public boolean supportsAggressiveRelease() {
    return false;
  }

  @Override
  public boolean isUnwrappableAs(Class aClass) {
    return false;
  }

  @Override
  public <T> T unwrap(Class<T> aClass) {
    return null;
  }
}
