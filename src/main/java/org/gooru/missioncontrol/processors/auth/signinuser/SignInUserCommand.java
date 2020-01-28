package org.gooru.missioncontrol.processors.auth.signinuser;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.ResourceBundle;
import org.gooru.missioncontrol.constants.HttpConstants;
import org.gooru.missioncontrol.constants.MessageConstants;
import org.gooru.missioncontrol.processors.exceptions.HttpResponseWrapperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonObject;


class SignInUserCommand {
  private String email;
  private String password;
  private static final Logger LOGGER = LoggerFactory.getLogger(SignInUserCommand.class);
  private static ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");


  static SignInUserCommand builder(JsonObject requestBody) {
    SignInUserCommand result = buildFromJsonObject(requestBody);
    result.validate();
    return result;
  }

  public SignInUserCommandBean asBean() {
    SignInUserCommandBean bean = new SignInUserCommandBean();
    bean.email = email;
    bean.password = password;
    return bean;
  }

  private void validate() {
    if (email == null) {
      LOGGER.debug("email address not present at basic authentication header");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "missing email address");
    }
    if (password == null) {
      LOGGER.debug("password not present at basic authentication header");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "missing password");
    }
  }

  private static SignInUserCommand buildFromJsonObject(JsonObject requestHeaders) {
    SignInUserCommand command = new SignInUserCommand();
    String basicAuthCredentials = requestHeaders.getString(MessageConstants.MSG_HEADER_BASIC_AUTH);
    String credentials[] = getUsernameAndPassword(basicAuthCredentials);
    final String email = credentials[0];
    final String password = encryptPassword(credentials[1]);
    command.email = email;
    command.password = password;
    return command;
  }

  public static class SignInUserCommandBean {

    private String email;
    private String password;

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }
  }



  private static String[] getUsernameAndPassword(String basicAuthCredentials) {
    byte credentialsDecoded[] = Base64.getDecoder().decode(basicAuthCredentials);
    final String credential = new String(credentialsDecoded, 0, credentialsDecoded.length);

    int index = credential.indexOf(':');
    if (index <= 0) {
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          resourceBundle.getString("invalid.credential"));
    }

    String[] credentials = new String[2];
    credentials[0] = credential.substring(0, index);
    credentials[1] = credential.substring((index + 1));

    return credentials;
  }

  private static String encryptPassword(final String text) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
      messageDigest.update(text.getBytes(StandardCharsets.UTF_8));
      byte raw[] = messageDigest.digest();
      return Base64.getEncoder().encodeToString(raw);
    } catch (NoSuchAlgorithmException e) {
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Error while authenticating user - No algorithm exists");
    }
  }

}
