
package org.gooru.missioncontrol.processors.dbhelpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 09-Jul-2019
 */
public class CountryModelMapper implements ResultSetMapper<CountryModel> {

  @Override
  public CountryModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    CountryModel model = new CountryModel();
    model.setId(r.getLong("id"));
    model.setName(r.getString("name"));
    model.setCode(r.getString("code"));
    return model;
  }

}
