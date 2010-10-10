/**
 * Copyright (C) 2010 Alfredo Morresi
 * 
 * This file is part of WebcamHolmes project.
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
 * This class it's a wrapper around domain model objects of
 * WebcamHolmes domain. If they changes, only changes to this
 * wrapper are needed, and no other classes of the project
 * must be modified.
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public class ItemWrapper {

	//---------- Private fields

	
	
	
	//---------- Constructor
	public ItemWrapper(ItemToDisplay item) {
		mItem = item;
	}

	
	
	
	//---------- Public properties
	private final ItemToDisplay mItem;
	public ItemToDisplay getItem()
	{ return mItem; }
	
	public boolean isWebcam() {
		return mItem instanceof ItemWebcam;
	}

	public boolean isCategory() {
		return mItem instanceof ItemCategory;
	}

	public long getParentAliasId() {
		return mItem.getParentAliasId();
	}

	public long getAliasId() {
		if (isWebcam()) return 0;
		return ((ItemCategory) mItem).getAliasId();
	}

	public void setParentAliasId(long newValue) {
		mItem.setParentAliasId(newValue);
	}
	
	


		
	
	//---------- Public methods

	
	
	
	//---------- Private methods

}
