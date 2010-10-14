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

package it.rainbowbreeze.webcamholmes.domain;

import android.text.TextUtils;
import junit.framework.TestCase;

/**
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public class ItemWebcamTest extends TestCase {
	//---------- Constructor

	//---------- Private fields

	//---------- Public properties

	//---------- Test methods
	public void testFactoryMethods() {
		ItemWebcam webcam;
		
		webcam = ItemWebcam.Factory.getSystemWebcam(100, "Webcam di prova", "www.url.com/test.img", 60);
		assertEquals("Wrong id", 0, webcam.getId());
		assertEquals("Wrong parent id", 100, webcam.getParentId());
		assertEquals("Wrong name", "Webcam di prova", webcam.getName());
		assertEquals("Wrong type", 1, webcam.getType());
		assertEquals("Wrong imageUrl", "www.url.com/test.img", webcam.getImageUrl());
		assertEquals("Wrong refresh interval", 60, webcam.getReloadInterval());
		assertFalse("Wrong preferred status", webcam.isPreferred());
		assertFalse("Wrong user created", webcam.isUserCreated());
		assertTrue("Wrong freeData1", TextUtils.isEmpty(webcam.getFreeData1()));
		assertTrue("Wrong freeData2", TextUtils.isEmpty(webcam.getFreeData2()));
		assertTrue("Wrong freeData3", TextUtils.isEmpty(webcam.getFreeData3()));

		webcam = ItemWebcam.Factory.getUserWebcam(93, "Webcam di prova 1", "www.url2.com/car.jpg", 20);
		assertEquals("Wrong id", 0, webcam.getId());
		assertEquals("Wrong parent id", 93, webcam.getParentId());
		assertEquals("Wrong name", "Webcam di prova 1", webcam.getName());
		assertEquals("Wrong type", 1, webcam.getType());
		assertEquals("Wrong imageUrl", "www.url2.com/car.jpg", webcam.getImageUrl());
		assertEquals("Wrong refresh interval", 20, webcam.getReloadInterval());
		assertFalse("Wrong preferred status", webcam.isPreferred());
		assertTrue("Wrong user created", webcam.isUserCreated());
		assertTrue("Wrong freeData1", TextUtils.isEmpty(webcam.getFreeData1()));
		assertTrue("Wrong freeData2", TextUtils.isEmpty(webcam.getFreeData2()));
		assertTrue("Wrong freeData3", TextUtils.isEmpty(webcam.getFreeData3()));
		
		webcam = ItemWebcam.Factory.getWebcamTravelWebcam(42, "WebcamTravel Webcam", "http://webcam.travel/webcam10.png", "User1", "http://forum.user1.it", "http://webcam.travel/webcam10");
		assertEquals("Wrong id", 0, webcam.getId());
		assertEquals("Wrong parent id", 42, webcam.getParentId());
		assertEquals("Wrong name", "WebcamTravel Webcam", webcam.getName());
		assertEquals("Wrong type", 2, webcam.getType());
		assertEquals("Wrong imageUrl", "http://webcam.travel/webcam10.png", webcam.getImageUrl());
		assertEquals("Wrong refresh interval", 30, webcam.getReloadInterval());
		assertFalse("Wrong preferred status", webcam.isPreferred());
		assertFalse("Wrong user created", webcam.isUserCreated());
		assertEquals("Wrong freeData1", "User1", webcam.getFreeData1());
		assertEquals("Wrong freeData2", "http://forum.user1.it", webcam.getFreeData2());
		assertEquals("Wrong freeData3", "http://webcam.travel/webcam10", webcam.getFreeData3());
	}
	

	//---------- Private methods

}
