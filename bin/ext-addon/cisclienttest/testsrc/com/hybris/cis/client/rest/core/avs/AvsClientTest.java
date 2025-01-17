package com.hybris.cis.client.rest.core.avs;


import de.hybris.bootstrap.annotations.ManualTest;
import de.hybris.platform.servicelayer.ServicelayerTest;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;

import com.hybris.cis.api.avs.model.AvsResult;
import com.hybris.cis.api.model.CisAddress;
import com.hybris.cis.api.model.CisDecision;
import com.hybris.cis.client.rest.avs.AvsClient;
import com.hybris.commons.client.RestResponse;


/**
 * Validates that the "out-of-the-box" spring configuration will wire in the mock client if mock mode is set.
 */
@ManualTest
public class AvsClientTest extends ServicelayerTest
{
	private static final String CLIENT_REF = "abc";

	@Resource
	private AvsClient avsClient;


	@Test
	public void shouldAcceptAddress()
	{
		System.out.println("enteringshouldAcceptAddress()");

		Assert.assertNotNull(avsClient);
		final CisAddress address = new CisAddress("1700 Broadway  Fl 26", "10019", "New York", "NY", "US");
		final RestResponse<AvsResult> response = avsClient.verifyAddress(CLIENT_REF, address);
		Assert.assertEquals(CisDecision.ACCEPT, response.getResult().getDecision());
	}



}
