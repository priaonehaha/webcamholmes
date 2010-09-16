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

import it.rainbowbreeze.libs.common.BaseResultOperation;
import it.rainbowbreeze.libs.common.ServiceLocator;
import it.rainbowbreeze.libs.log.BaseLogFacility;
import it.rainbowbreeze.libs.media.BaseImageMediaHelper;
import it.rainbowbreeze.webcamholmes.R;
import it.rainbowbreeze.webcamholmes.common.App;
import it.rainbowbreeze.webcamholmes.common.ResultOperation;
import it.rainbowbreeze.webcamholmes.data.IImageUrlProvider;
import it.rainbowbreeze.webcamholmes.data.ItemsDao;
import it.rainbowbreeze.webcamholmes.domain.ItemWebcam;
import it.rainbowbreeze.webcamholmes.logic.GlobalHelper;
import it.rainbowbreeze.webcamholmes.logic.LoadImageTask;
import it.rainbowbreeze.webcamholmes.logic.SaveWebcamImageThread;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
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
	private final static int OPTIONMENU_FULLSCREEN = 13;
	private final static int OPTIONMENU_SHARE = 14;

	private final static int DIALOG_PREPARE_FOR_FULLSCREEN = 10;
	private final static int DIALOG_PREPARE_FOR_SHARING = 11;

	private final static String BUNDLEKEY_USERRELOADPAUSED = "UserReloadPaused";

	private BaseLogFacility mLogFacility;
	private ActivityHelper mActivityHelper;
	private ItemWebcam mWebcam;
	private ImageView mImgWebcam;
	private LoadImageTask mLoadWebcamTask;
	private BaseImageMediaHelper mImageMediaHelper;
	private boolean mReloadPaused;
	private boolean mUserReloadPaused;
	private ItemsDao mItemsDao;
	private SaveWebcamImageThread mSaveWebcamImageThread;

	
	
	//---------- Public properties

	
	
	
	//---------- Events
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        mLogFacility = checkNotNull(ServiceLocator.get(BaseLogFacility.class), "LogFacility");
        mActivityHelper = checkNotNull(ServiceLocator.get(ActivityHelper.class), "ActivityHelper");
        mItemsDao = checkNotNull(ServiceLocator.get(ItemsDao.class), "ItemsDao");
        mImageMediaHelper = checkNotNull(ServiceLocator.get(BaseImageMediaHelper.class), "ImageMediaHelper");
        
        getDataFromIntent(getIntent());

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    	setContentView(R.layout.actwebcam);
        setTitle(String.format(getString(R.string.actwebcam_lblTitle), App.APP_DISPLAY_NAME, mWebcam.getName()));
        
        mImgWebcam = (ImageView) findViewById(R.id.actwebcam_imgWebcam);
        mImgWebcam.setOnClickListener(mWebcamImageOnClickListener);
        
        if (null == savedInstanceState) {
        	//first start of the activity
	        mReloadPaused = false;
	        mUserReloadPaused = false;
        }
	}
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();

		//retrieve bitmap
		Object[] objects = (Object[]) getLastNonConfigurationInstance();
		//nothing saved, probably first run of the activity
		if (null == objects) return;

		Drawable drawable = (Drawable) objects[0];
    	if (null != drawable) mImgWebcam.setImageDrawable(drawable);
    	//retrieve backgroud thread object
		mSaveWebcamImageThread = (SaveWebcamImageThread) objects[1];
		if (null != mSaveWebcamImageThread) {
			mSaveWebcamImageThread.registerCallerHandler(mActivityHandler);
		}
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		if (null != mSaveWebcamImageThread) {
			mSaveWebcamImageThread.unregisterCallerHandler();
		}
		super.onStop();
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
		return new Object[]{mImgWebcam.getDrawable(), mSaveWebcamImageThread};
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
		menu.add(0, OPTIONMENU_FULLSCREEN, 4, R.string.actwebcam_mnuFullscreen)
			.setIcon(android.R.drawable.ic_menu_gallery);
		menu.add(0, OPTIONMENU_SHARE, 5, R.string.actwebcam_mnuShare)
			.setIcon(android.R.drawable.ic_menu_view);
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

		case OPTIONMENU_FULLSCREEN:
			showWebcamFullscreen();
			break;

		case OPTIONMENU_SHARE:
			shareWebcamImage();
			break;

		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog retDialog;
		
		switch(id) {
		case(DIALOG_PREPARE_FOR_FULLSCREEN):
			retDialog = mActivityHelper.createProgressDialog(this, R.string.actwebcam_msgPrepareForFullscreen);
			break;
		case(DIALOG_PREPARE_FOR_SHARING):
			retDialog = mActivityHelper.createProgressDialog(this, R.string.actwebcam_msgPrepareForSharing);
			break;
		default:
			retDialog = super.onCreateDialog(id);
			break;
		}
		
		return retDialog;
	}
	
	
	private OnClickListener mWebcamImageOnClickListener = new OnClickListener() {
		public void onClick(View v) {
			showWebcamFullscreen();
		}
	};

	/**
	 * Hander to call when a message is sent or a captcha code is inserted
	 */
	private Handler mActivityHandler = new Handler() {
		public void handleMessage(Message msg)
		{
			mLogFacility.i("Returned to ActWebcam from external thread with message " + msg.what);
			//check if the message is for this handler
			if (msg.what != SaveWebcamImageThread.WHAT_DUMP_WEBCAM_IMAGE_FOR_FULLSCREEN
				&& msg.what != SaveWebcamImageThread.WHAT_DUMP_WEBCAM_IMAGE_FOR_SHARE)
				return;
			
			BaseResultOperation<String> res;
			switch (msg.what) {
			case SaveWebcamImageThread.WHAT_DUMP_WEBCAM_IMAGE_FOR_FULLSCREEN:
				//may happens that the thread is null (rotation and a call to handler in the same moment?)
				if (null != mSaveWebcamImageThread) {
					//get result from method
					res = mSaveWebcamImageThread.getResult();
					mSaveWebcamImageThread = null;
					prepareForFullscreenComplete(res);
				}
				break;
				
			case SaveWebcamImageThread.WHAT_DUMP_WEBCAM_IMAGE_FOR_SHARE:
				//may happens that the thread is null (rotation and a call to handler in the same moment?)
				if (null != mSaveWebcamImageThread) {
					//get result from method
					res = mSaveWebcamImageThread.getResult();
					mSaveWebcamImageThread = null;
					prepareForSharingComplete(res);
				}
				break;
			}
		}

	};


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
				LoadImageTask task = new LoadImageTask(
						imageUrlProvider, mWebcam, getWindow(), mImgWebcam, App.i().getFailWebcamBitmap());
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


	/**
	 * Show the webcam in fullscreen image browser
	 */
	private void showWebcamFullscreen() {
		//show a progress dialog
		showDialog(DIALOG_PREPARE_FOR_FULLSCREEN);
		
		mSaveWebcamImageThread = new SaveWebcamImageThread(
				mLogFacility,
				mImageMediaHelper,
				ActWebcam.this,
				mActivityHandler,
				mImgWebcam,
				App.WEBCAM_IMAGE_DUMP_FILE,
				SaveWebcamImageThread.AT_THE_END_FULLSCREEN);
		mSaveWebcamImageThread.run();
	}
	
	private void shareWebcamImage() {
		//show a progress dialog
		showDialog(DIALOG_PREPARE_FOR_SHARING);
		
		mSaveWebcamImageThread = new SaveWebcamImageThread(
				mLogFacility,
				mImageMediaHelper,
				ActWebcam.this,
				mActivityHandler,
				mImgWebcam,
				App.WEBCAM_IMAGE_DUMP_FILE,
				SaveWebcamImageThread.AT_THE_END_SHARE);
		mSaveWebcamImageThread.run();
	}


	/**
	 * Called when dump of webcam image is completed
	 * @param res
	 */
	private void prepareForFullscreenComplete(BaseResultOperation<String> res) {
		if (res.hasErrors()) {
			mActivityHelper.reportError(this, res.getException(), ResultOperation.RETURNCODE_ERROR_APPLICATION_ARCHITECTURE);
		} else {
			//remove the dialog
			removeDialog(DIALOG_PREPARE_FOR_FULLSCREEN);
			//open fullscreen activity
			mActivityHelper.openFullscreenImageActivity(this, App.WEBCAM_IMAGE_DUMP_FILE);
		}
	};

	/**
	 * Called when dump of webcam image is completed
	 * @param res
	 */
	private void prepareForSharingComplete(BaseResultOperation<String> res) {
		if (res.hasErrors()) {
			mActivityHelper.reportError(this, res.getException(), ResultOperation.RETURNCODE_ERROR_APPLICATION_ARCHITECTURE);
		} else {
			//remove the dialog
			removeDialog(DIALOG_PREPARE_FOR_SHARING);
			//in the result there is the file path
			String fileFullPath = res.getResult();
			//launch share intent
			mActivityHelper.sendEmail(this,
					"", 
					getString(R.string.actwebcam_msgShareSubject),
					getString(R.string.actwebcam_msgShareBody),
					fileFullPath);
		}
	};

}

