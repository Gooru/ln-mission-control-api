package org.gooru.missioncontrol.processors.auth.signinuser;

import java.util.UUID;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.vertx.core.json.JsonObject;


public final class TokenCreator {

  private final Session token;
  private TenantModelForSignIn tenantModel;
  private NucleusUserModelForSignIn userModel;
  private UUID tenantRoot;
  private JsonNode prefs;
  private ArrayNode permissions;
  private JsonNode tenantSettings;

  public TokenCreator(TenantModelForSignIn tenantModel, NucleusUserModelForSignIn userModel,
      UUID tenantRoot, JsonNode prefs, ArrayNode permissions, JsonNode tenantSettings) {
    this.token = new Session();
    this.tenantModel = tenantModel;
    this.userModel = userModel;
    this.tenantRoot = tenantRoot;
    this.prefs = prefs;
    this.permissions = permissions;
    this.tenantSettings = tenantSettings;
  }

  Session createToken() {
    return populateAccessToken();
  }

  private Session populateAccessToken() {
    token.setUserId(userModel.getUserId());
    token.setUsername(userModel.getUsername());
    token.setPartnerId(null);
    token.setEmail(userModel.getEmail());
    populateCdnUrls();
    token.setAccessTokenValidity(tenantModel.getAccessTokenValidity());
    long issueTime = System.currentTimeMillis();
    token.setProvidedAt(issueTime);
    populateTenant();
    token.setPreferenceSettings(prefs);
    token.setPermissions(permissions);
    token.setAccessToken(TokenGeneratorUtils.generateToken(token.getUserId(), token.getPartnerId(),
        token.getTenant().getTenantId(), issueTime));
    return token;
  }

  private void populateCdnUrls() {
    Session.CdnUrls cdnUrls = new Session.CdnUrls();
    final JsonObject cdnUrlsJsonObject = tenantModel.getCdnUrls();
    if (cdnUrlsJsonObject != null && !cdnUrlsJsonObject.isEmpty()) {
      cdnUrls.setContentCdnUrl(cdnUrlsJsonObject.getString("content_cdn_url"));
      cdnUrls.setUserCdnUrl(cdnUrlsJsonObject.getString("user_cdn_url"));
    }
    token.setCdnUrls(cdnUrls);
  }

  private void populateTenant() {
    Session.Tenant tenant = new Session.Tenant();
    tenant.setTenantId(tenantModel.getTenantId().toString());
    String tenantRootString = tenantRoot == null ? null : tenantRoot.toString();
    tenant.setTenantRoot(tenantRootString);
    tenant.setSettings(tenantSettings);
    token.setTenant(tenant);
  }

}
