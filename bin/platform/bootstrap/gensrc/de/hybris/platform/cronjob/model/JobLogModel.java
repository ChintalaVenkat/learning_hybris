/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Dec 5, 2014 7:51:06 PM                      ---
 * ----------------------------------------------------------------
 *  
 * [y] hybris Platform
 *  
 * Copyright (c) 2000-2011 hybris AG
 * All rights reserved.
 *  
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *  
 */
package de.hybris.platform.cronjob.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.enums.JobLogLevel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.StepModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type JobLog first defined at extension processing.
 */
@SuppressWarnings("all")
public class JobLogModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "JobLog";
	
	/**<i>Generated relation code constant for relation <code>CronJobJobLogsRelation</code> defining source attribute <code>cronJob</code> in extension <code>processing</code>.</i>*/
	public final static String _CRONJOBJOBLOGSRELATION = "CronJobJobLogsRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>JobLog.level</code> attribute defined at extension <code>processing</code>. */
	public static final String LEVEL = "level";
	
	/** <i>Generated constant</i> - Attribute key of <code>JobLog.message</code> attribute defined at extension <code>processing</code>. */
	public static final String MESSAGE = "message";
	
	/** <i>Generated constant</i> - Attribute key of <code>JobLog.cronJob</code> attribute defined at extension <code>processing</code>. */
	public static final String CRONJOB = "cronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>JobLog.step</code> attribute defined at extension <code>processing</code>. */
	public static final String STEP = "step";
	
	/** <i>Generated constant</i> - Attribute key of <code>JobLog.shortMessage</code> attribute defined at extension <code>processing</code>. */
	public static final String SHORTMESSAGE = "shortMessage";
	
	
	/** <i>Generated variable</i> - Variable of <code>JobLog.level</code> attribute defined at extension <code>processing</code>. */
	private JobLogLevel _level;
	
	/** <i>Generated variable</i> - Variable of <code>JobLog.message</code> attribute defined at extension <code>processing</code>. */
	private String _message;
	
	/** <i>Generated variable</i> - Variable of <code>JobLog.cronJob</code> attribute defined at extension <code>processing</code>. */
	private CronJobModel _cronJob;
	
	/** <i>Generated variable</i> - Variable of <code>JobLog.step</code> attribute defined at extension <code>processing</code>. */
	private StepModel _step;
	
	/** <i>Generated variable</i> - Variable of <code>JobLog.shortMessage</code> attribute defined at extension <code>processing</code>. */
	private String _shortMessage;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public JobLogModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public JobLogModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _cronJob initial attribute declared by type <code>JobLog</code> at extension <code>processing</code>
	 * @param _level initial attribute declared by type <code>JobLog</code> at extension <code>processing</code>
	 */
	@Deprecated
	public JobLogModel(final CronJobModel _cronJob, final JobLogLevel _level)
	{
		super();
		setCronJob(_cronJob);
		setLevel(_level);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _cronJob initial attribute declared by type <code>JobLog</code> at extension <code>processing</code>
	 * @param _level initial attribute declared by type <code>JobLog</code> at extension <code>processing</code>
	 * @param _message initial attribute declared by type <code>JobLog</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _step initial attribute declared by type <code>JobLog</code> at extension <code>processing</code>
	 */
	@Deprecated
	public JobLogModel(final CronJobModel _cronJob, final JobLogLevel _level, final String _message, final ItemModel _owner, final StepModel _step)
	{
		super();
		setCronJob(_cronJob);
		setLevel(_level);
		setMessage(_message);
		setOwner(_owner);
		setStep(_step);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JobLog.cronJob</code> attribute defined at extension <code>processing</code>. 
	 * @return the cronJob - assigned CronJob
	 */
	public CronJobModel getCronJob()
	{
		if (this._cronJob!=null)
		{
			return _cronJob;
		}
		return _cronJob = getPersistenceContext().getValue(CRONJOB, _cronJob);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JobLog.level</code> attribute defined at extension <code>processing</code>. 
	 * @return the level
	 */
	public JobLogLevel getLevel()
	{
		if (this._level!=null)
		{
			return _level;
		}
		return _level = getPersistenceContext().getValue(LEVEL, _level);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JobLog.message</code> attribute defined at extension <code>processing</code>. 
	 * @return the message
	 */
	public String getMessage()
	{
		if (this._message!=null)
		{
			return _message;
		}
		return _message = getPersistenceContext().getValue(MESSAGE, _message);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JobLog.shortMessage</code> attribute defined at extension <code>processing</code>. 
	 * @return the shortMessage
	 */
	public String getShortMessage()
	{
		if (this._shortMessage!=null)
		{
			return _shortMessage;
		}
		return _shortMessage = getPersistenceContext().getValue(SHORTMESSAGE, _shortMessage);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JobLog.step</code> attribute defined at extension <code>processing</code>. 
	 * @return the step
	 */
	public StepModel getStep()
	{
		if (this._step!=null)
		{
			return _step;
		}
		return _step = getPersistenceContext().getValue(STEP, _step);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>JobLog.cronJob</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the cronJob - assigned CronJob
	 */
	public void setCronJob(final CronJobModel value)
	{
		_cronJob = getPersistenceContext().setValue(CRONJOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>JobLog.level</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the level
	 */
	public void setLevel(final JobLogLevel value)
	{
		_level = getPersistenceContext().setValue(LEVEL, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>JobLog.message</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the message
	 */
	public void setMessage(final String value)
	{
		_message = getPersistenceContext().setValue(MESSAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>JobLog.step</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the step
	 */
	public void setStep(final StepModel value)
	{
		_step = getPersistenceContext().setValue(STEP, value);
	}
	
}
