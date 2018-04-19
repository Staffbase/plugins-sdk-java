/**
 * Remote call interface, based on this doc:
 * https://developers.staffbase.com/api/plugin-sso/
 *
 * @category  Authentication
 * @copyright 2018 Staffbase, GmbH.
 * @author    Stefan Staude
 * @license   http://www.apache.org/licenses/LICENSE-2.0
 * @link      https://github.com/staffbase/plugins-sdk-java
 */
package com.staffbase.plugins.sdk.RemoteCall;

/**
 * Interface RemoteCallInterface
 *
 * A generic interface describing the protocol with the
 * Staffbase Backend after a Remote SSO cal was issued.
 */
public interface RemoteCallInterface {

    /**
	 * Stop the execution by providing a 2XX HTTP response
	 *
	 * This will tell Staffbase that everything went OK.
	 */
    public void exitSuccess();

    /**
	 * Stop the execution by providing a 5XX HTTP response
	 *
	 * This will tell Staffbase that it should try again later.
	 */
    public void exitFailure();
}
