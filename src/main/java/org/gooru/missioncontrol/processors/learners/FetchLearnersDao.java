package org.gooru.missioncontrol.processors.learners;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;


interface FetchLearnersDao {

  @Mapper(FetchLearnersModelMapper.class)
  @SqlQuery("SELECT distinct u.* from users u left join class_member cm on cm.user_id = u.id inner join class c on c.id = cm.class_id where "
      + "((:query is null or username ilike CONCAT('%', :query, '%')) OR "
      + "(:query is null or u.email ilike CONCAT('%', :query, '%')) OR (:query is null or first_name ilike CONCAT('%', :query, '%')) OR "
      + "(:query is null or last_name ilike CONCAT('%', :query, '%')) "
      + ") AND (:tenantId is null or u.tenant_id = :tenantId) AND user_category = 'student' AND u.is_deleted = false AND  "
      + "(:classIds::uuid[] is null or c.id = ANY(:classIds::uuid[])) AND "
      + "(:schoolIds::bigint[] is null or c.school_id = ANY(:schoolIds::bigint[])) order by first_name, "
      + "last_name  offset :offset limit :limit")
  List<LearnersModel> fetchLearners(@BindBean LearnerListCommand.LearnerListCommandBean command);
}
