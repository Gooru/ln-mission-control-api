
package org.gooru.missioncontrol.processors.groups.create;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gooru.missioncontrol.bootstrap.component.jdbi.DBICreator;
import org.gooru.missioncontrol.constants.MessageConstants;
import org.gooru.missioncontrol.processors.groups.entities.Group;
import org.gooru.missioncontrol.processors.groups.entities.GroupDataEntity;
import org.gooru.missioncontrol.processors.groups.entities.School;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 01-Sep-2019
 */
public class GroupsCreationProcessor {

  private final static Logger LOGGER = LoggerFactory.getLogger(GroupsCreationProcessor.class);

  private final List<GroupDataEntity> groups;
  private final JsonObject session;

  private static final String GROUPS_CREATION_METHOD_API = "api";
  private static final String GROUPS_UNIQUE_COLUMN_CODE = "code";
  private static final String GROUPS_UNIQUE_COLUMN_REFERENCE_ID = "reference_id";
  private static final List<String> VALID_UNIQUE_COLUMNS =
      Arrays.asList(GROUPS_UNIQUE_COLUMN_CODE, GROUPS_UNIQUE_COLUMN_REFERENCE_ID);

  private final static GroupsService GROUP_SERVICE =
      new GroupsService(DBICreator.getDbiForDefaultDS());

  public GroupsCreationProcessor(List<GroupDataEntity> groups, JsonObject session) {
    this.groups = groups;
    this.session = session;
  }

