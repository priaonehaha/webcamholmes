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

import it.rainbowbreeze.libs.common.RainbowServiceLocator;
import it.rainbowbreeze.libs.logic.RainbowCrashReporter;
import it.rainbowbreeze.libs.media.RainbowImageMediaHelper;
import it.rainbowbreeze.webcamholmes.R;
import it.rainbowbreeze.webcamholmes.data.AppPreferencesDao;
import it.rainbowbreeze.webcamholmes.data.ItemsDao;
import it.rainbowbreeze.webcamholmes.logic.LogicManager;
import it.rainbowbreeze.webcamholmes.ui.ActivityHelper;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static it.rainbowbreeze.libs.common.RainbowContractHelper.checkNotNull;

/**
 * Main application classes lazy-loading singleton
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 */
public class AppEnv {
    // ------------------------------------------ Private Fields
    private static final String LOG_HASH = AppEnv.class.getSimpleName();
    private static final Object mSyncObject = new Object();

    
    // -------------------------------------------- Constructors
    private AppEnv(Context context) {
        //use default objects factory
        this(context, getDefaultObjectsFactory());
    }
    
    private AppEnv(Context context, ObjectsFactory objectsFactory) {
        //use a custom object factory
        checkNotNull(objectsFactory, "ObjectsFactory");
        setupVolatileData(context, objectsFactory);
    }

    
    // --------------------------------------- Public Properties
    /** keys for application preferences */
    public final static String APP_PREFERENCES_KEY = "WebcamHolmesPrefs";

    /** lazy loading singleton */
    public static AppEnv i(Context context) {
        synchronized (mSyncObject) {
            if (null == mInstance)
                mInstance = new AppEnv(context);
        }
        return mInstance;
    }
    /** lazy loading singleton, reload the environment each time (for testing purposes) */
    public static AppEnv i(Context context, ObjectsFactory objectsFactory) {
        synchronized (mSyncObject) {
            if (null == mInstance) {
                mInstance = new AppEnv(context, objectsFactory);
            } else {
                //force the rebuild of all the environment
                mInstance.setupVolatileData(context, objectsFactory);
            }
        }
        return mInstance;
    }
    private static AppEnv mInstance;

    /** The name of the application, as showed to the user */
    public String APP_DISPLAY_NAME;
    
    /** Application version displayed to the user (about activity etc) */
    public static final String APP_DISPLAY_VERSION = "2.0";
    
    /** Application name used during the ping of update site */
    public static final String APP_INTERNAL_NAME = "WebcamHolmes-Android";

    /** Application version for internal use (update, crash report etc) */
    public static final String APP_INTERNAL_VERSION = "02.00.00";

    /** Application version for internal use (update, crash report etc) */
    public static final String EMAIL_FOR_ERROR_LOG = "webcamholmes@gmail.com";
    
    /** Tag to use in the log */
    public static final String LOG_TAG = "WebcamHolmes";
    
    /** Not found item in content provider */
    public static final long NOT_FOUND = -1;

    /** url where send statistics about application */
    public static final String STATISTICS_WEBSERVER_URL = "http://www.rainbowbreeze.it/devel/getlatestversion.php";
    
    public static final String WEBCAM_IMAGE_DUMP_FILE = "webcamDump.png";
    
    /** Default objects factory, testing purposes */
    protected static final ObjectsFactory defaultObjectsFactory = new ObjectsFactory();
    public static final ObjectsFactory getDefaultObjectsFactory()
    { return defaultObjectsFactory; }

    
    // ------------------------------------------ Public Methods

    public LogFacility getLogFacility() {
        return checkNotNull(RainbowServiceLocator.get(
                LogFacility.class), LogFacility.class.getSimpleName());
    }
    
    public AppPreferencesDao getAppPreferencesDao() {
        return checkNotNull(RainbowServiceLocator.get(
                AppPreferencesDao.class), AppPreferencesDao.class.getSimpleName());
    }
    
    public LogicManager getLogicManager() {
        return checkNotNull(RainbowServiceLocator.get(
                LogicManager.class), LogicManager.class.getSimpleName());
    }
    
    public ActivityHelper getActivityHelper() {
        return checkNotNull(RainbowServiceLocator.get(
                ActivityHelper.class), ActivityHelper.class.getSimpleName());
	}
    
