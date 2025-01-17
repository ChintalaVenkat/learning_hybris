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
package de.hybris.platform.yb2bacceleratorstorefront.security;

import de.hybris.platform.core.Constants;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.spring.security.CoreAuthenticationProvider;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Derived authentication provider supporting additional authentication checks. See
 * {@link de.hybris.platform.spring.security.RejectUserPreAuthenticationChecks}.
 *
 * <ul>
 * <li>prevent login without password for users created via CSCockpit</li>
 * <li>prevent login as user in group admingroup</li>
 * <li>prevent login as user if not authorised for B2B</li>
 * <li>prevent login as user if not authorised for B2B</li>
 * </ul>
 *
 * any login as admin disables SearchRestrictions and therefore no page can be viewed correctly
 */
public class AcceleratorAuthenticationProvider extends CoreAuthenticationProvider
{
	private static final Logger LOG = Logger.getLogger(AcceleratorAuthenticationProvider.class);
	private static final String ROLE_ADMIN_GROUP = "ROLE_" + Constants.USER.ADMIN_USERGROUP.toUpperCase();

	private BruteForceAttackCounter bruteForceAttackCounter;
	private UserService userService;
	private ModelService modelService;
	private GrantedAuthority adminAuthority = new SimpleGrantedAuthority(ROLE_ADMIN_GROUP);
	private CartService cartService;

	private B2BUserGroupProvider b2bUserGroupProvider;


	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException
	{
		final String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
		if (getBruteForceAttackCounter().isAttack(username))
		{
			try
			{
				UserModel userModel = getUserService().getUserForUID(StringUtils.lowerCase(username));
				userModel.setLoginDisabled(true);
				getModelService().save(userModel);
				bruteForceAttackCounter.resetUserCounter(userModel.getUid());
			}
			catch (UnknownIdentifierException e)
			{
				LOG.warn("Brute force attack attempt for non existing user name " + username);
			}
			finally
			{
				throw new BadCredentialsException(messages.getMessage("CoreAuthenticationProvider.badCredentials", "Bad credentials"));
			}
		}

		// check if the user of the cart matches the current user and if the
		// user is not anonymous. If otherwise, remove delete the session cart as it might
		// be stolen / from another user
		String sessionCartUserId = getCartService().getSessionCart().getUser().getUid();

		if (!username.equals(sessionCartUserId) &&
				!sessionCartUserId.equals(userService.getAnonymousUser().getUid()))
		{
			getCartService().setSessionCart(null);
		}
		return super.authenticate(authentication);
	}

	/**
	 * @see de.hybris.platform.spring.security.CoreAuthenticationProvider#additionalAuthenticationChecks(org.springframework.security.core.userdetails.UserDetails,
	 *      org.springframework.security.authentication.AbstractAuthenticationToken)
	 */
	@Override
	protected void additionalAuthenticationChecks(final UserDetails details, final AbstractAuthenticationToken authentication)
			throws AuthenticationException
	{
		super.additionalAuthenticationChecks(details, authentication);

		// Check if user has supplied no password
		if (StringUtils.isEmpty((String) authentication.getCredentials()))
		{
			throw new BadCredentialsException("Login without password");
		}

		// Check if the user is in role admingroup
		if (getAdminAuthority() != null && details.getAuthorities().contains(getAdminAuthority()))
		{
			throw new LockedException("Login attempt as " + Constants.USER.ADMIN_USERGROUP + " is rejected");
		}

		// Check if the customer is B2B type
		if (!getB2bUserGroupProvider().isUserAuthorized(details.getUsername()))
		{
			throw new InsufficientAuthenticationException(messages.getMessage("checkout.error.invalid.accountType",
					"You are not allowed to login"));
		}

		if (!getB2bUserGroupProvider().isUserEnabled(details.getUsername()))
		{
			throw new DisabledException("User " + details.getUsername() + " is disabled... "
					+ messages.getMessage("text.company.manage.units.disabled"));
		}
	}

	/**
	 * @return the b2bUserGroupProvider
	 */
	protected B2BUserGroupProvider getB2bUserGroupProvider()
	{
		return b2bUserGroupProvider;
	}

	/**
	 * @param b2bUserGroupProvider the b2bUserGroupProvider to set
	 */
	public void setB2bUserGroupProvider(final B2BUserGroupProvider b2bUserGroupProvider)
	{
		this.b2bUserGroupProvider = b2bUserGroupProvider;
	}

	/**
	 * @param adminGroup the adminGroup to set
	 */
	public void setAdminGroup(final String adminGroup)
	{
		if (StringUtils.isBlank(adminGroup))
		{
			adminAuthority = null;
		}
		else
		{
			adminAuthority = new SimpleGrantedAuthority(adminGroup);
		}
	}

	protected GrantedAuthority getAdminAuthority()
	{
		return adminAuthority;
	}


	protected BruteForceAttackCounter getBruteForceAttackCounter()
	{
		return bruteForceAttackCounter;
	}

	@Required
	public void setBruteForceAttackCounter(BruteForceAttackCounter bruteForceAttackCounter)
	{
		this.bruteForceAttackCounter = bruteForceAttackCounter;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(ModelService modelService)
	{
		this.modelService = modelService;
	}

	public CartService getCartService()
	{
		return cartService;
	}

	public void setCartService(CartService cartService)
	{
		this.cartService = cartService;
	}
}
