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

package it.rainbowbreeze.webcamholmes.spiders;

import it.rainbowbreeze.webcamholmes.domain.ItemWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


/**
 * @author Alfredo "Rainbowbreeze" Morresi
 */

public abstract class BaseSpider {
	//---------- Private fields
	protected final long mRootParentAliasId;
	protected final long mReservedAliasIdStart;
	protected final long mReservedAliasIdStop;
	
	
	
	//---------- Constructor
	public BaseSpider(String spiderName, long rootParentAliasId, long reservedAliasIdStart, long reservedAliasIdStop) {
		mName = spiderName;
		mRootParentAliasId = rootParentAliasId;
		mReservedAliasIdStart = reservedAliasIdStart;
		mReservedAliasIdStop = reservedAliasIdStop;
	}
	
	

	//---------- Public properties
	protected final String mName;
	public String getName()
	{ return mName; }

	
	

	//---------- Public methods
	public abstract void parseResource(List<ItemWrapper> items);

	
	
	
	//---------- Private methods
	
    protected String getPageContent(String url) {
    	StringBuilder sb = new StringBuilder();
    	
		try { 
		    URL pageUrl = new URL(url);
		    BufferedReader in = new BufferedReader(
		    		new InputStreamReader(pageUrl.openStream())); 
			String inputLine; 
			
			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine);
		    } 
		    in.close(); 
		
		} catch (MalformedURLException me) { 
		    System.out.println(me); 
		    return null;
		
		} catch (IOException ioe) { 
		    System.out.println(ioe); 
		    return null;
		}
		
		return sb.toString();
    }
    

}
