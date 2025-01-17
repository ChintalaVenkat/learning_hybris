/*
 *
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
package de.hybris.platform.licence;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.licence.internal.HybrisLicenceValidator;
import de.hybris.platform.licence.internal.HybrisLicenceDAO;

import java.util.Date;
import java.util.Properties;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class HybrisLicenceValidatorTest
{
	public static final String TENANT_PREFIX = "junit_";
	private HybrisLicenceValidator validator;
	private HybrisLicenceValidator.HybrisDemoLicenceValidator demoValidator;
	@Mock
	private Licence licence;
	@Mock
	private HybrisLicenceDAO hybrisLicenceDAO;
	@Mock
	private HybrisDataSource dataSource;
	@Mock
	private Properties properties;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		given(Boolean.valueOf(licence.isDemoOrDevelopLicence())).willReturn(Boolean.TRUE);
		given(dataSource.getTablePrefix()).willReturn(TENANT_PREFIX);

		setupValidatorInstances();
	}

	private void setupValidatorInstances()
	{
		validator = new HybrisLicenceValidator();
		demoValidator = new HybrisLicenceValidator.HybrisDemoLicenceValidator(dataSource)
		{
			@Override
			protected HybrisLicenceDAO getHybrisLicenceDAO()
			{
				return hybrisLicenceDAO;
			}
		};
	}

	@Test
	public void shouldThrowNullPointerExceptionWhenCheckedLicenceIsNull() throws Exception
	{
		// given
		final Licence licence = null;

		try
		{
			// when
			validator.checkLicence(licence);
			fail("should throw NullPointerException");
		}
		catch (final NullPointerException e)
		{
			// then fine
		}
	}

	@Test
	public void shouldThrowIllegalStateExceptionWhenCheckedLicenceHasNotValidSignature() throws Exception
	{
		// given
		given(licence.getSignature()).willReturn("not-valid".getBytes());
		given(licence.getLicenceProperties()).willReturn(properties);

		try
		{
			// when
			validator.checkLicence(licence);
			fail("should throw IllegalStateException");
		}
		catch (final IllegalStateException e)
		{
			// then fine
		}
	}

	@Test
	public void shouldThrowIllegalStateExceptionLicenceHasExpired() throws Exception
	{
		// given
		given(licence.getSignature()).willReturn("not-valid".getBytes());
		given(licence.getLicenceProperties()).willReturn(properties);

		try
		{
			// when
			validator.checkLicence(licence);
			fail("should throw IllegalStateException");
		}
		catch (final IllegalStateException e)
		{
			// then fine
		}
	}

	@Test
	public void licenseShouldNotBeExpiredIfCurrentDateIsBeforeExpirationDate() throws Exception
	{
		// given
		final Date date = DateTime.now().minusDays(10).toDate();
		given(hybrisLicenceDAO.getStartingPointDateForPlatformInstance(TENANT_PREFIX, dataSource)).willReturn(date);

		// when
		final boolean expired = demoValidator.isLicenceExpiredIfDemoLicence(licence);

		// then
		assertThat(expired).isFalse();
	}

	@Test
	public void licenseShouldNotBeExpiredIfCurrentDateIsThirtyDaysAfterStartingPoint() throws Exception
	{
		// given
		final Date date = DateTime.now().minusDays(30).toDate();
		given(hybrisLicenceDAO.getStartingPointDateForPlatformInstance(TENANT_PREFIX, dataSource)).willReturn(date);

		// when
		final boolean expired = demoValidator.isLicenceExpiredIfDemoLicence(licence);

		// then
		assertThat(expired).isFalse();
	}

	@Test
	public void licenseShouldBeExpiredIfCurrentDateIsMoreThanThirtyDaysAfterStartingPoint() throws Exception
	{
		// given
		final Date date = DateTime.now().minusDays(31).toDate();
		given(hybrisLicenceDAO.getStartingPointDateForPlatformInstance(TENANT_PREFIX, dataSource)).willReturn(date);

		// when
		final boolean expired = demoValidator.isLicenceExpiredIfDemoLicence(licence);

		// then
		assertThat(expired).isTrue();
	}

	@Test
	public void shouldReturnMinusOneDayLeftIfStartingPointDateIs31DaysBackFromNow() throws Exception
	{
		// given
		final Date date = DateTime.now().minusDays(31).toDate();
		given(hybrisLicenceDAO.getStartingPointDateForPlatformInstance(TENANT_PREFIX, dataSource)).willReturn(date);

		// when
		final Integer daysLeft = demoValidator.getDaysLeft(licence);

		// then
		assertThat(daysLeft).isNotNull().isEqualTo(-1);
	}

	@Test
	public void shouldReturnZeroDaysLeftIfStartingPointDateIs30DaysBackFromNow() throws Exception
	{
		// given
		final Date date = DateTime.now().minusDays(30).toDate();
		given(hybrisLicenceDAO.getStartingPointDateForPlatformInstance(TENANT_PREFIX, dataSource)).willReturn(date);

		// when
		final Integer daysLeft = demoValidator.getDaysLeft(licence);

		// then
		assertThat(daysLeft).isNotNull().isEqualTo(0);
	}

	@Test
	public void shouldReturnOneDayLeftIfStartingPointDateIs29DaysBackFromNow() throws Exception
	{
		// given
		final Date date = DateTime.now().minusDays(29).toDate();
		given(hybrisLicenceDAO.getStartingPointDateForPlatformInstance(TENANT_PREFIX, dataSource)).willReturn(date);

		// when
		final Integer daysLeft = demoValidator.getDaysLeft(licence);

		// then
		assertThat(daysLeft).isNotNull().isEqualTo(1);
	}

	@Test
	public void shouldReturn30DaysLeftIfStartingPointDateIsNow() throws Exception
	{
		// given
		final Date date = DateTime.now().toDate();
		given(hybrisLicenceDAO.getStartingPointDateForPlatformInstance(TENANT_PREFIX, dataSource)).willReturn(date);

		// when
		final Integer daysLeft = demoValidator.getDaysLeft(licence);

		// then
		assertThat(daysLeft).isNotNull().isEqualTo(30);
	}

	@Test
	public void shouldReturnNullAsDaysLeftIfPassedLicenceIsNotDemoOrDevelop() throws Exception
	{
		// given
		given(Boolean.valueOf(licence.isDemoOrDevelopLicence())).willReturn(Boolean.FALSE);

		// when
		final Integer daysLeft = demoValidator.getDaysLeft(licence);

		// then
		assertThat(daysLeft).isNull();
	}
}
