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
package it.rainbowbreeze.webcamholmes.logic;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.widget.ImageView;
import it.rainbowbreeze.libs.common.BaseResultOperation;
import it.rainbowbreeze.libs.log.BaseLogFacility;
import it.rainbowbreeze.libs.logic.BaseBackgroundThread;
import it.rainbowbreeze.libs.media.BaseImageMediaHelper;
import it.rainbowbreeze.webcamholmes.common.ResultOperation;

import static it.rainbowbreeze.libs.common.ContractHelper.*;

/**
 * Dump a bitmap to a file
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public class SaveWebcamImageThread extends BaseBackgroundThread<String> {

	//---------- Private fields
	private final BaseLogFacility mLogFacility;
	private final BaseImageMediaHelper mMediaHelper;
	private WeakReference<Bitmap> mBitmapToDump;
	private WeakReference<ImageView> mImageViewWithBitmapToDump;
	private final String mDumpFileName;
	
	
	//---------- Constructor
	
	/**
	 * 
	 */
	public SaveWebcamImageThread(
			BaseLogFacility logFacility,
			BaseImageMediaHelper imageMediaHelper,
			Context context,
			Handler handler,
			Bitmap bitmapToSave,
			String fileName)
	{
		super(context, handler);
		mLogFacility = checkNotNull(logFacility, "BaseLogFacility");
		mMediaHelper = checkNotNull(imageMediaHelper, "BaseImageMediaHelper");
		mDumpFileName = checkNotNullOrEmpty(fileName, "Dump file name");
		mBitmapToDump = new WeakReference<Bitmap>(checkNotNull(bitmapToSave, "Bitmap"));
	}

	/**
	 * 
	 */
	public SaveWebcamImageThread(
			BaseLogFacility logFacility,
			BaseImageMediaHelper imageMediaHelper,
			Context context,
			Handler handler,
			ImageView imageViewWithBitmapToSave,
			String fileName)
	{
		super(context, handler);
		mLogFacility = checkNotNull(logFacility, "BaseLogFacility");
		mMediaHelper = checkNotNull(imageMediaHelper, "BaseImageMediaHelper");
		mDumpFileName = checkNotNullOrEmpty(fileName, "Dump file name");
		mImageViewWithBitmapToDump = new WeakReference<ImageView>(checkNotNull(imageViewWithBitmapToSave, "ImageView with Bitmap"));
		mBitmapToDump = null;
	}
	
	
	
	//---------- Public properties
	public final static int WHAT_DUMP_WEBCAM_IMAGE = 1000;

	
	
	
	//---------- Public methods
	/* (non-Javadoc)
	 * @see it.rainbowbreeze.libs.logic.BaseBackgroundThread#run()
	 */
	@Override
	public void run() {
		Bitmap bitmap = null;

		//extract bitmap
		if (null == mBitmapToDump && null != mImageViewWithBitmapToDump.get()) {
			BitmapDrawable drawableBitmap = (BitmapDrawable) mImageViewWithBitmapToDump.get().getDrawable();
			if (null != drawableBitmap)
				bitmap = drawableBitmap.getBitmap();
		} else if (null != mBitmapToDump) {
			bitmap = mBitmapToDump.get();
		}
		
		if (null == bitmap) {
			mLogFacility.v("Cannot obtain a bitmap from the ImageView");
			mResultOperation = new BaseResultOperation<String>(ResultOperation.RETURNCODE_ERROR_APPLICATION_ARCHITECTURE, "Cannot obtain a bitmap from the ImageView");
		} else {
			//save the image
			mLogFacility.v("Dump bitmap to file " + mDumpFileName);
			mResultOperation = mMediaHelper.saveImage(getContext(), bitmap, mDumpFileName, CompressFormat.PNG, 9);
		}
		//and call the caller activity handler when the execution is terminated
		callHandlerAndRetry(WHAT_DUMP_WEBCAM_IMAGE);
	}

	
	
	
	//---------- Private methods
}
