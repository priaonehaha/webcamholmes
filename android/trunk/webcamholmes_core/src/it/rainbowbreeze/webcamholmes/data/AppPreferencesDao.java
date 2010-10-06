/**
 * Copyright (C) 2010 Alfredo Morresi
 * 
 * This file is part of Webcam project.
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

package it.rainbowbreeze.webcamholmes.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import it.rainbowbreeze.libs.data.RainbowAppPreferencesDao;
import it.rainbowbreeze.libs.helper.RainbowStringHelper;

public class AppPreferencesDao
	extends RainbowAppPreferencesDao
{
	//---------- Private fields
	protected static final String PROP_LATEST_CATEGORY = "latestCategory";
	protected static final String PROP_RESOURCE_TO_REMOVE = "resourceToRemove";
	protected static final String RESOURCES_SEPARATOR = "#@#@##@";



	//---------- Constructor
	/**
	 * @param context
	 * @param preferenceKey
	 */
	public AppPreferencesDao(Context context, String preferenceKey) {
		super(context, preferenceKey);
	}

	
	
	
	//---------- Public properties
	public long getLatestCategory()
	{ return mSettings.getLong(PROP_LATEST_CATEGORY, 0); }
    public void setLatestCategory(long newValue)
    { mEditor.putLong(PROP_LATEST_CATEGORY, newValue); }

	public String[] getResourcesToRemove(){
		String allResources = mSettings.getString(PROP_RESOURCE_TO_REMOVE, "");
		
		if (TextUtils.isEmpty(allResources))
			return new String[]{};
		else
			return allResources.split(RESOURCES_SEPARATOR);
		
	}
    public boolean setResourcesToRemove(String[] newValue){
    	mEditor.putString(PROP_RESOURCE_TO_REMOVE, RainbowStringHelper.join(newValue, RESOURCES_SEPARATOR));
    	return save();
	}
    public boolean addResourceToRemove(String newValue){
    	if (TextUtils.isEmpty(newValue)) return false;
    	
    	String[] resources = getResourcesToRemove();
    	boolean exists = (null != resources);
    	
    	//find if the element already exists
    	if (exists) {
    		exists = false;
    		for(String resource:resources) {
    			if (newValue.equals(resource)) {
    				exists = true;
    				break;
    			}
    		}
    	}
    	
    	//true because the item is in the resources, at the end
    	if (exists) return true;

    	//add the new resource
    	String allResources = mSettings.getString(PROP_RESOURCE_TO_REMOVE, "");
    	if (TextUtils.isEmpty(allResources))
    		allResources = newValue;
    	else
    		allResources += RESOURCES_SEPARATOR + newValue;
    	
    	//persists resources
    	mEditor.putString(PROP_RESOURCE_TO_REMOVE, allResources);
    	return save();
	}
    /**
     * Remove all resource to remove
     */
    public boolean cleanResourcesToRemove() {
    	mEditor.putString(PROP_RESOURCE_TO_REMOVE, "");
    	return save();
    }


	
	
	//---------- Public methods

	
	
	
	//---------- Private methods

	@Override
	protected void backupProperties(Editor editorBackup) {
	}

	@Override
	protected void restoreProperties(SharedPreferences settingsBackup) {
	}
}
