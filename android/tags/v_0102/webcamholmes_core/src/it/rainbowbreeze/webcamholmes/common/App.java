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

import it.rainbowbreeze.libs.common.RainbowAppGlobalBag;
import it.rainbowbreeze.libs.common.RainbowResultOperation;
import it.rainbowbreeze.libs.common.RainbowServiceLocator;
import it.rainbowbreeze.libs.common.RainbowLogFacility;
import it.rainbowbreeze.libs.logic.RainbowCrashReporter;
import it.rainbowbreeze.libs.media.RainbowImageMediaHelper;
import it.rainbowbreeze.webcamholmes.R;
import it.rainbowbreeze.webcamholmes.data.AppPreferencesDao;
import it.rainbowbreeze.webcamholmes.data.IImageUrlProvider;
import it.rainbowbreeze.webcamholmes.data.ImageUrlProvider;
import it.rainbowbreeze.webcamholmes.data.ItemsDao;
import it.rainbowbreeze.webcamholmes.logic.LogicManager;
import it.rainbowbreeze.webcamholmes.ui.ActivityHelper;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static it.rainbowbreeze.libs.common.RainbowContractHelper.*;

/**
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public class App
	extends Application implements RainbowAppGlobalBag
{
	//---------- Constructor
	public App()
	{
		super();
		//this is the first instruction, so no fear that mInstance is null is following calls
		mInstance = this;
	}

	//---------- Private fields
	private Class<? extends ImageUrlProvider> mImageUrlProvider;




	//---------- Public properties
	//singleton
    private static App mInstance;
    public static App i()
    { return mInstance; }
    
	/** keys for application preferences */
	public final static String APP_PREFERENCES_KEY = "WebcamHolmesPrefs";

	/** Application name */
	public static String APP_DISPLAY_NAME = "WebcamHolmes";

	/** Application version displayed to the user (about activity etc) */
	public final static String APP_DISPLAY_VERSION = "1.2";

	/** Application name used during the ping of update site */
	public final static String APP_INTERNAL_NAME = "WebcamHolmes-Android";
    
	/** Application version for internal use (update, crash report etc) */
	public final static String APP_INTERNAL_VERSION = "01.02.00";

	/** address where send log */
	public final static String EMAIL_FOR_LOG = "webcamholmes@gmail.com";
	
	/** Tag to use in the log */
	public final static String LOG_TAG = "WebcamHolmes";

	/** url where send statistics about application */
	public final static String STATISTICS_WEBSERVER_URL = "http://www.rainbowbreeze.it/devel/getlatestversion.php";
	
	public final static String WEBCAM_IMAGE_DUMP_FILE = "webcamDump.png";
	

	/** the application was correctly initialized */
    private boolean mIsCorrectlyInitialized;
	public boolean isCorrectlyInitialized()
	{ return mIsCorrectlyInitialized; }
	public void setCorrectlyInitialized(boolean newValue)
	{ mIsCorrectlyInitialized = newValue; }

	/** First run after an update of the application */
	protected boolean mFirstRunAfterUpdate;
	public void setFirstRunAfterUpdate(boolean newValue)
	{ mFirstRunAfterUpdate = newValue; }
	public boolean isFirstRunAfterUpdate()
	{ return mFirstRunAfterUpdate; }



    
	//---------- Events
	@Override
	public void onCreate() {
		super.onCreate();
		
		setupEnvironment(getApplicationContext());
	}

	@Override
	public void onTerminate() {
		LogicManager logicManager = checkNotNull(RainbowServiceLocator.get(LogicManager.class), "LogicManager");
		//execute end tasks
		RainbowResultOperation<Void> res = logicManager.executeEndTasks(this);
		if (res.hasErrors()) {
			RainbowServiceLocator.get(ActivityHelper.class).reportError(this, res.getException(), res.getReturnCode());
		}

		
		//log the end of the application
		RainbowLogFacility logFacility = checkNotNull(RainbowServiceLocator.get(RainbowLogFacility.class), "LogFacility");
		logFacility.i("App ending: " + App.APP_INTERNAL_NAME);
		super.onTerminate();
	}
    
    
    
	//---------- Public methods
	
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
	 * The error bitmap to display when webcam image isn't available
	 * @return
	 */
	protected Bitmap mFailBitmap;
	public Bitmap getFailWebcamBitmap() {
		if (null == mFailBitmap)
			mFailBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_connection);
		return mFailBitmap;
	}
    
    


	//---------- Private methods
	/**
	 * Setup the application environment.
	 */
	private void setupEnvironment(Context context) {
		//set the log tag
		RainbowLogFacility logFacility = new RainbowLogFacility(LOG_TAG);
		//log the begin of the application
		logFacility.i("App started: " + App.APP_INTERNAL_NAME);

		//calculate application name
		APP_DISPLAY_NAME = getString(R.string.common_appName);
		
		//initialize (and automatically register) crash reporter
		RainbowCrashReporter crashReport = new RainbowCrashReporter(context);
		RainbowServiceLocator.put(crashReport);
		
		RainbowServiceLocator.put(logFacility);
		
		//create services and helper respecting IoC dependencies
		ItemsDao itemsDao = new ItemsDao(context, logFacility);
		RainbowServiceLocator.put(itemsDao);
		ActivityHelper activityHelper = new ActivityHelper(logFacility, context);
		RainbowServiceLocator.put(activityHelper);
		AppPreferencesDao appPreferencesDao = new AppPreferencesDao(context, APP_PREFERENCES_KEY);
		RainbowServiceLocator.put(appPreferencesDao);
		RainbowImageMediaHelper imageMediaHelper = new RainbowImageMediaHelper(logFacility);
		RainbowServiceLocator.put(imageMediaHelper);
		LogicManager logicManager = new LogicManager(logFacility, appPreferencesDao, this, APP_INTERNAL_VERSION, itemsDao);
		RainbowServiceLocator.put(logicManager);
		
		mImageUrlProvider = ImageUrlProvider.class;
	}
}
