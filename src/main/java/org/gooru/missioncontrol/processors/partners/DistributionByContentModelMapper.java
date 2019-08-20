
package org.gooru.missioncontrol.processors.partners;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;


public class DistributionByContentModelMapper
    implements ResultSetMapper<DistributionByContentModel> {

  @Override
  public DistributionByContentModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    DistributionByContentModel model = new DistributionByContentModel();
    model.setTotalCount(r.getLong("total_count"));
    model.setContentType(r.getString("content_type"));
    return model;
  }

}
