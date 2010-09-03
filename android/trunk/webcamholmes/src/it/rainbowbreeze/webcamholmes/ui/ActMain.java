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

import java.util.List;

import it.rainbowbreeze.libs.common.ServiceLocator;
import it.rainbowbreeze.libs.log.BaseLogFacility;
import it.rainbowbreeze.libs.logic.BaseCrashReporter;
import it.rainbowbreeze.libs.logic.SendStatisticsTask;
import it.rainbowbreeze.webcamholmes.R;
import it.rainbowbreeze.webcamholmes.common.App;
import it.rainbowbreeze.webcamholmes.common.GlobalDefs;
import it.rainbowbreeze.webcamholmes.data.ItemsDao;
import it.rainbowbreeze.webcamholmes.domain.ItemToDisplay;
import android.app.Dialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static it.rainbowbreeze.libs.common.ContractHelper.*;

/**
 * Application main activity, display a list
 * of available categories and webcams
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public class ActMain
	extends ListActivity
{
	//---------- Private fields
	private final static String PROP_KEY_CURRENT_PARENT_ITEM_ID = "CurrentParentItemId";
	
	private static final int DIALOG_SEND_CRASH_REPORTS = 10;
	private static final int DIALOG_STARTUP_INFOBOX = 11;

	private List<ItemToDisplay> mItemsToDisplay;
	private long mCurrentCategoryId = 0;
	private BaseLogFacility mLogFacility;
	private ActivityHelper mActivityHelper;
	private ItemsDao mItemsDao;

	private final static int OPTIONMENU_SETTINGS = 2;
	private final static int OPTIONMENU_ABOUT = 3;




	//---------- Public properties

	
	
	
	//---------- Events
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    	//checks for app was correctly initialized
    	if (!App.i().isCorrectlyInitialized()) {
    		//application is expired
            setContentView(R.layout.actinitializationerror);
            setTitle(String.format(
            		getString(R.string.actinitialization_title), GlobalDefs.APP_NAME));
    		return;
    	}
    	
        mLogFacility = checkNotNull(ServiceLocator.get(BaseLogFacility.class), "LogFacility");
        mItemsDao = checkNotNull(ServiceLocator.get(ItemsDao.class), "ItemsDao");
        mActivityHelper = checkNotNull(ServiceLocator.get(ActivityHelper.class), "ActivityHelper");
        
        setContentView(R.layout.actmain);
        setTitle(R.string.actmain_lblTitle);
        
		//register the context menu to defaul ListView of the view
		//alternative method:
		//http://www.anddev.org/creating_a_contextmenu_on_a_listview-t2438.html
		registerForContextMenu(getListView());
		
    	//executed when the application first runs
        if (null == savedInstanceState) {
    		mLogFacility.i("App started: " + GlobalDefs.APP_NAME);
        	//send statistics data first time the app runs
//	        SendStatisticsTask statsTask = new SendStatisticsTask(mLogFacility, );
//	        Thread t = new Thread(statsTask);
//	        t.start();

//	        //load values of view from previous application execution
//        	restoreLastRunViewValues();
//
//        	//show info dialog, if needed
//        	if (App.i().isStartupInfoboxRequired())
//        		showDialog(DIALOG_STARTUP_INFOBOX);
//        	
        	//checks for previous crash reports
    		BaseCrashReporter crashReporter = checkNotNull(ServiceLocator.get(BaseCrashReporter.class), "CrashReporter");
        	if (crashReporter.isCrashReportPresent(this)) {
        		showDialog(DIALOG_SEND_CRASH_REPORTS);
        	}
        }
    }
    
    
    
    @Override
    protected void onStart() {
    	super.onStart();
    	loadNewLevel(mCurrentCategoryId);
    }


	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id) {
		ItemToDisplay item = mItemsToDisplay.get(position);
		
		if (item.hasChildren()) {
			//it's a category
			loadNewLevel(item.getId());
		} else {
			//it's a webcam
			mActivityHelper.openShowWebcam(this, item.getId());
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong(PROP_KEY_CURRENT_PARENT_ITEM_ID, mCurrentCategoryId);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle state) {
		mCurrentCategoryId = state.getLong(PROP_KEY_CURRENT_PARENT_ITEM_ID);
		super.onRestoreInstanceState(state);
	}
	

	/**
	 * Intercept when the user press the Back button and create an event tracking
	 * of the event
	 * @param keyCode
	 * @param event
	 * @return
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

	/**
	 * Intercept when the user release the Back button, call the method for
	 * saving data and close the activity
	 * @param keyCode
	 * @param event
	 * @return
	 */
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            backOnCategory();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    
    /* (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	//errors on initialization
    	if (!App.i().isCorrectlyInitialized()) return true;

		menu.add(0, OPTIONMENU_SETTINGS, 1, R.string.actmain_mnuSettings)
			.setIcon(android.R.drawable.ic_menu_preferences);
    	menu.add(0, OPTIONMENU_ABOUT, 2, R.string.actmain_mnuAbout)
    		.setIcon(android.R.drawable.ic_menu_info_details);
		return true;
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case OPTIONMENU_SETTINGS:
			mActivityHelper.openSettingsMain(this, false);
			break;
			
		case OPTIONMENU_ABOUT:
			mActivityHelper.openAbout(this, GlobalDefs.APP_NAME, GlobalDefs.APP_VERSION_DESCRIPTION, GlobalDefs.EMAIL_FOR_LOG);
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
    	Dialog retDialog = null;
    	
    	switch (id) {
    	case DIALOG_STARTUP_INFOBOX:
    		retDialog = mActivityHelper.createInformativeDialog(this,
    				this.getString(R.string.actsendsms_msg_infobox_title),
    				this.getString(R.string.actabout_lblDescription) + "\n\n" + this.getString(R.string.actabout_msgChangeslog),
    				this.getString(R.string.common_btnOk));
    		break;
    	
    	case DIALOG_SEND_CRASH_REPORTS:
    		retDialog = mActivityHelper.createSendCrashReportRequestDialog(
    				ServiceLocator.get(BaseCrashReporter.class),
    				this,
    				GlobalDefs.APP_NAME,
    				GlobalDefs.APP_VERSION,
    				GlobalDefs.EMAIL_FOR_LOG,
    				GlobalDefs.LOG_TAG);
    		break;
    		
		default:
			retDialog = super.onCreateDialog(id);
    	}
    	
    	return retDialog;
    }

    
    
    
	//---------- Public methods
	

	
	
	//---------- Private methods
	private void loadNewLevel(long parentId) {
        //setup the list of webcams and categories to show
		mCurrentCategoryId = parentId;
        mItemsToDisplay = mItemsDao.getChildrenOfParentItem(mCurrentCategoryId);
        ArrayAdapter<ItemToDisplay> mItemsListAdapter = new ArrayAdapter<ItemToDisplay>(
        		this, android.R.layout.simple_list_item_1, mItemsToDisplay);
        setListAdapter(mItemsListAdapter);
    }
	
    /**
	 * Navigate one category  back or close the application if the category is the first
	 */
	private void backOnCategory() {
		if (0 == mCurrentCategoryId) {
			finish();
			return;
		}
		
		//find previous category
		long parentId = mItemsDao.getParentIdOfCategory(mCurrentCategoryId);
		loadNewLevel(parentId);
	}

}