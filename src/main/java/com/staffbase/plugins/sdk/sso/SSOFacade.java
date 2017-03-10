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

import java.security.interfaces.RSAPublicKey;
import java.nio.charset.Charset;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;



/**
 * Simple facade for handling staffbase's single-sign-on attempts and verifying the
 * corresponding JWT data.
 */
public class SSOFacade {

  private static final Logger logger = LogManager.getLogger(SSOFacade.class);

  /**
   * The name of the claim which holds the mandatory instance id for the
   * single-sign-on attempt.
   */
  public static final String EXPECTED_CLAIM_INSTANCE_ID = SSOData.KEY_INSTANCE_ID;

  /**
   * Initialize the facade using the given RSA public key
   *
   * @param rsaPublicKey the RSA public key to be used for verification.
   *
   * @return the facade.
   */
  public static SSOFacade create(final RSAPublicKey rsaPublicKey) {

    return new SSOFacade()
    .initialize(rsaPublicKey);
  }


  /**********************************************
   * Members
   **********************************************/

  /**
   * The consumer instance to be used when validating single-sign-on attempt via
   * JWT.
   */
  private JwtConsumer jwtConsumer;


  /**********************************************
   * Constructors
   **********************************************/

  /**
   * Private constructor.
   */
  SSOFacade() {
  }


  /**********************************************
   * Initialization
   **********************************************/

  /**
   * Initialize this component by building up the consumer for JWT using the
   * pre-configured secret
   *
   * @param rsaPublicKey the RSA public key to be used for verification.
   *
   * @return Fluent interface.
   */
  SSOFacade initialize(final RSAPublicKey rsaPublicKey) {

    if (logger.isDebugEnabled()) {
      logger.debug("Initializing single-sign-on manager SSOFacade. ");
    }

    Objects.requireNonNull(rsaPublicKey);

    // Build up the algorithm constraints by only accepting RSA_USING_SHA256.
    final AlgorithmConstraints algorithmConstraints = new AlgorithmConstraints(
      AlgorithmConstraints.ConstraintType.WHITELIST, AlgorithmIdentifiers.RSA_USING_SHA256);

    this.jwtConsumer = new JwtConsumerBuilder()
      .setJwsAlgorithmConstraints(algorithmConstraints)
      .setSkipDefaultAudienceValidation()
      .setVerificationKey(rsaPublicKey)
      .setRequireExpirationTime()
      .setRequireNotBefore()
      .setRequireIssuedAt() 
      .build();

    return this;
  }


  /**********************************************
   * Methods
   **********************************************/

  /**
   * Verify and parse a single-sign-on attempt coming from the staffbase app.
   *
   * @param raw the raw JWT string
   * @return the parsed data from the sign-on attempt
   * @throws SSOException if the verification of the sign-on attempt fails
   */
  public SSOData verify(final String raw) throws SSOException {

    if (logger.isDebugEnabled()) {
      logger.debug("Attempting to decrypt SSO JWT. "
          + "[raw=" + raw + "]");
    }

    Objects.requireNonNull(raw);

    try {

      // Process and verify the request on the basis of jwt verification
      final JwtClaims jwtClaims = this.jwtConsumer
          .process(raw)
          .getJwtClaims();

      // Add the instance id verification step
      final String instanceId = jwtClaims.getClaimValue(EXPECTED_CLAIM_INSTANCE_ID, String.class);
      if (instanceId == null || instanceId.isEmpty()) {
        if (logger.isFatalEnabled()) {
          logger.fatal("Encountered illegal sso attempt. "
              + "Bad instance_id. "
              + "[instance_id=" + instanceId + "]");
        }

        throw new SSOException("Missing or malformed instnance_id.");
      }

      if (logger.isDebugEnabled()) {
        logger.debug("Verification of single-sign-on JWT succeeded. "
            + "[raw=" + raw + "] "
            + "[claims=" + jwtClaims + "]");
      }

      // Parse and return the container data.
      return new SSOData(jwtClaims);
    } catch (final MalformedClaimException malformationException) {
      if (logger.isFatalEnabled()) {
        logger.fatal("Encountered malformed sso attempt.", malformationException);
      }

      throw new SSOException(malformationException.getMessage(), malformationException);
    } catch (final InvalidJwtException invalidJwtException) {
      if (logger.isFatalEnabled()) {
        logger.fatal("Encountered illegal sso attempt.", invalidJwtException);
      }

      throw new SSOException(invalidJwtException.getMessage(), invalidJwtException);
    }
  }
}
