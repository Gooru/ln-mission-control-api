
package org.gooru.missioncontrol.processors.groups.create;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.gooru.missioncontrol.processors.groups.entities.Group;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 11-Sep-2019
 */
public class GroupMapperByReferenceId implements ResultSetMapper<Group> {
  @Override
  public Group map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    Group g = new Group();
    g.setId(r.getLong("id"));
    g.setReferenceId(r.getString("reference_id"));
    return g;
  }
}
