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
		this(id, parentId, name, webcamType, imageUrl, reloadInterval, preferred, userCreated, "", "", "");
	}
	
	public ItemWebcam(
			long id,
			long parentId,
			String name,
			int webcamType,
			String imageUrl,
			int reloadInterval,
			boolean preferred,
			boolean userCreated,
			String freeData1,
			String freeData2,
			String freeData3) {
		super(id, parentId, name);
		mType = webcamType;
		mImageUrl = imageUrl;
		mReloadInterval = reloadInterval;
		mPreferred = preferred;
		mUserCreated = userCreated;
		mFreeData1 = freeData1;
		mFreeData2 = freeData2;
		mFreeData3 = freeData3;
	}
	
	
	//---------- Public properties
	public static final int TYPE_NORMAL = 1;
	public static final int TYPE_WEBCAMTRAVEL = 2;
	
	
	
	public void setId(long newValue)
	{ mId = newValue; }
	
	@Override
	public boolean hasChildren()
	{ return false; }

	/**
	 * Type of webcam
	 * 1 - normal, display the image in the url
	 * 2 - webcam.traverl webcam, display additional credits
	 */
	protected final int mType;
	public int getType()
	{ return mType; }

	protected final String mImageUrl;
	public String getImageUrl()
	{ return mImageUrl; }

	protected final int mReloadInterval;
	public int getReloadInterval()
	{ return mReloadInterval; }
	
	protected boolean mPreferred;
	public boolean isPreferred()
	{ return mPreferred; }
	public void setPreferred(boolean newValue)
	{ mPreferred = newValue; }

	protected final boolean mUserCreated;
	public boolean isUserCreated()
	{ return mUserCreated; }

	protected final String mFreeData1;
	public String getFreeData1()
	{ return mFreeData1; }

	protected final String mFreeData2;
	public String getFreeData2()
	{ return mFreeData2; }

	protected final String mFreeData3;
	public String getFreeData3()
	{ return mFreeData3; }




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
			return new ItemWebcam(0, parentId, name, 1, imageUrl, reloadInterval, false, false);
		}

		/**
		 * Create a user webcam
		 * @param parentId
		 * @param name
		 * @param imageUrl
		 * @param reloadInterval
		 * @return
		 */
		public static ItemWebcam getUserWebcam(long parentId, String name, String imageUrl, int reloadInterval) {
			return new ItemWebcam(0, parentId, name, 1, imageUrl, reloadInterval, false, true);
		}
		
		
		/**
		 * Create a webcam.travel webcam
		 * @param parentId
		 * @param name
		 * @param imageUrl
		 * @param reloadInterval
		 * @return
		 */
		public static ItemWebcam getWebcamTravelWebcam(long parentId, String name, String imageUrl, String userName, String userAreaUrl, String webcamOnWebcamTravelUrl) {
			return new ItemWebcam(0, parentId, name, 2, imageUrl, 30, false, false, userName, userAreaUrl, webcamOnWebcamTravelUrl);
		}
	}

	
	
	
	//---------- Private methods
}
