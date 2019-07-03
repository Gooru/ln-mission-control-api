
package org.gooru.missioncontrol.processors.partners;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 02-Jul-2019
 */
public class UserCountModelMapper implements ResultSetMapper<UserCountModel> {

  @Override
  public UserCountModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    UserCountModel model = new UserCountModel();
    model.setCount(r.getLong("user_count"));
    model.setTenantId(UUID.fromString(r.getString("tenant_id")));
    return model;
  }

}
