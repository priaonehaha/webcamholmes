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

import it.rainbowbreeze.libs.common.RainbowLogFacility;
import it.rainbowbreeze.libs.ui.RainbowActivityHelper;
import it.rainbowbreeze.webcamholmes.R;
import it.rainbowbreeze.webcamholmes.common.App;
import it.rainbowbreeze.webcamholmes.common.ResultOperation;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * Generic helper for activities task
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 */
public class ActivityHelper
	extends RainbowActivityHelper
{
	//---------- Private fields




	//---------- Constructors
	/**
	 * @param logFacility
	 * @param context
	 */
	public ActivityHelper(RainbowLogFacility logFacility, Context context) {
		super(logFacility, context);
	}

	
	
	
	//---------- Public properties
	public static final String INTENTKEY_SENDLOGREPORT = "SendLogReport";
	public static final String INTENTKEY_WEBCAMID = "WebcamId";
	public static final String INTENTKEY_IMAGETODISPLAY_PATH = "imageToDisplay";
	
	private static final int MSG_INDEX_ERROR_IMPORT_FROM_RESOURCE = 10;

	
	

	//---------- Public methods
	
	public void openMain(Activity callerActivity, Bundle extras){
		openActivity(callerActivity, ActMain.class, extras, true, REQUESTCODE_MAINACTIVITY);
	}
	

	@Override
	public void openAbout(
			Activity callerActivity,
			String appName,
			String appVersion,
			String contactEmail) {
		openAbout(callerActivity, ActAbout.class, appName, appVersion, contactEmail);
	}
	
	
	/**
	 * Open Settings activity
	 * 
	 * @param callerActivity caller activity
	 */
	public void openSettingsMain(Activity callerActivity, boolean mustSendLogReport)
	{
		openSettingsMain(
				callerActivity,
				mustSendLogReport,
				App.APP_DISPLAY_NAME,
				App.APP_INTERNAL_VERSION,
				App.EMAIL_FOR_LOG,
				App.LOG_TAG);
	}
	
	/**
	 * Open Webcam activity
	 * 
	 * @param callerActivity caller activity
	 */
	public void openShowWebcam(Activity callerActivity, long webcamId)
	{
		Bundle extras = new Bundle();
		extras.putString(INTENTKEY_WEBCAMID, String.valueOf(webcamId));
		
		openActivity(callerActivity, ActWebcam.class, extras, true, REQUESTCODE_NONE);
	}
	
	/**
	 * Opens the ImageFullscreen browser activity
	 * 
	 * @param callerActivity
	 * @param imageFile 
	 */
	public void openFullscreenImageActivity(Activity callerActivity, String imageFile){
		Bundle extras = new Bundle();
		extras.putString(INTENTKEY_IMAGETODISPLAY_PATH, imageFile);

		openActivity(callerActivity, ActImageFullscreen.class, extras, false, REQUESTCODE_NONE);
	}
	

	/**
	 * Process in a standard way the result of SmsService extended command
	 * execution
	 * 
	 * @param context
	 * @param result
	 */
	public void showCommandExecutionResult(Context context, ResultOperation<String> result)
	{
		//show command results
		if (result.hasErrors()) {
			reportError(context, result);
		} else {
			if (!TextUtils.isEmpty(result.getResult())){
				//shows the output of the command
				mBaseLogFacility.i(result.getResult());
				showInfo(context, result.getResult());
			}
		}		
	}

	
	/**
	 * Open the market searching for more WebcamHolmes family application
	 * @param callerActivity
	 */
	public void launchAndroidMarketForMoreWebcams(Activity callerActivity) {
		launchAndroidMarket(callerActivity,
				"pub:\"Alfredo Morresi\"");
	}


	
	
	//---------- Protected methods

	@Override
	protected void loadCustomMessageStrings(Context context, Map<Integer, String> messages) {
		super.loadCustomMessageStrings(context, messages);
		messages.put(MSG_INDEX_ERROR_IMPORT_FROM_RESOURCE, context.getString(R.string.common_msg_errorImportingFromResource));
	}
	
	@Override
	public String getErrorMessage(int returnCode, Exception exception) {
		//First of all, examines return code for standard errors
		String userMessage = null;
		String exceptionMessage = null != exception ? exception.getMessage() : getMessage(MSG_INDEX_NO_ERROR_MESSAGE);

		switch (returnCode) {
			case ResultOperation.RETURNCODE_ERROR_IMPORT_FROM_RESOURCE:
				userMessage = String.format(
					getMessage(MSG_INDEX_ERROR_IMPORT_FROM_RESOURCE), exceptionMessage);
			break;

			default:
				userMessage = super.getErrorMessage(returnCode, exception);
				break;
					
		}
		
		return userMessage;
	}




	//---------- Private methods
	
}
