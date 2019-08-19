
package org.gooru.missioncontrol.processors.partners;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;


public class StatsBySubjectModelMapper implements ResultSetMapper<StatsBySubjectModel> {

  @Override
  public StatsBySubjectModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    StatsBySubjectModel model = new StatsBySubjectModel();
    model.setTotalCount(r.getLong("total_count"));
    model.setSubjectCode(r.getString("tx_subject_code"));
    return model;
  }

}
