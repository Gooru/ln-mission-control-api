package org.gooru.missioncontrol.bootstrap.component.jdbi;

import javax.sql.DataSource;
import org.gooru.missioncontrol.bootstrap.component.DataSourceRegistry;
import org.gooru.missioncontrol.constants.AppConstants;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 1/10/18.
 */
public final class DBICreator {

  private DBICreator() {
    throw new AssertionError();
  }

  public static DBI getDbiForDefaultDS() {
    return createDBI(DataSourceRegistry.getInstance().getDefaultDataSource());
  }

  public static DBI getDbiForDatascopeDB() {
    return createDBI(
        DataSourceRegistry.getInstance().getDataSourceByName(AppConstants.DATASCOPE_DATA_SOURCE));
  }
  
  public static DBI getDbiForAuthDB() {
    return createDBI(
        DataSourceRegistry.getInstance().getDataSourceByName(AppConstants.AUTH_DATA_SOURCE));
  }

  private static DBI createDBI(DataSource dataSource) {
    DBI dbi = new DBI(dataSource);
    dbi.registerArgumentFactory(new PostgresIntegerArrayArgumentFactory());
    dbi.registerArgumentFactory(new PostgresStringArrayArgumentFactory());
    dbi.registerArgumentFactory(new PostgresLongArrayArgumentFactory());
    dbi.registerArgumentFactory(new PostgresUUIDArrayArgumentFactory());
    return dbi;
  }

}
