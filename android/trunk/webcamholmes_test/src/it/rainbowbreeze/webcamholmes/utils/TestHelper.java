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
package it.rainbowbreeze.webcamholmes.utils;

import android.content.Context;
import it.rainbowbreeze.libs.common.RainbowLogFacility;
import it.rainbowbreeze.webcamholmes.common.App;
import it.rainbowbreeze.webcamholmes.common.AppEnv;
import it.rainbowbreeze.webcamholmes.common.LogFacility;
import it.rainbowbreeze.webcamholmes.data.AppPreferencesDao;
import it.rainbowbreeze.webcamholmes.data.ItemsDao;
import it.rainbowbreeze.webcamholmes.logic.LogicManager;

/**
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public class TestHelper {
	//---------- Private fields
	private static final String LOG_TAG = "WebcamHolmes-Test";

	private static LogFacility mLogFacility;
	private static AppEnv mApp;
	
	
	
	//---------- Constructor
	static {
		mLogFacility = new RainbowLogFacility(LOG_TAG);
		mApp = new App();
	}

	
	
	//---------- Public methods
	public static class Factory{
		private Factory(){}
		
		public static RainbowLogFacility getLogFacility()
		{ return mLogFacility; }
		
		public static AppPreferencesDao getAppPreferencesDao(Context context)
		{ return new AppPreferencesDao(context, AppEnv.APP_PREFERENCES_KEY); }
		
		public static ItemsDao getItemsDao(Context context)
		{ return new ItemsDao(context, mLogFacility); }
		
		public static LogicManager getLogicManager(Context context)
		{ return new LogicManager(mLogFacility, getAppPreferencesDao(context), AppEnv.APP_INTERNAL_VERSION, getItemsDao(context)); }


	}

	//---------- Private methods
}
