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
package it.rainbowbreeze.webcamholmes.common;

import it.rainbowbreeze.libs.common.BaseResultOperation;
import it.rainbowbreeze.webcamholmes.common.ResultOperation;
import it.rainbowbreeze.webcamholmes.data.AppPreferencesDao;
import it.rainbowbreeze.webcamholmes.data.IImageUrlProvider;
import it.rainbowbreeze.webcamholmes.data.ImageUrlProvider;
import it.rainbowbreeze.webcamholmes.data.ItemsDao;
import it.rainbowbreeze.webcamholmes.logic.LogicManager;
import it.rainbowbreeze.webcamholmes.ui.ActivityHelper;

import android.app.Application;

/**
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public class App
	extends Application
{
	//---------- Constructor
	public App()
	{
		super();
		//this is the first instruction, so no fear that mInstance is null is following calls
		mInstance = this;
	}

	//---------- Private fields
	private ActivityHelper mActivityHelper;
	private ItemsDao mItemsDao;
	private AppPreferencesDao mAppPreferencesDao;
	private LogicManager mLogicManager;
	private LogFacility mLogFacility;

	private Class<? extends ImageUrlProvider> mImageUrlProvider;




	//---------- Public properties
	//singleton
    private static App mInstance;
    public static App i()
    { return mInstance; }

	/** the application was correctly initialized */
    private boolean mIsCorrectlyInitialized;
	public boolean isCorrectlyInitialized()
	{ return mIsCorrectlyInitialized; }


	
    
	//---------- Events
	@Override
	public void onCreate() {
		super.onCreate();

		//set the log tag
		mLogFacility = new LogFacility(GlobalDefs.LOG_TAG);
		mLogFacility.i("App started");
		
		//create services and helper respecting IoC dependencies
		mImageUrlProvider = ImageUrlProvider.class;
		mItemsDao = new ItemsDao(getApplicationContext());
		mActivityHelper = new ActivityHelper(mLogFacility, getApplicationContext());
		mAppPreferencesDao = new AppPreferencesDao(getApplicationContext(), GlobalDefs.APP_PREFERENCES_KEY);
		mLogicManager = new LogicManager(mLogFacility, mAppPreferencesDao, GlobalDefs.APP_PREFERENCES_KEY, mItemsDao);
		
		//execute begin task
		BaseResultOperation<Void> res = mLogicManager.executeBeginTask(this);
		if (res.hasErrors()) {
			mIsCorrectlyInitialized = false;
			mActivityHelper.reportError(this, res.getException(), res.getReturnCode());
		} else {
			mIsCorrectlyInitialized = true;
		}
	}

	@Override
	public void onTerminate() {
		//execute end tasks
		ResultOperation<Void> res = (ResultOperation<Void>) mLogicManager.executeEndTast(this);
		if (res.hasErrors()) {
			mActivityHelper.reportError(this, res.getException(), res.getReturnCode());
		}
		super.onTerminate();
	}
    
    
    
	//---------- Public methods

	/**
	 * Factory method to obtain a singleton instance of {@link ActivityHelper} object.
	 */
	public ActivityHelper getActivityHelper()
	{ return mActivityHelper; }
	
	/**
	 * Factory method to obtain a singleton instance of {@link ItemsDao} object.
	 */
	public ItemsDao getItemsDao()
	{ return mItemsDao; }

	
	/**
	 * Set a new {@link IImageUrlProvider} class to return
	 * when the getImageUrlProvider method is called.
	 * 
	 * @param newValue
	 */
	public void setMockImageUrlProvider(Class<? extends ImageUrlProvider> newValue)
	{
		mImageUrlProvider = newValue;
	}
	
	/**
	 * Factory method to obtain a {@link IImageUrlProvider} object.
	 * The object has one shot.
	 *  
	 * @return
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 */
	public IImageUrlProvider getImageUrlProvider()
		throws IllegalAccessException, InstantiationException
	{
		return mImageUrlProvider.newInstance();
	}
	
	/**
	 * Factory method to obtain a singleton instance of {@link AppPreferencesDao} object.
	 */
	public AppPreferencesDao getAppPreferencesDao()
	{ return mAppPreferencesDao; }
	
	/**
	 * Factory method to obtain a singleton instance of {@link LogicManager} object.
	 */
	public LogicManager getLogicManager()
	{ return mLogicManager; }
	
	/**
	 * Factory method to obtain a singleton instance of {@link LogFacility} object.
	 */
	public LogFacility getLogFacility()
	{ return mLogFacility; }
    
    
    
	//---------- Private methods
}
