package org.gooru.missioncontrol.processors.auth.signinuser;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;


public class SignInUserModelMapper implements ResultSetMapper<SignInUserModel> {


  @Override
  public SignInUserModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    SignInUserModel model = new SignInUserModel();
    model.setId(r.getLong(MapperFields.USER_ID));
    model.setFirstName(r.getString(MapperFields.FIRST_NAME));
    model.setLastName(r.getString(MapperFields.LAST_NAME));
    model.setAbout(r.getString(MapperFields.ABOUT));
    model.setDisplayName(r.getString(MapperFields.DISPLAY_NAME));
    model.setEmail(r.getString(MapperFields.EMAIL));
    model.setDesignation(r.getString(MapperFields.DESIGNATION));
    model.setThumbnail(r.getString(MapperFields.THUMBNAIL));
    model.setTenantId(r.getString(MapperFields.TENANT_ID));
    model.setTenantRoot(r.getString(MapperFields.TENANT_ROOT));
    model.setPassword(r.getString(MapperFields.PASSWORD));
    return model;
  }



  static final class MapperFields {

    private MapperFields() {
      throw new AssertionError();
    }

    static final String USER_ID = "id";
    static final String USERNAME = "username";
    static final String EMAIL = "email";
    static final String FIRST_NAME = "first_name";
    static final String LAST_NAME = "last_name";
    static final String THUMBNAIL = "thumbnail";
    static final String ABOUT = "about";
    static final String DISPLAY_NAME = "display_name";
    static final String DESIGNATION = "designation";
    static final String TENANT_ID = "tenant_id";
    static final String TENANT_ROOT = "tenant_root";
    static final String PASSWORD = "password";
  }

}
