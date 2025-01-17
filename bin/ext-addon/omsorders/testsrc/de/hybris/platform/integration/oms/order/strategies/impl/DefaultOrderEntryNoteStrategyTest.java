/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2012 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package de.hybris.platform.integration.oms.order.strategies.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.basecommerce.util.BaseCommerceBaseTest;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.site.BaseSiteService;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultOrderEntryNoteStrategyTest extends BaseCommerceBaseTest
{
	@Resource
	private DefaultOrderEntryNoteStrategy orderEntryNoteStrategy;

	@Resource
	private BaseSiteService baseSiteService;


	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();
		createDefaultUsers();
		importCsv("/omsorders/test/testAcceleratorData.csv", "UTF-8");
		importCsv("/omsorders/test/testOrders.csv", "UTF-8");


		final BaseSiteModel testSite = baseSiteService.getBaseSiteForUID("testSite");
		Assert.assertNotNull("BaseSite 'testSite' was null", testSite);
		Assert.assertFalse("BaseStore does not have stores", testSite.getStores().isEmpty());
		Assert.assertFalse("BaseStore does not have PointOfService", testSite.getStores().get(0).getPointsOfService().isEmpty());
		Assert.assertNotNull("BaseStore does not have an address", testSite.getStores().get(0).getPointsOfService().get(0)
				.getAddress());
		testSite.setChannel(SiteChannel.B2C);
		baseSiteService.setCurrentBaseSite(testSite, false);
	}

	@Test
	public void shouldGetOrderEntryNote()
	{
		final String orderCode = "testOrderCode";
		final OrderModel orderModel = getOrderForCode(orderCode);

		final String note = orderEntryNoteStrategy.getNote((OrderEntryModel) orderModel.getEntries().get(0));
		Assert.assertNotNull("Order note should not be null", note);
		Assert.assertEquals(orderModel.getEntries().get(0).getProduct().getName(), note);
	}

	@Override
	protected OrderModel getOrderForCode(final String orderCode)
	{
		final DefaultGenericDao defaultGenericDao = new DefaultGenericDao(OrderModel._TYPECODE);
		defaultGenericDao.setFlexibleSearchService(flexibleSearchService);
		final List<OrderModel> orders = defaultGenericDao.find(Collections.singletonMap(OrderModel.CODE, orderCode));
		Assert.assertFalse(orders.isEmpty());
		final OrderModel orderModel = orders.get(0);
		Assert.assertNotNull("Order should have been loaded from database", orderModel);
		return orderModel;
	}

}
