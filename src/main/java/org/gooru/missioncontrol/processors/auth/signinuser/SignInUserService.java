package org.gooru.missioncontrol.processors.auth.signinuser;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;
import org.gooru.missioncontrol.app.data.JsonNodeHolder;
import org.gooru.missioncontrol.bootstrap.component.jdbi.PGArrayUtils;
import org.gooru.missioncontrol.constants.HttpConstants;
import org.gooru.missioncontrol.constants.MessageConstants;
import org.gooru.missioncontrol.processors.auth.signinuser.SignInUserCommand.SignInUserCommandBean;
import org.gooru.missioncontrol.processors.common.tenant.TenantRootFinderService;
import org.gooru.missioncontrol.processors.exceptions.HttpResponseWrapperException;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hazelcast.util.StringUtil;


class SignInUserService {

  private final SignInUserDao signInUserDao;
  private final NucleusSignInUserDao nucleusSignInUserDao;
  private SignInUserCommandBean commandBean;
  private SignInUserModel signInUserModel;
  private NucleusUserModelForSignIn nucleusUserModelForAccessToken;
  private JsonNode prefs;
  private ArrayNode permissions;
  private JsonNode tenantSettings;
  private TenantModelForSignIn tenantModel;
  private NucleusUserModelForSignIn userModel;
  private UUID tenantRoot = MessageConstants.Params.NO_UUID;
  private TenantRootFinderService tenantRootFinderService;
  private DBI authDbi;
  private DBI coreDbi;
  private static ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");
  private static final List<String> TENANT_SETTING_KEYS_LIST =
      Collections.unmodifiableList(Arrays.asList("allow_multi_grade_class","usage_metrics_visibility"));
  private static final String OPEN_CURLY_BRACE = "{";
  private static final String CLOSE_CURLY_BRACE = "}";


  private static final Logger LOGGER = LoggerFactory.getLogger(SignInUserService.class);

  SignInUserService(DBI authDbi, DBI coreDbi) {
    this.signInUserDao = authDbi.onDemand(SignInUserDao.class);
    this.nucleusSignInUserDao = coreDbi.onDemand(NucleusSignInUserDao.class);
    this.authDbi = authDbi;
    this.coreDbi = coreDbi;
  }

  private void initializeTokenAttributes() {
    tenantModel = tenantModel == null ? fetchTenantModel() : tenantModel;
    tenantRoot = MessageConstants.Params.NO_UUID.equals(tenantRoot) ? findTenantRoot() : tenantRoot;
    userModel = userModel == null ? fetchUserModel() : userModel;
    prefs = prefs == null ? fetchPrefs() : prefs;
    permissions = (permissions == null) ? fetchPermissions() : permissions;
    tenantSettings = fetchTenantSettings();
  }

   Session signInUser(SignInUserCommand command) {
    this.commandBean = command.asBean();
    this.signInUserModel = signInUserDao.fetchUserByEmail(commandBean);
    this.validate();
    initializeTokenAttributes();
    return generateToken();
  }

