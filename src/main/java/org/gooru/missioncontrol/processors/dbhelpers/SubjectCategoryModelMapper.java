
package org.gooru.missioncontrol.processors.dbhelpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;


public class SubjectCategoryModelMapper implements ResultSetMapper<SubjectCategoryModel> {

  @Override
  public SubjectCategoryModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    SubjectCategoryModel model = new SubjectCategoryModel();
    model.setId(r.getString("id"));
    model.setName(r.getString("name"));
    model.setCode(r.getString("code"));
    return model;
  }

}
