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
 *
 *
 */
package de.hybris.platform.sap.productconfig.facades.impl;

import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.sap.productconfig.facades.ConfigPricing;
import de.hybris.platform.sap.productconfig.facades.PricingData;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Required;


public class ConfigPricingImpl implements ConfigPricing
{
	private PriceDataFactory priceDataFactory;

	@Required
	public void setPriceDataFactory(final PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}

	@Override
	public PricingData getPricingData(final ConfigModel model)
	{
		final PricingData pricingData = new PricingData();

		final PriceData basePrice;
		final PriceData selectedOptions;
		final PriceData currentTotal;

		if (model.getBasePrice() != null)
		{
			basePrice = getPriceData(model.getBasePrice().getPriceValue(), model.getBasePrice().getCurrency());
		}
		else
		{
			basePrice = getPriceData(null, null);
		}

		if (model.getSelectedOptionsPrice() != null)
		{
			selectedOptions = getPriceData(model.getSelectedOptionsPrice().getPriceValue(), model.getSelectedOptionsPrice()
					.getCurrency());
		}
		else
		{
			selectedOptions = getPriceData(null, null);
		}

		if (model.getCurrentTotalPrice() != null)
		{
			currentTotal = getPriceData(model.getCurrentTotalPrice().getPriceValue(), model.getCurrentTotalPrice().getCurrency());
		}
		else
		{
			currentTotal = getPriceData(null, null);
		}

		pricingData.setBasePrice(basePrice);
		pricingData.setSelectedOptions(selectedOptions);
		pricingData.setCurrentTotal(currentTotal);

		return pricingData;
	}

	private PriceData getPriceData(final BigDecimal price, final String currency)
	{
		if (price == null || currency == null)
		{
			return ConfigPricing.NO_PRICE;
		}

		final PriceData priceData = priceDataFactory.create(PriceDataType.BUY, price, currency);

		return priceData;
	}
}