  private void validate() {
    if (this.signInUserModel == null) {
      LOGGER.warn("user not found in auth database for email: {}", commandBean.getEmail());
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.NOT_FOUND, resourceBundle.getString("user.not.found"));
    }
    // Check if provided password matches with what stored in DB
    if (!commandBean.getPassword().equals(signInUserModel.getPassword())) {
      LOGGER.warn("Invalid password provided while login");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.UNAUTHORIZED, resourceBundle.getString("invalid.password"));
    }
    this.nucleusUserModelForAccessToken = this.nucleusSignInUserDao
        .fetchActiveUser(UUID.fromString(signInUserModel.getId()), UUID.fromString(signInUserModel.getTenantId()));
    if (nucleusUserModelForAccessToken == null) {
      LOGGER.warn("user not found in nucleus database for this admin user: {}",
          signInUserModel.getId());
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.NOT_FOUND, resourceBundle.getString("user.not.found"));
    }
  }

  private Session generateToken() {
    TokenCreator tokenCreator =
        new TokenCreator(tenantModel, userModel, tenantRoot, prefs, permissions, tenantSettings);
    return tokenCreator.createToken();
  }

  private NucleusUserModelForSignIn fetchUserModel() {
    LOGGER.debug("Generating access token for admin user id: '{}'", signInUserModel.getId());

    NucleusUserModelForSignIn model = nucleusSignInUserDao
        .fetchActiveUser(UUID.fromString(signInUserModel.getId()), UUID.fromString(signInUserModel.getTenantId()));
    if (model == null) {
      LOGGER.warn("Invalid user: admin user id: '{}'", signInUserModel.getId());
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          resourceBundle.getString("userid.invalid"));
    }
    return model;
  }

  private JsonNode fetchPrefs() {
    return fetchPreferences(UUID.fromString(userModel.getUserId()));
  }

  private JsonNode fetchTenantSettings() {
    return fetchTenantSetting(UUID.fromString(signInUserModel.getTenantId()));
  }

  private ArrayNode fetchPermissions() {
    UUID userId = UUID.fromString(userModel.getUserId());
    List<Integer> userRoles = nucleusSignInUserDao.fetchUserRole(userId);
    if (userRoles.isEmpty()) {
      return null;
    }

    List<String> userPermissions =
        nucleusSignInUserDao.fetchPermissionsByRole(toPostgresArrayInt(userRoles));
    return (userPermissions.isEmpty()) ? null : new ObjectMapper().valueToTree(userPermissions);
  }

  private TenantModelForSignIn fetchTenantModel() {
    return nucleusSignInUserDao.fetchTenantForToken(UUID.fromString(signInUserModel.getTenantId()));
  }


  private UUID findTenantRoot() {
    if (tenantModel.getParentTenantId() == null) {
      return null;
    }
    if (tenantRootFinderService == null) {
      tenantRootFinderService = new TenantRootFinderService(this.coreDbi);
    }
    return tenantRootFinderService.findTenantRoot(tenantModel.getParentTenantId());
  }

  private JsonNode fetchPreferences(UUID userId) {
    JsonNodeHolder result = nucleusSignInUserDao.fetchUserPreferences(userId);
    if (result == null) {
      result = nucleusSignInUserDao.fetchDefaultUserPreferences();
    }
    return (result == null) ? null : result.getJsonNode();
  }

  public JsonNode fetchTenantSetting(UUID tenantId) {
    Map<String, Object> settings = null;
    List<TenantSettingModel> tenantSettings = this.nucleusSignInUserDao.fetchSetting(tenantId,
        PGArrayUtils.convertFromListStringToSqlArrayOfString(TENANT_SETTING_KEYS_LIST));
    if (tenantSettings != null && !tenantSettings.isEmpty()) {
      settings = new HashMap<>();
      for (TenantSettingModel tenantSetting : tenantSettings) {
        String value = tenantSetting.getValue();
        if (!StringUtil.isNullOrEmpty(value) && value.startsWith(OPEN_CURLY_BRACE)
            && value.endsWith(CLOSE_CURLY_BRACE)) {
          ObjectMapper mapper = new ObjectMapper();
          try {
            settings.put(tenantSetting.getKey(),
                mapper.readValue(tenantSetting.getValue(), Map.class));
          } catch (Exception e) {
            LOGGER.error("Error while parsing tenant settings json");
          }
        } else {
          settings.put(tenantSetting.getKey(), tenantSetting.getValue());
        }
      }
    }
    return (settings == null || settings.isEmpty()) ? null
        : new ObjectMapper().valueToTree(settings);
  }

  private String toPostgresArrayInt(Collection<Integer> input) {
    Iterator<Integer> it = input.iterator();
    if (!it.hasNext()) {
      return "{}";
    }

    StringBuilder sb = new StringBuilder();
    sb.append('{');
    for (;;) {
      Integer i = it.next();
      sb.append(i);
      if (!it.hasNext()) {
        return sb.append('}').toString();
      }
      sb.append(',');
    }
  }

}
