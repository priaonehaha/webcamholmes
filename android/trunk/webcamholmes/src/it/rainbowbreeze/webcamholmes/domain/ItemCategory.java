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
	public ItemCategory(long id, long parentId, String name, boolean userCreated) {
		super(id, parentId, name);
		mUserCreated = userCreated;
	}





	//---------- Public properties
	public void setId(long newValue)
	{ mId = newValue; }
	
	public boolean hasChildren()
	{ return true; }

	protected final boolean mUserCreated;
	public boolean isUserCreated()
	{ return mUserCreated; }

	
	
	//---------- Public methods
	/** Factory class for creating categories */
	public static class Factory {
		/**
		 * Create a system category
		 * @param parentId
		 * @param name
		 * @return
		 */
		public static ItemCategory getSystemCategory(long parentId, String name) {
			return getSystemCategory(0, parentId, name);
		}

		/**
		 * Create a system category
		 * @param id
		 * @param parentId
		 * @param name
		 * @return
		 */
		public static ItemCategory getSystemCategory(long id, long parentId, String name) {
			return new ItemCategory(id, parentId, name, false);
		}
	}

	
	
	
	//---------- Private methods
}
