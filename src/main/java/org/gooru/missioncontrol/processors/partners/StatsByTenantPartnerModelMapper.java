
package org.gooru.missioncontrol.processors.partners;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 08-Jul-2019
 */
public class StatsByTenantPartnerModelMapper implements ResultSetMapper<StatsByTenantPartnerModel> {

  @Override
  public StatsByTenantPartnerModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    StatsByTenantPartnerModel model = new StatsByTenantPartnerModel();
    model.setTotalStudents(r.getLong("total_students"));
    model.setTotalTeachers(r.getLong("total_teachers"));
    model.setTotalOthers(r.getLong("total_others"));
    model.setTotalClasses(r.getLong("total_classes"));
    model.setTotalCompetenciesGained(r.getLong("total_competencies_gained"));
    model.setTotalTimespent(r.getLong("total_timespent"));
    model.setTotalActivitiesConducted(r.getLong("total_activities_conducted"));
    model.setTotalNavgiatorCourses(r.getLong("total_navigator_courses"));
    model.setTenant(r.getString("tenant"));
    model.setPartner(r.getString("partner"));
    return model;
  }

}
