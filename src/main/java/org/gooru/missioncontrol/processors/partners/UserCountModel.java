
package org.gooru.missioncontrol.processors.partners;

import java.util.UUID;

/**
 * @author szgooru Created On 02-Jul-2019
 */
public class UserCountModel {

  private Long count;
  private UUID tenantId;

  public Long getCount() {
    return count;
  }

  public void setCount(Long count) {
    this.count = count;
  }

  public UUID getTenantId() {
    return tenantId;
  }

  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }

}
