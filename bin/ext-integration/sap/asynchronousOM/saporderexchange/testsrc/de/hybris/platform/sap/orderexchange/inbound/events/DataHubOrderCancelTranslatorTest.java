package de.hybris.platform.sap.orderexchange.inbound.events;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.sap.orderexchange.constants.DataHubInboundConstants;
import de.hybris.platform.sap.orderexchange.datahub.inbound.DataHubInboundDeliveryHelper;
import de.hybris.platform.sap.orderexchange.datahub.inbound.DataHubInboundOrderHelper;
import de.hybris.platform.sap.orderexchange.datahub.inbound.impl.DefaultDataHubInboundDeliveryHelper;
import de.hybris.platform.sap.orderexchange.inbound.events.DataHubOrderCancelTranslator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;


@SuppressWarnings("javadoc")
@UnitTest
public class DataHubOrderCancelTranslatorTest
{

	@InjectMocks
	private DataHubOrderCancelTranslator classUnderTest;
	@Mock
	private Item processedItem;

	@Mock
	private DataHubInboundOrderHelper orderHubService;

	@Before
	public void setUp() throws JaloInvalidParameterException, JaloSecurityException
	{
		classUnderTest = new DataHubOrderCancelTranslator();
		processedItem = org.mockito.Mockito.mock(Item.class);
		orderHubService = org.mockito.Mockito.mock(DataHubInboundOrderHelper.class);
		classUnderTest.setInboundHelper(orderHubService);
		Mockito.when(processedItem.getAttribute(DataHubInboundConstants.CODE)).thenReturn("0815");
	}

	@Test
	public void testPerformCancellation() throws ImpExException, JaloInvalidParameterException, JaloSecurityException,
			OrderCancelException
	{
		classUnderTest.performImport("00", processedItem);
		org.mockito.Mockito.verify(orderHubService).cancelOrder("00", "0815");
	}
}
