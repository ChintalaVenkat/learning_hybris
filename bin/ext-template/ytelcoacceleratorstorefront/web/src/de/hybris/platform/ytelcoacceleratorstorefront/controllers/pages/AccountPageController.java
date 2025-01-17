/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.ytelcoacceleratorstorefront.controllers.pages;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.TitleData;
import de.hybris.platform.commercefacades.user.exceptions.PasswordMismatchException;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.configurablebundlefacades.order.BundleCartFacade;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.subscriptionfacades.SubscriptionFacade;
import de.hybris.platform.subscriptionfacades.action.SubscriptionUpdateActionEnum;
import de.hybris.platform.subscriptionfacades.converters.SubscriptionXStreamAliasConverter;
import de.hybris.platform.subscriptionfacades.data.SubscriptionBillingData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionBillingDetailFileStream;
import de.hybris.platform.subscriptionfacades.data.SubscriptionData;
import de.hybris.platform.subscriptionfacades.exceptions.SubscriptionFacadeException;
import de.hybris.platform.subscriptionservices.enums.SubscriptionStatus;
import de.hybris.platform.ytelcoacceleratorstorefront.breadcrumb.Breadcrumb;
import de.hybris.platform.ytelcoacceleratorstorefront.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.ytelcoacceleratorstorefront.controllers.ControllerConstants;
import de.hybris.platform.ytelcoacceleratorstorefront.controllers.pages.checkout.MultiStepCheckoutController.SelectOption;
import de.hybris.platform.ytelcoacceleratorstorefront.controllers.util.GlobalMessages;
import de.hybris.platform.ytelcoacceleratorstorefront.forms.AddressForm;
import de.hybris.platform.ytelcoacceleratorstorefront.forms.PaymentSubscriptionsForm;
import de.hybris.platform.ytelcoacceleratorstorefront.forms.UpdateEmailForm;
import de.hybris.platform.ytelcoacceleratorstorefront.forms.UpdatePasswordForm;
import de.hybris.platform.ytelcoacceleratorstorefront.forms.UpdateProfileForm;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;


/**
 * Controller for home page
 */
@Controller
@RequestMapping("/my-account")
public class AccountPageController extends AbstractSearchPageController
{
	// Internal Redirects
	private static final String REDIRECT_MY_ACCOUNT = REDIRECT_PREFIX + "/my-account";
	private static final String REDIRECT_TO_ADDRESS_BOOK_PAGE = REDIRECT_PREFIX + "/my-account/address-book";
	private static final String REDIRECT_TO_PAYMENT_INFO_PAGE = REDIRECT_PREFIX + "/my-account/payment-details";
	private static final String REDIRECT_TO_PAYMENTMETHOD_EDIT_PAGE = REDIRECT_PREFIX + "/paymentDetails/edit-payment-details";
	private static final String REDIRECT_TO_PROFILE_PAGE = REDIRECT_PREFIX + "/my-account/profile";
	private static final String REDIRECT_URL_CART = REDIRECT_PREFIX + "/cart";
	private static final String REDIRECT_MY_ACCOUNT_SUBSCRIPTION = REDIRECT_PREFIX + "/my-account/subscription/";
	/**
	 * We use this suffix pattern because of an issue with Spring 3.1 where a Uri value is incorrectly extracted if it
	 * contains on or more '.' characters. Please see https://jira.springsource.org/browse/SPR-6164 for a discussion on
	 * the issue and future resolution.
	 */
	private static final String ORDER_CODE_PATH_VARIABLE_PATTERN = "{orderCode:.*}";
	private static final String ADDRESS_CODE_PATH_VARIABLE_PATTERN = "{addressCode:.*}";
	private static final String SUBSCRIPTION_ID_PATH_VARIABLE_PATTERN = "{subscriptionId:.*}";

	// CMS Pages
	private static final String ACCOUNT_CMS_PAGE = "account";
	private static final String PROFILE_CMS_PAGE = "profile";
	private static final String ADDRESS_BOOK_CMS_PAGE = "address-book";
	private static final String ADD_EDIT_ADDRESS_CMS_PAGE = "add-edit-address";
	private static final String PAYMENT_DETAILS_CMS_PAGE = "payment-details";
	private static final String ORDER_HISTORY_CMS_PAGE = "orders";
	private static final String ORDER_DETAIL_CMS_PAGE = "order";
	private static final String SUBSCRIPTIONS_CMS_PAGE = "subscriptions";
	private static final String SUBSCRIPTION_DETAILS_CMS_PAGE = "subscription";
	private static final String SUBSCRIPTION_BILLING_ACTIVITY_CMS_PAGE = "subscriptionBillingActivity";
	private static final String IS_ALIVE_MSG = "alive";

	private static final Logger LOG = Logger.getLogger(AccountPageController.class);

	@Resource(name = "orderFacade")
	private OrderFacade orderFacade;

	@Resource(name = "acceleratorCheckoutFacade")
	private CheckoutFacade checkoutFacade;

	@Resource(name = "userFacade")
	protected UserFacade userFacade;

	@Resource(name = "customerFacade")
	protected CustomerFacade customerFacade;

