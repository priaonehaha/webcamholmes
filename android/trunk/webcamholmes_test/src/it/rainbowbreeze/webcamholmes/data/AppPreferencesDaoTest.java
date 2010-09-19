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
		mAppPreferencesDao.cleanResourcesToRemove();
		assertTrue("Cannot save resources", mAppPreferencesDao.save());
		assertEquals("Wrong number of resources", 0, mAppPreferencesDao.getResourcesToRemove().length);
		
		String[] resources = new String[] {"Res1", "res2"};
		mAppPreferencesDao.setResourcesToRemove(resources);
		assertTrue("Cannot save resources", mAppPreferencesDao.save());
		assertEquals("Wrong number of resources", 2, mAppPreferencesDao.getResourcesToRemove().length);
		
		mAppPreferencesDao.cleanResourcesToRemove();
		assertTrue("Cannot save resources", mAppPreferencesDao.save());
		assertEquals("Wrong number of resources", 0, mAppPreferencesDao.getResourcesToRemove().length);
	}
	
	
	public void testAddResourceToRemove() {
		mAppPreferencesDao.cleanResourcesToRemove();
		assertTrue("Cannot save resources", mAppPreferencesDao.save());
		assertEquals("Wrong number of resources", 0, mAppPreferencesDao.getResourcesToRemove().length);

		mAppPreferencesDao.addResourceToRemove("Res1");
		assertTrue("Cannot save resources", mAppPreferencesDao.save());
		assertEquals("Wrong number of resources", 1, mAppPreferencesDao.getResourcesToRemove().length);
		
		mAppPreferencesDao.addResourceToRemove("Res2");
		assertTrue("Cannot save resources", mAppPreferencesDao.save());
		assertEquals("Wrong number of resources", 2, mAppPreferencesDao.getResourcesToRemove().length);

		//a duplicated resources isn't inserted
		mAppPreferencesDao.addResourceToRemove("Res2");
		assertTrue("Cannot save resources", mAppPreferencesDao.save());
		assertEquals("Wrong number of resources", 2, mAppPreferencesDao.getResourcesToRemove().length);

		mAppPreferencesDao.addResourceToRemove("Res3");
		assertTrue("Cannot save resources", mAppPreferencesDao.save());
		assertEquals("Wrong number of resources", 3, mAppPreferencesDao.getResourcesToRemove().length);
	}

	//---------- Private methods
}
