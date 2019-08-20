
package org.gooru.missioncontrol.processors.partners;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;


public class DistributionBySubjectCategoryModelMapper implements ResultSetMapper<DistributionBySubjectCategoryModel> {

  @Override
  public DistributionBySubjectCategoryModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    DistributionBySubjectCategoryModel model = new DistributionBySubjectCategoryModel();
    model.setTotalCount(r.getLong("total_count"));
    model.setCategoryCode(r.getString("tx_sub_category_code"));
    return model;
  }

}
