package org.gooru.missioncontrol.bootstrap.component.jdbi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author ashish on 1/10/18.
 */
public class UUIDMapper implements ResultSetMapper<UUID> {

  @Override
  public UUID map(final int index, final ResultSet resultSet,
      final StatementContext statementContext) throws SQLException {
    return UUID.fromString(resultSet.getString(1));
  }

}
