
package org.gooru.missioncontrol.processors.groups.create;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gooru.missioncontrol.processors.groups.entities.Group;
import org.gooru.missioncontrol.processors.groups.entities.School;
import org.gooru.missioncontrol.processors.utils.HelperUtility;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 01-Sep-2019
 */
public class GroupsService {

  private GroupsDao dao;

  public GroupsService(DBI dbi) {
    this.dao = dbi.onDemand(GroupsDao.class);
  }

  public Long insertDistrict(Group group) {
    return this.dao.insertDistrict(group);
  }

  public Long insertBlock(Group group) {
    return this.dao.insertBlock(group);
  }

  public Long insertCluster(Group group) {
    return this.dao.insertCluster(group);
  }

  public Long insertSchool(School school) {
    return this.dao.insertSchool(school);
  }

  public void insertGroupSchoolMapping(Long groupId, Long schoolId) {
    this.dao.insertGroupSchoolMapping(groupId, schoolId);
  }

  public Long fetchCountryId(String code) {
    return this.dao.fetchCountryId(code);
  }

  public Long fetchStateId(String code, Long countryId) {
    return this.dao.fetchStateId(code, countryId);
  }

  public String fetchGroupsCreationMethod(String tenantId) {
    return this.dao.fetchGroupsCreationMethod(tenantId);
  }

  public String fetchGroupsUniqueColumn(String tenantId) {
    return this.dao.fetchGroupsUniqueColumn(tenantId);
  }

  public Map<String, Group> fetchDistrictsByCodes(Set<String> codes, String tenantId) {
    List<Group> groups =
        this.dao.fetchDistrictsByCodes(HelperUtility.toPostgresArrayString(codes), tenantId);
    Map<String, Group> districts = new HashMap<>();
    groups.forEach(group -> {
      districts.put(group.getCode(), group);
    });
    return districts;
  }

  public Map<String, Group> fetchDistrictsByReferenceIds(Set<String> referenceIds,
      String tenantId) {
    List<Group> groups = this.dao
        .fetchDistrictsByReferenceIds(HelperUtility.toPostgresArrayString(referenceIds), tenantId);
    Map<String, Group> districts = new HashMap<>();
    groups.forEach(group -> {
      districts.put(group.getReferenceId(), group);
    });
    return districts;
  }

  public Map<String, Group> fetchBlocksByCodes(Set<String> codes, String tenantId) {
    List<Group> groups =
        this.dao.fetchBlocksByCodes(HelperUtility.toPostgresArrayString(codes), tenantId);
    Map<String, Group> blocks = new HashMap<>();
    groups.forEach(group -> {
      blocks.put(group.getCode(), group);
    });
    return blocks;
  }

  public Map<String, Group> fetchBlocksByReferenceIds(Set<String> referenceIds, String tenantId) {
    List<Group> groups = this.dao
        .fetchBlocksByReferenceIds(HelperUtility.toPostgresArrayString(referenceIds), tenantId);
    Map<String, Group> blocks = new HashMap<>();
    groups.forEach(group -> {
      blocks.put(group.getReferenceId(), group);
    });
    return blocks;
  }

  public Map<String, Group> fetchClustersByCodes(Set<String> codes, String tenantId) {
    List<Group> groups =
        this.dao.fetchClustersByCodes(HelperUtility.toPostgresArrayString(codes), tenantId);
    Map<String, Group> clusters = new HashMap<>();
    groups.forEach(group -> {
      clusters.put(group.getCode(), group);
    });
    return clusters;
  }

  public Map<String, Group> fetchClustersByReferenceIds(Set<String> referenceIds, String tenantId) {
    List<Group> groups = this.dao
        .fetchClustersByReferenceIds(HelperUtility.toPostgresArrayString(referenceIds), tenantId);
    Map<String, Group> clusters = new HashMap<>();
    groups.forEach(group -> {
      clusters.put(group.getReferenceId(), group);
    });
    return clusters;
  }

  public Map<String, School> fetchSchoolsByCodes(Set<String> codes, String tenantId) {
    List<School> schools =
        this.dao.fetchSchoolsByCodes(HelperUtility.toPostgresArrayString(codes), tenantId);
    Map<String, School> schoolMap = new HashMap<>();
    schools.forEach(school -> {
      schoolMap.put(school.getCode(), school);
    });
    return schoolMap;
  }

  public Map<String, School> fetchSchoolsByReferenceIds(Set<String> referenceIds, String tenantId) {
    List<School> schools = this.dao
        .fetchSchoolsByReferenceIds(HelperUtility.toPostgresArrayString(referenceIds), tenantId);
    Map<String, School> schoolMap = new HashMap<>();
    schools.forEach(school -> {
      schoolMap.put(school.getReferenceId(), school);
    });
    return schoolMap;
  }
}
