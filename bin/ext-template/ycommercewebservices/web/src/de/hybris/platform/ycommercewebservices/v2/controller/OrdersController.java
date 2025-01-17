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
package de.hybris.platform.ycommercewebservices.v2.controller;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderHistoriesData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commercewebservicescommons.cache.CacheControl;
import de.hybris.platform.commercewebservicescommons.cache.CacheControlDirective;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderHistoryListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.ProductLowStockException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.StockSystemException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.ycommercewebservices.constants.YcommercewebservicesConstants;
import de.hybris.platform.ycommercewebservices.exceptions.NoCheckoutCartException;
import de.hybris.platform.ycommercewebservices.exceptions.PaymentAuthorizationException;
import de.hybris.platform.ycommercewebservices.strategies.OrderCodeIdentificationStrategy;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Web Service Controller for the ORDERS resource. Most methods check orders of the user. Methods require authentication
 * and are restricted to https channel.
 * 
 * @pathparam code Order GUID (Globally Unique Identifier) or order CODE
 * @pathparam userId User identifier or one of the literals below :
 *            <ul>
 *            <li>'current' for currently authenticated user</li>
 *            <li>'anonymous' for anonymous user</li>
 *            </ul>
 */
@Controller
@RequestMapping(value = "/{baseSiteId}")
public class OrdersController extends BaseCommerceController
{
	private final static Logger LOG = Logger.getLogger(OrdersController.class);
	@Resource(name = "orderFacade")
	private OrderFacade orderFacade;
	@Resource(name = "orderCodeIdentificationStrategy")
	private OrderCodeIdentificationStrategy orderCodeIdentificationStrategy;
	//TODO avoid using following services
	@Resource(name = "commerceCartService")
	private CommerceCartService commerceCartService;
	@Resource(name = "cartService")
	private CartService cartService;
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	/**
	 * Returns specific order details by order GUID (Globally Unique Identifier) or order CODE. Response contains a
	 * detailed order info object.
	 * 
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Order data
	 */
	@Secured("ROLE_TRUSTED_CLIENT")
	@RequestMapping(value = "/orders/{code}", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 120)
	@ResponseBody
	public OrderWsDTO getOrder(@PathVariable final String code, @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		OrderData orderData;
		if (orderCodeIdentificationStrategy.isID(code))
		{
			try
			{
				orderData = orderFacade.getOrderDetailsForGUID(code);
				return dataMapper.map(orderData, OrderWsDTO.class, fields);

			}
			catch (final UnknownIdentifierException ex)
			{
				if (orderCodeIdentificationStrategy.isFailIfNotFound())
				{
					throw ex;
				}
				else
				{
					//silence exception, search for order by code
					orderData = orderFacade.getOrderDetailsForCodeWithoutUser(code);
					return dataMapper.map(orderData, OrderWsDTO.class, fields);
				}
			}
		}

		orderData = orderFacade.getOrderDetailsForCodeWithoutUser(code);
		return dataMapper.map(orderData, OrderWsDTO.class, fields);
	}

	/**
	 * Returns specific order details by a specific order code. Response contains a detailed order info object.
	 * 
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Order data
	 */
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/users/{userId}/orders/{code}", method = RequestMethod.GET)
	@ResponseBody
	public OrderWsDTO getOrderForUserByCode(@PathVariable final String code,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final OrderData orderData = orderFacade.getOrderDetailsForCode(code);
		final OrderWsDTO dto = dataMapper.map(orderData, OrderWsDTO.class, fields);
		return dto;
	}

