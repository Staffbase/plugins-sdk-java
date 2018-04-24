/**
 * SSO implementation, based on this doc:
 * https://developers.staffbase.com/api/plugin-sso/
 *
 * @copyright 2017 Staffbase GmbH. 
 * @author    Thilo Schmalfuß
 * @author    Vitaliy Ivanov
 * @license   http://www.apache.org/licenses/LICENSE-2.0
 * @link      https://github.com/staffbase/plugins-sdk-java
 */
package com.staffbase.plugins.sdk.sso;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;

import com.staffbase.plugins.sdk.util.TextUtil;

/**
 * A container for the data transmitted from staffbase to a plugin using staffbase's
 * single-sign-on methodology.
 */
public class SSOData {

  /**********************************************
   * Constants
   **********************************************/

  /**
   * The name of the role which is sent by staffbase to a plugin, if the requesting
   * user may alter the plugin's contents, i.e. has an editing permission
   * to the specific plugin instance.
   */
  public static final String ROLE_EDITOR = "editor";

  /**
   * The user id/subject to identify if the SSO call is an instance deletion call.
   */
  public static final String REMOTE_CALL_DELETE = "delete";

  /**
   * The key in the JWT claims for fetching the requested plugin instance's
   * unique id.
   */
  public static final String KEY_INSTANCE_ID = "instance_id";

  /**
   * The key in the JWT claims for fetching the requesting user's unique id.
   */
  public static final String KEY_USER_ID = "sub";

  /**
   * The key in the JWT claims for fetching the requesting user's id in an
   * external system.
   */
  public static final String KEY_USER_EXTERNAL_ID = "external_id";

  /**
   * The key in the JWT claims for fetching the requesting user's first name.
   */
  public static final String KEY_USER_FIRST_NAME = "given_name";

  /**
   * The key in the JWT claims for fetching the requesting users last name.
   */
  public static final String KEY_USER_LAST_NAME = "family_name";

  /**
   * The key in the JWT claims for fetching the requesting users role.
   */
  public static final String KEY_USER_ROLE = "role";

  /**
   * The key in the JWT claims for fetching the requesting user's locale
   * settings.
   */
  public static final String KEY_USER_LOCALE = "locale";

  /**
   * The key in the JWT claims for fetching the issuer name.
   */
  public static final String KEY_ISSUER = "iss";

  /**
   * The key in the JWT claims for fetching the audience of the request.
   */
  public static final String KEY_AUDIENCE = "aud";

  /**
   * The key in the JWT claims for fetching the requested plugin instance's
   * name.
   */
  public static final String KEY_INSTANCE_NAME = "instance_name";

  /**
   * The key in the JWT claims for fetching the requesting user's full
   * name.
   */
  public static final String KEY_USER_FULL_NAME = "name";

  /**
   * The key in the JWT claims for fetching the the type of the accessing
   * entity.
   */
  public static final String KEY_ENTITY_TYPE = "type";

  /**
   * The key in the JWT claims for fetching the color of the text that 
   * is configured in the Staffbase app.
   */
  public static final String KEY_THEME_TEXT_COLOR = "theming_text";

  /**
   * The key in the JWT claims for fetching the color of the background
   * that is configured in the Staffbase app. 
   */
  public static final String KEY_THEME_BACKGROUND_COLOR = "theming_bg";

  /**
   * The key in the JWT claims for fetching the list of tags
   * that are configured in the Staffbase app. 
   */
  public static final String KEY_TAGS = "tags";

  /**********************************************
   * Members
   **********************************************/

  /**
   * The unique id of the specific plugin instance that was requested using
   * staffbase's SSO.
   */
  private final String instanceID;

  /**
   * The unique id of the staffbase user making the request to the plugin using staffbase's
   * SSO.
   */
  private final String userID;

  /**
   * The id of the requesting user in an external system, if given.
   */
  private final String userExternalID;

  /**
   * The first, i.e. given name of the user making the request using staffbase's SSO.
   */
  private final String userFirstName;

  /**
   * The last, i.e. family name of the user making the request using staffbase's SSO.
   */
  private final String userLastName;

  /**
   * The name of the issuing authority for Stabase's SSO .
   */
  private final String issuer;

  /**
   * The name of the audience the staffbase's SSO data is dedicated.
   */
  private final String audience;

