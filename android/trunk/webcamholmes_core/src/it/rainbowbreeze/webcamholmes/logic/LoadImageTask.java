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

import it.rainbowbreeze.webcamholmes.data.IImageUrlProvider;
import it.rainbowbreeze.webcamholmes.domain.ItemWebcam;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.Window;
import android.widget.ImageView;

/**
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public class LoadImageTask
	extends AsyncTask<Void, Bitmap, Void>
{
	//---------- Private fields
	private WeakReference<ImageView> mImageViewWhereShowBitmap;
	private WeakReference<Window> mWindowWhereUpdateProgress;
	private ItemWebcam mWebcam;
	private boolean mInterruptReload;
	private final IImageUrlProvider mImageUrlProvider;
	private final Bitmap mFailBitmap;
	private boolean mImagedLoadedAtLeastOneTime;
	
	
	
	//---------- Public properties




	//---------- Constructor
	
	/**
	 * 
	 * @param imageProvider
	 * @param webcam
	 * @param windowToUpdate
	 * @param imageToUpdate
	 * @param failBitmap The Bitmap to show when no connection or other issues happened
	 * is present or an error during the retrieve of the image happens 
	 * 
	 */
	public LoadImageTask(
			IImageUrlProvider imageProvider,
			ItemWebcam webcam,
			Window windowToUpdate,
			ImageView imageToUpdate,
			Bitmap failBitmap)
	{
		mImageUrlProvider = imageProvider;
		mWindowWhereUpdateProgress = new WeakReference<Window>(windowToUpdate);
		mImageViewWhereShowBitmap = new WeakReference<ImageView>(imageToUpdate);
		mWebcam = webcam;
		mInterruptReload = false;
		mFailBitmap = failBitmap;
		mImagedLoadedAtLeastOneTime = true;
	}




	//---------- Public methods
	public boolean getImagedLoadedAtLeastOneTime()
	{ return mImagedLoadedAtLeastOneTime; }




	//---------- Public methods
	public void pauseReload() {
		mInterruptReload = true;
	}




	//---------- Private methods
	
	@Override
	protected Void doInBackground(Void... params)
	{
		//check start condition
		if (null == mWebcam) return null;
		if (null == mImageViewWhereShowBitmap) return null;

		String imagePath = mWebcam.getImageUrl();
		int waitInterval = mWebcam.getReloadInterval();
		
		while (true) {
			//TODO
			//checks for connection errors or other problems
			
			//start animation
			publishProgress(new Bitmap[]{});
			
			//load bitmap
			Bitmap newBitmap = loadBitmapFromUrl(imagePath);
			
			//display results
			publishProgress(newBitmap);

			//no repetition needed
			if (0 == waitInterval) break;
			
			//thread interrupted
			if (mInterruptReload) break;
			
			//wait some times before reload the image again
			try {
				Thread.sleep(waitInterval * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	
	@Override
	protected void onProgressUpdate(Bitmap... values)
	{
		//different behavior for different situations
		if (null == values || 0 == values.length) {
			//start the progress animation
			startWindowProgressAnimation();
		} else {
			//display the bitmap and stop the progress animation
			assignBitmap(values[0]);
			stopWindowProgressAnimation();
			mImagedLoadedAtLeastOneTime = true;
		}
	}


	@Override
	protected void onCancelled() {
		mImageViewWhereShowBitmap = null;
		mWindowWhereUpdateProgress = null;
		mWebcam = null;
		mInterruptReload = true;
		super.onCancelled();
	}


	/**
	 * Start window progress animation
	 */
	private void startWindowProgressAnimation() {
		if (null != mWindowWhereUpdateProgress) {
			//progress image near the window title
			mWindowWhereUpdateProgress.get().setFeatureInt(Window.FEATURE_INDETERMINATE_PROGRESS,
	                Window.PROGRESS_INDETERMINATE_ON);
			mWindowWhereUpdateProgress.get().setFeatureInt(Window.FEATURE_INDETERMINATE_PROGRESS,
	                Window.PROGRESS_VISIBILITY_ON);
		}
	}	
		
	/**
	 * 
	 */
	private void stopWindowProgressAnimation() {
		//stop window progress animation
		if (null != mWindowWhereUpdateProgress) {
			//progress image near the window title
			mWindowWhereUpdateProgress.get().setFeatureInt(Window.FEATURE_INDETERMINATE_PROGRESS,
	                Window.PROGRESS_INDETERMINATE_OFF);
			mWindowWhereUpdateProgress.get().setFeatureInt(Window.FEATURE_INDETERMINATE_PROGRESS,
	                Window.PROGRESS_VISIBILITY_OFF);
		}
	}
		
	/**
	 * Load an image from an URL and put it in a Bitmap
	 * 
	 * @param imagePath
	 * @return the bitmap with the image or null if some errors happened
	 */
	private Bitmap loadBitmapFromUrl(final String imagePath)
	{
		try {
			//load the bitmap
			return mImageUrlProvider.loadBitmap(imagePath);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mFailBitmap;
	}

	/**
	 * Assign bitmap to the view and stop the progress indicator
	 * on the window
	 * 
	 * @param bitmap
	 */
	private void assignBitmap(Bitmap newBitmap) {
		//assign the bitmap to the view
		if (null != mImageViewWhereShowBitmap && null != newBitmap) {
				mImageViewWhereShowBitmap.get().setImageBitmap(newBitmap);
		} else {
			//set default bitmap
			//TODO
		}
	}

}
