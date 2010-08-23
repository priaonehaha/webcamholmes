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
 * A category in the list
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 */
public class ItemCategory
	extends ItemToDisplay
{
	//---------- Private fields

	
	
	
	//---------- Constructor
	public ItemCategory(long id, long parentId, String name) {
		super(id, parentId, name);
	}





	//---------- Public properties
	public boolean hasChildren()
	{ return true; }


	
	
	//---------- Events

	
	
	
	//---------- Public methods

	
	
	
	//---------- Private methods
}
