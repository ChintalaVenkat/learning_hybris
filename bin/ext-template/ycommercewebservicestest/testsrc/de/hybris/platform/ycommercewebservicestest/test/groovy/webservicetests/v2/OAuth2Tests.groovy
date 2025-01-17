/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */

package de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.v2
import de.hybris.bootstrap.annotations.ManualTest
import groovy.json.JsonSlurper
import org.junit.Test

@ManualTest
class OAuth2Tests extends BaseWSTest {

	@Test
	void testGetAccessToken() {
		assert testUtil.getAccessToken(testUtil.USERNAME, testUtil.PASSWORD) : 'Unable to obtain access_token!'
	}

	@Test
	void testGetRefreshToken() {
		def tokenMap = testUtil.getAccessTokenMap(testUtil.USERNAME, testUtil.PASSWORD)
		assert testUtil.refreshToken(tokenMap.refresh_token) : 'Could not obtian new access token'
	}

	@Test
	void testGetClientCredentialsToken() {
		assert testUtil.getClientCredentialsToken('mobile_android', 'secret')
	}

	@Test
	void testGetWrongClientIdClientCredentialsToken() {
		testUtil.fakeSecurity()
		def con = testUtil.getSecureConnection("${testUtil.OAUTH2_TOKEN_ENDPOINT}?client_id=WRONG&client_secret=secret&grant_type=client_credentials", 'POST', 'JSON', HttpURLConnection.HTTP_UNAUTHORIZED)
		def error = con.errorStream.text;
		def response = new JsonSlurper().parseText(error);
		assert response.errors[0].message == "No client with requested id: WRONG"
		assert response.errors[0].type == "UnauthorizedError"
	}

	@Test
	void testGetWrongGrantTypeToken() {
		testUtil.fakeSecurity()
		def con = testUtil.getSecureConnection("${testUtil.OAUTH2_TOKEN_ENDPOINT}?client_id=mobile_android&client_secret=secret&grant_type=WRONG", 'POST', 'JSON', HttpURLConnection.HTTP_BAD_REQUEST)
		def error = con.errorStream.text;
		def response = new JsonSlurper().parseText(error);
		assert response.errors[0].message == "Unsupported grant type: WRONG"
		assert response.errors[0].type == "UnsupportedGrantTypeError"
	}
}