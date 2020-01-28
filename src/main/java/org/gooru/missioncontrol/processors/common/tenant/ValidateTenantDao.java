package org.gooru.missioncontrol.processors.common.tenant;

import java.util.Objects;
import java.util.UUID;
import org.gooru.missioncontrol.processors.exceptions.ActiveTenantNotFoundException;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


abstract class ValidateTenantDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(ValidateTenantDao.class);

  @SqlQuery("select count(*) from tenant where id = :tenantId and status = 'active'")
  protected abstract int fetchActiveTenant(@Bind("tenantId") UUID tenantId);

  public void validate(UUID tenantId) {
    Objects.requireNonNull(tenantId);

    int count = this.fetchActiveTenant(tenantId);
    if (count != 1) {
      LOGGER.warn("Found '{}' record(s) for the tenant '{}'", count, tenantId);
      throw new ActiveTenantNotFoundException();
    }
  }

}
