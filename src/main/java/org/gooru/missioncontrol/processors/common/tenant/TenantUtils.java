package org.gooru.missioncontrol.processors.common.tenant;

import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
import org.gooru.missioncontrol.constants.HttpConstants.HttpStatus;
import org.gooru.missioncontrol.constants.MessageConstants;
import org.gooru.missioncontrol.processors.exceptions.HttpResponseWrapperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class TenantUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(TenantUtils.class);
  private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");

  private TenantUtils() {
    throw new AssertionError();
  }

  public static UUID validateAndGetTenant(Optional<String> tenantValue) {
    if (!tenantValue.isPresent()) {
      LOGGER.warn("Tenant id from present");
      throw new HttpResponseWrapperException(HttpStatus.BAD_REQUEST,
          resourceBundle.getString("tenantid.invalid"));
    }
    String tenantId = tenantValue.get();
    if (tenantId.isEmpty() || TenantUtils.isInvalidUUID(tenantId)) {
      LOGGER.warn("Invalid tenant id: '{}'", Objects.toString(tenantId));
      throw new HttpResponseWrapperException(HttpStatus.BAD_REQUEST,
          resourceBundle.getString("tenantid.invalid"));
    }
    return UUID.fromString(tenantId);
  }

  private static boolean isInvalidUUID(String uuidString) {
    if (uuidString != null && !uuidString.isEmpty()
        && uuidString.length() == MessageConstants.NO_VALUE.length()) {
      try {
        UUID.fromString(uuidString);
        return false;
      } catch (IllegalArgumentException e) {
        LOGGER.warn("Invalid string for UUID: '{}", uuidString);
      }
    }
    return true;
  }

}
