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




	//---------- Constructor
	public ItemToDisplay(long id) {
		this(id, 0);
	}

	public ItemToDisplay(long id, long parentId) {
		this(id, parentId, null);
	}

	public ItemToDisplay(long id, String name) {
		this(id, 0, name);
	}

	public ItemToDisplay(long id, long parentId, String name) {
		mId = id;
		mParentId = parentId;
		mName = name;
	}




	//---------- Public properties
	/** The item has children **/
	public abstract boolean hasChildren();

	/** The Id of the item */
	protected final long mId;
	public long getId()
	{ return mId; }

	/** The name of the item */
	protected String mName;
	public String getName()
	{ return mName; }
	public void setName(String newValue)
	{ mName = newValue; }

	/** The Id of the item */
	protected final long mParentId;
	public long getParentId()
	{ return mParentId; }




	//---------- Public methods
	@Override
	public String toString() {
		return getName();
	}



	//---------- Private methods

	
}
