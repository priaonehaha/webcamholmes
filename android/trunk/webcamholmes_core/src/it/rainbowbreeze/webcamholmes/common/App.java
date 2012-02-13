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
	extends Application
{
	//---------- Private fields
	private Class<? extends ImageUrlProvider> mImageUrlProvider;




	//---------- Public properties


    
	//---------- Events
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
		logFacility.i("App ending: " + AppEnv.APP_INTERNAL_NAME);
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
	

	//---------- Private methods
	/**
	 * Setup the application environment.
	 */
	private void setupEnvironment(Context context) {
		mImageUrlProvider = ImageUrlProvider.class;
	}
}
