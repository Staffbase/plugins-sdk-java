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

/**
 * Special exception expressing issues in the verification process of a
 * single-sign-on attempt with staffbase.
 */
public class SSOException extends Exception {

  /**
   * @see java.io.Serializable
   */
  private static final long serialVersionUID = 6803609782363892321L;


  /**********************************************
   * Constructors
   **********************************************/

  SSOException(final String message, final Throwable cause) {
    super(message, cause);
  }

  SSOException(final String message) {
    super(message);
  }
}
