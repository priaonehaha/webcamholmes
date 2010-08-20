/**
 * Copyright (C) 2010 Alfredo Morresi
 * 
 * This file is part of SmsForFree project.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/>.
 */
package it.rainbowbreeze.webcamholmes.domain;

/**
 * Base interface for elements to display in the main list
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public abstract class ItemToDisplay
{
	//---------- Private fields




	//---------- Public properties
	/** The item has children **/
	public abstract boolean hasChildren();

	/** The name of the item */
	public abstract String getName();

	/** The Id of the item */
	public abstract String getId();


	//---------- Public methods
	@Override
	public String toString() {
		return getName();
	}



	//---------- Private methods

	
}
