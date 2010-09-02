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
	//---------- Private fields

	
	
	
	//---------- Constructor
	public ItemWebcam(long id, long parentId, String name, int webcamType, String imageUrl, int reloadInterval, boolean preferred, boolean userCreated) {
		super(id, parentId, name);
		mWebcamType = webcamType;
		mImageUrl = imageUrl;
		mReloadInterval = reloadInterval;
		mPreferred = preferred;
		mUserCreated = userCreated;
	}
	
	
	//---------- Public properties
	public void setId(long newValue)
	{ mId = newValue; }
	
	@Override
	public boolean hasChildren()
	{ return false; }

	protected final int mWebcamType;
	public int getType()
	{ return mWebcamType; }

	protected final String mImageUrl;
	public String getImageUrl()
	{ return mImageUrl; }

	protected final int mReloadInterval;
	public int getReloadInterval()
	{ return mReloadInterval; }
	
	protected boolean mPreferred;
	public boolean getPreferred()
	{ return mPreferred; }
	public void setPreferred(boolean newValue)
	{ mPreferred = newValue; }

	protected final boolean mUserCreated;
	public boolean isUserCreated()
	{ return mUserCreated; }
	



	//---------- Public methods
	/** Factory class for creating webcams */
	public static class Factory {
		/**
		 * Create a system webcam
		 * @param parentId
		 * @param name
		 * @param imageUrl
		 * @param reloadInterval
		 * @return
		 */
		public static ItemWebcam getSystemWebcam(long parentId, String name, String imageUrl, int reloadInterval) {
			return getSystemWebcam(0, parentId, name, imageUrl, reloadInterval);
		}

		/**
		 * Create a system webcam
		 * @param id
		 * @param parentId
		 * @param name
		 * @param imageUrl
		 * @param reloadInterval
		 * @return
		 */
		public static ItemWebcam getSystemWebcam(long id, long parentId, String name, String imageUrl, int reloadInterval) {
			return new ItemWebcam(id, parentId, name, 0, imageUrl, reloadInterval, false, false);
		}
	}

	
	
	
	//---------- Private methods
}
