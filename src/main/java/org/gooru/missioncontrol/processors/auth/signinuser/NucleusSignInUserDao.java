package org.gooru.missioncontrol.processors.auth.signinuser;

import java.util.List;
import java.util.UUID;
import org.gooru.missioncontrol.app.data.JsonNodeHolder;
import org.gooru.missioncontrol.bootstrap.component.jdbi.JsonNodeHolderMapper;
import org.gooru.missioncontrol.bootstrap.component.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;


interface NucleusSignInUserDao {

  @Mapper(NucleusUserModelForSignInMapper.class)
  @SqlQuery("select id, username, email from users where admin_user_id = :adminUserId and tenant_id = :tenantId "
      + "and is_deleted = false")
  NucleusUserModelForSignIn fetchActiveUser(@Bind("adminUserId") long userId,
      @Bind("tenantId") UUID tenantId);

  @Mapper(JsonNodeHolderMapper.class)
  @SqlQuery("select preference_settings from user_preference where user_id = :userId")
  JsonNodeHolder fetchUserPreferences(@Bind("userId") UUID userId);

  @Mapper(TenantModelForSignInMapper.class)
  @SqlQuery("select id, cdn_urls, parent_tenant, access_token_validity from tenant where id = :tenantId and "
      + "status = 'active'")
  TenantModelForSignIn fetchTenantForToken(@Bind("tenantId") UUID tenantId);

  @Mapper(JsonNodeHolderMapper.class)
  @SqlQuery("select value from default_lookup where key = 'DEFAULT_PREFERENCE'")
  JsonNodeHolder fetchDefaultUserPreferences();

  @SqlQuery("SELECT role_id FROM user_role_mapping WHERE user_id = :userId")
  List<Integer> fetchUserRole(@Bind("userId") UUID userId);

  @SqlQuery("SELECT distinct p.code  FROM role_permission_mapping rp INNER JOIN permission p ON p.id = rp.permission_id WHERE role_id = ANY(:roleIds::int[])")
  List<String> fetchPermissionsByRole(@Bind("roleIds") String roleIds);

  @Mapper(TenantSettingMapper.class)
  @SqlQuery("select key, value from tenant_setting where id = :tenantId and "
      + "key = ANY(:keys::text[])")
  List<TenantSettingModel> fetchSetting(@Bind("tenantId") UUID tenantId,
      @Bind("keys") PGArray<String> pgArray);


}
