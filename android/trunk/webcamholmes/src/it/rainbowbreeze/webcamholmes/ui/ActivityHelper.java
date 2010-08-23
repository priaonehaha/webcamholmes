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

import it.rainbowbreeze.libs.log.LogFacility;
import it.rainbowbreeze.libs.ui.BaseActivityHelper;
import it.rainbowbreeze.webcamholmes.R;
import it.rainbowbreeze.webcamholmes.common.ResultOperation;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

/**
 * Generic helper for activities task
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 */
public class ActivityHelper
	extends BaseActivityHelper
{
	//---------- Constructors
	public ActivityHelper(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	
	
	
	//---------- Private fields




	//---------- Public properties
	public final static String INTENTKEY_SENDLOGREPORT = "SendLogReport";
	public final static String INTENTKEY_WEBCAMID = "WebcamId";
	
	private static final int MSG_INDEX_EMPTY_REPLY = 10;
	private static final int MSG_INDEX_ERROR_LOAD_PROVIDER_DATA = 11;
	private static final int MSG_INDEX_ERROR_SAVE_PROVIDER_DATA = 12;

	
	

	//---------- Public methods
	
//	/**
//	 * Open Settings activity
//	 * 
//	 * @param callerActivity caller activity
//	 */
//	public void openSettingsMain(Activity callerActivity)
//	{
//		openSettingsMain(callerActivity, false);
//	}
//	
//	/**
//	 * Open Settings activity and call error report
//	 * 
//	 * @param callerActivity caller activity
//	 */
//	public void openSettingsMain(Activity callerActivity, boolean callErrorReport)
//	{
//		Map<String, String> extraData = new HashMap<String, String>();
//		if (callErrorReport) {
//			extraData.put(INTENTKEY_SENDLOGREPORT, "true");
//		}
//		openActivity(callerActivity, ActSettingsMain.class, extraData, true, REQUESTCODE_SETTINGS);
//	}
	
	/**
	 * Open Webcam activity
	 * 
	 * @param callerActivity caller activity
	 */
	public void openShowWebcam(Activity callerActivity, long webcamId)
	{
		Map<String, String> extraData = new HashMap<String, String>();
		extraData.put(INTENTKEY_WEBCAMID, String.valueOf(webcamId));
		
		openActivity(callerActivity, ActWebcam.class, extraData, true, REQUESTCODE_NONE);
	}
//	
//
//	/**
//	 * Open about activity
//	 */
//	public void openAbout(Activity callerActivity)
//	{
//		LogFacility.i("Launching activity About");
//		openActivity(callerActivity, ActAbout.class, null, false, REQUESTCODE_NONE);
//	}
	
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
				LogFacility.i(result.getResult());
				showInfo(context, result.getResult());
			}
		}		
	}


	
	
	//---------- Protected methods
	protected Map<Integer, String> loadMessagesStrings(Context context) {
		Map<Integer, String> messages = super.loadMessagesStrings(context);
		messages.put(MSG_INDEX_EMPTY_REPLY, context.getString(R.string.common_msg_noReplyFromProvider));
		messages.put(MSG_INDEX_ERROR_LOAD_PROVIDER_DATA, context.getString(R.string.common_msg_cannotLoadProviderData));
		messages.put(MSG_INDEX_ERROR_SAVE_PROVIDER_DATA, context.getString(R.string.common_msg_cannotSaveProviderData));
		return messages;
	}

	
	protected String getErrorDescription(int returnCode, Exception exception) {
		//First of all, examines return code for standard errors
		String userMessage = null;
		String exceptionMessage = null != exception ?  exception.getMessage() : getMessage(MSG_INDEX_NO_ERROR_MESSAGE);

		//TODO
		switch (returnCode) {
			case ResultOperation.RETURNCODE_ERROR_EMPTY_REPLY:
				userMessage = String.format(
						getMessage(MSG_INDEX_EMPTY_REPLY), exceptionMessage);
				break;
			case ResultOperation.RETURNCODE_ERROR_LOAD_PROVIDER_DATA:
				userMessage = String.format(
						getMessage(MSG_INDEX_ERROR_LOAD_PROVIDER_DATA), exceptionMessage);
				break;
			case ResultOperation.RETURNCODE_ERROR_SAVE_PROVIDER_DATA:
				userMessage = String.format(
						getMessage(MSG_INDEX_ERROR_SAVE_PROVIDER_DATA), exceptionMessage);
				break;
			default:
				super.getErrorDescription(returnCode, exception);
				break;
					
		}
		
		return userMessage;
	}




	//---------- Private methods
	
}
