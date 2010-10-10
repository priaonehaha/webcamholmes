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

import static it.rainbowbreeze.libs.common.RainbowContractHelper.checkNotNull;
import android.content.Intent;
import android.os.Bundle;
import it.rainbowbreeze.libs.common.RainbowResultOperation;
import it.rainbowbreeze.libs.common.RainbowServiceLocator;
import it.rainbowbreeze.libs.ui.RainbowSplashScreenActivity;
import it.rainbowbreeze.webcamholmes.common.App;


/**
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public class ActSplashScreen extends RainbowSplashScreenActivity {

	//---------- Private fields
	private ActivityHelper mActivityHelper;

	
	
	
	//---------- Public properties

	
	
	
	//---------- Events
	/* (non-Javadoc)
	 * @see it.rainbowbreeze.libs.ui.BaseSplashScreenActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        mActivityHelper = checkNotNull(RainbowServiceLocator.get(ActivityHelper.class), "ActivityHelper");
		super.onCreate(savedInstanceState);
	}
	
    


	//---------- Public methods

	
	
	
	//---------- Private methods


	/* (non-Javadoc)
	 * @see it.rainbowbreeze.libs.ui.BaseSplashScreenActivity#beginTaskFailed(it.rainbowbreeze.libs.common.BaseResultOperation)
	 */
	protected void beginTaskFailed(RainbowResultOperation<Void> result) {
		mBaseLogFacility.e("Cannot launch the application, error during initialization");
		//report the errors
		mBaseActivityHelper.reportError(this, result);
	}


	/* (non-Javadoc)
	 * @see it.rainbowbreeze.libs.ui.BaseSplashScreenActivity#beginTasksCompleted(it.rainbowbreeze.libs.common.BaseResultOperation)
	 */
	protected void beginTasksCompleted(RainbowResultOperation<Void> result) {
		//call main activity
		Intent i = getIntent();
		Bundle extras = null != i ? i.getExtras() : null;
		mActivityHelper.openMain(this, extras);
	}

	protected String getApplicationInternalName() {
		return App.APP_INTERNAL_NAME;
	}

	protected String getApplicationInternalVersion() {
		return App.APP_INTERNAL_VERSION;
	}

	protected String getEmailForLog() {
		return App.EMAIL_FOR_LOG;
	}

	protected String getLogTag() {
		return App.LOG_TAG;
	}
}
