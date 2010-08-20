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

import it.rainbowbreeze.webcamholmes.domain.ItemToDisplay;
import it.rainbowbreeze.webcamholmes.domain.ItemWebcam;

import java.util.List;

/**
 * Provider for categories and webcams
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 */
public class ItemsProvider {
	//---------- Constructor

	
	
	
	//---------- Private fields

	
	
	
	//---------- Public properties
	
	/** Singleton */
	private static ItemsProvider mInstance;
	public static ItemsProvider instance()
	{
		if (null == mInstance) mInstance = new ItemsProvider();
		return mInstance;
	}


    /** List of category and webcam */
	protected List<ItemToDisplay> mItemsList;
	public List<ItemToDisplay> getAllItemsList()
	{ return mItemsList; }
	public void setAllItemsList(List<ItemToDisplay> newValue)
	{ mItemsList = newValue; }

	
	//---------- Public methods
	public ItemWebcam getWebcamById(String webcamId) {
		//TODO
		ItemWebcam webcam = new ItemWebcam("1", "Paris - Tour Eiffel", "http://www.parislive.net/eiffelwebcam01.jpg");
		//ItemWebcam webcam = new ItemWebcam("1", "Test location", "http://amrc.ssec.wisc.edu/~amrc/webcam/b15k/20050216_02.jpg");
		webcam.setReloadInterval(5);
		return webcam;
	}
	
	
	
	
	//---------- Private methods
}
