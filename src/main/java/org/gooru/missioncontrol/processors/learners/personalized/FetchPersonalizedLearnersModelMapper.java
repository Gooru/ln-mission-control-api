package org.gooru.missioncontrol.processors.learners.personalized;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;


public class FetchPersonalizedLearnersModelMapper
    implements ResultSetMapper<PersonalizedLearnersModel> {


  @Override
  public PersonalizedLearnersModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    PersonalizedLearnersModel model = new PersonalizedLearnersModel();
    model.setUserId(r.getString(MapperFields.USER_ID));
    model.setUsername(r.getString(MapperFields.USERNAME));
    model.setFirstname(r.getString(MapperFields.FIRST_NAME));
    model.setLastname(r.getString(MapperFields.LAST_NAME));
    model.setReferenceId(r.getString(MapperFields.REFERENCE_ID));
    model.setAbout(r.getString(MapperFields.ABOUT));
    model.setCountry(r.getString(MapperFields.COUNTRY));
    model.setCountryId(r.getString(MapperFields.COUNTRY_ID));
    model.setDisplayname(r.getString(MapperFields.DISPLAY_NAME));
    model.setEmail(r.getString(MapperFields.EMAIL));
    model.setGender(r.getString(MapperFields.GENDER));
    model.setRosterGlobalUserId(r.getString(MapperFields.ROSTER_GLOBAL_USER_ID));
    model.setRosterId(r.getString(MapperFields.ROSTER_ID));
    model.setSchoolDistrict(r.getString(MapperFields.SCHOOL_DISTRICT));
    model.setSchoolDistrictId(r.getString(MapperFields.SCHOOL_DISTRICT_ID));
    model.setState(r.getString(MapperFields.STATE));
    model.setStateId(r.getString(MapperFields.STATE_ID));
    model.setThumbnail(r.getString(MapperFields.THUMBNAIL));
    model.setUserCategory(r.getString(MapperFields.USER_CATEGORY));
    model.setGradename(r.getString(MapperFields.GRADE_NAME));
    Array gutGradeCodes = r.getArray(MapperFields.GUT_GRADE_CODES);
    if (gutGradeCodes != null) {
      model.setGutGradeCodes(
          Arrays.stream((String[]) gutGradeCodes.getArray()).collect(Collectors.toList()));
    }
    return model;
  }



  static final class MapperFields {

    private MapperFields() {
      throw new AssertionError();
    }

    static final String USER_ID = "id";
    static final String USERNAME = "username";
    static final String REFERENCE_ID = "reference_id";
    static final String EMAIL = "email";
    static final String FIRST_NAME = "first_name";
    static final String LAST_NAME = "last_name";
    static final String USER_CATEGORY = "user_category";
    static final String THUMBNAIL = "thumbnail";
    static final String GENDER = "gender";
    static final String ABOUT = "about";
    static final String SCHOOL_ID = "school_id";
    static final String SCHOOL = "school";
    static final String SCHOOL_DISTRICT_ID = "school_district_id";
    static final String SCHOOL_DISTRICT = "school_district";
    static final String COUNTRY_ID = "country_id";
    static final String COUNTRY = "country";
    static final String STATE_ID = "state_id";
    static final String STATE = "state";
    static final String DISPLAY_NAME = "display_name";
    static final String ROSTER_GLOBAL_USER_ID = "roster_global_userid";
    static final String ROSTER_ID = "roster_id";
    static final String GRADE_NAME = "grade_name";
    static final String GUT_GRADE_CODES = "gut_grade_codes";
  }

}
