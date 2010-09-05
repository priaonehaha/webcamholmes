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
package it.rainbowbreeze.webcamholmes.ui;

import it.rainbowbreeze.libs.common.ServiceLocator;
import it.rainbowbreeze.libs.log.BaseLogFacility;
import it.rainbowbreeze.webcamholmes.R;
import it.rainbowbreeze.webcamholmes.common.App;
import it.rainbowbreeze.webcamholmes.common.ResultOperation;
import it.rainbowbreeze.webcamholmes.data.IImageUrlProvider;
import it.rainbowbreeze.webcamholmes.data.ItemsDao;
import it.rainbowbreeze.webcamholmes.domain.ItemWebcam;
import it.rainbowbreeze.webcamholmes.logic.GlobalHelper;
import it.rainbowbreeze.webcamholmes.logic.LoadImageTask;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;

import static it.rainbowbreeze.libs.common.ContractHelper.*;

/**
 * Display webcam image and reload it
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */

public class ActWebcam
	extends Activity
{
	//---------- Private fields
	private final static int OPTIONMENU_FORCE_RELOAD = 10;
	private final static int OPTIONMENU_PAUSE_RELOAD = 11;
	private final static int OPTIONMENU_START_RELOAD = 12;

	private final static String BUNDLEKEY_USERRELOADPAUSED = "UserReloadPaused";

	private BaseLogFacility mLogFacility;
	private ActivityHelper mActivityHelper;
	private ItemWebcam mWebcam;
	private ImageView mImgWebcam;
	private LoadImageTask mLoadWebcamTask;
	private boolean mReloadPaused;
	private boolean mUserReloadPaused;

	private ItemsDao mItemsDao;

	
	
	//---------- Public properties

	
	
	
	//---------- Events
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        mLogFacility = checkNotNull(ServiceLocator.get(BaseLogFacility.class), "LogFacility");
        mActivityHelper = checkNotNull(ServiceLocator.get(ActivityHelper.class), "ActivityHelper");
        mItemsDao = checkNotNull(ServiceLocator.get(ItemsDao.class), "ItemsDao");
		
        getDataFromIntent(getIntent());

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    	setContentView(R.layout.actwebcam);
        setTitle(String.format(getString(R.string.actwebcam_lblTitle), mWebcam.getName()));
        
        mImgWebcam = (ImageView) findViewById(R.id.actwebcam_imgWebcam);
        
        if (null == savedInstanceState) {
        	//first start of the activity
	        mReloadPaused = false;
	        mUserReloadPaused = false;
        } else {
        	//activity destroyed and re-createad
        	Drawable drawable = (Drawable) getLastNonConfigurationInstance();
        	if (null != drawable) mImgWebcam.setImageDrawable(drawable);
        }
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(BUNDLEKEY_USERRELOADPAUSED, mUserReloadPaused);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mUserReloadPaused = savedInstanceState.getBoolean(BUNDLEKEY_USERRELOADPAUSED);
		mReloadPaused = mUserReloadPaused;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onRetainNonConfigurationInstance()
	 */
	@Override
	public Object onRetainNonConfigurationInstance() {
		return mImgWebcam.getDrawable();
	}

	@Override
	protected void onPause() {
		stopWebcamLoadTask();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		if (!mUserReloadPaused) startWebcamLoadTask();
		super.onRestart();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(0, OPTIONMENU_FORCE_RELOAD, 1, R.string.actwebcam_mnuReload)
//			.setIcon(R.drawable.ic_menu_refresh);
		menu.add(0, OPTIONMENU_PAUSE_RELOAD, 2, R.string.actwebcam_mnuStopReload)
			.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		menu.add(0, OPTIONMENU_START_RELOAD, 3, R.string.actwebcam_mnuStartReload)
			.setIcon(R.drawable.ic_menu_play_clip);
	return true;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.findItem(OPTIONMENU_START_RELOAD).setVisible(mReloadPaused);
		menu.findItem(OPTIONMENU_PAUSE_RELOAD).setVisible(!mReloadPaused);
		return super.onPrepareOptionsMenu(menu);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case OPTIONMENU_FORCE_RELOAD:
			break;
			
		case OPTIONMENU_PAUSE_RELOAD:
			mUserReloadPaused = true;
			stopWebcamLoadTask();
			break;

		case OPTIONMENU_START_RELOAD:
			mUserReloadPaused = false;
			startWebcamLoadTask();
			break;

		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}



	//---------- Public methods

	
	
	
	//---------- Private methods

	/**
	 * Get data from intent and configured internal fields
	 * @param intent
	 */
	private void getDataFromIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		//checks if current editing is for a provider or a subservice
		if(extras != null) {
			long webcamId = checkNotNull(Long.parseLong(extras.getString(ActivityHelper.INTENTKEY_WEBCAMID)), "WebcamId");
			mWebcam = checkNotNull(mItemsDao.getWebcamById(webcamId), "Webcam to load");
			mLogFacility.v("Loading webcam " + mWebcam.getImageUrl());
		} else {
			checkNotNull(null, "Webcam to load");
		}
	}

	/**
	 * Starts the ASyncTask that loads webcam image
	 */
	private void startWebcamLoadTask() {
		if (GlobalHelper.isOnline(this)) {
			IImageUrlProvider imageUrlProvider;
			try {
				imageUrlProvider = App.i().getImageUrlProvider();
				LoadImageTask task = new LoadImageTask(imageUrlProvider, mWebcam, getWindow(), mImgWebcam);
				task.execute();
				mLoadWebcamTask = task;
				mReloadPaused = false;
				return;
			} catch (Exception e) {
				mActivityHelper.reportError(this, e, ResultOperation.RETURNCODE_ERROR_GENERIC);
			}
		} else {
			mActivityHelper.reportError(this, new Exception(), ResultOperation.RETURNCODE_ERROR_COMMUNICATION);
		}
		mImgWebcam.setImageResource(R.drawable.no_connection);
		mLoadWebcamTask = null;
		mReloadPaused = true;
	}
	

	/**
	 * Stops the ASyncTask that loads webcam image
	 */
	private void stopWebcamLoadTask() {
		//stop execution of webcam task when the activity is destroyed, if a task is in progress
        if (null != mLoadWebcamTask && AsyncTask.Status.RUNNING == mLoadWebcamTask.getStatus()) {
        	mLoadWebcamTask.cancel(true);
        	mLoadWebcamTask = null;
        }
        mReloadPaused = true;
	}
	
}

