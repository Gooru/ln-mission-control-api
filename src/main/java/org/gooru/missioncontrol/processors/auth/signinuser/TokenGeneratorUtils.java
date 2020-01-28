package org.gooru.missioncontrol.processors.auth.signinuser;

import java.util.Base64;


final class TokenGeneratorUtils {

  private TokenGeneratorUtils() {
    throw new AssertionError();
  }

  private static final String TOKEN_VERSION = "2";
  private static final String SEPARATOR = ":";

  public static String generateToken(String userId, String partnerId, String clientId,
      long issuedAt) {
    String result = TOKEN_VERSION + SEPARATOR + issuedAt + SEPARATOR + userId + SEPARATOR
        + (partnerId != null ? partnerId : "") + SEPARATOR + clientId;

    return Base64.getEncoder().encodeToString(result.getBytes());
  }

}
