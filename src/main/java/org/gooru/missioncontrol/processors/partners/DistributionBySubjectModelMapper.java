
package org.gooru.missioncontrol.processors.partners;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;


public class DistributionBySubjectModelMapper implements ResultSetMapper<DistributionBySubjectModel> {

  @Override
  public DistributionBySubjectModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    DistributionBySubjectModel model = new DistributionBySubjectModel();
    model.setTotalCount(r.getLong("total_count"));
    model.setSubjectCode(r.getString("tx_subject_code"));
    return model;
  }

}
