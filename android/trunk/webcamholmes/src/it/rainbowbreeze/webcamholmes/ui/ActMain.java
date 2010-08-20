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
package it.rainbowbreeze.webcamholmes.ui;

import java.util.List;

import it.rainbowbreeze.webcamholmes.R;
import it.rainbowbreeze.webcamholmes.common.App;
import it.rainbowbreeze.webcamholmes.data.ItemsProvider;
import it.rainbowbreeze.webcamholmes.domain.ItemToDisplay;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Application main activity, display a list
 * of available categories and webcams
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public class ActMain
	extends ListActivity
{
	//---------- Private fields
	private List<ItemToDisplay> mItemsToDisplay;




	//---------- Public properties

	
	
	
	//---------- Events
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actmain);
        
        setTitle(R.string.actmain_lblTitle);
        
        //setup the list of webcams to show
        mItemsToDisplay = ItemsProvider.instance().getAllItemsList();
        ArrayAdapter<ItemToDisplay> mItemsListAdapter = new ArrayAdapter<ItemToDisplay>(
        		this, android.R.layout.simple_list_item_1, mItemsToDisplay);
        setListAdapter(mItemsListAdapter);
        
		//register the context menu to defaul ListView of the view
		//alternative method:
		//http://www.anddev.org/creating_a_contextmenu_on_a_listview-t2438.html
		registerForContextMenu(getListView());        
    }


	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id) {
		ItemToDisplay item = mItemsToDisplay.get(position);
		
		App.instance().getActivityHelper().openShowWebcam(this, item.getId());
	}

	//---------- Public methods
	

	
	
	//---------- Private methods
	
}