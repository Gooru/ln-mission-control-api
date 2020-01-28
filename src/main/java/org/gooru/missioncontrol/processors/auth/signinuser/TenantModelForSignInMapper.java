package org.gooru.missioncontrol.processors.auth.signinuser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import io.vertx.core.json.JsonObject;


public class TenantModelForSignInMapper implements ResultSetMapper<TenantModelForSignIn> {

  @Override
  public TenantModelForSignIn map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    TenantModelForSignIn model = new TenantModelForSignIn();
    model.setTenantId(UUID.fromString(r.getString(MapperFields.TENANT_ID)));
    setParentTenant(r, model);
    setCdnUrls(r, model);
    model.setAccessTokenValidity(r.getLong(MapperFields.ACCESS_TOKEN_VALIDITY));
    return model;
  }

  private void setCdnUrls(ResultSet r, TenantModelForSignIn model) throws SQLException {
    String cdnUrlString = r.getString(MapperFields.CDN_URLS);
    if (cdnUrlString == null || cdnUrlString.isEmpty()) {
      model.setCdnUrls(new JsonObject());
    } else {
      model.setCdnUrls(new JsonObject(cdnUrlString));
    }
  }

  private void setParentTenant(ResultSet r, TenantModelForSignIn model) throws SQLException {
    String parentTenant = r.getString(MapperFields.PARENT_TENANT);
    if (parentTenant == null) {
      model.setParentTenantId(null);
    } else {
      model.setParentTenantId(UUID.fromString(parentTenant));
    }
  }

  private static final class MapperFields {
    private MapperFields() {
      throw new AssertionError();
    }

    private static final String TENANT_ID = "id";
    private static final String PARENT_TENANT = "parent_tenant";
    private static final String CDN_URLS = "cdn_urls";
    private static final String ACCESS_TOKEN_VALIDITY = "access_token_validity";
  }

}
