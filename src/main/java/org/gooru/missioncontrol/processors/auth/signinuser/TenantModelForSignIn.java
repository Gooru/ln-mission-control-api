package org.gooru.missioncontrol.processors.auth.signinuser;

import java.util.UUID;
import io.vertx.core.json.JsonObject;


public class TenantModelForSignIn {
  private UUID tenantId;
  private UUID parentTenantId;
  private JsonObject cdnUrls;
  private Long accessTokenValidity;

  public UUID getTenantId() {
    return tenantId;
  }

  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }

  public UUID getParentTenantId() {
    return parentTenantId;
  }

  public void setParentTenantId(UUID parentTenantId) {
    this.parentTenantId = parentTenantId;
  }

  public JsonObject getCdnUrls() {
    return cdnUrls;
  }

  public void setCdnUrls(JsonObject cdnUrls) {
    this.cdnUrls = cdnUrls;
  }

  public Long getAccessTokenValidity() {
    return accessTokenValidity;
  }

  public void setAccessTokenValidity(Long accessTokenValidity) {
    this.accessTokenValidity = accessTokenValidity;
  }

  @Override
  public String toString() {
    return "TenantModelForSignIn [tenantId=" + tenantId + ", parentTenantId=" + parentTenantId
        + ", cdnUrls=" + cdnUrls + ", accessTokenValidity=" + accessTokenValidity + "]";
  }

  
}
