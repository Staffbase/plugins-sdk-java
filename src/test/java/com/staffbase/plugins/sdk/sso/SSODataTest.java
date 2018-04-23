/**
 * SSO implementation test, based on this doc:
 * https://developers.staffbase.com/api/plugin-sso/
 *
 * @copyright 2017 Staffbase GmbH. 
 * @author    Thilo Schmalfuß
 * @author    Vitaliy Ivanov
 * @license   http://www.apache.org/licenses/LICENSE-2.0
 * @link      https://github.com/staffbase/plugins-sdk-java
 */

package com.staffbase.plugins.sdk.sso;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Locale;
import java.util.Arrays;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.junit.Test;


public class SSODataTest {

  private static final String ROLE_EDITOR = "editor";
  private static final String REMOTE_CALL_DELETE = "delete";

  public static final String DATA_INSTANCE_ID = "55c79b6ee4b06c6fb19bd1e2";
  public static final String DATA_USER_ID = "541954c3e4b08bbdce1a340a";
  public static final String DATA_USER_EXTERNAL_ID = "jdoe";
  public static final String DATA_USER_FIRST_NAME = "John";
  public static final String DATA_USER_LAST_NAME = "Doe";
  public static final String DATA_USER_ROLE = "editor";
  public static final String DATA_USER_LOCALE = "en-US";
  public static final String DATA_ISSUER = "api.staffbase.com";
  public static final String DATA_AUDIENCE = "map";
  public static final String DATA_INSTANCE_NAME = "Our locations";
  public static final String DATA_USER_FULL_NAME = "John Doe";
  public static final String DATA_ENTITY_TYPE = "user";
  public static final String DATA_THEME_TEXT_COLOR = "#000000";
  public static final String DATA_THEME_BACKGROUND_COLOR = "#FFFFFF";
  public static final List<String> DATA_TAGS = Arrays.asList("profile:tag1", "profile:tag2");

  /**
   * Test empty claims.
   */
  @Test
  public void testNullClaims() {
    try {
      final SSOData ssoData = new SSOData(null);
      fail("Expected Exception not thrown.");
    } catch (Exception e) {}
  }

  /**
   * Test claim accessors.
   * @throws MalformedClaimException
   */
  @Test
  public void createWithJwtClaims() throws MalformedClaimException {

    final JwtClaims claims = mock(JwtClaims.class);

    when(claims.getClaimValue(SSOData.KEY_INSTANCE_ID, String.class)).thenReturn(DATA_INSTANCE_ID);
    when(claims.getClaimValue(SSOData.KEY_USER_ID, String.class)).thenReturn(DATA_USER_ID);
    when(claims.getClaimValue(SSOData.KEY_USER_EXTERNAL_ID, String.class)).thenReturn(DATA_USER_EXTERNAL_ID);
    when(claims.getClaimValue(SSOData.KEY_USER_FIRST_NAME, String.class)).thenReturn(DATA_USER_FIRST_NAME);
    when(claims.getClaimValue(SSOData.KEY_USER_LAST_NAME, String.class)).thenReturn(DATA_USER_LAST_NAME);
    when(claims.getClaimValue(SSOData.KEY_USER_ROLE, String.class)).thenReturn(DATA_USER_ROLE);
    when(claims.getClaimValue(SSOData.KEY_USER_LOCALE, String.class)).thenReturn(DATA_USER_LOCALE);
    when(claims.getClaimValue(SSOData.KEY_ISSUER, String.class)).thenReturn(DATA_ISSUER);
    when(claims.getClaimValue(SSOData.KEY_AUDIENCE, String.class)).thenReturn(DATA_AUDIENCE);
    when(claims.getClaimValue(SSOData.KEY_INSTANCE_NAME, String.class)).thenReturn(DATA_INSTANCE_NAME);
    when(claims.getClaimValue(SSOData.KEY_USER_FULL_NAME, String.class)).thenReturn(DATA_USER_FULL_NAME);
    when(claims.getClaimValue(SSOData.KEY_ENTITY_TYPE, String.class)).thenReturn(DATA_ENTITY_TYPE);
    when(claims.getClaimValue(SSOData.KEY_THEME_TEXT_COLOR, String.class)).thenReturn(DATA_THEME_TEXT_COLOR);
    when(claims.getClaimValue(SSOData.KEY_THEME_BACKGROUND_COLOR, String.class)).thenReturn(DATA_THEME_BACKGROUND_COLOR);
    when(claims.getClaimValue(SSOData.KEY_TAGS, List.class)).thenReturn(DATA_TAGS);

    final SSOData ssoData = new SSOData(claims);

    assertEquals(DATA_INSTANCE_ID, ssoData.getInstanceID());
    assertEquals(DATA_USER_ID, ssoData.getUserID().get());
    assertEquals(DATA_USER_EXTERNAL_ID, ssoData.getUserExternalID().get());
    assertEquals(DATA_USER_FIRST_NAME, ssoData.getUserFirstName().get());
    assertEquals(DATA_USER_LAST_NAME, ssoData.getUserLastName().get());
    assertEquals(DATA_USER_ROLE, ssoData.getUserRole().get());
    assertEquals(DATA_USER_LOCALE, ssoData.getUserLocaleAsString().get());

    assertEquals(DATA_ISSUER, ssoData.getIssuer().get());
    assertEquals(DATA_AUDIENCE, ssoData.getAudience().get());
    assertEquals(DATA_INSTANCE_NAME, ssoData.getInstanceName().get());
    assertEquals(DATA_USER_FULL_NAME, ssoData.getUserFullName().get());
    assertEquals(DATA_ENTITY_TYPE, ssoData.getEntityType().get());
    assertEquals(DATA_THEME_TEXT_COLOR, ssoData.getThemeTextColor().get());
    assertEquals(DATA_THEME_BACKGROUND_COLOR, ssoData.getThemeBackgroundColor().get());
    assertEquals(DATA_TAGS, ssoData.getTags().get());

    assertEquals(DATA_USER_ROLE.equals(ROLE_EDITOR), ssoData.isEditor());

    assertEquals(Locale.US, ssoData.getUserLocale().get());
  }

  /**
   * Test deletion claim accessor.
   * @throws MalformedClaimException
   */
  @Test
  public void testWithDeleteJWTClaims() throws MalformedClaimException {

    final JwtClaims claims = mock(JwtClaims.class);

    when(claims.getClaimValue(SSOData.KEY_INSTANCE_ID, String.class)).thenReturn(DATA_INSTANCE_ID);
    when(claims.getClaimValue(SSOData.KEY_USER_ID, String.class)).thenReturn(REMOTE_CALL_DELETE);

    final SSOData ssoData = new SSOData(claims);
    assertTrue(ssoData.isDeleteInstanceCall());
  }
}
