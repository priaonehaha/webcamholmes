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
package it.rainbowbreeze.webcamholmes.data;

import it.rainbowbreeze.webcamholmes.domain.ItemWebcam;
import android.test.AndroidTestCase;

/**
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public class ItemsDaoTest extends AndroidTestCase {

	ItemsDao mDao;
	
	@Override
	protected void setUp() throws Exception {
		mDao = new ItemsDao();
	}
	
	public void testWebcamInsert() {
		ItemWebcam webcam = new ItemWebcam(0, 0, "Paris - Tour Eiffel", "http://www.parislive.net/eiffelwebcam01.jpg", 5);

		long webcamId = mDao.insertWebcam(webcam);
		
		ItemWebcam loadedWebcam = mDao.getWebcamById(webcamId);
		assertEquals("Wrong id", webcamId, loadedWebcam.getId());
		assertEquals("Wrong parentId", 0, loadedWebcam.getParentId());
		assertEquals("Wrong name", "Paris - Tour Eiffel", loadedWebcam.getName());
		assertEquals("Wrong url", "http://www.parislive.net/eiffelwebcam01.jpg", loadedWebcam.getImageUrl());
		assertEquals("Wrong interval", 5, loadedWebcam.getReloadInterval());
		
		mDao.deleteWebcam(webcamId);
	}
	
	public void testWebcamSave() {
		
	}

}
