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

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import it.rainbowbreeze.libs.data.BaseAppPreferencesDao;
import it.rainbowbreeze.webcamholmes.common.GlobalDef;

public class AppPreferencesDao
	extends BaseAppPreferencesDao
{
	//---------- Constructor

	//---------- Private fields

	//---------- Public properties

	//---------- Events

	//---------- Public methods

	//---------- Private methods
	@Override
	protected String getPreferencesKey() {
		return GlobalDef.APP_PREFERENCES_KEY;
	}

	@Override
	protected void backupProperties(Editor editorBackup) {
	}

	@Override
	protected void restoreProperties(SharedPreferences settingsBackup) {
	}
}
