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

/**
 * 
 */
package it.rainbowbreeze.webcamholmes.ui;

import static it.rainbowbreeze.libs.common.ContractHelper.checkNotNull;
import android.content.Intent;
import android.os.Bundle;
import it.rainbowbreeze.libs.common.BaseResultOperation;
import it.rainbowbreeze.libs.common.ServiceLocator;
import it.rainbowbreeze.libs.log.BaseLogFacility;
import it.rainbowbreeze.libs.ui.BaseSplashScreenActivity;
import it.rainbowbreeze.webcamholmes.common.App;


/**
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public class ActSplashScreen extends BaseSplashScreenActivity {

	//---------- Private fields
	private ActivityHelper mActivityHelper;
	protected BaseLogFacility mLogFacility;

	
	
	
	//---------- Public properties

	
	
	
	//---------- Events
	/* (non-Javadoc)
	 * @see it.rainbowbreeze.libs.ui.BaseSplashScreenActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
        mActivityHelper = checkNotNull(ServiceLocator.get(ActivityHelper.class), "ActivityHelper");
        mLogFacility = checkNotNull(ServiceLocator.get(BaseLogFacility.class), "LogFacility");
		
	}
	
    


	//---------- Public methods

	
	
	
	//---------- Private methods


	/* (non-Javadoc)
	 * @see it.rainbowbreeze.libs.ui.BaseSplashScreenActivity#beginTaskFailed(it.rainbowbreeze.libs.common.BaseResultOperation)
	 */
	protected void beginTaskFailed(BaseResultOperation<Void> result) {
		App.i().setCorrectlyInitialized(false);
		//some errors
		mBaseActivityHelper.reportError(this, result);
	}


	/* (non-Javadoc)
	 * @see it.rainbowbreeze.libs.ui.BaseSplashScreenActivity#beginTasksCompleted(it.rainbowbreeze.libs.common.BaseResultOperation)
	 */
	protected void beginTasksCompleted(BaseResultOperation<Void> result) {
		App.i().setCorrectlyInitialized(true);

		//and call main activity
		Intent i = getIntent();
		Bundle extras = null != i ? i.getExtras() : null;
		mActivityHelper.openMain(this, extras);
	}
}
