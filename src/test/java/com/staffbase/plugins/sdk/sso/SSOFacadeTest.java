/**
 * SSO implementation test, based on this doc:
 * https://developers.staffbase.com/api/plugin-sso/
 *
 * @copyright 2017 Staffbase GmbH. 
 * @author    Vitaliy Ivanov
 * @license   http://www.apache.org/licenses/LICENSE-2.0
 * @link      https://github.com/staffbase/plugins-sdk-java
 */

package com.staffbase.plugins.sdk.sso;

import org.junit.Test;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.lang.JoseException;


public class SSOFacadeTest {

  private static final String ROLE_EDITOR = "editor";

  /**
   * Generate a 2048 bits JSON web key.
   * @return JSON web key
   * @throws JoseException
   */
  private RsaJsonWebKey generateRsaJwk() throws JoseException {

    return RsaJwkGenerator.generateJwk(2048);
  }

  /**
   * Create a JwtClaims object with prefilled sane defaults.
   * @return JwtClaims
   */
  private JwtClaims createDefaultClaims() {

    JwtClaims claims = new JwtClaims();

    claims.setIssuer(SSODataTest.DATA_ISSUER);  // who creates the token and signs it
    claims.setAudience(SSODataTest.DATA_AUDIENCE); // to whom the token is intended to be sent
    claims.setExpirationTimeMinutesInTheFuture(10); // time when the token will expire (10 minutes from now)
    claims.setGeneratedJwtId(); // a unique identifier for the token
    claims.setIssuedAtToNow();  // when the token was issued/created (now)
    claims.setNotBeforeMinutesInThePast(2); // time before which the token is not yet valid (2 minutes ago)
    claims.setSubject(SSODataTest.DATA_USER_ID); // the subject/principal is whom the token is about
    claims.setClaim(SSOData.KEY_INSTANCE_ID, SSODataTest.DATA_INSTANCE_ID); // additional claims/attributes about the subject can be added

    return claims;
  }

  /**
   * Create a JwtClaims object with prefilled sane defaults and missing mandatory claims.
   * @return JwtClaims
   */
  private JwtClaims createMalformedClaims() {

    JwtClaims claims = new JwtClaims();

    claims.setIssuer(SSODataTest.DATA_ISSUER);  // who creates the token and signs it
    claims.setGeneratedJwtId(); // a unique identifier for the token
    claims.setSubject(SSODataTest.DATA_USER_ID); // the subject/principal is whom the token is about
    claims.setClaim(SSOData.KEY_INSTANCE_ID, SSODataTest.DATA_INSTANCE_ID); // additional claims/attributes about the subject can be added

    return claims; 
  }

  /**
   * Create a RSA256 signed token from given claims and RSA jwk.
   * 
   * @param JwtClaims claims
   * @param RsaJsonWebKey rsaJsonWebKey
   * @return String
   * @throws JoseException
   */
  private String createSignedTokenFromClaims(JwtClaims claims, RsaJsonWebKey rsaJsonWebKey) throws JoseException {

    // A JWT is a JWS and/or a JWE with JSON claims as the payload.
    // In this example it is a JWS so we create a JsonWebSignature object.
    JsonWebSignature jws = new JsonWebSignature();

    // The payload of the JWS is JSON content of the JWT Claims
    jws.setPayload(claims.toJson());

    // The JWT is signed using the private key
    jws.setKey(rsaJsonWebKey.getPrivateKey());

    // Set the signature algorithm on the JWT/JWS that will integrity protect the claims
    jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

    return jws.getCompactSerialization();
  }

  /**
   * Create a RSA384 signed token from given claims and RSA jwk.
   * 
   * @param JwtClaims claims
   * @param RsaJsonWebKey rsaJsonWebKey
   * @return String
   * @throws JoseException
   */
  private String createUnsupportedSignedTokenFromClaims(JwtClaims claims, RsaJsonWebKey rsaJsonWebKey) throws JoseException {

    // A JWT is a JWS and/or a JWE with JSON claims as the payload.
    // In this example it is a JWS so we create a JsonWebSignature object.
    JsonWebSignature jws = new JsonWebSignature();

    // The payload of the JWS is JSON content of the JWT Claims
    jws.setPayload(claims.toJson());

    // The JWT is signed using the private key
    jws.setKey(rsaJsonWebKey.getPrivateKey());

    // Set the signature algorithm on the JWT/JWS that will integrity protect the claims
    jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA384);    

