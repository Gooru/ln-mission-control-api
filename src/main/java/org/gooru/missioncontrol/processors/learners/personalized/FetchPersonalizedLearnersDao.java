package org.gooru.missioncontrol.processors.learners.personalized;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;


interface FetchPersonalizedLearnersDao {

  @Mapper(FetchPersonalizedLearnersModelMapper.class)
  @SqlQuery("SELECT id, username, reference_id, email, first_name, last_name, user_category, thumbnail, gender, about, school_id, school, "
      + "school_district_id, school_district, country_id, country, state_id, state, display_name, roster_global_userid, roster_id,grade_name,gut_grade_codes"
      + " from demo_users where grade_name is not null and gut_grade_codes is not null order by user_seq")
  List<PersonalizedLearnersModel> fetchPersonalizedLearners();
}
