package org.gooru.missioncontrol.processors.learners;

import java.util.UUID;
import org.gooru.missioncontrol.bootstrap.component.AppConfiguration;
import org.gooru.missioncontrol.bootstrap.component.jdbi.PGArray;
import org.gooru.missioncontrol.bootstrap.component.jdbi.PGArrayUtils;
import org.gooru.missioncontrol.constants.HttpConstants;
import org.gooru.missioncontrol.processors.exceptions.HttpResponseWrapperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonObject;


class LearnerListCommand {
  private Integer offset;
  private Integer limit;
  private UUID tenantId;
  private String query;
  private PGArray<UUID> classIds;
  private PGArray<Long> schoolIds;

  private static final Logger LOGGER = LoggerFactory.getLogger(LearnerListCommand.class);

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

  public PGArray<Long> getSchoolIds() {
    return schoolIds;
  }

  public PGArray<UUID> getClassIds() {
    return classIds;
  }


  static LearnerListCommand builder(JsonObject requestBody, JsonObject session) {
    LearnerListCommand result = LearnerListCommand.buildFromJsonObject(requestBody, session);
    result.validate();
    return result;
  }

  public LearnerListCommandBean asBean() {
    LearnerListCommandBean bean = new LearnerListCommandBean();
    bean.limit = limit;
    bean.offset = offset;
    bean.tenantId = tenantId;
    bean.query = query;
    bean.classIds = classIds;
    bean.schoolIds = schoolIds;
    return bean;
  }

  @SuppressWarnings("unchecked")
  private static LearnerListCommand buildFromJsonObject(JsonObject requestBody, JsonObject session) {
    LearnerListCommand result = new LearnerListCommand();
    Integer offset = requestBody.getInteger(CommandAttributes.OFFSET);
    Integer limit = requestBody.getInteger(CommandAttributes.LIMIT);
    populateTenantFromSession(session, result);
    populateOffsetAndLimit(offset, limit, result);
    if (requestBody.containsKey(CommandAttributes.QUERY)) {
      result.query = requestBody.getString(CommandAttributes.QUERY);
    }

    if (requestBody.containsKey(CommandAttributes.CLASS_IDS)) {
      result.classIds = PGArrayUtils.convertFromListStringToSqlArrayOfUUID(
          requestBody.getJsonArray(CommandAttributes.CLASS_IDS).getList());
    }

    if (requestBody.containsKey(CommandAttributes.SCHOOL_IDS)) {
      result.schoolIds = PGArrayUtils.convertFromListLongToSqlArrayOfLong(
          requestBody.getJsonArray(CommandAttributes.SCHOOL_IDS).getList());
    }

    return result;
  }

  private static void populateTenantFromSession(JsonObject session, LearnerListCommand command) {
    JsonObject tenant = session.getJsonObject(CommandAttributes.TENANT);
    if (tenant != null) {
      command.tenantId = UUID.fromString(tenant.getString(CommandAttributes.TENANT_ID));
    }
  }

  private static void populateOffsetAndLimit(Integer offset, Integer limit,
      LearnerListCommand command) {
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



  private void validate() {
    if (offset == null || limit == null) {
      LOGGER.info("Invalid offset/limit provided for request");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Invalid offset/limit");
    }

  }

  public static class LearnerListCommandBean {
    private Integer offset;
    private Integer limit;
    private UUID tenantId;
    private String query;
    private PGArray<UUID> classIds;
    private PGArray<Long> schoolIds;

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

    public PGArray<UUID> getClassIds() {
      return classIds;
    }

    public void setClassIds(PGArray<UUID> classIds) {
      this.classIds = classIds;
    }

    public PGArray<Long> getSchoolIds() {
      return schoolIds;
    }

    public void setSchoolIds(PGArray<Long> schoolIds) {
      this.schoolIds = schoolIds;
    }

  }

  static class CommandAttributes {
    private static final String OFFSET = "offset";
    private static final String LIMIT = "limit";
    private static final String TENANT = "tenant";
    private static final String TENANT_ID = "tenant_id";
    private static final String QUERY = "query";
    private static final String CLASS_IDS = "class_ids";
    private static final String SCHOOL_IDS = "school_ids";

    private CommandAttributes() {
      throw new AssertionError();
    }
  }

}