    return jws.getCompactSerialization();
  }

  /**
   * Test unsupported signing algorithm.
   * @throws JoseException
   */
  @Test(expected=SSOException.class)
  public void testUnsupportedSignToken() throws JoseException,SSOException {

    RsaJsonWebKey jwk = this.generateRsaJwk();
    JwtClaims claims = this.createDefaultClaims();
    String jwt = this.createUnsupportedSignedTokenFromClaims(claims,jwk);


    final SSOFacade ssoFac = SSOFacade.create(jwk.getRsaPublicKey());
    ssoFac.verify(jwt);

  }

  /**
   * Test proper signed token.
   * @throws JoseException
   */
  @Test
  public void testProperSignedToken() throws JoseException, SSOException {

    RsaJsonWebKey jwk = this.generateRsaJwk();
    JwtClaims claims = this.createDefaultClaims();
    String jwt = this.createSignedTokenFromClaims(claims, jwk);

    final SSOFacade ssoFac = SSOFacade.create(jwk.getRsaPublicKey());
    ssoFac.verify(jwt);
  }

  /**
   * Test proper signed token.
   * @throws JoseException
   */
  @Test(expected=SSOException.class)
  public void testProperSignedTokenWithDiffrentKey() throws JoseException, SSOException {

    RsaJsonWebKey jwk = this.generateRsaJwk();
    JwtClaims claims = this.createDefaultClaims();
    String jwt = this.createSignedTokenFromClaims(claims, jwk);

    jwk = this.generateRsaJwk();


    final SSOFacade ssoFac = SSOFacade.create(jwk.getRsaPublicKey());
    ssoFac.verify(jwt);

  }

  /**
   * Test proper signed token missing mandatory claims.
   * @throws JoseException
   */
  @Test(expected=SSOException.class)
  public void testMalformedCLaims() throws JoseException, SSOException {

    RsaJsonWebKey jwk = this.generateRsaJwk();
    JwtClaims claims = this.createMalformedClaims();

    String jwt = this.createSignedTokenFromClaims(claims, jwk);

    final SSOFacade ssoFac = SSOFacade.create(jwk.getRsaPublicKey());
    ssoFac.verify(jwt);

  }

  /**
   * Test proper signed token missing mandatory nbf claim.
   * @throws JoseException
   */
  @Test(expected=SSOException.class)
  public void testMissingNBFCLaim() throws JoseException, SSOException {

    RsaJsonWebKey jwk = this.generateRsaJwk();
    JwtClaims claims = this.createMalformedClaims();

    claims.setExpirationTimeMinutesInTheFuture(10);
    claims.setIssuedAtToNow();
    //claims.setNotBeforeMinutesInThePast(2);

    String jwt = this.createSignedTokenFromClaims(claims, jwk);

    final SSOFacade ssoFac = SSOFacade.create(jwk.getRsaPublicKey());
    ssoFac.verify(jwt);
  }

  /**
   * Test proper signed token valid in an hour.
   * @throws JoseException
   */
  @Test(expected=SSOException.class)
  public void testFutureNBFCLaim() throws JoseException, SSOException {

    RsaJsonWebKey jwk = this.generateRsaJwk();
    JwtClaims claims = this.createMalformedClaims();

    NumericDate nbf = NumericDate.now();
    nbf.addSeconds(3600);

    claims.setExpirationTimeMinutesInTheFuture(10);
    claims.setIssuedAtToNow();
    claims.setNotBefore(nbf);

    String jwt = this.createSignedTokenFromClaims(claims, jwk);

    final SSOFacade ssoFac = SSOFacade.create(jwk.getRsaPublicKey());
    ssoFac.verify(jwt);
  }

  /**
   * Test proper signed token missing mandatory iat claim.
   * @throws JoseException
   */
  @Test(expected=SSOException.class)
  public void testMissingIATCLaim() throws JoseException, SSOException {

    RsaJsonWebKey jwk = this.generateRsaJwk();
    JwtClaims claims = this.createMalformedClaims();

    claims.setExpirationTimeMinutesInTheFuture(10);
    //claims.setIssuedAtToNow();
    claims.setNotBeforeMinutesInThePast(2);

    String jwt = this.createSignedTokenFromClaims(claims, jwk);

    final SSOFacade ssoFac = SSOFacade.create(jwk.getRsaPublicKey());
    ssoFac.verify(jwt);
  }

  /**
   * Test proper signed token missing mandatory exp claim.
   * @throws JoseException
   */
  @Test(expected=SSOException.class)
  public void testMissingEXPCLaim() throws JoseException, SSOException {

    RsaJsonWebKey jwk = this.generateRsaJwk();
    JwtClaims claims = this.createMalformedClaims();

    //claims.setExpirationTimeMinutesInTheFuture(10);
    claims.setIssuedAtToNow();
    claims.setNotBeforeMinutesInThePast(2);

    String jwt = this.createSignedTokenFromClaims(claims, jwk);

    final SSOFacade ssoFac = SSOFacade.create(jwk.getRsaPublicKey());
    ssoFac.verify(jwt);
  }

  /**
   * Test proper signed token already expired.
   * @throws JoseException
   */
  @Test(expected=SSOException.class)
  public void testPastEXPCLaim() throws JoseException, SSOException  {

    RsaJsonWebKey jwk = this.generateRsaJwk();
    JwtClaims claims = this.createMalformedClaims();

    NumericDate exp = NumericDate.now();
    exp.addSeconds(-3600);

    claims.setExpirationTime(exp);
    claims.setIssuedAtToNow();
    claims.setNotBeforeMinutesInThePast(2);

    String jwt = this.createSignedTokenFromClaims(claims, jwk);

    final SSOFacade ssoFac = SSOFacade.create(jwk.getRsaPublicKey());
    ssoFac.verify(jwt);
   }

  /**
   * Test missing decryption key.
   * @throws JoseException
   */
  @Test(expected=NullPointerException.class)
  public void testNullKey() throws JoseException, SSOException {

    final SSOFacade ssoFac = SSOFacade.create(null);

  }

  /**
   * Test missing jwt.
   * @throws JoseException
   */
  @Test(expected=NullPointerException.class)
  public void testNullJwt() throws JoseException, SSOException {

    RsaJsonWebKey jwk = this.generateRsaJwk();

    final SSOFacade ssoFac = SSOFacade.create(jwk.getRsaPublicKey());

      ssoFac.verify(null);
  }

  /**
   * Test empty jwt.
   * @throws JoseException
   */
  @Test(expected=SSOException.class)
  public void testEmptyJwt() throws JoseException, SSOException {

    RsaJsonWebKey jwk = this.generateRsaJwk();

    final SSOFacade ssoFac = SSOFacade.create(jwk.getRsaPublicKey());

    ssoFac.verify("");
  }
  
}

