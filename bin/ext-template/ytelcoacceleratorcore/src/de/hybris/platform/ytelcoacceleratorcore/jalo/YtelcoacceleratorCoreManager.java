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
package de.hybris.platform.ytelcoacceleratorcore.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.ytelcoacceleratorcore.constants.YtelcoacceleratorCoreConstants;
import de.hybris.platform.ytelcoacceleratorcore.setup.CoreSystemSetup;



/**
 * Do not use, please use {@link CoreSystemSetup} instead.
 * 
 */
@SuppressWarnings("PMD")
public class YtelcoacceleratorCoreManager extends GeneratedYtelcoacceleratorCoreManager
{
	public static final YtelcoacceleratorCoreManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (YtelcoacceleratorCoreManager) em.getExtension(YtelcoacceleratorCoreConstants.EXTENSIONNAME);
	}
}
