package org.gooru.missioncontrol.processors.auth.signinuser;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;


public class Session {
  @JsonProperty("user_id")
  private String userId;
  @JsonProperty("app_id")
  private String appId;
  @JsonProperty("partner_id")
  private String partnerId;
  @JsonProperty("username")
  private String username;
  @JsonProperty("provided_at")
  private Long providedAt;
  @JsonProperty("email")
  private String email;
  @JsonProperty("access_token_validity")
  private Long accessTokenValidity;
  @JsonProperty("access_token")
  private String accessToken;
  @JsonProperty("cdn_urls")
  private CdnUrls cdnUrls;
  @JsonProperty("tenant")
  private Tenant tenant;
  @JsonProperty("preference_settings")
  private JsonNode preferenceSettings;
  @JsonProperty("permissions")
  private ArrayNode permissions;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public String getPartnerId() {
    return partnerId;
  }

  public void setPartnerId(String partnerId) {
    this.partnerId = partnerId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Long getProvidedAt() {
    return providedAt;
  }

  public void setProvidedAt(Long providedAt) {
    this.providedAt = providedAt;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Long getAccessTokenValidity() {
    return accessTokenValidity;
  }

  public void setAccessTokenValidity(Long accessTokenValidity) {
    this.accessTokenValidity = accessTokenValidity;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public CdnUrls getCdnUrls() {
    return cdnUrls;
  }

  public void setCdnUrls(CdnUrls cdnUrls) {
    this.cdnUrls = cdnUrls;
  }

  public Tenant getTenant() {
    return tenant;
  }

  public void setTenant(Tenant tenant) {
    this.tenant = tenant;
  }

  public JsonNode getPreferenceSettings() {
    return preferenceSettings;
  }

  public void setPreferenceSettings(JsonNode preferenceSettings) {
    this.preferenceSettings = preferenceSettings;
  }

  public ArrayNode getPermissions() {
    return permissions;
  }

  public void setPermissions(ArrayNode permissions) {
    this.permissions = permissions;
  }


  public static class CdnUrls {
    @JsonProperty("user_cdn_url")
    private String userCdnUrl;
    @JsonProperty("content_cdn_url")
    private String contentCdnUrl;

    public String getUserCdnUrl() {
      return userCdnUrl;
    }

    public void setUserCdnUrl(String userCdnUrl) {
      this.userCdnUrl = userCdnUrl;
    }

    public String getContentCdnUrl() {
      return contentCdnUrl;
    }

    public void setContentCdnUrl(String contentCdnUrl) {
      this.contentCdnUrl = contentCdnUrl;
    }
  }

  public static class Tenant {
    @JsonProperty("tenant_id")
    private String tenantId;
    @JsonProperty("tenant_root")
    private String tenantRoot;
    @JsonProperty("settings")
    private JsonNode settings;

    public String getTenantId() {
      return tenantId;
    }

    public void setTenantId(String tenantId) {
      this.tenantId = tenantId;
    }

    public String getTenantRoot() {
      return tenantRoot;
    }

    public void setTenantRoot(String tenantRoot) {
      this.tenantRoot = tenantRoot;
    }

    public JsonNode getSettings() {
      return settings;
    }

    public void setSettings(JsonNode settings) {
      this.settings = settings;
    }
  }
}
