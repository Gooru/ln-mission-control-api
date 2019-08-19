
package org.gooru.missioncontrol.processors.partners;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;


public class StatsBySubjectCategoryModelMapper implements ResultSetMapper<StatsBySubjectCategoryModel> {

  @Override
  public StatsBySubjectCategoryModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    StatsBySubjectCategoryModel model = new StatsBySubjectCategoryModel();
    model.setTotalCount(r.getLong("total_count"));
    model.setCategoryCode(r.getString("tx_sub_category_code"));
    return model;
  }

}
