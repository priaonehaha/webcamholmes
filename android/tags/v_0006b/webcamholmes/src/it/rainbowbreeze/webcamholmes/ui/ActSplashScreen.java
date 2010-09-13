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
import it.rainbowbreeze.libs.common.BaseResultOperation;
import it.rainbowbreeze.libs.common.ServiceLocator;
import it.rainbowbreeze.libs.log.BaseLogFacility;
import it.rainbowbreeze.libs.logic.LogicManagerExecuteBeginTasksThread;
import it.rainbowbreeze.webcamholmes.R;
import it.rainbowbreeze.webcamholmes.common.App;
import it.rainbowbreeze.webcamholmes.logic.LogicManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Spashscreen activity, simply execute application begin tasks
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public class ActSplashScreen extends Activity {
	
	//---------- Private fields
	private final static int DIALOG_EXECUTING_BEGIN_TASKS = 10;
	
	private LogicManagerExecuteBeginTasksThread mExecuteBeginTaskThread;
	private BaseLogFacility mLogFacility;
	private ActivityHelper mActivityHelper;
	private boolean mFirstStart;

	
	
	
	//---------- Public properties

	
	
	
	//---------- Events
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        mLogFacility = checkNotNull(ServiceLocator.get(BaseLogFacility.class), "LogFacility");
        mActivityHelper = checkNotNull(ServiceLocator.get(ActivityHelper.class), "ActivityHelper");
        LogicManager logicManager = checkNotNull(ServiceLocator.get(LogicManager.class), "LogicManager");
		
		setContentView(R.layout.actsplashscreen);
        setTitle(String.format(getString(R.string.actsplashscreen_lblTitle), App.APP_DISPLAY_NAME));

        mFirstStart = null == savedInstanceState;
    	//executed when the application first runs
        if (null == savedInstanceState) {
    		mLogFacility.i("App started: " + App.APP_INTERNAL_NAME);
	        
    		showDialog(DIALOG_EXECUTING_BEGIN_TASKS);
	        //execute begin tasks and other initialization
			//preparing the background thread for executing service command
			mExecuteBeginTaskThread = new LogicManagerExecuteBeginTasksThread(
					this.getApplicationContext(),
					mActSplashScreenHandler,
					logicManager);
			mExecuteBeginTaskThread.start();
			
			//at the end, execute method completeBeginTasks()
        }
        
	}

    /* (non-Javadoc)
     * @see android.app.Activity#onStart()
     */
    @Override
    protected void onStart() {
		super.onStart();
		if (!mFirstStart) {
			mExecuteBeginTaskThread = (LogicManagerExecuteBeginTasksThread) getLastNonConfigurationInstance();
			if (null != mExecuteBeginTaskThread) {
				//register new handler
				mExecuteBeginTaskThread.registerCallerHandler(mActSplashScreenHandler);
			}
		}
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onStop()
     */
    @Override
    protected void onStop() {
		if (null != mExecuteBeginTaskThread) {
			//unregister handler from background thread
			mExecuteBeginTaskThread.unregisterCallerHandler();
		}
		super.onStop();
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onRetainNonConfigurationInstance()
     */
    @Override
    public Object onRetainNonConfigurationInstance() {
    	//save eventually open thread
    	return mExecuteBeginTaskThread;
    }    

    /* (non-Javadoc)
     * @see android.app.Activity#onCreateDialog(int)
     */
    @Override
    protected Dialog onCreateDialog(int id) {
    	Dialog retDialog = null;
    	
    	switch (id) {
    	case DIALOG_EXECUTING_BEGIN_TASKS:
    		retDialog = mActivityHelper.createProgressDialog(this, R.string.actsplashscreen_msgExecutingBeginTasks);
    		break;
    		
		default:
			retDialog = super.onCreateDialog(id);
    	}
    	
    	return retDialog;
    }
   
    /* (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (ActivityHelper.REQUESTCODE_MAINACTIVITY == requestCode) {
    		//end the app when the main activity returns
    		finish();
    	} else {
    		super.onActivityResult(requestCode, resultCode, data);
    	}
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
    	if (isFinishing()) {
    		mLogFacility.v("App ending: " + App.APP_INTERNAL_NAME);
    	}
    	super.onDestroy();
    }
    
    
	/**
	 * Hander to call when the execute command menu option ended
	 */
	private Handler mActSplashScreenHandler = new Handler() {
		public void handleMessage(Message msg)
		{
			//check if the message is for this handler
			if (msg.what != LogicManagerExecuteBeginTasksThread.WHAT_EXECUTE_BEGIN_TASK)
				return;
			
			//dismisses progress dialog
			dismissDialog(DIALOG_EXECUTING_BEGIN_TASKS);
			BaseResultOperation<Void> res = mExecuteBeginTaskThread.getResult();
			if (res.hasErrors()) {
				App.i().setCorrectlyInitialized(false);
				//some errors
				mActivityHelper.reportError(ActSplashScreen.this, res);
			} else {
				App.i().setCorrectlyInitialized(true);
				callMainApplicationActivity();
			}
			//free the thread
			mExecuteBeginTaskThread = null;
		};
	};
    
    
	
	//---------- Public methods
	
	
	

	//---------- Private methods
    /**
	 * 
	 */
	protected void callMainApplicationActivity() {
		Intent i = getIntent();
		
		Bundle extras = null != i ? i.getExtras() : null;
		mActivityHelper.openMain(this, extras);
	}	
	
	
}