  /**
   * The Name of the specific plugin instance that was requested using
   * staffbase's SSO.
   */
  private final String instanceName;

  /**
   * The full name of the user making the request using staffbase's SSO.
   */
  private final String userFullName;

  /**
   * the the type of the accessing entity making the request using staffbase's SSO.
   */
  private final String entityType;

  /**
   * The color of the text that is configured in the Staffbase app.
   */
  private final String themeTextColor;

  /**
   * The color of the background that is configured in the Staffbase app.
   */
  private final String themeBackgroundColor;

  /**
   * The tags that are configured in the Staffbase app.
   */
  private final List<String> tags;

  /**
   * The locale of the user requesting the plugin instance.
   *
   * @see <a href=
   *      "http://www.oracle.com/technetwork/java/javase/javase7locales-334809.html">
   *      JDK 7 and JRE 7 Supported Locales</a>
   */
  private final String userLocale;

  /**
   * The role of the user in regards of the requested {@link #instanceID}. If
   * the requesting user does have editing permissions, this value is set to
   * {@value #ROLE_EDITOR}.
   *
   * @see #instanceID
   */
  private final String userRole;


  /**********************************************
   * Constructors
   **********************************************/

  public SSOData(final JwtClaims jwtClaims) throws MalformedClaimException {

    Objects.requireNonNull(jwtClaims);

    this.instanceID = jwtClaims.getClaimValue(KEY_INSTANCE_ID, String.class);
    this.userID = jwtClaims.getClaimValue(KEY_USER_ID, String.class);
    this.userExternalID = jwtClaims.getClaimValue(KEY_USER_EXTERNAL_ID, String.class);
    this.userFirstName = jwtClaims.getClaimValue(KEY_USER_FIRST_NAME, String.class);
    this.userLastName = jwtClaims.getClaimValue(KEY_USER_LAST_NAME, String.class);
    this.userRole = jwtClaims.getClaimValue(KEY_USER_ROLE, String.class);
    this.userLocale = jwtClaims.getClaimValue(KEY_USER_LOCALE, String.class);
    this.issuer = jwtClaims.getClaimValue(KEY_ISSUER, String.class);
    this.audience = jwtClaims.getClaimValue(KEY_AUDIENCE, String.class);
    this.instanceName = jwtClaims.getClaimValue(KEY_INSTANCE_NAME, String.class);
    this.userFullName = jwtClaims.getClaimValue(KEY_USER_FULL_NAME, String.class);
    this.entityType = jwtClaims.getClaimValue(KEY_ENTITY_TYPE, String.class);
    this.themeTextColor = jwtClaims.getClaimValue(KEY_THEME_TEXT_COLOR, String.class);
    this.themeBackgroundColor = jwtClaims.getClaimValue(KEY_THEME_BACKGROUND_COLOR, String.class);
    this.tags = jwtClaims.getClaimValue(KEY_TAGS, List.class);
  }

  /**********************************************
   * Getters
   **********************************************/

  /** 
   * Get the name of the issuing authority for Stabase's SSO .
   *
   * @see #issuer
   * @return the name of the issuing authority
   */
  public Optional<String> getIssuer() { 
    return Optional.ofNullable(this.issuer);
  }

  /** 
   * Get the name of the audience the staffbase's SSO data is dedicated.
   *
   * @see #audience
   * @return the name of the audience the data is dedicated to
   */
  public Optional<String> getAudience() { 
    return Optional.ofNullable(this.audience);
  }

  /** 
   * Get the Name of the specific plugin instance that was requested using
   * staffbase's SSO.
   *
   * @see #instanceName
   * @return the Name of the specific plugin instance
   */
  public Optional<String> getInstanceName() { 
    return Optional.ofNullable(this.instanceName);
  }

  /** 
   * Get the full name of the user making the request using staffbase's SSO.
   *
   * @see #userFullName
   * @return the full name of the user
   */
  public Optional<String> getUserFullName() { 
    return Optional.ofNullable(this.userFullName);
  }

  /** 
   * Get the type of the accessing entity making the request using staffbase's SSO.
   *
   * @see #entityType
   * @return type of the accessing entity
   */
  public Optional<String> getEntityType() { 
    return Optional.ofNullable(this.entityType);
  }

