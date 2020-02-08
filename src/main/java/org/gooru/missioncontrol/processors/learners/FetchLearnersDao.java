package org.gooru.missioncontrol.processors.learners;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;


interface FetchLearnersDao {

  @Mapper(FetchLearnersModelMapper.class)
  @SqlQuery("SELECT id, username, reference_id, email, first_name, last_name, user_category, thumbnail, gender, about, school_id, school, "
      + "school_district_id, school_district, country_id, country, state_id, state, display_name, "
      + "roster_global_userid, roster_id from users where ((:query is null or username = :query)) "
      + "AND (:tenantId is null or tenant_id = :tenantId)  offset :offset limit :limit")
  List<LearnersModel> fetchLearners(@BindBean UserListCommand.UserListCommandBean command);
}
