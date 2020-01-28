package org.gooru.missioncontrol.processors.auth.signinuser;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;


public class NucleusUserModelForSignInMapper implements ResultSetMapper<NucleusUserModelForSignIn> {
  @Override
  public NucleusUserModelForSignIn map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    NucleusUserModelForSignIn model = new NucleusUserModelForSignIn();
    model.setUserId(r.getString(MapperFields.ID));
    model.setUsername(r.getString(MapperFields.USERNAME));
    model.setEmail(r.getString(MapperFields.EMAIL));
    return model;
  }

  private static final class MapperFields {
    private MapperFields() {
      throw new AssertionError();
    }

    private static final String ID = "id";
    private static final String USERNAME = "username";
    private static final String EMAIL = "email";
  }

}
