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
package de.hybris.platform.yb2bacceleratorstorefront.forms;

import javax.validation.constraints.Size;


/**
 * Form object for updating the password.
 */
public class UpdatePasswordForm
{
	private String currentPassword;
	private String newPassword;
	private String checkNewPassword;

    @Size(min = 1, message = "{profile.currentPassword.invalid}")
	public String getCurrentPassword()
	{
		return currentPassword;
	}

	public void setCurrentPassword(final String currentPassword)
	{
		this.currentPassword = currentPassword;
	}

	@Size(min = 6, max = 255, message = "{updatePwd.pwd.invalid}")
	public String getNewPassword()
	{
		return newPassword;
	}

	public void setNewPassword(final String newPassword)
	{
		this.newPassword = newPassword;
	}

    @Size(min = 6, max = 255, message = "{updatePwd.pwd.invalid}")
	public String getCheckNewPassword()
	{
		return checkNewPassword;
	}

	public void setCheckNewPassword(final String checkNewPassword)
	{
		this.checkNewPassword = checkNewPassword;
	}
}
