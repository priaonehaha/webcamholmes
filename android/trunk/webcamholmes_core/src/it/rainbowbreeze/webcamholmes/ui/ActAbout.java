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

package it.rainbowbreeze.webcamholmes.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import it.rainbowbreeze.libs.common.ServiceLocator;
import it.rainbowbreeze.libs.ui.BaseAboutActivity;
import it.rainbowbreeze.webcamholmes.R;

import static it.rainbowbreeze.libs.common.ContractHelper.checkNotNull;

/**
 * Improuved About activity
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public class ActAbout extends BaseAboutActivity {
	//---------- Private fields
	private static final int OPTIONMENU_DISCOVER_NEW_WEBCAM = 4;
	
	private ActivityHelper mActivityHelper;
	
	
	//---------- Public properties

	
	
	
	//---------- Events
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivityHelper = checkNotNull(ServiceLocator.get(ActivityHelper.class), "ActivityHelper");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		
		if (!result) return result;
    	menu.add(0, OPTIONMENU_DISCOVER_NEW_WEBCAM, 10, R.string.actmain_mnuMoreWebcams)
			.setIcon(android.R.drawable.ic_menu_mapmode);
    	
    	return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case OPTIONMENU_DISCOVER_NEW_WEBCAM:
			mActivityHelper.launchAndroidMarketForMoreWebcams(this);
			break;

		default:
			return super.onOptionsItemSelected(item);
		}
		
		return true;
	}


	
	
	
	//---------- Public methods

	
	
	
	//---------- Private methods

}