	/**
	 * Returns order history data for all orders placed by the specific user for the specific base store. Response
	 * contains orders search result displayed in several pages if needed.
	 * 
	 * @queryparam statuses Filters only certain order statuses. It means: statuses=CANCELLED,CHECKED_VALID would only
	 *             return orders with status CANCELLED or CHECKED_VALID.
	 * @queryparam currentPage The current result page requested.
	 * @queryparam pageSize The number of results returned per page.
	 * @queryparam sort Sorting method applied to the return results.
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Order history data.
	 */
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/users/{userId}/orders", method = RequestMethod.GET)
	@ResponseBody
	public OrderHistoryListWsDTO getOrdersForUser(@RequestParam(required = false) final String statuses,
			@RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@RequestParam(required = false) final String sort, @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(currentPage);
		pageableData.setPageSize(pageSize);
		pageableData.setSort(sort);

		final OrderHistoriesData orderHistoriesData;
		if (statuses != null)
		{
			final Set<OrderStatus> statusSet = extractOrderStatuses(statuses);
			orderHistoriesData = createOrderHistoriesData(orderFacade.getPagedOrderHistoryForStatuses(pageableData,
					statusSet.toArray(new OrderStatus[statusSet.size()])));
		}
		else
		{
			orderHistoriesData = createOrderHistoriesData(orderFacade.getPagedOrderHistoryForStatuses(pageableData));
		}

		final OrderHistoryListWsDTO dto = dataMapper.map(orderHistoriesData, OrderHistoryListWsDTO.class, fields);
		return dto;
	}

	/**
	 * Method authorizes cart and places the order. Response contains the new order data.
	 * 
	 * @formparam cartId Cart code for logged in user, cart GUID for guest checkout
	 * @formparam securityCode CCV security code.
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Created order data
	 * @throws PaymentAuthorizationException
	 *            When there are problems with the payment authorization. For example: there is no session cart or no
	 *            payment information set for the cart.
	 * @throws InvalidCartException
	 * @throws WebserviceValidationException
	 *            When the cart is not filled properly (e. g. delivery mode is not set, payment method is not set)
	 * @throws ProductLowStockException
	 *            When product is out of stock in store
	 * @throws StockSystemException
	 *            When there is no information about stock for stores.
	 */
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/users/{userId}/orders", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public OrderWsDTO placeOrder(@RequestParam(required = true) final String cartId,
			@RequestParam(required = true) final String securityCode,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields) throws PaymentAuthorizationException,
			InvalidCartException, WebserviceValidationException, NoCheckoutCartException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.info("placeOrder");
		}

		loadCart(cartId);

		validateCartForPlaceOrder();

		//authorize
		if (!checkoutFacade.authorizePayment(securityCode))
		{
			throw new PaymentAuthorizationException();
		}

		//placeorder
		final OrderData orderData = checkoutFacade.placeOrder();
		final OrderWsDTO dto = dataMapper.map(orderData, OrderWsDTO.class, fields);
		return dto;
	}

	//TODO move it to commerceservices/facades
	protected void loadCart(final String cartId)
	{
		final UserModel currentUser = userService.getCurrentUser();
		final BaseSiteModel baseSite = baseSiteService.getCurrentBaseSite();
		final CartModel cart;

		if (userFacade.isAnonymousUser())
		{
			cart = commerceCartService.getCartForGuidAndSite(cartId, baseSite);
			if (cart != null && CustomerModel.class.isAssignableFrom(cart.getUser().getClass()))
			{
				final CustomerModel cartOwner = (CustomerModel) cart.getUser();
				if (!userService.isAnonymousUser(cartOwner) && !CustomerType.GUEST.equals(cartOwner.getType()))
				{
					// 'access denied' presented as 'not found' for security reasons
					throw new CartException("Cart not found.", CartException.NOT_FOUND, cartId);
				}
			}
		}
		else
		{
			cart = commerceCartService.getCartForCodeAndUser(cartId, currentUser);
		}
		if (cart == null)
		{
			throw new CartException("Cart not found.", CartException.NOT_FOUND, cartId);
		}
		cartService.setSessionCart(cart);
	}

	protected Set<OrderStatus> extractOrderStatuses(final String statuses)
	{
		final String statusesStrings[] = statuses.split(YcommercewebservicesConstants.OPTIONS_SEPARATOR);

		final Set<OrderStatus> statusesEnum = new HashSet<>();
		for (final String status : statusesStrings)
		{
			statusesEnum.add(OrderStatus.valueOf(status));
		}
		return statusesEnum;
	}

	protected OrderHistoriesData createOrderHistoriesData(final SearchPageData<OrderHistoryData> result)
	{
		final OrderHistoriesData orderHistoriesData = new OrderHistoriesData();

		orderHistoriesData.setOrders(result.getResults());
		orderHistoriesData.setSorts(result.getSorts());
		orderHistoriesData.setPagination(result.getPagination());

		return orderHistoriesData;
	}

}
