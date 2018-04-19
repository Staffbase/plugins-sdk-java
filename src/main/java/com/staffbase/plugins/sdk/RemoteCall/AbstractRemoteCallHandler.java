/**
 * Abstract remote handler implementation, based on this doc:
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
 * class AbstractRemoteCallHandler
 *
 * An Abstract RemoteCallHandler implementation
 * which can be used in conjunction with all
 * remote call interfaces
 */
public abstract class AbstractRemoteCallHandler implements RemoteCallInterface{
    @Override
    public void exitSuccess() {
        
    }

    @Override
    public void exitFailure() {

    }
}
