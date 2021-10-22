package com.bug.tracker.config.tenantConfig;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

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
    logger.info("Get connection for tenant {}", tenantIdentifier);
    final Connection connection = getAnyConnection();
    connection.setSchema(tenantIdentifier);
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
