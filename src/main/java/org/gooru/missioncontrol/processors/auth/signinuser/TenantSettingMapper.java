package org.gooru.missioncontrol.processors.auth.signinuser;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;


public class TenantSettingMapper implements ResultSetMapper<TenantSettingModel> {
  @Override
  public TenantSettingModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    TenantSettingModel model = new TenantSettingModel();
    model.setKey(r.getString(MapperFields.KEY));
    model.setValue(r.getString(MapperFields.VALUE));
    return model;
  }

  private static final class MapperFields {
    private MapperFields() {
      throw new AssertionError();
    }

    private static final String KEY = "key";
    private static final String VALUE = "value";
  }

}
