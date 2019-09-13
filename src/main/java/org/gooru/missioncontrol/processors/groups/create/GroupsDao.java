
package org.gooru.missioncontrol.processors.groups.create;

import java.util.List;
import org.gooru.missioncontrol.processors.groups.entities.Group;
import org.gooru.missioncontrol.processors.groups.entities.School;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru Created On 01-Sep-2019
 */
public interface GroupsDao {

  @SqlUpdate("INSERT INTO groups(name, code, type, sub_type, state_id, country_id, tenant, tenant_root, creator_id, modifier_id, reference_id, parent_id)"
      + " VALUES (:name, :code, 'system', 'district', :stateId, :countryId, :tenant, :tenantRoot, :creatorId, :modifierId, :referenceId, :parentId)")
  @GetGeneratedKeys
  public Long insertDistrict(@BindBean Group group);
  
  @SqlUpdate("INSERT INTO groups(name, code, type, sub_type, state_id, country_id, tenant, tenant_root, creator_id, modifier_id, reference_id, parent_id)"
      + " VALUES (:name, :code, 'system', 'block', :stateId, :countryId, :tenant, :tenantRoot, :creatorId, :modifierId, :referenceId, :parentId)")
  @GetGeneratedKeys
  public Long insertBlock(@BindBean Group group);
  
  @SqlUpdate("INSERT INTO groups(name, code, type, sub_type, state_id, country_id, tenant, tenant_root, creator_id, modifier_id, reference_id, parent_id)"
      + " VALUES (:name, :code, 'system', 'cluster', :stateId, :countryId, :tenant, :tenantRoot, :creatorId, :modifierId, :referenceId, :parentId)")
  @GetGeneratedKeys
  public Long insertCluster(@BindBean Group group);
  
  @SqlUpdate("INSERT INTO school_ds(name, code, tenant, reference_id) VALUES(:name, :code, :tenant, :referenceId)")
  @GetGeneratedKeys
  public Long insertSchool(@BindBean School school);
  
  @SqlUpdate("INSERT INTO group_school_mapping(group_id, school_id) VALUES (:groupId, :schoolId)")
  public void insertGroupSchoolMapping(@Bind("groupId") Long groupId, @Bind("schoolId") Long schoolId);
  
  @SqlQuery("SELECT id FROM country_ds WHERE code = :code")
  public Long fetchCountryId(@Bind("code") String code);
  
  @SqlQuery("SELECT id FROM state_ds WHERE code = :code AND country_id = :countryId")
  public Long fetchStateId(@Bind("code") String code, @Bind("countryId") Long countryId);
  
  @SqlQuery("SELECT value FROM tenant_setting WHERE id = :tenantId::uuid AND key = 'groups_creation_method'")
  public String fetchGroupsCreationMethod(@Bind("tenantId") String tenantId);
  
  @SqlQuery("SELECT value FROM tenant_setting WHERE id = :tenantId::uuid AND key = 'groups_unique_column'")
  public String fetchGroupsUniqueColumn(@Bind("tenantId") String tenantId);
  
  @Mapper(GroupMapperByCode.class)
  @SqlQuery("SELECT id, code FROM groups WHERE code = ANY(:codes::text[]) AND tenant = :tenantId AND sub_type = 'district'")
  public List<Group> fetchDistrictsByCodes(@Bind("codes") String codes, @Bind("tenantId") String tenantId);
  
  @Mapper(GroupMapperByReferenceId.class)
  @SqlQuery("SELECT id, reference_id FROM groups WHERE reference_id = ANY(:referenceIds::text[]) AND tenant = :tenantId AND sub_type = 'district'")
  public List<Group> fetchDistrictsByReferenceIds(@Bind("referenceIds") String referenceIds, @Bind("tenantId") String tenantId);
  
  @Mapper(GroupMapperByCode.class)
  @SqlQuery("SELECT id, code FROM groups WHERE code = ANY(:codes::text[]) AND tenant = :tenantId AND sub_type = 'block'")
  public List<Group> fetchBlocksByCodes(@Bind("codes") String codes, @Bind("tenantId") String tenantId);
  
  @Mapper(GroupMapperByReferenceId.class)
  @SqlQuery("SELECT id, reference_id FROM groups WHERE reference_id = ANY(:referenceIds::text[]) AND tenant = :tenantId AND sub_type = 'block'")
  public List<Group> fetchBlocksByReferenceIds(@Bind("referenceIds") String referenceIds, @Bind("tenantId") String tenantId);
  
  @Mapper(GroupMapperByCode.class)
  @SqlQuery("SELECT id, code FROM groups WHERE code = ANY(:codes::text[]) AND tenant = :tenantId AND sub_type = 'cluster'")
  public List<Group> fetchClustersByCodes(@Bind("codes") String codes, @Bind("tenantId") String tenantId);
  
  @Mapper(GroupMapperByReferenceId.class)
  @SqlQuery("SELECT id, reference_id FROM groups WHERE reference_id = ANY(:referenceIds::text[]) AND tenant = :tenantId AND sub_type = 'cluster'")
  public List<Group> fetchClustersByReferenceIds(@Bind("referenceIds") String referenceIds, @Bind("tenantId") String tenantId);
  
  @Mapper(SchoolMapperByCode.class)
  @SqlQuery("SELECT id, code FROM school_ds WHERE code = ANY(:codes::text[]) AND tenant = :tenantId")
  public List<School> fetchSchoolsByCodes(@Bind("codes") String codes, @Bind("tenantId") String tenantId);
  
  @Mapper(SchoolMapperByReferenceId.class)
  @SqlQuery("SELECT id, reference_id FROM school_ds WHERE reference_id = ANY(:referenceIds::text[]) AND tenant = :tenantId")
  public List<School> fetchSchoolsByReferenceIds(@Bind("referenceIds") String referenceIds, @Bind("tenantId") String tenantId);
}
 