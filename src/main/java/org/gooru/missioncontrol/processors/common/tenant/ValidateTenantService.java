package org.gooru.missioncontrol.processors.common.tenant;

import java.util.ResourceBundle;
import java.util.UUID;
import org.gooru.missioncontrol.constants.HttpConstants.HttpStatus;
import org.gooru.missioncontrol.processors.exceptions.ActiveTenantNotFoundException;
import org.gooru.missioncontrol.processors.exceptions.HttpResponseWrapperException;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ValidateTenantService {

  private final DBI dbi;
  private static final Logger LOGGER = LoggerFactory.getLogger(ValidateTenantService.class);
  private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");

  public ValidateTenantService(DBI dbi) {
    this.dbi = dbi;
  }

  public void validateTenant(UUID tenantId) {
    ValidateTenantDao validateTenantDao = this.dbi.onDemand(ValidateTenantDao.class);
    try {
      validateTenantDao.validate(tenantId);
    } catch (ActiveTenantNotFoundException e) {
      LOGGER.warn("Invalid tenant: '{}'", tenantId);
      throw new HttpResponseWrapperException(HttpStatus.BAD_REQUEST,
          resourceBundle.getString("tenantid.invalid"));
    }
  }

}
