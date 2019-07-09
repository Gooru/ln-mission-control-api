
package org.gooru.missioncontrol.processors.partners;

/**
 * @author szgooru Created On 08-Jul-2019
 */
public class StatsByTenantPartnerModel {

  private Long totalStudents;
  private Long totalTeachers;
  private Long totalOthers;
  private Long totalClasses;
  private Long totalCompetenciesGained;
  private Long totalTimespent;
  private Long totalActivitiesConducted;
  private Long totalNavgiatorCourses;
  private String tenant;
  private String partner;

  public Long getTotalStudents() {
    return totalStudents;
  }

  public void setTotalStudents(Long totalStudents) {
    this.totalStudents = totalStudents;
  }

  public Long getTotalTeachers() {
    return totalTeachers;
  }

  public void setTotalTeachers(Long totalTeachers) {
    this.totalTeachers = totalTeachers;
  }

  public Long getTotalOthers() {
    return totalOthers;
  }

  public void setTotalOthers(Long totalOthers) {
    this.totalOthers = totalOthers;
  }

  public Long getTotalClasses() {
    return totalClasses;
  }

  public void setTotalClasses(Long totalClasses) {
    this.totalClasses = totalClasses;
  }

  public Long getTotalCompetenciesGained() {
    return totalCompetenciesGained;
  }

  public void setTotalCompetenciesGained(Long totalCompetenciesGained) {
    this.totalCompetenciesGained = totalCompetenciesGained;
  }

  public Long getTotalTimespent() {
    return totalTimespent;
  }

  public void setTotalTimespent(Long totalTimespent) {
    this.totalTimespent = totalTimespent;
  }

  public Long getTotalActivitiesConducted() {
    return totalActivitiesConducted;
  }

  public void setTotalActivitiesConducted(Long totalActivitiesConducted) {
    this.totalActivitiesConducted = totalActivitiesConducted;
  }

  public Long getTotalNavgiatorCourses() {
    return totalNavgiatorCourses;
  }

  public void setTotalNavgiatorCourses(Long totalNavgiatorCourses) {
    this.totalNavgiatorCourses = totalNavgiatorCourses;
  }

  public String getTenant() {
    return tenant;
  }

  public void setTenant(String tenant) {
    this.tenant = tenant;
  }

  public String getPartner() {
    return partner;
  }

  public void setPartner(String partner) {
    this.partner = partner;
  }

}
