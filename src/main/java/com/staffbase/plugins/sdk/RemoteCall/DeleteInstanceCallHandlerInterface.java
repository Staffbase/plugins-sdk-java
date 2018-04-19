/**
 * Delete remote handler interface, based on this doc:
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
 * Interface DeleteInstanceCallHandlerInterface
 */
public interface DeleteInstanceCallHandlerInterface extends RemoteCallInterface {

    /**
     * Method to remove and cleanup every plugin related data of the given identifier.
     *
     * @param instanceId Plugin Instance identifier
     * @return <code>false</code> if the deletion goes wrong and should be retried later.
     */
    public boolean deleteInstance(String instanceId);
}
