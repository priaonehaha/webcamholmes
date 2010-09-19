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

package it.rainbowbreeze.webcamholmes.data;

import java.io.File;

import it.rainbowbreeze.webcamholmes.utils.TestHelper;
import android.test.AndroidTestCase;

/**
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public class AppPreferencesDaoTest extends AndroidTestCase {
	//---------- Constructor

	//---------- Private fields
	private AppPreferencesDao mAppPreferencesDao;

	//---------- Test initialization
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		mAppPreferencesDao = TestHelper.Factory.getAppPreferencesDao(getContext());
	}

	//---------- Test cases	
	public void testCleanResourcesToRemove() {
		//test clean method
		assertTrue("Cannot clean resources", mAppPreferencesDao.cleanResourcesToRemove());
		assertEquals("Wrong number of resources", 0, mAppPreferencesDao.getResourcesToRemove().length);
		
		String[] resources = new String[] {"Res1", "res2"};
		assertTrue("Cannot set resources", mAppPreferencesDao.setResourcesToRemove(resources));
		assertEquals("Wrong number of resources", 2, mAppPreferencesDao.getResourcesToRemove().length);
		
		assertTrue("Cannot clean resources", mAppPreferencesDao.cleanResourcesToRemove());
		assertEquals("Wrong number of resources", 0, mAppPreferencesDao.getResourcesToRemove().length);
	}
	
	
	public void testAddResourceToRemove() {
		assertTrue("Cannot clean resources", mAppPreferencesDao.cleanResourcesToRemove());
		assertEquals("Wrong number of resources", 0, mAppPreferencesDao.getResourcesToRemove().length);

		assertTrue("Cannot add resource", mAppPreferencesDao.addResourceToRemove("Res1"));
		assertEquals("Wrong number of resources", 1, mAppPreferencesDao.getResourcesToRemove().length);
		
		assertTrue("Cannot add resource", mAppPreferencesDao.addResourceToRemove("Res2"));
		assertEquals("Wrong number of resources", 2, mAppPreferencesDao.getResourcesToRemove().length);

		//a duplicated resources isn't inserted
		assertTrue("Added resource", mAppPreferencesDao.addResourceToRemove("Res2"));
		assertEquals("Wrong number of resources", 2, mAppPreferencesDao.getResourcesToRemove().length);

		assertTrue("Cannot add resource", mAppPreferencesDao.addResourceToRemove("Res3"));
		assertEquals("Wrong number of resources", 3, mAppPreferencesDao.getResourcesToRemove().length);
	}
	
	
	public void testSetResourcesToRemove() {
		assertTrue("Cannot clean resources", mAppPreferencesDao.cleanResourcesToRemove());
		assertEquals("Wrong number of resources", 0, mAppPreferencesDao.getResourcesToRemove().length);

		String[] exptectedResources = new String[]{
				getContext().getFilesDir().getAbsolutePath() + File.separator + "Test1",
				getContext().getFilesDir().getAbsolutePath() + File.separator + "Test2"
		};
		
		assertTrue("Cannot set resources", mAppPreferencesDao.setResourcesToRemove(exptectedResources));
		String[] currentResources = mAppPreferencesDao.getResourcesToRemove();
		assertEquals("Wrong number of resources", exptectedResources.length, currentResources.length);
		for (int i=0; i<exptectedResources.length; i++)
			assertEquals("Resource doesn't match", exptectedResources[i], currentResources[i]);
	}

	
	
	
	//---------- Private methods
}
