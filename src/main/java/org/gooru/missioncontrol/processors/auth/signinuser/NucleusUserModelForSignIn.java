package org.gooru.missioncontrol.processors.auth.signinuser;


public class NucleusUserModelForSignIn {
  private String userId;
  private String appId;
  private String partnerId;
  private String username;
  private String email;

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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    return "UserModelForSessionToken{" + "userId='" + userId + '\'' + ", appId='" + appId + '\''
        + ", partnerId='" + partnerId + '\'' + ", username='" + username + '\'' + ", email='"
        + email + '\'' + '}';
  }
}
