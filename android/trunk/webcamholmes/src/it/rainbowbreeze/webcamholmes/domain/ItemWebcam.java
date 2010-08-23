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

	public ItemWebcam(long id, long parentId, String name, String imageUrl, int reloadInterval) {
		super(id, parentId, name);
		mImageUrl = imageUrl;
		mReloadInterval = reloadInterval;
	}
	
	public ItemWebcam(long id, long parentId, String name, String imageUrl, int reloadInterval, boolean preferred) {
		super(id, parentId, name);
		mImageUrl = imageUrl;
		mReloadInterval = reloadInterval;
		mPreferred = preferred;
	}
	
	
	//---------- Private fields

	
	
	
	//---------- Public properties
	@Override
	public boolean hasChildren()
	{ return false; }

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

	protected boolean mPreferred;
	public boolean getPreferred()
	{ return mPreferred; }
	public void setPreferred(boolean newValue)
	{ mPreferred = newValue; }


	
	
	//---------- Public methods

	
	
	
	//---------- Private methods
}
