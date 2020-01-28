package org.gooru.missioncontrol.processors.common.tenant;

import java.util.Objects;
import java.util.UUID;
import org.gooru.missioncontrol.bootstrap.component.jdbi.UUIDMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;


abstract class TenantRootFinderDao {
  @Mapper(UUIDMapper.class)
  @SqlQuery("select parent_tenant from tenant where id = :tenantId")
  protected abstract UUID fetchParentTenant(@Bind("tenantId") UUID tenantId);

  public UUID fetchTenantRoot(UUID tenantId) {
    Objects.requireNonNull(tenantId);
    boolean reachedRoot = false;
    UUID parentTenant;
    UUID currentTenant = tenantId;
    while (!reachedRoot) {
      parentTenant = fetchParentTenant(currentTenant);
      if (parentTenant == null) {
        reachedRoot = true;
      }
      currentTenant = parentTenant;
    }
    return currentTenant;
  }

}
