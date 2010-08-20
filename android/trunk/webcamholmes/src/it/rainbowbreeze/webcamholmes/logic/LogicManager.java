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
package it.rainbowbreeze.webcamholmes.logic;

import java.util.ArrayList;
import java.util.List;

import it.rainbowbreeze.webcamholmes.common.ResultOperation;
import it.rainbowbreeze.webcamholmes.data.ItemsProvider;
import it.rainbowbreeze.webcamholmes.domain.ItemToDisplay;
import it.rainbowbreeze.webcamholmes.domain.ItemWebcam;
import android.content.Context;

/**
 * @author Alfredo "Rainbowbreeze" Morresi
 */
public class LogicManager {
	//---------- Constructor

	
	
	
	//---------- Private fields

	
	
	
	//---------- Public properties
	/**
	 * Initializes data, execute begin operation
	 */
	public static ResultOperation<Void> executeBeginTask(Context context)
	{
		ResultOperation<Void> res = new ResultOperation<Void>();
		
		
		List<ItemToDisplay> items = new ArrayList<ItemToDisplay>();
		items.add(new ItemWebcam("0", "Webcam 1", ""));
		items.add(new ItemWebcam("1", "Webcam 2", ""));
		ItemsProvider.instance().setAllItemsList(items);

		return res;
	}

	/**
	 * Executes final operation, just before the app close
	 * @param context
	 * @return
	 */
	public static ResultOperation<Void> executeEndTast(Context context)
	{
		ResultOperation<Void> res = new ResultOperation<Void>();

		return res;
	}

	
	
	
	//---------- Public methods

	
	
	
	//---------- Private methods
}
