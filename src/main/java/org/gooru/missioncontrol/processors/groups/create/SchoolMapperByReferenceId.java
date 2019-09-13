
package org.gooru.missioncontrol.processors.groups.create;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.gooru.missioncontrol.processors.groups.entities.School;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru
 * Created On 11-Sep-2019
 */
public class SchoolMapperByReferenceId implements ResultSetMapper<School> {

  @Override
  public School map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    School school = new School();
    school.setId(r.getLong("id"));
    school.setReferenceId(r.getString("reference_id"));
    return school;
  }

}