  public void process() {

    // Extract tenant and user details from the session
    JsonObject tenant = this.session.getJsonObject(MessageConstants.MSG_KEY_TENANT);
    String tenantId = tenant.getString(MessageConstants.MSG_KEY_TENANT_ID);
    String tenantRoot = tenant.getString(MessageConstants.MSG_KEY_TENANT_ROOT);
    String userId = this.session.getString(MessageConstants.MSG_USER_ID);

    // Fetch group creation method of the tenant, if it is not API based groups creation then abort
    String groupsCreationMethod = GROUP_SERVICE.fetchGroupsCreationMethod(tenantId);
    if (groupsCreationMethod == null
        || !groupsCreationMethod.equalsIgnoreCase(GROUPS_CREATION_METHOD_API)) {
      LOGGER.warn("tenant '{}' does not allowed to create groups via API", tenantId);
      return;
    }

    // Fetch the unique column on which the groups creation should happen
    String groupsUniqueColumn = GROUP_SERVICE.fetchGroupsUniqueColumn(tenantId);
    if (groupsUniqueColumn == null || !VALID_UNIQUE_COLUMNS.contains(groupsUniqueColumn)) {
      LOGGER.warn(
          "invalid or null groups unique column specified in tenant settings for tenant '{}'",
          tenantId);
      return;
    }

    // Fetch tenant uniqueness settings, whether it is code or reference_id
    Map<String, Group> districts = new HashMap<>();
    Map<String, Map<String, Group>> blocksByDistrict = new HashMap<>();
    Map<String, Map<String, Group>> clustersByBlock = new HashMap<>();
    Map<String, Map<String, School>> schoolsByGroup = new HashMap<>();

    Long countryId = fetchCountryId();
    Long stateId = fetchStateId(countryId);

    // Read each record of the CSV and create district, block, cluster and school objects with the
    // hierarchy
    groups.forEach(grp -> {
      Group district = new Group();
      String districtCode = grp.getDistrictCode();
      if (groupsUniqueColumn.equalsIgnoreCase(GROUPS_UNIQUE_COLUMN_CODE)) {
        district.setCode(districtCode);
      } else if (groupsUniqueColumn.equalsIgnoreCase(GROUPS_UNIQUE_COLUMN_REFERENCE_ID)) {
        district.setReferenceId(districtCode);
      }
      district.setName(grp.getDistrict());
      district.setType("system");
      district.setSubType("district");
      district.setCountryId(countryId);
      district.setStateId(stateId);
      district.setTenant(tenantId);
      district.setTenantRoot(tenantRoot);
      district.setCreatorId(userId);
      district.setModifierId(userId);

      if (!districts.containsKey(districtCode)) {
        districts.put(districtCode, district);
      }

      Group block = new Group();
      String blockCode = grp.getBlockCode();
      if (groupsUniqueColumn.equalsIgnoreCase(GROUPS_UNIQUE_COLUMN_CODE)) {
        block.setCode(blockCode);
      } else if (groupsUniqueColumn.equalsIgnoreCase(GROUPS_UNIQUE_COLUMN_REFERENCE_ID)) {
        block.setReferenceId(blockCode);
      }

      block.setName(grp.getBlock());
      block.setType("system");
      block.setSubType("block");
      block.setCountryId(countryId);
      block.setStateId(stateId);
      block.setTenant(tenantId);
      block.setTenantRoot(tenantRoot);
      block.setCreatorId(userId);
      block.setModifierId(userId);

      if (blocksByDistrict.containsKey(districtCode)) {
        blocksByDistrict.get(districtCode).put(blockCode, block);
      } else {
        Map<String, Group> blocks = new HashMap<>();
        blocks.put(blockCode, block);
        blocksByDistrict.put(districtCode, blocks);
      }

      Group cluster = new Group();
      String clusterCode = grp.getClusterCode();
      if (groupsUniqueColumn.equalsIgnoreCase(GROUPS_UNIQUE_COLUMN_CODE)) {
        cluster.setCode(clusterCode);
      } else if (groupsUniqueColumn.equalsIgnoreCase(GROUPS_UNIQUE_COLUMN_REFERENCE_ID)) {
        cluster.setReferenceId(clusterCode);
      }

      cluster.setName(grp.getCluster());
      cluster.setType("system");
      cluster.setSubType("cluster");
      cluster.setCountryId(countryId);
      cluster.setStateId(stateId);
      cluster.setTenant(tenantId);
      cluster.setTenantRoot(tenantRoot);
      cluster.setCreatorId(userId);
      cluster.setModifierId(userId);

      if (clustersByBlock.containsKey(blockCode)) {
        clustersByBlock.get(blockCode).put(clusterCode, cluster);
      } else {
        Map<String, Group> clusters = new HashMap<>();
        clusters.put(clusterCode, cluster);
        clustersByBlock.put(blockCode, clusters);
      }

      School school = new School();
      String schoolCode = grp.getSchoolCode();
      if (groupsUniqueColumn.equalsIgnoreCase(GROUPS_UNIQUE_COLUMN_CODE)) {
        school.setCode(schoolCode);
      } else if (groupsUniqueColumn.equalsIgnoreCase(GROUPS_UNIQUE_COLUMN_REFERENCE_ID)) {
        school.setReferenceId(schoolCode);
      }

      school.setName(grp.getSchool());
      school.setTenant(tenantId);

      if (schoolsByGroup.containsKey(clusterCode)) {
        schoolsByGroup.get(clusterCode).put(schoolCode, school);
      } else {
        Map<String, School> schools = new HashMap<>();
        schools.put(schoolCode, school);
        schoolsByGroup.put(clusterCode, schools);
      }
    });

    Map<String, Group> existingDistricts = null;
    Map<String, Group> existingBlocks = null;
    Map<String, Group> existingClusters = null;
    Map<String, School> existingSchools = null;

    // Based on the unique column, fetch the existing records if there are any
    if (groupsUniqueColumn.equalsIgnoreCase(GROUPS_UNIQUE_COLUMN_CODE)) {
      existingDistricts = GROUP_SERVICE.fetchDistrictsByCodes(blocksByDistrict.keySet(), tenantId);
      existingBlocks = GROUP_SERVICE.fetchBlocksByCodes(clustersByBlock.keySet(), tenantId);
      existingClusters = GROUP_SERVICE.fetchClustersByCodes(schoolsByGroup.keySet(), tenantId);
    } else if (groupsUniqueColumn.equalsIgnoreCase(GROUPS_UNIQUE_COLUMN_REFERENCE_ID)) {
      existingDistricts =
          GROUP_SERVICE.fetchDistrictsByReferenceIds(blocksByDistrict.keySet(), tenantId);
      existingBlocks = GROUP_SERVICE.fetchBlocksByReferenceIds(clustersByBlock.keySet(), tenantId);
      existingClusters =
          GROUP_SERVICE.fetchClustersByReferenceIds(schoolsByGroup.keySet(), tenantId);
    }

    // Iterate on districts map and persist if its already not present. Set the id for further use
    Set<String> districtCodes = districts.keySet();
    for (String code : districtCodes) {
      Group district = districts.get(code);
      if (!existingDistricts.containsKey(code)) {
        Long groupId = GROUP_SERVICE.insertDistrict(district);
        district.setId(groupId);
        LOGGER.debug("{}", district.toString());
      } else {
        district.setId(existingDistricts.get(code).getId());
        LOGGER.debug(
            "district with code or reference id is already exists for tenant '{}', skipping", code,
            tenantId);
      }
    }

    // Iterate on the blocks map and persist if its already no present. Set the id for further use
    Map<String, Group> allBlocks = new HashMap<>();
    Set<String> districtCodeSet = blocksByDistrict.keySet();
    for (String code : districtCodeSet) {
      Map<String, Group> blocks = blocksByDistrict.get(code);
      allBlocks.putAll(blocks);
      Group district = districts.get(code);
      for (Map.Entry<String, Group> entry : blocks.entrySet()) {
        Group block = entry.getValue();
        if (!existingBlocks.containsKey(entry.getKey())) {
          block.setParentId(district.getId());
          Long id = GROUP_SERVICE.insertBlock(block);
          block.setId(id);
          LOGGER.debug("{}", block.toString());
        } else {
          block.setId(existingBlocks.get(entry.getKey()).getId());
          LOGGER.debug(
              "block with code or reference id is already exists for tenant '{}', skipping",
              entry.getKey(), tenantId);
        }
      }
    }

    // Iterate on the cluster map and persist if its already no present. Set the id for further use
    Map<String, Group> allClusters = new HashMap<>();
    Set<String> blockCodeSet = clustersByBlock.keySet();
    for (String code : blockCodeSet) {
      Map<String, Group> clusters = clustersByBlock.get(code);
      allClusters.putAll(clusters);
      Group block = allBlocks.get(code);
      for (Map.Entry<String, Group> entry : clusters.entrySet()) {

        Group cluster = entry.getValue();
        if (!existingClusters.containsKey(entry.getKey())) {
          cluster.setParentId(block.getId());
          Long id = GROUP_SERVICE.insertCluster(cluster);
          cluster.setId(id);
          LOGGER.debug("{}", cluster.toString());
        } else {
          cluster.setId(existingClusters.get(entry.getKey()).getId());
          LOGGER.debug(
              "cluster with code or reference id is already exists for tenant '{}', skipping",
              entry.getKey(), tenantId);
        }
      }
    }

    // Iterate on the schools map and persist if its already no present. Persist the mapping of the
    // school and group
    Set<String> clusterCodes = schoolsByGroup.keySet();
    for (String code : clusterCodes) {
      Group cluster = allClusters.get(code);
      Map<String, School> schools = schoolsByGroup.get(code);

      // Fetch existing school if there are any based on the tenant's unique column
      if (groupsUniqueColumn.equalsIgnoreCase(GROUPS_UNIQUE_COLUMN_CODE)) {
        existingSchools = GROUP_SERVICE.fetchSchoolsByCodes(schools.keySet(), tenantId);
      } else if (groupsUniqueColumn.equalsIgnoreCase(GROUPS_UNIQUE_COLUMN_REFERENCE_ID)) {
        existingSchools = GROUP_SERVICE.fetchSchoolsByReferenceIds(schools.keySet(), tenantId);
      }

      for (Map.Entry<String, School> entry : schools.entrySet()) {
        if (!existingSchools.containsKey(entry.getKey())) {
          School school = entry.getValue();
          Long id = GROUP_SERVICE.insertSchool(school);
          GROUP_SERVICE.insertGroupSchoolMapping(cluster.getId(), id);
        } else {
          LOGGER.debug(
              "school with code or reference id is already exists for tenant '{}', skipping",
              entry.getKey(), tenantId);
        }
      }
    }
  }

  private Long fetchCountryId() {
    GroupDataEntity groupData = groups.get(0);
    return GROUP_SERVICE.fetchCountryId(groupData.getCountry());
  }

  private Long fetchStateId(Long countryId) {
    GroupDataEntity groupData = groups.get(0);
    return GROUP_SERVICE.fetchStateId(groupData.getState(), countryId);
  }
}
