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
 * A webcam to display in the list
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 */
public class ItemWebcam
	extends ItemToDisplay
{
	//---------- Constructor
	public ItemWebcam() {
	}

	public ItemWebcam(String id, String name, String imageUrl) {
		mId = id;
		mName = name;
		mImageUrl = imageUrl;
	}
	
	
	//---------- Private fields

	
	
	
	//---------- Public properties
	@Override
	public boolean hasChildren() {
		return false;
	}

	protected String mName;
	@Override
	public String getName()
	{ return mName; }
	public void setName(String newValue)
	{ mName = newValue; }

	protected String mId;
	@Override
	public String getId()
	{ return mId; }

	protected String mImageUrl;
	public String getImageUrl()
	{ return mImageUrl; }
	public void setImageUrl(String newValue)
	{ mImageUrl = newValue; }

	protected int mReloadInterval;
	public int getReloadInterval()
	{ return mReloadInterval; }
	public void setReloadInterval(int newValue)
	{ mReloadInterval = newValue; }


	
	
	//---------- Public methods

	
	
	
	//---------- Private methods
}
