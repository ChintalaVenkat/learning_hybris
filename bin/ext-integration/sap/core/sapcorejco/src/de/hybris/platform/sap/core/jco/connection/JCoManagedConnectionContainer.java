/*
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
package de.hybris.platform.sap.core.jco.connection;


/**
 * Container for managed JCO connections. <br>
 */
public interface JCoManagedConnectionContainer
{

	/**
	 * Getter for managed connection. <br>
	 * Throws BackendRuntimeException in case of managed connection with given name cannot be constructed.
	 * 
	 * @param connectionName
	 *           name of the connection.
	 * @return managed connection.
	 */
	public JCoConnection getManagedConnection(String connectionName);

}
