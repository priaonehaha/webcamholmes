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
package it.rainbowbreeze.webcamholmes.ui;


import it.rainbowbreeze.libs.common.RainbowResultOperation;
import it.rainbowbreeze.libs.ui.RainbowSplashScreenActivity;
import it.rainbowbreeze.webcamholmes.common.AppEnv;
import it.rainbowbreeze.webcamholmes.common.LogFacility;

import android.content.Intent;
import android.os.Bundle;

/**
 * Simple implementation of a splashscreen activity
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 */
public class SplashscreenActivity extends RainbowSplashScreenActivity {
    // ------------------------------------------ Private Fields
    private static final String LOG_HASH = SplashscreenActivity.class.getSimpleName();
    private ActivityHelper mActivityHelper;
    
    // -------------------------------------------- Constructors

    // --------------------------------------- Public Properties

    // -------------------------------------------------- Events
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //WARNING
        //The AppEnv lazy-loading singleton must be called at least one time before the
        // RainbowSplashScreen#onCreate is called.
        // In order to avoid problems with Robolectric, the initialization is
        // performed here
        LogFacility logFacility = AppEnv.i(getApplicationContext()).getLogFacility();
        logFacility.i(LOG_HASH, "Starting application " + AppEnv.APP_INTERNAL_NAME + " v" + AppEnv.APP_INTERNAL_VERSION);
        
        super.onCreate(savedInstanceState);
    }
    
    // ------------------------------------------ Public Methods

    // ----------------------------------------- Private Methods

    @Override
    protected void additionalInitialization(Bundle savedInstanceState) {
        mActivityHelper = AppEnv.i(this).getActivityHelper();
    }
    
    @Override
    protected void beginTasksCompleted(RainbowResultOperation<Void> result) {
        //call main activity
        Intent i = getIntent();
        Bundle extras = null != i ? i.getExtras() : null;
        mActivityHelper.openMain(this, extras);
    }

    @Override
    protected void beginTaskFailed(RainbowResultOperation<Void> result) {
        mBaseLogFacility.e("Cannot launch the application, error during initialization");
        //report the errors
        mBaseActivityHelper.reportError(this, result);
    }

    @Override
    protected String getApplicationInternalName() {
        return AppEnv.APP_INTERNAL_NAME;
    }

    @Override
    protected String getApplicationInternalVersion() {
        return AppEnv.APP_INTERNAL_VERSION;
    }

    @Override
    protected String getEmailForLog() {
        return AppEnv.EMAIL_FOR_ERROR_LOG;
    }

    @Override
    protected String getLogTag() {
        return AppEnv.LOG_TAG;
    }
    
    protected boolean isBackgroundGrayed() {
        return false;
    }
}
