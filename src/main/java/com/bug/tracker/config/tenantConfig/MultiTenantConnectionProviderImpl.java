package com.bug.tracker.config.tenantConfig;

import com.bug.tracker.config.ClientDBCache;
import com.bug.tracker.config.MultiLocationDBSource;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

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
      logger.info("**********Fetching default connection 1***********");
    } else if (tenantIdentifier != null) {
      if (ClientDBCache.driverManagerMap.isEmpty()) {
        new MultiLocationDBSource().databaseDetail();
      }
      if (ClientDBCache.driverManagerMap.get(tenantIdentifier) != null) {
        logger.info("**********Fetching Client DB connection***********");
        connection = ClientDBCache.driverManagerMap.get(tenantIdentifier).getConnection();
      } else {
        logger.info("**********Fetching default connection 2***********");
        connection = getAnyConnection();
      }
    } else {
      logger.info("**********Fetching default connection 3***********");
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
