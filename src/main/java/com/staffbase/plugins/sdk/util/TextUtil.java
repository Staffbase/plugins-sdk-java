/**
 * SSO implementation, based on this doc:
 * https://developers.staffbase.com/api/plugin-sso/
 *
 * @copyright 2017 Staffbase GmbH. 
 * @author    Thilo Schmalfuß
 * @license   http://www.apache.org/licenses/LICENSE-2.0
 * @link      https://github.com/staffbase/plugins-sdk-java
 */

package com.staffbase.plugins.sdk.util;

import java.util.Locale;
import java.util.Optional;

import com.staffbase.plugins.sdk.sso.SSOData;

public class TextUtil {

  /**
   * Parse a {@link Locale} from a string version as used in staffbase's SSO context.
   *
   * @see SSOData
   * @param input the input string to parse, e.g. "de_DE"
   * @return the parsed locale instance
   */
  public static Optional<Locale> parseLocale(final String input) {
    if (input == null || input.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(Locale.forLanguageTag(input.replace("_", "-")));
  }
}
