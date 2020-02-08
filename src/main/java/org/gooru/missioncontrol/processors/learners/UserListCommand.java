package org.gooru.missioncontrol.processors.learners;

import java.util.UUID;
import org.gooru.missioncontrol.bootstrap.component.AppConfiguration;
import org.gooru.missioncontrol.constants.HttpConstants;
import org.gooru.missioncontrol.processors.exceptions.HttpResponseWrapperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonObject;


class UserListCommand {
  private Integer offset;
  private Integer limit;
  private UUID tenantId;
  private String query;

  private static final Logger LOGGER = LoggerFactory.getLogger(UserListCommand.class);

  public Integer getOffset() {
    return offset;
  }

  public Integer getLimit() {
    return limit;
  }

  public UUID getTenantId() {
    return tenantId;
  }

  public String getQuery() {
    return query;
  }


  static UserListCommand builder(JsonObject requestBody, JsonObject session) {
    UserListCommand result = UserListCommand.buildFromJsonObject(requestBody, session);
    result.validate();
    return result;
  }

  public UserListCommandBean asBean() {
    UserListCommandBean bean = new UserListCommandBean();
    bean.limit = limit;
    bean.offset = offset;
    bean.tenantId = tenantId;
    bean.query = query;
    return bean;
  }

  private static UserListCommand buildFromJsonObject(JsonObject requestBody, JsonObject session) {
    UserListCommand result = new UserListCommand();
    Integer offset = getAsInt(requestBody, CommandAttributes.OFFSET);
    Integer limit = getAsInt(requestBody, CommandAttributes.LIMIT);
    populateTenantFromSession(session, result);
    populateOffsetAndLimit(offset, limit, result);
    if (requestBody.containsKey(CommandAttributes.QUERY)) {
      result.query = requestBody.getString(CommandAttributes.QUERY);
    }

    return result;
  }

  private static void populateTenantFromSession(JsonObject session, UserListCommand command) {
    JsonObject tenant = session.getJsonObject(CommandAttributes.TENANT);
    if (tenant != null) {
      command.tenantId = UUID.fromString(tenant.getString(CommandAttributes.TENANT_ID));
    }
  }

  private static void populateOffsetAndLimit(Integer offset, Integer limit,
      UserListCommand command) {
    if (offset == null) {
      command.offset = AppConfiguration.getInstance().getDefaultOffset();
    } else {
      if (offset >= 0) {
        command.offset = offset;
      } else {
        command.offset = null;
      }
    }
    if (limit == null) {
      command.limit = AppConfiguration.getInstance().getDefaultLimit();
    } else {
      Integer maxLimit = AppConfiguration.getInstance().getDefaultMaxLimit();
      if (limit > 0 && limit <= maxLimit) {
        command.limit = limit;
      } else if (limit > maxLimit) {
        command.limit = maxLimit;
      } else {
        command.limit = null;
      }
    }
  }

  private static Integer getAsInt(JsonObject requestBody, String key) {
    String value = requestBody.getString(key);
    Integer result = null;
    if (key != null) {
      try {
        result = Integer.valueOf(value);
      } catch (NumberFormatException e) {
        LOGGER.info("Invalid number format for {}", key);
        result = null;
      }
    }
    return result;
  }

  private void validate() {
    if (offset == null || limit == null) {
      LOGGER.info("Invalid offset/limit provided for request");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Invalid offset/limit");
    }

  }

  public static class UserListCommandBean {
    private Integer offset;
    private Integer limit;
    private UUID tenantId;
    private String query;

    public Integer getOffset() {
      return offset;
    }

    public void setOffset(Integer offset) {
      this.offset = offset;
    }

    public Integer getLimit() {
      return limit;
    }

    public void setLimit(Integer limit) {
      this.limit = limit;
    }

    public UUID getTenantId() {
      return tenantId;
    }

    public void setTenantId(UUID tenantId) {
      this.tenantId = tenantId;
    }

    public String getQuery() {
      return query;
    }

    public void setQuery(String query) {
      this.query = query;
    }

  }

  static class CommandAttributes {
    private static final String OFFSET = "offset";
    private static final String LIMIT = "limit";
    private static final String TENANT = "tenant";
    private static final String TENANT_ID = "tenant_id";
    private static final String QUERY = "query";

    private CommandAttributes() {
      throw new AssertionError();
    }
  }

}
