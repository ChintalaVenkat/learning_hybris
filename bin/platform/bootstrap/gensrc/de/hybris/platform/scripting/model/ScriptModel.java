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
package de.hybris.platform.scripting.model;

import de.hybris.platform.core.model.AbstractDynamicContentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.scripting.enums.ScriptType;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type Script first defined at extension scripting.
 */
@SuppressWarnings("all")
public class ScriptModel extends AbstractDynamicContentModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Script";
	
	/** <i>Generated constant</i> - Attribute key of <code>Script.scriptType</code> attribute defined at extension <code>scripting</code>. */
	public static final String SCRIPTTYPE = "scriptType";
	
	/** <i>Generated constant</i> - Attribute key of <code>Script.autodisabling</code> attribute defined at extension <code>scripting</code>. */
	public static final String AUTODISABLING = "autodisabling";
	
	/** <i>Generated constant</i> - Attribute key of <code>Script.disabled</code> attribute defined at extension <code>scripting</code>. */
	public static final String DISABLED = "disabled";
	
	/** <i>Generated constant</i> - Attribute key of <code>Script.description</code> attribute defined at extension <code>scripting</code>. */
	public static final String DESCRIPTION = "description";
	
	
	/** <i>Generated variable</i> - Variable of <code>Script.scriptType</code> attribute defined at extension <code>scripting</code>. */
	private ScriptType _scriptType;
	
	/** <i>Generated variable</i> - Variable of <code>Script.autodisabling</code> attribute defined at extension <code>scripting</code>. */
	private Boolean _autodisabling;
	
	/** <i>Generated variable</i> - Variable of <code>Script.disabled</code> attribute defined at extension <code>scripting</code>. */
	private Boolean _disabled;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ScriptModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ScriptModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractDynamicContent</code> at extension <code>core</code>
	 * @param _content initial attribute declared by type <code>AbstractDynamicContent</code> at extension <code>core</code>
	 */
	@Deprecated
	public ScriptModel(final String _code, final String _content)
	{
		super();
		setCode(_code);
		setContent(_content);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractDynamicContent</code> at extension <code>core</code>
	 * @param _content initial attribute declared by type <code>AbstractDynamicContent</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ScriptModel(final String _code, final String _content, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setContent(_content);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Script.description</code> attribute defined at extension <code>scripting</code>. 
	 * @return the description
	 */
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Script.description</code> attribute defined at extension <code>scripting</code>. 
	 * @param loc the value localization key 
	 * @return the description
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Script.scriptType</code> attribute defined at extension <code>scripting</code>. 
	 * @return the scriptType
	 */
	public ScriptType getScriptType()
	{
		if (this._scriptType!=null)
		{
			return _scriptType;
		}
		return _scriptType = getPersistenceContext().getValue(SCRIPTTYPE, _scriptType);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Script.autodisabling</code> attribute defined at extension <code>scripting</code>. 
	 * @return the autodisabling
	 */
	public boolean isAutodisabling()
	{
		return toPrimitive( _autodisabling = getPersistenceContext().getValue(AUTODISABLING, _autodisabling));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Script.disabled</code> attribute defined at extension <code>scripting</code>. 
	 * @return the disabled
	 */
	public boolean isDisabled()
	{
		return toPrimitive( _disabled = getPersistenceContext().getValue(DISABLED, _disabled));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Script.autodisabling</code> attribute defined at extension <code>scripting</code>. 
	 *  
	 * @param value the autodisabling
	 */
	public void setAutodisabling(final boolean value)
	{
		_autodisabling = getPersistenceContext().setValue(AUTODISABLING, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Script.description</code> attribute defined at extension <code>scripting</code>. 
	 *  
	 * @param value the description
	 */
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Script.description</code> attribute defined at extension <code>scripting</code>. 
	 *  
	 * @param value the description
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Script.disabled</code> attribute defined at extension <code>scripting</code>. 
	 *  
	 * @param value the disabled
	 */
	public void setDisabled(final boolean value)
	{
		_disabled = getPersistenceContext().setValue(DISABLED, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Script.scriptType</code> attribute defined at extension <code>scripting</code>. 
	 *  
	 * @param value the scriptType
	 */
	public void setScriptType(final ScriptType value)
	{
		_scriptType = getPersistenceContext().setValue(SCRIPTTYPE, value);
	}
	
}