  /** 
   * Get the color of the text that is configured in the Staffbase app.
   *
   * @see #themeTextColor
   * @return the hex color of the text 
   */
  public Optional<String> getThemeTextColor() { 
    return Optional.ofNullable(this.themeTextColor);
  }

  /** 
   * Get the color of the background that is configured in the Staffbase app.
   *
   * @see #themeBackgroundColor
   * @return the hex color of the background 
   */
  public Optional<String> getThemeBackgroundColor() { 
    return Optional.ofNullable(this.themeBackgroundColor);
  }

  /**
   * Get the unique id of the specific plugin instance that was requested using
   * staffbase's SSO.
   *
   * @see #instanceID
   * @return requested plugin instance
   */
  public String getInstanceID() {
    return this.instanceID;
  }

  /**
   * Get the unique id of the staffbase user making the request to the plugin using
   * staffbase's SSO.
   *
   * @see #userID
   * @return the unique id of the requesting user
   */
  public Optional<String> getUserID() {
    return Optional.ofNullable(this.userID);
  }

  /**
   * Get the id of the requesting user in an external system, if given.
   *
   * @see #userExternalID
   * @return the requesting user's id in an external system
   */
  public Optional<String> getUserExternalID() {
    return Optional.ofNullable(this.userExternalID);
  }

  /**
   * Get the first, i.e. given name of the user making the request using staffbase's
   * SSO.
   *
   * @see #userFirstName
   * @return the requesting user's first name
   */
  public Optional<String> getUserFirstName() {
    return Optional.ofNullable(this.userFirstName);
  }

  /**
   * Get the last, i.e. family name of the user making the request using staffbase's
   * SSO.
   *
   * @see #userLastName
   * @return the requesting user's last name
   */
  public Optional<String> getUserLastName() {
    return Optional.ofNullable(this.userLastName);
  }

  /**
   * Get the locale of the user requesting the plugin instance as the originally
   * transmitted string.
   *
   * @see #userLocale
   * @return the locale of the requesting user
   */
  public Optional<String> getUserLocaleAsString() {
    return Optional.ofNullable(this.userLocale);
  }

  /**
   * Get the locale of the user requesting the plugin instance.
   *
   * @see #userLocale
   * @return the locale of the requesting user
   */
  public Optional<Locale> getUserLocale() {
    return TextUtil.parseLocale(this.userLocale);
  }

  /**
   * Get the role of the user in regards of the requested {@link #instanceID}.
   * If the requesting user does have admin permissions, this value is set to
   * {@value #ROLE_EDITOR}.
   *
   * @see #userRole
   * @return the requesting user's access role
   */
  public Optional<String> getUserRole() {
    return Optional.ofNullable(this.userRole);
  }

  /**
   * Check whether the requesting user is configured as an editor of the
   * requested plugin instance.
   *
   * @see #userRole
   * @return <code>true</code> if the requesting user is an editor.
   */
  public boolean isEditor() {
    return ROLE_EDITOR.equals(this.userRole);
  }

  /**
   * Check if the SSO call is an instance deletion call.
   *
   * If an editor deletes a plugin instance in Staffbase,
   * this will be true.
   *
   * @return <code>true</code> if the SSO call is an instance deletion call
   */
  public boolean isDeleteInstanceCall() {
    return REMOTE_CALL_DELETE.equals(this.userID);
  }

  /**
   * Get the tags of the user in regards of the requested {@link #instanceID}.
   * If the requesting user does have admin permissions, this value is set to
   * {@value #ROLE_EDITOR}.
   *
   * @see #tags
   * @return the requesting user's tags
   */
  public Optional<List<String>> getTags() {
    return Optional.ofNullable(this.tags);
  }

  @Override
  public String toString() {
    return "SSOData ["+
  " instanceID="+ this.instanceID+
  ", userID="+ this.userID+
  ", userExternalID="+ this.userExternalID+
  ", userFirstName="+ this.userFirstName+
  ", userLastName="+ this.userLastName+
  ", userRole="+ this.userRole+
  ", userLocale="+ this.userLocale+
  ", issuer="+ this.issuer+
  ", audience="+ this.audience+
  ", instanceName="+ this.instanceName+
  ", userFullName="+ this.userFullName+
  ", entityType="+ this.entityType+
  ", themeTextColor="+ this.themeTextColor+
  ", themeBackgroundColor="+ this.themeBackgroundColor+
  " ]";
  }
}
