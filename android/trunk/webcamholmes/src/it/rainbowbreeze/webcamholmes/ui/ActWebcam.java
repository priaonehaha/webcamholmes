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

import it.rainbowbreeze.webcamholmes.R;
import it.rainbowbreeze.webcamholmes.data.ItemsProvider;
import it.rainbowbreeze.webcamholmes.domain.ItemWebcam;
import it.rainbowbreeze.webcamholmes.logic.LoadImageTask;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

/**
 * Display webcam image and reload it
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */

/**
 * TODO
 * -save pause/play status thru restart of the activity
 * 
 */
public class ActWebcam
	extends Activity
{
	//---------- Private fields
	private ItemWebcam mWebcam;
	private boolean mInvalidWebcam;
	private ImageView mImgWebcam;
	private LoadImageTask mLoadWebcamTask;
	private boolean mReloadPaused;

	
	
	//---------- Public properties

	
	
	
	//---------- Events
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        getDataFromIntent(getIntent());

        if (null != mWebcam) {
        	mInvalidWebcam = false;
        } else {
        	mInvalidWebcam = true;
        	//TODO
        	//error layout
        	return;
        }
        
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    	setContentView(R.layout.actwebcam);
        setTitle(String.format(getString(R.string.actwebcam_lblTitle), mWebcam.getName()));
        
        mImgWebcam = (ImageView) findViewById(R.id.actwebcam_imgWebcam);
        mReloadPaused = false;
        mLoadWebcamTask = startLoadWebcamImage(mWebcam, getWindow(), mImgWebcam);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		//stop execution of webcam task when the activity is destroyed, if a task is in progress
        if (null != mLoadWebcamTask && AsyncTask.Status.RUNNING == mLoadWebcamTask.getStatus()) {
        	mLoadWebcamTask.cancel(true);
        	mLoadWebcamTask = null;
        }
	}

	@Override
	protected void onPause() {
		super.onPause();
		mReloadPaused = true;
	}
	
	@Override
	protected void onRestart() {
		mReloadPaused = false;
		super.onRestart();
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
			String webcamId = extras.getString(ActivityHelper.INTENTKEY_WEBCAMID);
			mWebcam = ItemsProvider.instance().getWebcamById(webcamId);
		} else {
			mWebcam = null;
		}
	}

	/**
	 * Start the ASyncTask that loads and reloads webcam image
	 */
	private LoadImageTask startLoadWebcamImage(ItemWebcam webcam, Window window, ImageView imageView)
	{
		LoadImageTask task = new LoadImageTask(webcam, window, imageView);
		task.execute();
        
        return task;
	}
}