	@Resource(name = "accountBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

	@Resource(name = "subscriptionFacade")
	private SubscriptionFacade subscriptionFacade;

	@Resource(name = "productFacade")
	private ProductFacade productFacade;

	@Resource(name = "productService")
	private ProductService productService;

	@Resource(name = "subscriptionXStreamAliasConverter")
	SubscriptionXStreamAliasConverter subscriptionXStreamAliasConverter;

	@Resource(name = "cartFacade")
	private BundleCartFacade cartFacade;

	@RequestMapping(method = RequestMethod.GET)
	public String account(final Model model) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs(null));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.Account.AccountHomePage;
	}

	@RequestMapping(value = "/is-alive", method = RequestMethod.GET)
	public @ResponseBody
	String isAlive()
	{
		return IS_ALIVE_MSG;
	}

	@RequestMapping(value = "/orders", method = RequestMethod.GET)
	public String orders(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode, final Model model)
			throws CMSItemNotFoundException
	{
		// Handle paged search results
		final PageableData pageableData = createPageableData(page, 5, sortCode, showMode);
		final SearchPageData<OrderHistoryData> searchPageData = orderFacade.getPagedOrderHistoryForStatuses(pageableData);
		populateModel(model, searchPageData, showMode);

		storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_HISTORY_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORDER_HISTORY_CMS_PAGE));
		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.orderHistory"));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.Account.AccountOrderHistoryPage;
	}

	@RequestMapping(value = "/order/" + ORDER_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String order(@PathVariable("orderCode") final String orderCode, final Model model) throws CMSItemNotFoundException
	{
		try
		{
			final OrderData orderDetails = orderFacade.getOrderDetailsForCode(orderCode);
			model.addAttribute("orderData", orderDetails);

			final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
			breadcrumbs.add(new Breadcrumb("/my-account/orders", getMessageSource().getMessage("text.account.orderHistory", null,
					getI18nService().getCurrentLocale()), null));
			breadcrumbs.add(new Breadcrumb("#", getMessageSource().getMessage("text.account.order.orderBreadcrumb", new Object[]
			{ orderDetails.getCode() }, "Order {0}", getI18nService().getCurrentLocale()), null));
			model.addAttribute("breadcrumbs", breadcrumbs);

		}
		catch (final UnknownIdentifierException e)
		{
			LOG.warn("Attempted to load a order that does not exist or is not visible", e);
			return REDIRECT_MY_ACCOUNT;
		}
		storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_DETAIL_CMS_PAGE));
		model.addAttribute("metaRobots", "no-index,no-follow");
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORDER_DETAIL_CMS_PAGE));
		return ControllerConstants.Views.Pages.Account.AccountOrderPage;
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String profile(final Model model) throws CMSItemNotFoundException
	{
		final List<TitleData> titles = userFacade.getTitles();

		final CustomerData customerData = customerFacade.getCurrentCustomer();

		if (customerData.getTitleCode() != null)
		{
			model.addAttribute("title", CollectionUtils.find(titles, new Predicate()
			{
				@Override
				public boolean evaluate(final Object object)
				{
					if (object instanceof TitleData)
					{
						return customerData.getTitleCode().equals(((TitleData) object).getCode());
					}
					return false;
				}
			}));
		}

		model.addAttribute("customerData", customerData);

		storeCmsPageInModel(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.profile"));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.Account.AccountProfilePage;
	}

	@RequestMapping(value = "/subscriptions", method = RequestMethod.GET)
	public String subscriptions(final Model model) throws CMSItemNotFoundException
	{
		List<SubscriptionData> sortedSubscriptions = new ArrayList<SubscriptionData>();
		try
		{
			final Collection<SubscriptionData> subscriptions = subscriptionFacade.getSubscriptions();

			if (subscriptions != null)
			{
				sortedSubscriptions = new ArrayList<SubscriptionData>(subscriptions);
			}
		}
		catch (final SubscriptionFacadeException e)
		{
			LOG.error("Error while retrieving subscriptions", e);
		}

		final List<CCPaymentInfoData> paymentInfoData = userFacade.getCCPaymentInfos(true);

		final Map<String, CCPaymentInfoData> paymentInfoMap = new HashMap<>();

		for (final CCPaymentInfoData paymentInfo : paymentInfoData)
		{
			paymentInfoMap.put(paymentInfo.getSubscriptionId(), paymentInfo);
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(SUBSCRIPTIONS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(SUBSCRIPTIONS_CMS_PAGE));
		model.addAttribute("subscriptionFacade", subscriptionFacade);
		model.addAttribute("paymentInfoMap", paymentInfoMap);
		model.addAttribute("subscriptions", sortedSubscriptions);
		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.subscriptions"));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.Account.AccountSubscriptionsPage;
	}

	@RequestMapping(value = "/subscription/" + SUBSCRIPTION_ID_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String subscription(@PathVariable("subscriptionId") final String subscriptionId, final Model model)
			throws CMSItemNotFoundException
	{
		try
		{
			model.addAttribute("paymentInfos", userFacade.getCCPaymentInfos(true));

			final SubscriptionData subscriptionDetails = subscriptionFacade.getSubscription(subscriptionId);
			model.addAttribute("subscriptionData", subscriptionDetails);

			final ProductData subscriptionProductData = getProductForSubscription(subscriptionDetails);
			model.addAttribute("subscriptionProductData", subscriptionProductData);

			final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
			breadcrumbs.add(new Breadcrumb("/my-account/subscriptions", getMessageSource().getMessage("text.account.subscriptions",
					null, getI18nService().getCurrentLocale()), null));
			breadcrumbs.add(new Breadcrumb("#", getMessageSource().getMessage("text.account.subscription.subscriptionBreadcrumb",
					new Object[]
					{ subscriptionDetails.getId() }, "Subscription {0}", getI18nService().getCurrentLocale()), null));
			model.addAttribute("breadcrumbs", breadcrumbs);

			final List<SelectOption> extensionOptions = new ArrayList<SelectOption>();
			for (int i = 1; i <= 3; i++)
			{
				final String counter = String.valueOf(i);
				extensionOptions.add(new SelectOption(counter, getMessageSource().getMessage("text.account.subscription.extendTerm",
						new Object[]
						{ subscriptionDetails.getContractFrequency(), counter }, null, getI18nService().getCurrentLocale())));
			}
			model.addAttribute("extensionOptions", extensionOptions);

			final List<CCPaymentInfoData> paymentMethods = userFacade.getCCPaymentInfos(true);
			model.addAttribute("paymentInfos", paymentMethods);

			final List<ProductData> upsellingOptions = subscriptionFacade.getUpsellingOptionsForSubscription(subscriptionDetails
					.getProductCode());
			model.addAttribute("upgradable", Boolean.valueOf(CollectionUtils.isNotEmpty(upsellingOptions)));

		}
		catch (final UnknownIdentifierException | SubscriptionFacadeException e)
		{
			LOG.warn("Attempted to load a subscription that does not exist or is not visible", e);
			return REDIRECT_MY_ACCOUNT;
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(SUBSCRIPTION_DETAILS_CMS_PAGE));
		model.addAttribute("metaRobots", "no-index,no-follow");
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(SUBSCRIPTION_DETAILS_CMS_PAGE));
		return ControllerConstants.Views.Pages.Account.AccountSubscriptionPage;
	}

	@RequestMapping(value = "/upgradeSubscriptionComparison", method = RequestMethod.GET)
	public String upgradeSubscriptionComparison(@RequestParam(value = "subscriptionId") final String subscriptionId,
			final Model model) throws CMSItemNotFoundException
	{
		try
		{
			// initially leave upgradePreviewData empty because it's filled dynamically in method viewPotentialUpgradeBillingDetails
			final List<SubscriptionBillingData> upgradePreviewData = new ArrayList<SubscriptionBillingData>();
			model.addAttribute("upgradePreviewData", upgradePreviewData);

			final SubscriptionData subscriptionDetails = subscriptionFacade.getSubscription(subscriptionId);
			final ProductData subscriptionProductData = getProductForSubscription(subscriptionDetails);
			model.addAttribute("subscriptionProductData", subscriptionProductData);

			final List<ProductData> upsellingOptions = subscriptionFacade.getUpsellingOptionsForSubscription(subscriptionDetails
					.getProductCode());
			model.addAttribute("upsellingOptions", upsellingOptions);

			final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
			breadcrumbs.add(new Breadcrumb("/my-account/subscriptions", getMessageSource().getMessage("text.account.subscriptions",
					null, getI18nService().getCurrentLocale()), null));
			breadcrumbs.add(new Breadcrumb("#", getMessageSource().getMessage("text.account.subscription.subscriptionBreadcrumb",
					new Object[]
					{ subscriptionDetails.getId() }, "Subscription {0}", getI18nService().getCurrentLocale()), null));
			model.addAttribute("breadcrumbs", breadcrumbs);
			model.addAttribute("subscriptionData", subscriptionDetails);

			final CartData cartData = cartFacade.getSessionCart();
			model.addAttribute("cartData", cartData);
		}
		catch (final UnknownIdentifierException | SubscriptionFacadeException e)
		{
			LOG.warn("Attempted to load a subscription that does not exist or is not visible", e);
			return REDIRECT_MY_ACCOUNT;
		}
		storeCmsPageInModel(model, getContentPageForLabelOrId(SUBSCRIPTION_DETAILS_CMS_PAGE));
		model.addAttribute("metaRobots", "no-index,no-follow");
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(SUBSCRIPTION_DETAILS_CMS_PAGE));

		return ControllerConstants.Views.Pages.Account.AccountUpgradeSubscriptionComparisonPage;
	}

	@RequestMapping(value = "/viewPotentialUpgradeBillingDetails", method = RequestMethod.GET)
	public String viewPotentialUpgradeBillingDetails(@RequestParam(value = "subscriptionId") final String subscriptionId,
			@RequestParam(value = "upgradeId") final String upgradeId, final Model model) throws CMSItemNotFoundException
	{
		try
		{
			final List<SubscriptionBillingData> upgradePreviewData = subscriptionFacade.getUpgradePreviewBillings(subscriptionId,
					upgradeId);
			model.addAttribute("upgradePreviewData", upgradePreviewData);
			model.addAttribute("tabId", upgradeId);

			final SubscriptionData subscriptionDetails = subscriptionFacade.getSubscription(subscriptionId);
			model.addAttribute("subscriptionData", subscriptionDetails);

			final OrderData subscriptionOrderData = orderFacade.getOrderDetailsForCode(subscriptionDetails.getOrderNumber());
			ProductData subscriptionProductData = null;

			for (final OrderEntryData orderEntry : subscriptionOrderData.getEntries())
			{
				if (orderEntry.getEntryNumber().intValue() == subscriptionDetails.getOrderEntryNumber().intValue())
				{
					subscriptionProductData = orderEntry.getProduct();
					model.addAttribute("subProdData", subscriptionProductData);
					break;
				}
			}

			final List<ProductOption> productOptions = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE);
			final ProductData upgradeData = productFacade.getProductForCodeAndOptions(upgradeId, productOptions);
			model.addAttribute("upgradeData", upgradeData);
		}
		catch (final SubscriptionFacadeException e)
		{
			GlobalMessages.addErrorMessage(model, "form.global.error");
			return REDIRECT_MY_ACCOUNT;
		}

		return ControllerConstants.Views.Pages.Account.AccountUpgradeSubscriptionPotentialBillingDetails;
	}

	protected ProductData getProductForSubscription(final SubscriptionData subscriptionDetails)
	{
		final OrderData subscriptionOrderData = orderFacade.getOrderDetailsForCode(subscriptionDetails.getOrderNumber());
		final OrderData orderData = orderFacade.getOrderDetailsForCode(subscriptionOrderData.getCode());
		ProductData productData = null;

		for (final OrderEntryData orderEntry : orderData.getEntries())
		{
			if (orderEntry.getEntryNumber().intValue() == subscriptionDetails.getOrderEntryNumber().intValue())
			{
				productData = orderEntry.getProduct();
				return productData;
			}
		}

		return null;
	}

	@RequestMapping(value = "/update-email", method = RequestMethod.GET)
	public String editEmail(final Model model) throws CMSItemNotFoundException
	{
		final CustomerData customerData = customerFacade.getCurrentCustomer();
		final UpdateEmailForm updateEmailForm = new UpdateEmailForm();

		updateEmailForm.setEmail(customerData.getDisplayUid());

		model.addAttribute("updateEmailForm", updateEmailForm);

		storeCmsPageInModel(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.profile"));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.Account.AccountProfileEmailEditPage;
	}

	@RequestMapping(value = "/update-email", method = RequestMethod.POST)
	public String updateEmail(@Valid final UpdateEmailForm updateEmailForm, final BindingResult bindingResult, final Model model,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		String returnAction = REDIRECT_TO_PROFILE_PAGE;

		if (!updateEmailForm.getEmail().equals(updateEmailForm.getChkEmail()))
		{
			bindingResult.rejectValue("chkEmail", "validation.checkEmail.equals", new Object[] {}, "validation.checkEmail.equals");
		}

		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "form.global.error");
			storeCmsPageInModel(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
			model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.profile"));
			returnAction = ControllerConstants.Views.Pages.Account.AccountProfileEmailEditPage;
		}
		else
		{
			try
			{
				customerFacade.changeUid(updateEmailForm.getEmail().toLowerCase(), updateEmailForm.getPassword());

				//temporary solution to set oryginal UID - with new version of commerceservices it will not be necessary
				final CustomerData customerData = customerFacade.getCurrentCustomer();
				customerData.setDisplayUid(updateEmailForm.getEmail());
				customerFacade.updateProfile(customerData);
				//end of temporary solution

				redirectAttributes.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
						Collections.singletonList("text.account.profile.confirmationUpdated"));

				// Replace the spring security authentication with the new UID
				final String newUid = customerFacade.getCurrentCustomer().getUid().toLowerCase();
				final Authentication oldAuthentication = SecurityContextHolder.getContext().getAuthentication();
				final UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(newUid, null,
						oldAuthentication.getAuthorities());
				newAuthentication.setDetails(oldAuthentication.getDetails());
				SecurityContextHolder.getContext().setAuthentication(newAuthentication);
			}
			catch (final DuplicateUidException e)
			{
				redirectAttributes.addFlashAttribute(GlobalMessages.INFO_MESSAGES_HOLDER,
						Collections.singletonList("text.account.profile.emailNotChanged"));
			}
			catch (final PasswordMismatchException passwordMismatchException)
			{
				bindingResult.rejectValue("email", "profile.currentPassword.invalid");
				GlobalMessages.addErrorMessage(model, "form.global.error");
				storeCmsPageInModel(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
				setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
				model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.profile"));
				returnAction = ControllerConstants.Views.Pages.Account.AccountProfileEmailEditPage;
			}
		}

		return returnAction;
	}

	@RequestMapping(value = "/update-profile", method = RequestMethod.GET)
	public String editProfile(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute("titleData", userFacade.getTitles());

		final CustomerData customerData = customerFacade.getCurrentCustomer();
		final UpdateProfileForm updateProfileForm = new UpdateProfileForm();

		updateProfileForm.setTitleCode(customerData.getTitleCode());
		updateProfileForm.setFirstName(customerData.getFirstName());
		updateProfileForm.setLastName(customerData.getLastName());

		model.addAttribute("updateProfileForm", updateProfileForm);

		storeCmsPageInModel(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));

		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.profile"));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.Account.AccountProfileEditPage;
	}


	@RequestMapping(value = "/update-profile", method = RequestMethod.POST)
	public String updateProfile(@Valid final UpdateProfileForm updateProfileForm, final BindingResult bindingResult,
			final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		String returnAction = ControllerConstants.Views.Pages.Account.AccountProfileEditPage;
		final CustomerData currentCustomerData = customerFacade.getCurrentCustomer();
		final CustomerData customerData = new CustomerData();
		customerData.setTitleCode(updateProfileForm.getTitleCode());
		customerData.setFirstName(updateProfileForm.getFirstName());
		customerData.setLastName(updateProfileForm.getLastName());
		customerData.setUid(currentCustomerData.getUid());
		customerData.setDisplayUid(currentCustomerData.getDisplayUid());

		model.addAttribute("titleData", userFacade.getTitles());

		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "form.global.error");
		}
		else
		{
			try
			{
				customerFacade.updateProfile(customerData);
				redirectAttributes.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
						Collections.singletonList("text.account.profile.confirmationUpdated"));
				returnAction = REDIRECT_TO_PROFILE_PAGE;
			}
			catch (final DuplicateUidException e)
			{
				bindingResult.rejectValue("email", "registration.error.account.exists.title");
				GlobalMessages.addErrorMessage(model, "form.global.error");
			}
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.profile"));
		return returnAction;
	}

	@RequestMapping(value = "/update-password", method = RequestMethod.GET)
	public String updatePassword(final Model model) throws CMSItemNotFoundException
	{
		final UpdatePasswordForm updatePasswordForm = new UpdatePasswordForm();

		model.addAttribute("updatePasswordForm", updatePasswordForm);

		storeCmsPageInModel(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));

		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.profile.updatePasswordForm"));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.Account.AccountChangePasswordPage;
	}

	@RequestMapping(value = "/update-password", method = RequestMethod.POST)
	public String updatePassword(@Valid final UpdatePasswordForm updatePasswordForm, final BindingResult bindingResult,
			final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		if (updatePasswordForm.getNewPassword().equals(updatePasswordForm.getCheckNewPassword()))
		{
			try
			{
				customerFacade.changePassword(updatePasswordForm.getCurrentPassword(), updatePasswordForm.getNewPassword());
			}
			catch (final PasswordMismatchException localException)
			{
				bindingResult.rejectValue("currentPassword", "profile.currentPassword.invalid", new Object[] {},
						"profile.currentPassword.invalid");
			}
		}
		else
		{
			bindingResult.rejectValue("checkNewPassword", "validation.checkPwd.equals", new Object[] {},
					"validation.checkPwd.equals");
		}

		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "form.global.error");
			storeCmsPageInModel(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));

			model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.profile.updatePasswordForm"));
			return ControllerConstants.Views.Pages.Account.AccountChangePasswordPage;
		}
		else
		{
			redirectAttributes.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
					Collections.singletonList("text.account.confirmation.password.updated"));
			return REDIRECT_TO_PROFILE_PAGE;
		}
	}

	@RequestMapping(value = "/address-book", method = RequestMethod.GET)
	public String getAddressBook(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute("addressData", userFacade.getAddressBook());

		storeCmsPageInModel(model, getContentPageForLabelOrId(ADDRESS_BOOK_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADDRESS_BOOK_CMS_PAGE));
		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.addressBook"));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.Account.AccountAddressBookPage;
	}

	@RequestMapping(value = "/add-address", method = RequestMethod.GET)
	public String addAddress(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute("countryData", checkoutFacade.getDeliveryCountries());
		model.addAttribute("titleData", userFacade.getTitles());
		model.addAttribute("addressForm", new AddressForm());
		model.addAttribute("firstAddress", userFacade.isAddressBookEmpty());

		storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));

		final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
		breadcrumbs.add(new Breadcrumb("/my-account/address-book", getMessageSource().getMessage("text.account.addressBook", null,
				getI18nService().getCurrentLocale()), null));
		breadcrumbs.add(new Breadcrumb("#", getMessageSource().getMessage("text.account.addressBook.addEditAddress", null,
				getI18nService().getCurrentLocale()), null));
		model.addAttribute("breadcrumbs", breadcrumbs);
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.Account.AccountEditAddressPage;
	}

	@RequestMapping(value = "/add-address", method = RequestMethod.POST)
	public String addAddress(@Valid final AddressForm addressForm, final BindingResult bindingResult, final Model model,
			final HttpServletRequest request) throws CMSItemNotFoundException
	{
		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "form.global.error");
			storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			model.addAttribute("countryData", checkoutFacade.getDeliveryCountries());
			model.addAttribute("titleData", userFacade.getTitles());
			return ControllerConstants.Views.Pages.Account.AccountEditAddressPage;
		}

		final AddressData newAddress = new AddressData();
		newAddress.setTitleCode(addressForm.getTitleCode());
		newAddress.setFirstName(addressForm.getFirstName());
		newAddress.setLastName(addressForm.getLastName());
		newAddress.setLine1(addressForm.getLine1());
		newAddress.setLine2(addressForm.getLine2());
		newAddress.setTown(addressForm.getTownCity());
		newAddress.setPostalCode(addressForm.getPostcode());
		newAddress.setBillingAddress(false);
		newAddress.setShippingAddress(true);
		final CountryData countryData = new CountryData();
		countryData.setIsocode(addressForm.getCountryIso());
		newAddress.setCountry(countryData);
		newAddress.setVisibleInAddressBook(addressForm.getSaveInAddressBook().booleanValue());

		if (userFacade.isAddressBookEmpty())
		{
			newAddress.setDefaultAddress(true);
			checkoutFacade.setDeliveryAddress(newAddress);
		}
		else
		{
			newAddress.setDefaultAddress(addressForm.getDefaultAddress().booleanValue());
		}
		userFacade.addAddress(newAddress);
		final Map<String, Object> currentFlashScope = RequestContextUtils.getOutputFlashMap(request);
		currentFlashScope.put(GlobalMessages.CONF_MESSAGES_HOLDER, Collections.singletonList("account.confirmation.address.added"));

		return REDIRECT_TO_ADDRESS_BOOK_PAGE;
	}

	@RequestMapping(value = "/edit-address/" + ADDRESS_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String editAddress(@PathVariable("addressCode") final String addressCode, final Model model)
			throws CMSItemNotFoundException
	{
		final AddressForm addressForm = new AddressForm();
		model.addAttribute("countryData", checkoutFacade.getDeliveryCountries());
		model.addAttribute("titleData", userFacade.getTitles());
		model.addAttribute("addressForm", addressForm);

		for (final AddressData addressData : userFacade.getAddressBook())
		{
			if (addressData.getId() != null && addressData.getId().equals(addressCode))
			{
				model.addAttribute("addressData", addressData);
				addressForm.setAddressId(addressData.getId());
				addressForm.setTitleCode(addressData.getTitleCode());
				addressForm.setFirstName(addressData.getFirstName());
				addressForm.setLastName(addressData.getLastName());
				addressForm.setLine1(addressData.getLine1());
				addressForm.setLine2(addressData.getLine2());
				addressForm.setTownCity(addressData.getTown());
				addressForm.setPostcode(addressData.getPostalCode());
				addressForm.setCountryIso(addressData.getCountry().getIsocode());
				if (userFacade.getDefaultAddress() != null && userFacade.getDefaultAddress().getId() != null
						&& userFacade.getDefaultAddress().getId().equals(addressData.getId()))
				{
					addressForm.setDefaultAddress(Boolean.TRUE);
					checkoutFacade.setDeliveryAddress(addressData);
				}
				break;
			}
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));

		final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
		breadcrumbs.add(new Breadcrumb("/my-account/address-book", getMessageSource().getMessage("text.account.addressBook", null,
				getI18nService().getCurrentLocale()), null));
		breadcrumbs.add(new Breadcrumb("#", getMessageSource().getMessage("text.account.addressBook.addEditAddress", null,
				getI18nService().getCurrentLocale()), null));
		model.addAttribute("breadcrumbs", breadcrumbs);
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.Account.AccountEditAddressPage;
	}

	@RequestMapping(value = "/edit-address/" + ADDRESS_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.POST)
	public String editAddress(@Valid final AddressForm addressForm, final BindingResult bindingResult, final Model model,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		model.addAttribute("metaRobots", "no-index,no-follow");
		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "form.global.error");
			storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			model.addAttribute("countryData", checkoutFacade.getDeliveryCountries());
			model.addAttribute("titleData", userFacade.getTitles());
			return ControllerConstants.Views.Pages.Account.AccountEditAddressPage;
		}

		final AddressData newAddress = new AddressData();
		newAddress.setId(addressForm.getAddressId());
		newAddress.setTitleCode(addressForm.getTitleCode());
		newAddress.setFirstName(addressForm.getFirstName());
		newAddress.setLastName(addressForm.getLastName());
		newAddress.setLine1(addressForm.getLine1());
		newAddress.setLine2(addressForm.getLine2());
		newAddress.setTown(addressForm.getTownCity());
		newAddress.setPostalCode(addressForm.getPostcode());
		newAddress.setBillingAddress(false);
		newAddress.setShippingAddress(true);
		newAddress.setVisibleInAddressBook(true);
		final CountryData countryData = new CountryData();
		countryData.setIsocode(addressForm.getCountryIso());
		newAddress.setCountry(countryData);
		if (Boolean.TRUE.equals(addressForm.getDefaultAddress()))
		{
			newAddress.setDefaultAddress(true);
			checkoutFacade.setDeliveryAddress(newAddress);
		}
		userFacade.editAddress(newAddress);

		redirectModel.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
				Collections.singletonList("account.confirmation.address.updated"));
		return REDIRECT_TO_ADDRESS_BOOK_PAGE;
	}

	@RequestMapping(value = "/remove-address/" + ADDRESS_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String removeAddress(@PathVariable("addressCode") final String addressCode, final RedirectAttributes redirectModel)
	{
		final AddressData addressData = new AddressData();
		addressData.setId(addressCode);
		userFacade.removeAddress(addressData);

		redirectModel.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
				Collections.singletonList("account.confirmation.address.removed"));
		return REDIRECT_TO_ADDRESS_BOOK_PAGE;
	}

	@RequestMapping(value = "/set-default-address/" + ADDRESS_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String setDefaultAddress(@PathVariable("addressCode") final String addressCode, final RedirectAttributes redirectModel)
	{
		final AddressData addressData = new AddressData();
		addressData.setDefaultAddress(true);
		addressData.setId(addressCode);
		userFacade.setDefaultAddress(addressData);
		checkoutFacade.setDeliveryAddress(addressData);
		redirectModel.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
				Collections.singletonList("account.confirmation.default.address.changed"));
		return REDIRECT_TO_ADDRESS_BOOK_PAGE;
	}

	@RequestMapping(value = "/payment-details", method = RequestMethod.GET)
	public String paymentDetails(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute("customerData", customerFacade.getCurrentCustomer());

		final List<CCPaymentInfoData> ccPaymentInfos = userFacade.getCCPaymentInfos(true);

		if (CollectionUtils.isNotEmpty(ccPaymentInfos))
		{
			for (int index = 0; index < ccPaymentInfos.size(); index++)
			{
				final CCPaymentInfoData ccPaymentInfoData = ccPaymentInfos.get(index);

				if (ccPaymentInfoData.isDefaultPaymentInfo())
				{
					final CCPaymentInfoData temp = ccPaymentInfos.get(0);
					ccPaymentInfos.set(0, ccPaymentInfoData);
					ccPaymentInfos.set(index, temp);
					break;
				}
			}

			model.addAttribute("paymentInfoData", ccPaymentInfos);

			try
			{
				final Collection<SubscriptionData> allSubscriptions = subscriptionFacade.getSubscriptions();
				final HashMap<String, Integer> subscriptionsPerPaymentMethod = new HashMap<String, Integer>();

				for (final CCPaymentInfoData ccPaymentInfoData : ccPaymentInfos)
				{
					subscriptionsPerPaymentMethod.put(ccPaymentInfoData.getId(), Integer.valueOf(0));

					for (final SubscriptionData subscriptionData : allSubscriptions)
					{
						if (StringUtils.equals(subscriptionData.getPaymentMethodId(), ccPaymentInfoData.getSubscriptionId())
								&& !SubscriptionStatus.CANCELLED.getCode().equalsIgnoreCase(subscriptionData.getSubscriptionStatus()))
						{
							Integer amount = Integer.valueOf(1);
							if (subscriptionsPerPaymentMethod.containsKey(ccPaymentInfoData.getId()))
							{
								amount = Integer.valueOf(subscriptionsPerPaymentMethod.get(ccPaymentInfoData.getId()).intValue() + 1);
							}

							subscriptionsPerPaymentMethod.put(ccPaymentInfoData.getId(), amount);
						}
					}
				}

				model.addAttribute("subscriptionsPerPaymentMethod", subscriptionsPerPaymentMethod);
			}
			catch (final SubscriptionFacadeException e)
			{
				e.printStackTrace();
			}

		}
		else
		{
			model.addAttribute("paymentInfoData", Collections.EMPTY_LIST);
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(PAYMENT_DETAILS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.paymentDetails"));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.Account.AccountPaymentInfoPage;
	}

	@RequestMapping(value = "/set-default-payment-details", method = RequestMethod.POST)
	public String setDefaultPaymentDetails(@RequestParam final String paymentInfoId)
	{
		CCPaymentInfoData paymentInfoData = null;
		if (StringUtils.isNotBlank(paymentInfoId))
		{
			paymentInfoData = userFacade.getCCPaymentInfoForCode(paymentInfoId);
		}
		userFacade.setDefaultPaymentInfo(paymentInfoData);
		return REDIRECT_TO_PAYMENT_INFO_PAGE;
	}

	@RequestMapping(value = "/payment-method-subscriptions", method = RequestMethod.GET)
	public String viewPaymentMethodSubscriptions(@RequestParam(value = "paymentInfoId") final String paymentMethodId,
			final Model model) throws CMSItemNotFoundException
	{
		if (StringUtils.isBlank(paymentMethodId))
		{
			LOG.warn("Payment method id may not be blank.");
			return REDIRECT_TO_PAYMENT_INFO_PAGE;
		}

		final CCPaymentInfoData ccPaymentInfoData = userFacade.getCCPaymentInfoForCode(paymentMethodId);
		model.addAttribute("paymentInfo", ccPaymentInfoData);

		try
		{
			final List<SubscriptionData> paymentSubscriptions = getNonCancelledSubscriptionsForPayment(ccPaymentInfoData
					.getSubscriptionId());

			if (paymentSubscriptions.size() > 0)
			{
				model.addAttribute("subscriptions", paymentSubscriptions);
				return ControllerConstants.Views.Pages.Account.AccountPaymentMethodSubscriptionsPage;
			}

		}
		catch (final SubscriptionFacadeException e)
		{
			LOG.error("Error while retrieving subscriptions", e);
		}

		return REDIRECT_TO_PAYMENTMETHOD_EDIT_PAGE;
	}

	@RequestMapping(value = "/check-remove-payment-method", method = RequestMethod.GET)
	public String checkRemovePaymentMethod(@RequestParam(value = "paymentInfoId") final String paymentMethodId, final Model model)
			throws CMSItemNotFoundException
	{
		if (StringUtils.isBlank(paymentMethodId))
		{
			LOG.warn("Payment method id may not be blank.");
			return REDIRECT_TO_PAYMENT_INFO_PAGE;
		}

		final CCPaymentInfoData ccPaymentInfoData = userFacade.getCCPaymentInfoForCode(paymentMethodId);

		if (null == ccPaymentInfoData)
		{
			GlobalMessages.addErrorMessage(model, "text.account.paymentDetails.nonExisting.error");

			return ControllerConstants.Views.Pages.Payment.PaymentNotFound;
		}

		model.addAttribute("paymentInfo", ccPaymentInfoData);

		try
		{
			final List<SubscriptionData> paymentSubscriptions = getNonCancelledSubscriptionsForPayment(ccPaymentInfoData
					.getSubscriptionId());

			if (paymentSubscriptions.size() == 0)
			{
				return ControllerConstants.Views.Pages.Payment.ConfirmDeletePaymentMethodNoSubscriptions;
			}

			GlobalMessages.addErrorMessage(model, "text.account.paymentDetails.warningExistingSubscription.remove");
			model.addAttribute("subscriptions", paymentSubscriptions);
		}
		catch (final SubscriptionFacadeException e)
		{
			LOG.error(String.format("Removing payment method with id %s failed", paymentMethodId), e);
		}

		return ControllerConstants.Views.Pages.Payment.WarningExistingPaymentSubscriptions;
	}

	@RequestMapping(value = "/manage-payment-method", method = RequestMethod.GET)
	public String managePaymentMethod(@RequestParam(value = "paymentInfoId") final String paymentMethodId, final Model model,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		if (StringUtils.isBlank(paymentMethodId))
		{
			LOG.warn("Payment method id may not be blank.");
			return REDIRECT_TO_PAYMENT_INFO_PAGE;
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(PAYMENT_DETAILS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.paymentDetails"));
		model.addAttribute("metaRobots", "no-index,no-follow");

		final CCPaymentInfoData ccPaymentInfoData = userFacade.getCCPaymentInfoForCode(paymentMethodId);
		if (null == ccPaymentInfoData)
		{
			redirectAttributes.addFlashAttribute(GlobalMessages.ERROR_MESSAGES_HOLDER,
					Collections.singletonList("text.account.paymentDetails.nonExisting.error"));
		}
		else
		{
			model.addAttribute("paymentInfo", ccPaymentInfoData);

			try
			{
				final List<SubscriptionData> paymentSubscriptions = getNonCancelledSubscriptionsForPayment(ccPaymentInfoData
						.getSubscriptionId());

				final List<CCPaymentInfoData> paymentInfoDatas = userFacade.getCCPaymentInfos(true);
				final List<SelectOption> paymentMethods = new ArrayList<SelectOption>();

				for (final CCPaymentInfoData data : paymentInfoDatas)
				{
					if (!StringUtils.equals(paymentMethodId, data.getId()))
					{
						final StringBuffer paymentInfoString = new StringBuffer();
						paymentInfoString.append(data.getCardTypeData().getName() + ", "
								+ data.getCardNumber().replace("****", "**** ") + ", Exp: " + data.getExpiryMonth() + " / "
								+ data.getExpiryYear());
						paymentMethods.add(new SelectOption(data.getId(), paymentInfoString.toString()));
					}
				}

				model.addAttribute("paymentMethods", paymentMethods);
				model.addAttribute("subscriptions", paymentSubscriptions);
				model.addAttribute("paymentSubscriptionsForm", setupPaymentSubscriptionForm(paymentSubscriptions));

				return ControllerConstants.Views.Pages.Payment.RemoveAndReplacePaymentMethod;
			}
			catch (final SubscriptionFacadeException e)
			{
				LOG.error(String.format("Managing payment method with id %s failed", paymentMethodId), e);
			}
		}

		return REDIRECT_TO_PAYMENT_INFO_PAGE;
	}


	/**
	 * Fills up the form used for managing subscriptions
	 * 
	 * @param paymentSubscriptions
	 * @return the filled form
	 */
	private PaymentSubscriptionsForm setupPaymentSubscriptionForm(final List<SubscriptionData> paymentSubscriptions)
	{
		final PaymentSubscriptionsForm paymentSubscriptionsForm = new PaymentSubscriptionsForm();

		if (paymentSubscriptions != null)
		{
			final List<String> subscriptionIds = new ArrayList<String>();
			for (final SubscriptionData subscriptionData : paymentSubscriptions)
			{
				subscriptionIds.add(subscriptionData.getId());
			}

			paymentSubscriptionsForm.setSubscriptionsToChange(subscriptionIds);
		}

		return paymentSubscriptionsForm;
	}

	private List<SubscriptionData> getNonCancelledSubscriptionsForPayment(final String paymentMethodId)
			throws SubscriptionFacadeException
	{
		final List<SubscriptionData> subscriptionList = subscriptionFacade.getSubscriptionsForPaymentMethod(paymentMethodId);
		final List<SubscriptionData> nonCanclledsubscriptionList = new ArrayList<SubscriptionData>();

		for (final SubscriptionData subscription : subscriptionList)
		{
			if (!SubscriptionStatus.CANCELLED.getCode().equalsIgnoreCase(subscription.getSubscriptionStatus()))
			{
				nonCanclledsubscriptionList.add(subscription);
			}
		}

		return nonCanclledsubscriptionList;
	}


	@RequestMapping(value = "/cancelsubscription/" + SUBSCRIPTION_ID_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String cancelsubscription(@PathVariable(value = "subscriptionId") final String subscriptionId,
			final RedirectAttributes redirectAttributes)
	{
		try
		{
			subscriptionFacade.updateSubscription(subscriptionId, true, SubscriptionUpdateActionEnum.CANCEL, null);
		}
		catch (final SubscriptionFacadeException e)
		{
			redirectAttributes.addFlashAttribute(GlobalMessages.ERROR_MESSAGES_HOLDER,
					Collections.singletonList("text.account.subscription.cancel.unable"));
			LOG.error("Unable to cancel subscription", e);
		}

		return REDIRECT_MY_ACCOUNT_SUBSCRIPTION + subscriptionId;
	}

	@RequestMapping(value = "/upgradesubscription", method = RequestMethod.POST)
	public String upgradesubscription(@RequestParam(value = "productCode", required = true) final String productCode,
			@RequestParam(value = "subscriptionId", required = true) final String subscriptionId, final Model model,
			@RequestParam(value = "originalOrderCode", required = true) final String originalOrderCode,
			@RequestParam(value = "originalEntryNumber", required = true) final int originalEntryNumber)
	{
		CartModificationData cartModification;

		try
		{
			cartModification = cartFacade.addToCart(productCode, subscriptionId, originalOrderCode, originalEntryNumber);
			model.addAttribute("modifiedCartData", Collections.singletonList(cartModification));

			if (cartModification.getQuantityAdded() == 0L)
			{
				GlobalMessages.addErrorMessage(model, "basket.information.quantity.noItemsAdded." + cartModification.getStatusCode());
				model.addAttribute("errorMsg", "basket.information.quantity.noItemsAdded." + cartModification.getStatusCode());
			}
			else if (cartModification.getQuantityAdded() < 1)
			{
				GlobalMessages.addErrorMessage(model,
						"basket.information.quantity.reducedNumberOfItemsAdded." + cartModification.getStatusCode());
				model.addAttribute("errorMsg",
						"basket.information.quantity.reducedNumberOfItemsAdded." + cartModification.getStatusCode());
			}

			final CartData cartData = cartFacade.getSessionCart();
			model.addAttribute("cartData", cartData);
		}
		catch (final CommerceCartModificationException e)
		{
			model.addAttribute("errorMsg", "basket.error.occurred");
			GlobalMessages.addErrorMessage(model, "basket.information.quantity.noItemsAdded. " + e.getMessage());
			LOG.warn("Couldn't upgrade subscription '" + subscriptionId + "' to product '" + productCode + "'. AddToCart failed.", e);
		}

		return REDIRECT_URL_CART;
	}

	@RequestMapping(value = "/setAutorenewalStatus", method = RequestMethod.POST)
	public String setAutorenewalStatus(@RequestParam(value = "autorenew", required = true) final boolean autorenew,
			@RequestParam(value = "subscriptionId", required = true) final String subscriptionId,
			final RedirectAttributes redirectAttributes)
	{
		try
		{
			subscriptionFacade.updateSubscriptionAutorenewal(subscriptionId, autorenew, MapUtils.EMPTY_MAP);
			redirectAttributes.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
					Collections.singletonList("text.account.subscription.changeAutorenew.success"));
		}
		catch (final SubscriptionFacadeException e)
		{
			redirectAttributes.addFlashAttribute(GlobalMessages.ERROR_MESSAGES_HOLDER,
					Collections.singletonList("text.account.subscription.changeAutorenew.unable"));
			LOG.error(String.format("Unable to change auto-renew status to '%s' for subscription '%s'", String.valueOf(autorenew),
					subscriptionId), e);
		}

		return REDIRECT_MY_ACCOUNT_SUBSCRIPTION + subscriptionId;
	}

	@RequestMapping(value = "/changeSubscriptionState", method = RequestMethod.POST)
	public String changeSubscriptionState(@RequestParam(value = "newState", required = true) final String newState,
			@RequestParam(value = "subscriptionId", required = true) final String subscriptionId,
			final RedirectAttributes redirectAttributes)
	{
		try
		{
			subscriptionFacade.changeSubscriptionState(subscriptionId, newState, MapUtils.EMPTY_MAP);
			redirectAttributes.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
					Collections.singletonList("text.account.subscription.changeState.success"));
		}
		catch (final SubscriptionFacadeException e)
		{
			redirectAttributes.addFlashAttribute(GlobalMessages.ERROR_MESSAGES_HOLDER,
					Collections.singletonList("text.account.subscription.changeState.unable"));
			LOG.error(String.format("Unable to change state to '%s' for subscription '%s'", newState, subscriptionId), e);
		}

		return REDIRECT_MY_ACCOUNT_SUBSCRIPTION + subscriptionId;
	}

	@RequestMapping(value = "/extendSubscriptionTermDuration", method = RequestMethod.POST)
	public String extendSubscriptionTermDuration(
			@RequestParam(value = "contractDurationExtension", required = true) final Integer contractDurationExtension,
			@RequestParam(value = "subscriptionId", required = true) final String subscriptionId,
			final RedirectAttributes redirectAttributes)
	{
		try
		{
			subscriptionFacade.extendSubscriptionTermDuration(subscriptionId, contractDurationExtension, MapUtils.EMPTY_MAP);
			redirectAttributes.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
					Collections.singletonList("text.account.subscription.extendTerm.success"));
		}
		catch (final SubscriptionFacadeException e)
		{
			redirectAttributes.addFlashAttribute(GlobalMessages.ERROR_MESSAGES_HOLDER,
					Collections.singletonList("text.account.subscription.extendTerm.unable"));
			LOG.error(String.format("Unable to extend term duration by '%s' for subscription '%s'", contractDurationExtension,
					subscriptionId), e);
		}

		return REDIRECT_MY_ACCOUNT_SUBSCRIPTION + subscriptionId;
	}

	@RequestMapping(value = "/replaceSubscriptionPaymentMethod", method = RequestMethod.POST)
	public String replaceSubscriptionPaymentMethod(
			@RequestParam(value = "paymentMethodId", required = true) final String paymentMethodId,
			@RequestParam(value = "subscriptionId", required = true) final String subscriptionId,
			@RequestParam(value = "effectiveFrom", required = true) final String effectiveFrom,
			final RedirectAttributes redirectAttributes)
	{
		try
		{
			subscriptionFacade.replacePaymentMethod(subscriptionId, paymentMethodId, effectiveFrom, MapUtils.EMPTY_MAP);
			redirectAttributes.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
					Collections.singletonList("text.account.subscription.replacePaymentMethod.success"));
		}
		catch (final SubscriptionFacadeException e)
		{
			redirectAttributes.addFlashAttribute(GlobalMessages.ERROR_MESSAGES_HOLDER,
					Collections.singletonList("text.account.subscription.replacePaymentMethod.unable"));
			LOG.error(String.format("Unable to replace payment method for subscription '%s'", subscriptionId), e);
		}

		return REDIRECT_MY_ACCOUNT_SUBSCRIPTION + subscriptionId;
	}

	@RequestMapping(value = "/subscriptionBillingActivity", method = RequestMethod.GET)
	public String viewSubscriptionBillingActivity(
			@RequestParam(value = "subscriptionId", required = true) final String subscriptionId, final Model model)
			throws CMSItemNotFoundException
	{
		try
		{
			// retrieve subscription
			final SubscriptionData subscriptionData = subscriptionFacade.getSubscription(subscriptionId);
			model.addAttribute("subscriptionData", subscriptionData);

			// create breadcrumbs
			final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
			breadcrumbs.add(new Breadcrumb("/my-account/subscriptions", getMessageSource().getMessage("text.account.subscriptions",
					null, getI18nService().getCurrentLocale()), null));
			breadcrumbs.add(new Breadcrumb("/my-account/subscription/" + subscriptionData.getId(), getMessageSource().getMessage(
					"text.account.subscription.subscriptionBreadcrumb", new Object[]
					{ subscriptionData.getId() }, "Subscription {0}", getI18nService().getCurrentLocale()), null));
			model.addAttribute("breadcrumbs", breadcrumbs);

			// retrieve and sort billing activities
			final List<SubscriptionBillingData> billingActivities = subscriptionFacade.getBillingActivityList(subscriptionId,
					MapUtils.EMPTY_MAP);

			Collections.sort(billingActivities, new Comparator<SubscriptionBillingData>()
			{
				@Override
				public int compare(final SubscriptionBillingData billingData1, final SubscriptionBillingData billingData2)
				{
					final DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
					try
					{
						final Date date1 = dateFormat.parse(billingData1.getBillingDate());
						final Date date2 = dateFormat.parse(billingData2.getBillingDate());
						return -date1.compareTo(date2);
					}
					catch (final ParseException e)
					{
						LOG.warn("Unable to parse billing date. Billing activities will probably not be sorted properly", e);
						return 1;
					}
				}
			});

			model.addAttribute("billingActivities", billingActivities);
		}
		catch (final SubscriptionFacadeException e)
		{
			LOG.warn("Viewing billing activities for subscriptions failed. Returning to subscription details page.", e);
			return REDIRECT_MY_ACCOUNT_SUBSCRIPTION + subscriptionId;
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(SUBSCRIPTION_BILLING_ACTIVITY_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(SUBSCRIPTION_BILLING_ACTIVITY_CMS_PAGE));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.Account.AccountSubscriptionBillingActivityPage;
	}

	@RequestMapping(value = "/downloadSubscriptionBillingActivityDetail", method = RequestMethod.GET)
	public void downloadSubscriptionBillingActivityDetail(
			@RequestParam(value = "billingActivityId", required = true) final String billingActivityId,
			final HttpServletResponse response)
	{
		try
		{
			final SubscriptionBillingDetailFileStream fileStream = subscriptionFacade.getBillingActivityDetail(billingActivityId,
					MapUtils.EMPTY_MAP);

			response.setContentType(fileStream.getMimeType());
			response.setHeader("Content-Disposition", "attachment; filename=" + fileStream.getFileName());

			IOUtils.copy(fileStream.getInputStream(), response.getOutputStream());
			response.flushBuffer();
		}
		catch (final SubscriptionFacadeException | IOException e)
		{
			LOG.warn(String.format("Download of details for billing activity with id %s failed.", billingActivityId), e);
		}

	}
}
