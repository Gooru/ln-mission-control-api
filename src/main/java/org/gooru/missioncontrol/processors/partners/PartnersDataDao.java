
package org.gooru.missioncontrol.processors.partners;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru Created On 09-Jul-2019
 */
public interface PartnersDataDao {

  @Mapper(StatsByTenantPartnerModelMapper.class)
  @SqlQuery("SELECT SUM(total_students) as total_students, SUM(total_teachers) as total_teachers, SUM(total_others) as total_others,"
      + " SUM(total_classes) as total_classes, SUM(total_competencies_gained) as total_competencies_gained, SUM(total_timespent) as total_timespent,"
      + " SUM(total_activities_conducted) as total_activities_conducted, SUM(total_navigator_courses) as total_navigator_courses, tenant, partner"
      + " FROM group_client_data_reports WHERE month = :month AND year = :year GROUP BY tenant, partner")
  public List<StatsByTenantPartnerModel> fetchStatsByTenantPartners(@Bind("month") Integer month,
      @Bind("year") Integer year);

  @Mapper(StatsByCountryModelMapper.class)
  @SqlQuery("SELECT SUM(total_students) as total_students, SUM(total_teachers) as total_teachers, SUM(total_others) as total_others,"
      + " SUM(total_classes) as total_classes, SUM(total_competencies_gained) as total_competencies_gained, SUM(total_timespent) as total_timespent,"
      + " SUM(total_activities_conducted) as total_activities_conducted, SUM(total_navigator_courses) as total_navigator_courses, country_id FROM"
      + " group_client_data_reports WHERE month = :month AND year = :year GROUP BY country_id;")
  public List<StatsByCountryModel> fetchStatsByCountry(@Bind("month") Integer month,
      @Bind("year") Integer year);
}
