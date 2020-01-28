package org.gooru.missioncontrol.processors.common.tenant;

import java.util.UUID;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 9/3/18.
 */
public class TenantRootFinderService {
  private final DBI dbi;

  public TenantRootFinderService(DBI dbi) {
    this.dbi = dbi;
  }

  public UUID findTenantRoot(UUID tenantId) {
    TenantRootFinderDao dao = dbi.onDemand(TenantRootFinderDao.class);
    return dao.fetchTenantRoot(tenantId);
  }
}
