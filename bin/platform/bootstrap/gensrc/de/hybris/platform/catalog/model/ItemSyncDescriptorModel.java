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
package de.hybris.platform.catalog.model;

import de.hybris.platform.catalog.model.SyncItemCronJobModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.ChangeDescriptorModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.StepModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ItemSyncDescriptor first defined at extension catalog.
 */
@SuppressWarnings("all")
public class ItemSyncDescriptorModel extends ChangeDescriptorModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ItemSyncDescriptor";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncDescriptor.targetItem</code> attribute defined at extension <code>catalog</code>. */
	public static final String TARGETITEM = "targetItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncDescriptor.done</code> attribute defined at extension <code>catalog</code>. */
	public static final String DONE = "done";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncDescriptor.copiedImplicitely</code> attribute defined at extension <code>catalog</code>. */
	public static final String COPIEDIMPLICITELY = "copiedImplicitely";
	
	
	/** <i>Generated variable</i> - Variable of <code>ItemSyncDescriptor.targetItem</code> attribute defined at extension <code>catalog</code>. */
	private ItemModel _targetItem;
	
	/** <i>Generated variable</i> - Variable of <code>ItemSyncDescriptor.done</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _done;
	
	/** <i>Generated variable</i> - Variable of <code>ItemSyncDescriptor.copiedImplicitely</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _copiedImplicitely;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ItemSyncDescriptorModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ItemSyncDescriptorModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _changeType initial attribute declared by type <code>ChangeDescriptor</code> at extension <code>processing</code>
	 * @param _cronJob initial attribute declared by type <code>ItemSyncDescriptor</code> at extension <code>catalog</code>
	 * @param _sequenceNumber initial attribute declared by type <code>ChangeDescriptor</code> at extension <code>processing</code>
	 * @param _step initial attribute declared by type <code>ChangeDescriptor</code> at extension <code>processing</code>
	 */
	@Deprecated
	public ItemSyncDescriptorModel(final String _changeType, final SyncItemCronJobModel _cronJob, final Integer _sequenceNumber, final StepModel _step)
	{
		super();
		setChangeType(_changeType);
		setCronJob(_cronJob);
		setSequenceNumber(_sequenceNumber);
		setStep(_step);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _changeType initial attribute declared by type <code>ChangeDescriptor</code> at extension <code>processing</code>
	 * @param _cronJob initial attribute declared by type <code>ItemSyncDescriptor</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _sequenceNumber initial attribute declared by type <code>ChangeDescriptor</code> at extension <code>processing</code>
	 * @param _step initial attribute declared by type <code>ChangeDescriptor</code> at extension <code>processing</code>
	 */
	@Deprecated
	public ItemSyncDescriptorModel(final String _changeType, final SyncItemCronJobModel _cronJob, final ItemModel _owner, final Integer _sequenceNumber, final StepModel _step)
	{
		super();
		setChangeType(_changeType);
		setCronJob(_cronJob);
		setOwner(_owner);
		setSequenceNumber(_sequenceNumber);
		setStep(_step);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemSyncDescriptor.copiedImplicitely</code> attribute defined at extension <code>catalog</code>. 
	 * @return the copiedImplicitely
	 */
	public Boolean getCopiedImplicitely()
	{
		if (this._copiedImplicitely!=null)
		{
			return _copiedImplicitely;
		}
		return _copiedImplicitely = getPersistenceContext().getValue(COPIEDIMPLICITELY, _copiedImplicitely);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeDescriptor.cronJob</code> attribute defined at extension <code>processing</code> and redeclared at extension <code>catalog</code>. 
	 * @return the cronJob
	 */
	@Override
	public SyncItemCronJobModel getCronJob()
	{
		return (SyncItemCronJobModel) super.getCronJob();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemSyncDescriptor.done</code> attribute defined at extension <code>catalog</code>. 
	 * @return the done
	 */
	public Boolean getDone()
	{
		if (this._done!=null)
		{
			return _done;
		}
		return _done = getPersistenceContext().getValue(DONE, _done);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemSyncDescriptor.targetItem</code> attribute defined at extension <code>catalog</code>. 
	 * @return the targetItem
	 */
	public ItemModel getTargetItem()
	{
		if (this._targetItem!=null)
		{
			return _targetItem;
		}
		return _targetItem = getPersistenceContext().getValue(TARGETITEM, _targetItem);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemSyncDescriptor.copiedImplicitely</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the copiedImplicitely
	 */
	public void setCopiedImplicitely(final Boolean value)
	{
		_copiedImplicitely = getPersistenceContext().setValue(COPIEDIMPLICITELY, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ChangeDescriptor.cronJob</code> attribute defined at extension <code>processing</code> and redeclared at extension <code>catalog</code>. Can only be used at creation of model - before first save. Will only accept values of type {@link de.hybris.platform.catalog.model.SyncItemCronJobModel}.  
	 *  
	 * @param value the cronJob
	 */
	@Override
	public void setCronJob(final CronJobModel value)
	{
		super.setCronJob(value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemSyncDescriptor.done</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the done
	 */
	public void setDone(final Boolean value)
	{
		_done = getPersistenceContext().setValue(DONE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemSyncDescriptor.targetItem</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the targetItem
	 */
	public void setTargetItem(final ItemModel value)
	{
		_targetItem = getPersistenceContext().setValue(TARGETITEM, value);
	}
	
}
