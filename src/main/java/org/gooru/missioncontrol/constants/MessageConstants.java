package org.gooru.missioncontrol.constants;

public final class MessageConstants {

  public static final String MSG_HEADER_OP = "mb.operation";
  public static final String MSG_HEADER_TOKEN = "session.token";
  public static final String MSG_OP_STATUS = "mb.operation.status";
  public static final String MSG_KEY_SESSION = "session";
  public static final String MSG_KEY_TENANT = "tenant";
  public static final String MSG_KEY_TENANT_ID = "tenant_id";
  public static final String MSG_OP_STATUS_SUCCESS = "success";
  public static final String MSG_OP_STATUS_ERROR = "error";
  public static final String MSG_OP_STATUS_VALIDATION_ERROR = "error.validation";
  public static final String MSG_USER_ANONYMOUS = "anonymous";
  public static final String MSG_USER_ID = "user_id";
  public static final String MSG_HTTP_STATUS = "http.status";
  public static final String MSG_HTTP_BODY = "http.body";
  public static final String MSG_HTTP_RESPONSE = "http.response";
  public static final String MSG_HTTP_ERROR = "http.error";
  public static final String MSG_HTTP_VALIDATION_ERROR = "http.validation.error";
  public static final String MSG_HTTP_HEADERS = "http.headers";
  public static final String MSG_MESSAGE = "message";
  public static final String MSG_HEADER_CLIENTID = "client.id";
  public static final String MSG_HEADER_TENANTID = "tenant.id";
  public static final String MSG_API_VERSION = "api.version";
  
  public static final String HTTP_PORT = "http.port";
  public static final String METRICS_PERIODICITY = "metrics.periodicity.seconds";
  public static final String MBUS_TIMEOUT = "message.bus.send.timeout.seconds";
  public static final String MAX_REQ_BODY_SIZE = "request.body.size.max.mb";
  
  public static final String MSG_OP_STATUS_FAIL = "mb.op.status.fail";
  
  // Containers for different responses
  public static final String RESP_CONTAINER_MBUS = "mb.container";
  public static final String RESP_CONTAINER_EVENT = "mb.event";
  
  public static final String MSG_OP_API_AUTH = "api.auth";
  public static final String MSG_OP_PARTNERS_GET = "partners.get";
  public static final String MSG_OP_PARTNER_GET = "partner.get";
  public static final String MSG_OP_COUNTRIES_STATS = "countries.stats";

  private MessageConstants() {
    throw new AssertionError();
  }
}
