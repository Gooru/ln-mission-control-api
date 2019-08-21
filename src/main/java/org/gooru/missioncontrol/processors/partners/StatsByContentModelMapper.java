
package org.gooru.missioncontrol.processors.partners;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;


public class StatsByContentModelMapper implements ResultSetMapper<StatsByContentModel> {

  @Override
  public StatsByContentModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    StatsByContentModel model = new StatsByContentModel();
    model.setTotalCount(r.getLong("total_count"));
    model.setCountryId(r.getLong("country_id"));
    model.setContentType(r.getString("content_type"));
    return model;
  }

}
