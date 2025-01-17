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
import de.hybris.platform.cms2.model.pages.AbstractPageModel;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Simple CMS Content Page controller. Used only to preview CMS Pages. The DefaultPageController is used to serve
 * generic content pages.
 */
@Controller
@RequestMapping(value = "/preview-content")
public class PreviewContentPageController extends AbstractPageController
{
	@RequestMapping(method = RequestMethod.GET, params = { "uid" })
	public String get(@RequestParam(value = "uid") final String cmsPageUid, final Model model) throws CMSItemNotFoundException
	{
		final AbstractPageModel pageForRequest = getCmsPageService().getPageForId(cmsPageUid);
		storeCmsPageInModel(model, getCmsPageService().getPageForId(cmsPageUid));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(cmsPageUid));
		return getViewForPage(pageForRequest);
	}
}
