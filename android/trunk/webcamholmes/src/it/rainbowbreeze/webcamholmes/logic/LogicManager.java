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
package it.rainbowbreeze.webcamholmes.logic;

import java.util.Calendar;

import it.rainbowbreeze.libs.log.LogFacility;
import it.rainbowbreeze.webcamholmes.common.App;
import it.rainbowbreeze.webcamholmes.common.GlobalDef;
import it.rainbowbreeze.webcamholmes.common.ResultOperation;
import it.rainbowbreeze.webcamholmes.data.AppPreferencesDao;
import android.content.Context;

/**
 * @author Alfredo "Rainbowbreeze" Morresi
 */
public class LogicManager {
	//---------- Constructor

	
	
	
	//---------- Private fields

	
	
	
	//---------- Public properties
	/**
	 * Initializes data, execute begin operation
	 */
	public static ResultOperation<Void> executeBeginTask(Context context)
	{
		ResultOperation<Void> res = new ResultOperation<Void>();
		
		LogFacility.v("ExecuteBeginTask");
		
		//load configurations
		App.instance().getAppPreferencesDao().load(context);
		LogFacility.i("Preferences loaded");
		
		//checks for application upgrade
		res = performAppVersionUpgrade(context);
			
		return res;
	}

	/**
	 * Executes final operation, just before the app close
	 * @param context
	 * @return
	 */
	public static ResultOperation<Void> executeEndTast(Context context)
	{
		ResultOperation<Void> res = new ResultOperation<Void>();

		return res;
	}

	
	
	
	//---------- Public methods

	
	
	
	//---------- Private methods
	/**
	 * Checks if some upgrade is needed between current version of the
	 * application and the previous one
	 * 
	 *  @return true if all ok, otherwise false
	 */
	private static ResultOperation<Void> performAppVersionUpgrade(Context context)
	{
		AppPreferencesDao prefDao;
		
		prefDao = App.instance().getAppPreferencesDao();
		if (isNewAppVersion()) {
			LogFacility.i("Upgrading from " + prefDao.getAppVersion() + " to " + GlobalDef.APP_VERSION);
			//perform upgrade
			addItemsForVersion000100();
			
			//update expiration date
    	    final Calendar c = Calendar.getInstance();
    	    prefDao.setInstallationTime(c.getTimeInMillis());
			
			//update application version in the configuration
    	    prefDao.setAppVersion(GlobalDef.APP_VERSION);
			
			//and save updates
    	    prefDao.save();
			
			LogFacility.i("Upgrading complete");
		}
		
		return new ResultOperation<Void>();
	}
	
	
	
	private static ResultOperation<Void> addItemsForVersion000100() {
		
		return new ResultOperation<Void>();
	}

	/**
	 * Check if the current application is new compared to the last time
	 * the application run
	 * 
	 * @return
	 */
	public static boolean isNewAppVersion() {
		String currentAppVersion = App.instance().getAppPreferencesDao().getAppVersion();
		return GlobalDef.APP_VERSION.compareToIgnoreCase(currentAppVersion) > 0;
	}
}