    public AppPreferencesDao geAppPreferencesDao() {
        return checkNotNull(RainbowServiceLocator.get(
                AppPreferencesDao.class), AppPreferencesDao.class.getSimpleName()); 
    }
    
    public ItemsDao getItemsDao() {
        return checkNotNull(RainbowServiceLocator.get(
                ItemsDao.class), ItemsDao.class.getSimpleName());
    }
        
    public RainbowImageMediaHelper getImageMediaHelper() {
        return checkNotNull(RainbowServiceLocator.get(
                RainbowImageMediaHelper.class), RainbowImageMediaHelper.class.getSimpleName());
    }
        
    
    /**
     * The error bitmap to display when webcam image isn't available
     * @return
     */
    protected Bitmap mFailBitmap;
    public Bitmap getFailWebcamBitmap(Context context) {
        if (null == mFailBitmap)
            mFailBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_connection);
        return mFailBitmap;
    }
    
    /** First run after an update of the application */
    protected boolean mFirstRunAfterUpdate;
    public void setFirstRunAfterUpdate(boolean newValue)
    { mFirstRunAfterUpdate = newValue; }
    public boolean isFirstRunAfterUpdate()
    { return mFirstRunAfterUpdate; }

    
    // ----------------------------------------- Private Methods
    /**
     * Setup the volatile data of application.
     * This is needed because sometime the system release memory
     * without completely close the application, so all static fields
     * will become null :(
     * 
     */
    private void setupVolatileData(Context context, ObjectsFactory mObjectsFactory) {
        //set the log tag
        LogFacility logFacility = mObjectsFactory.createLogFacility(LOG_TAG);
        //log the begin of the application
        logFacility.i("App started: " + APP_INTERNAL_NAME);

        //calculate application name
        APP_DISPLAY_NAME = context.getString(R.string.common_appName);
        
        //empty service locator
        RainbowServiceLocator.clear();
        //put log facility
        RainbowServiceLocator.put(logFacility);

        //initialize (and automatically register) crash reporter
        RainbowCrashReporter crashReport = mObjectsFactory.createCrashReporter(context);
        RainbowServiceLocator.put(crashReport);
        //create services and helper respecting IoC dependencies
        ActivityHelper activityHelper = mObjectsFactory.createActivityHelper(context, logFacility);
        RainbowServiceLocator.put(activityHelper);
        AppPreferencesDao appPreferencesDao = mObjectsFactory.createAppPreferencesDao(context, APP_PREFERENCES_KEY);
        RainbowServiceLocator.put(appPreferencesDao);

        ItemsDao itemsDao = mObjectsFactory.createItemsDao(context, logFacility);
        RainbowServiceLocator.put(itemsDao);
        RainbowImageMediaHelper imageMediaHelper = mObjectsFactory.createImageMediaHelper(logFacility);
        RainbowServiceLocator.put(imageMediaHelper);

        LogicManager logicManager = new LogicManager(logFacility, appPreferencesDao, APP_INTERNAL_VERSION, itemsDao);
        RainbowServiceLocator.put(logicManager);
    }
    
    
    // ----------------------------------------- Private Classes
    public static class ObjectsFactory {
        public LogFacility createLogFacility(String logTag)
        { return new LogFacility(logTag); }
        
        public RainbowImageMediaHelper createImageMediaHelper(LogFacility logFacility)
        { return new RainbowImageMediaHelper(logFacility); }

        public RainbowCrashReporter createCrashReporter(Context context)
        { return new RainbowCrashReporter(context); }
        
        public ActivityHelper createActivityHelper(Context context, LogFacility logFacility)
        { return new ActivityHelper(logFacility, context); }
        
        public AppPreferencesDao createAppPreferencesDao(Context context, String appPreferencesKey)
        { return new AppPreferencesDao(context, appPreferencesKey); }
        
        public ItemsDao createItemsDao(Context context, LogFacility logFacility)
        { return new ItemsDao(context, logFacility); }
        
        public LogicManager createLogicManager(
                LogFacility logFacility,
                AppPreferencesDao appPreferencesDao,
                String currentAppVersion,
                ItemsDao itemsDao) {
            return new LogicManager(
                logFacility,
                appPreferencesDao,
                currentAppVersion,
                itemsDao);
        }
    }


}
