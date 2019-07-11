package org.gooru.missioncontrol.bootstrap.component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.gooru.missioncontrol.bootstrap.shutdown.Finalizer;
import org.gooru.missioncontrol.bootstrap.startup.Initializer;
import org.gooru.missioncontrol.constants.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 19-Jun-2019
 */
public final class DataSourceRegistry implements Initializer, Finalizer {

  private static final String DEFAULT_DATA_SOURCE_TYPE = "nucleus.ds.type";
  private static final String DS_HIKARI = "hikari";
  private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceRegistry.class);
  private static final String DATA_SOURCE_CLASS_NAME = "dataSourceClassName";
  private static final String JDBC_URL = "jdbcUrl";
  private static final String USERNAME = "username";
  private static final String PASSWORD = "password";
  private static final String AUTO_COMMIT = "autoCommit";
  private static final String CONNECTION_TIMEOUT = "connectionTimeout";
  private static final String IDLE_TIMEOUT = "idleTimeout";
  private static final String DATASOURCE = "datasource";
  private static final String THREAD_FACTORY = "threadFactory";
  private static final String DATA_SOURCE = "dataSource";
  private static final String LEAK_DETECTION_THRESHOLD = "leakDetectionThreshold";
  private static final String VALIDATION_TIMEOUT = "validationTimeout";
  private static final String TRANSACTION_ISOLATION = "transactionIsolation";
  private static final String DRIVER_CLASS_NAME = "driverClassName";
  private static final String CONNECTION_INIT_SQL = "connectionInitSql";
  private static final String CATALOG = "catalog";
  private static final String REGISTER_MBEANS = "registerMBeans";
  private static final String READ_ONLY = "readOnly";
  private static final String ALLOW_POOL_SUSPENSION = "allowPoolSuspension";
  private static final String ISOLATION_INTERNAL_QUERIES = "isolationInternalQueries";
  private static final String POOL_NAME = "poolName";
  private static final String HEALTH_CHECK_REGISTRY = "healthCheckRegistry";
  private static final String METRIC_REGISTRY = "metricRegistry";
  private static final String MAXIMUM_POOL_SIZE = "maximumPoolSize";
  private static final String MINIMUM_IDLE = "minimumIdle";
  private static final String CONNECTION_TEST_QUERY = "connectionTestQuery";
  private static final String MAX_LIFETIME = "maxLifetime";
 
  // All the elements in this array are supposed to be present in config file
  // as keys as we are going to initialize them with the value associated with
  // that key
  private final List<String> datasources = Arrays.asList(AppConstants.DEFAULT_DATA_SOURCE, AppConstants.DATASCOPE_DATA_SOURCE);
  private final Map<String, DataSource> registry = new HashMap<>();
  private volatile boolean initialized;

  private DataSourceRegistry() {
    // TODO Auto-generated constructor stub
  }

  public static DataSourceRegistry getInstance() {
    return Holder.INSTANCE;
  }

  @Override
  public void initializeComponent(Vertx vertx, JsonObject config) {
    // Skip if we are already initialized
    LOGGER.debug("initializing data source registery");
    if (!initialized) {
      LOGGER.debug("data source registry has not already initialized");
      // We need to do initialization, however, we are running it via
      // verticle instance which is going to run in
      // multiple threads hence we need to be safe for this operation
      synchronized (Holder.INSTANCE) {
        LOGGER.debug("initializing data source registry after double checking");
        if (!initialized) {
          LOGGER.debug("initializing data source registry");
          for (String datasource : datasources) {
            JsonObject dbConfig = config.getJsonObject(datasource);
            if (dbConfig != null) {
              DataSource ds = initializeDataSource(dbConfig);
              registry.put(datasource, ds);
            }
          }
          initialized = true;
        }
      }
    }
  }

  public DataSource getDefaultDataSource() {
    return registry.get(AppConstants.DEFAULT_DATA_SOURCE);
  }

  public DataSource getDataSourceByName(String name) {
    if (name != null) {
      return registry.get(name);
    }
    return null;
  }

  private DataSource initializeDataSource(JsonObject dbConfig) {
    // The default DS provider is hikari, so if set explicitly or not set
    // use it, else error out
    String dsType = dbConfig.getString(DEFAULT_DATA_SOURCE_TYPE);
    if (dsType != null && !dsType.equals(DS_HIKARI)) {
      // No support
      throw new IllegalStateException("Unsupported data store type");
    }
    final HikariConfig config = new HikariConfig();

    for (Map.Entry<String, Object> entry : dbConfig) {
      switch (entry.getKey()) {
        case DATA_SOURCE_CLASS_NAME:
          config.setDataSourceClassName((String) entry.getValue());
          break;
        case JDBC_URL:
          config.setJdbcUrl((String) entry.getValue());
          break;
        case USERNAME:
          config.setUsername((String) entry.getValue());
          break;
        case PASSWORD:
          config.setPassword((String) entry.getValue());
          break;
        case AUTO_COMMIT:
          config.setAutoCommit((Boolean) entry.getValue());
          break;
        case CONNECTION_TIMEOUT:
          config.setConnectionTimeout((Long) entry.getValue());
          break;
        case IDLE_TIMEOUT:
          config.setIdleTimeout((Long) entry.getValue());
          break;
        case MAX_LIFETIME:
          config.setMaxLifetime((Long) entry.getValue());
          break;
        case CONNECTION_TEST_QUERY:
          config.setConnectionTestQuery((String) entry.getValue());
          break;
        case MINIMUM_IDLE:
          config.setMinimumIdle((Integer) entry.getValue());
          break;
        case MAXIMUM_POOL_SIZE:
          config.setMaximumPoolSize((Integer) entry.getValue());
          break;
        case METRIC_REGISTRY:
          throw new UnsupportedOperationException(entry.getKey());
        case HEALTH_CHECK_REGISTRY:
          throw new UnsupportedOperationException(entry.getKey());
        case POOL_NAME:
          config.setPoolName((String) entry.getValue());
          break;
        case ISOLATION_INTERNAL_QUERIES:
          config.setIsolateInternalQueries((Boolean) entry.getValue());
          break;
        case ALLOW_POOL_SUSPENSION:
          config.setAllowPoolSuspension((Boolean) entry.getValue());
          break;
        case READ_ONLY:
          config.setReadOnly((Boolean) entry.getValue());
          break;
        case REGISTER_MBEANS:
          config.setRegisterMbeans((Boolean) entry.getValue());
          break;
        case CATALOG:
          config.setCatalog((String) entry.getValue());
          break;
        case CONNECTION_INIT_SQL:
          config.setConnectionInitSql((String) entry.getValue());
          break;
        case DRIVER_CLASS_NAME:
          config.setDriverClassName((String) entry.getValue());
          break;
        case TRANSACTION_ISOLATION:
          config.setTransactionIsolation((String) entry.getValue());
          break;
        case VALIDATION_TIMEOUT:
          config.setValidationTimeout((Long) entry.getValue());
          break;
        case LEAK_DETECTION_THRESHOLD:
          config.setLeakDetectionThreshold((Long) entry.getValue());
          break;
        case DATA_SOURCE:
          throw new UnsupportedOperationException(entry.getKey());
        case THREAD_FACTORY:
          throw new UnsupportedOperationException(entry.getKey());
        case DATASOURCE:
          for (Map.Entry<String, Object> key : ((JsonObject) entry.getValue())) {
            config.addDataSourceProperty(key.getKey(), key.getValue());
          }
          break;
      }
    }

    return new HikariDataSource(config);

  }

  @Override
  public void finalizeComponent() {
    for (String datasource : datasources) {
      DataSource ds = registry.get(datasource);
      if (ds != null) {
        if (ds instanceof HikariDataSource) {
          ((HikariDataSource) ds).close();
        }
      }
    }
  }

  private static final class Holder {
    private static final DataSourceRegistry INSTANCE = new DataSourceRegistry();
  }

}
