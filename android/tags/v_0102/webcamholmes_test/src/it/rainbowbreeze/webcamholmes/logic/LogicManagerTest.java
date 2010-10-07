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

package it.rainbowbreeze.webcamholmes.logic;

import it.rainbowbreeze.libs.common.RainbowResultOperation;
import it.rainbowbreeze.webcamholmes.data.AppPreferencesDao;
import it.rainbowbreeze.webcamholmes.utils.TestHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.test.AndroidTestCase;

/**
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public class LogicManagerTest extends AndroidTestCase {
	//---------- Constructor

	
	
	//---------- Private fields
	private LogicManager mLogicManager;


	
	
	//---------- Test initialization
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		mLogicManager = TestHelper.Factory.getLogicManager(getContext());
	}

	
	
	//---------- Test cases	
	public void testDeleteTempResources() throws IOException {
		AppPreferencesDao mAppPreferencesDao;
		FileOutputStream fos;
		String[] fileFullNames;
		
		mAppPreferencesDao = TestHelper.Factory.getAppPreferencesDao(getContext());
		fileFullNames = new String[]{
				getContext().getFilesDir().getAbsolutePath() + File.separator + "Test1",
				getContext().getFilesDir().getAbsolutePath() + File.separator + "Test2"
		};
		
		//add test files
		mAppPreferencesDao.cleanResourcesToRemove();
		for (String fileName:fileFullNames) {
			//load some file that must be removed when the application ends
			File file = new File(fileName);
			fos = getContext().openFileOutput(file.getName(), Context.MODE_PRIVATE);
			fos.write(("test file " + fileName).getBytes());
			fos.close();
			fos = null;
			assertTrue("Cannot add resource", mAppPreferencesDao.addResourceToRemove(fileName));
		}
		
		RainbowResultOperation<Void> res = mLogicManager.deleteTempResources(getContext());
		
		assertFalse("Error in operation", res.hasErrors());
		//checks if files were deleted
		for (String fileName:fileFullNames) {
			File file = new File(fileName);
			assertFalse("File still exists: " + fileName, file.exists());
		}
	}

	
	
	//---------- Private methods

}
