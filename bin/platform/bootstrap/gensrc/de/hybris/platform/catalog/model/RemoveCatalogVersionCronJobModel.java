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

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type RemoveCatalogVersionCronJob first defined at extension catalog.
 */
@SuppressWarnings("all")
public class RemoveCatalogVersionCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "RemoveCatalogVersionCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>RemoveCatalogVersionCronJob.dontRemoveObjects</code> attribute defined at extension <code>catalog</code>. */
	public static final String DONTREMOVEOBJECTS = "dontRemoveObjects";
	
	/** <i>Generated constant</i> - Attribute key of <code>RemoveCatalogVersionCronJob.currentProcessingItemCount</code> attribute defined at extension <code>catalog</code>. */
	public static final String CURRENTPROCESSINGITEMCOUNT = "currentProcessingItemCount";
	
	/** <i>Generated constant</i> - Attribute key of <code>RemoveCatalogVersionCronJob.notRemovedItems</code> attribute defined at extension <code>catalog</code>. */
	public static final String NOTREMOVEDITEMS = "notRemovedItems";
	
	/** <i>Generated constant</i> - Attribute key of <code>RemoveCatalogVersionCronJob.totalDeleteItemCount</code> attribute defined at extension <code>catalog</code>. */
	public static final String TOTALDELETEITEMCOUNT = "totalDeleteItemCount";
	
	/** <i>Generated constant</i> - Attribute key of <code>RemoveCatalogVersionCronJob.catalogVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>RemoveCatalogVersionCronJob.catalog</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATALOG = "catalog";
	
	
	/** <i>Generated variable</i> - Variable of <code>RemoveCatalogVersionCronJob.dontRemoveObjects</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _dontRemoveObjects;
	
	/** <i>Generated variable</i> - Variable of <code>RemoveCatalogVersionCronJob.currentProcessingItemCount</code> attribute defined at extension <code>catalog</code>. */
	private Integer _currentProcessingItemCount;
	
	/** <i>Generated variable</i> - Variable of <code>RemoveCatalogVersionCronJob.notRemovedItems</code> attribute defined at extension <code>catalog</code>. */
	private ImpExMediaModel _notRemovedItems;
	
	/** <i>Generated variable</i> - Variable of <code>RemoveCatalogVersionCronJob.totalDeleteItemCount</code> attribute defined at extension <code>catalog</code>. */
	private Integer _totalDeleteItemCount;
	
	/** <i>Generated variable</i> - Variable of <code>RemoveCatalogVersionCronJob.catalogVersion</code> attribute defined at extension <code>catalog</code>. */
	private CatalogVersionModel _catalogVersion;
	
	/** <i>Generated variable</i> - Variable of <code>RemoveCatalogVersionCronJob.catalog</code> attribute defined at extension <code>catalog</code>. */
	private CatalogModel _catalog;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public RemoveCatalogVersionCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public RemoveCatalogVersionCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalog initial attribute declared by type <code>RemoveCatalogVersionCronJob</code> at extension <code>catalog</code>
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public RemoveCatalogVersionCronJobModel(final CatalogModel _catalog, final JobModel _job)
	{
		super();
		setCatalog(_catalog);
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalog initial attribute declared by type <code>RemoveCatalogVersionCronJob</code> at extension <code>catalog</code>
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public RemoveCatalogVersionCronJobModel(final CatalogModel _catalog, final JobModel _job, final ItemModel _owner)
	{
		super();
		setCatalog(_catalog);
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RemoveCatalogVersionCronJob.catalog</code> attribute defined at extension <code>catalog</code>. 
	 * @return the catalog
	 */
	public CatalogModel getCatalog()
	{
		if (this._catalog!=null)
		{
			return _catalog;
		}
		return _catalog = getPersistenceContext().getValue(CATALOG, _catalog);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RemoveCatalogVersionCronJob.catalogVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the catalogVersion
	 */
	public CatalogVersionModel getCatalogVersion()
	{
		if (this._catalogVersion!=null)
		{
			return _catalogVersion;
		}
		return _catalogVersion = getPersistenceContext().getValue(CATALOGVERSION, _catalogVersion);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RemoveCatalogVersionCronJob.currentProcessingItemCount</code> attribute defined at extension <code>catalog</code>. 
	 * @return the currentProcessingItemCount - Current processed(removed) item instance
	 */
	public Integer getCurrentProcessingItemCount()
	{
		if (this._currentProcessingItemCount!=null)
		{
			return _currentProcessingItemCount;
		}
		return _currentProcessingItemCount = getPersistenceContext().getValue(CURRENTPROCESSINGITEMCOUNT, _currentProcessingItemCount);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RemoveCatalogVersionCronJob.dontRemoveObjects</code> attribute defined at extension <code>catalog</code>. 
	 * @return the dontRemoveObjects
	 */
	public Boolean getDontRemoveObjects()
	{
		if (this._dontRemoveObjects!=null)
		{
			return _dontRemoveObjects;
		}
		return _dontRemoveObjects = getPersistenceContext().getValue(DONTREMOVEOBJECTS, _dontRemoveObjects);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RemoveCatalogVersionCronJob.notRemovedItems</code> attribute defined at extension <code>catalog</code>. 
	 * @return the notRemovedItems
	 */
	public ImpExMediaModel getNotRemovedItems()
	{
		if (this._notRemovedItems!=null)
		{
			return _notRemovedItems;
		}
		return _notRemovedItems = getPersistenceContext().getValue(NOTREMOVEDITEMS, _notRemovedItems);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RemoveCatalogVersionCronJob.totalDeleteItemCount</code> attribute defined at extension <code>catalog</code>. 
	 * @return the totalDeleteItemCount - Predicted count of items to remove
	 */
	public Integer getTotalDeleteItemCount()
	{
		if (this._totalDeleteItemCount!=null)
		{
			return _totalDeleteItemCount;
		}
		return _totalDeleteItemCount = getPersistenceContext().getValue(TOTALDELETEITEMCOUNT, _totalDeleteItemCount);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RemoveCatalogVersionCronJob.catalog</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the catalog
	 */
	public void setCatalog(final CatalogModel value)
	{
		_catalog = getPersistenceContext().setValue(CATALOG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RemoveCatalogVersionCronJob.catalogVersion</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the catalogVersion
	 */
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		_catalogVersion = getPersistenceContext().setValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RemoveCatalogVersionCronJob.currentProcessingItemCount</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the currentProcessingItemCount - Current processed(removed) item instance
	 */
	public void setCurrentProcessingItemCount(final Integer value)
	{
		_currentProcessingItemCount = getPersistenceContext().setValue(CURRENTPROCESSINGITEMCOUNT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RemoveCatalogVersionCronJob.dontRemoveObjects</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the dontRemoveObjects
	 */
	public void setDontRemoveObjects(final Boolean value)
	{
		_dontRemoveObjects = getPersistenceContext().setValue(DONTREMOVEOBJECTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RemoveCatalogVersionCronJob.notRemovedItems</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the notRemovedItems
	 */
	public void setNotRemovedItems(final ImpExMediaModel value)
	{
		_notRemovedItems = getPersistenceContext().setValue(NOTREMOVEDITEMS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RemoveCatalogVersionCronJob.totalDeleteItemCount</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the totalDeleteItemCount - Predicted count of items to remove
	 */
	public void setTotalDeleteItemCount(final Integer value)
	{
		_totalDeleteItemCount = getPersistenceContext().setValue(TOTALDELETEITEMCOUNT, value);
	}
	
}
