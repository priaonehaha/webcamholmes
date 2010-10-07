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

import it.rainbowbreeze.webcamholmes.domain.ItemCategory;
import it.rainbowbreeze.webcamholmes.domain.ItemWebcam;
import it.rainbowbreeze.webcamholmes.domain.ItemWrapper;

import java.util.List;

/**
 * Create webcam from BBC London traffic webcams
 *  http://www.bbc.co.uk/london/travel/jam_cams/full_list/
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 */
public class BBCLondonTrafficSpider extends BaseSpider {
	//---------- Private fields
	private static final String PAGE_URL = "http://www.bbc.co.uk/london/travel/jam_cams/full_list/";
	private static final String ADDRESS_TOKEN_START = "<a href=\"/london/content/webcams/";
	private static final String ADDRESS_TOKEN_END = ".shtml\"";
	private static final String NAME_TOKEN_START_1 = "title=\"\">";
	private static final String NAME_TOKEN_START_2 = ".shtml\">";
	private static final String NAME_TOKEN_END = "</a></li>";
	private static final String WEBCAM_START_ADDRESS = "http://www.bbc.co.uk/cgi-perl/webcams/camcache.pl?r=60&h=mcs&l=london/webcams/london/";
	private static final String WEBCAM_END_ADDRESS = ".jpg";
	
	
	
	//---------- Constructor
	
	/**
	 * @param placeToSearch
	 * @param spiderName
	 * @param rootParentAliasId
	 * @param reservedAliasIdStart
	 * 
	 */
	public BBCLondonTrafficSpider(String spiderName, long rootParentAliasId, long reservedAliasIdStart, long reservedAliasIdStop) {
		super(spiderName, rootParentAliasId, reservedAliasIdStart, reservedAliasIdStop);
	}


	
	
	//---------- Public properties
	
	
	
	
	//---------- Public methods
	/* (non-Javadoc)
	 * @see it.rainbowbreeze.webcamholmes.spiders.BaseSpider#parseResource()
	 */
	@Override
	public void parseResource(List<ItemWrapper> items)
	{
		//current alias id to use
		long currentAliasId = mReservedAliasIdStart;
	
		//get the page
		String sourcePage = getPage();
		
		//create category for the traffic webcams
		ItemCategory cat = new ItemCategory(0, currentAliasId, 0, "Traffic", false);
		cat.setParentAliasId(mRootParentAliasId);
		items.add(new ItemWrapper(cat));

		//java way to pass parameters by reference :(
		String[] sourceArray = new String[1];
		sourceArray[0] = sourcePage;
		
		//parse webcams
		while(true) {
			String webcamAddress = getStringBetween(sourceArray, ADDRESS_TOKEN_START, ADDRESS_TOKEN_END);
			String webcamName = getStringBetween(sourceArray, NAME_TOKEN_START_1, NAME_TOKEN_END);
			if (null == webcamName || "".equals(webcamName)) webcamName = getStringBetween(sourceArray, NAME_TOKEN_START_2, NAME_TOKEN_END);
			
			if (null == webcamAddress || "".equals(webcamAddress))
				break;
			
			//add new webcam
			webcamAddress = WEBCAM_START_ADDRESS + webcamAddress + WEBCAM_END_ADDRESS;
			ItemWebcam webcam = ItemWebcam.Factory.getSystemWebcam(0, webcamName, webcamAddress, 30);
			webcam.setParentAliasId(currentAliasId);
			items.add(new ItemWrapper(webcam));
		}
	}
	
	
	
	
	//---------- Private methods
    private String getPage() {
    	return getPageContent(PAGE_URL);
    }
    
    
    /**
     * Get the string between the two token.
     * Source string, at the end of the method, is modified and starts
     * at the second token
     * 
     * @param source
     * @param tokenBefore
     * @param tokenAfter
     * @return
     */
	public static String getStringBetween(String[] sourceArray, String tokenBefore, String tokenAfter)
	{
		String result = "";
		String source = sourceArray[0];
		
		if (null == source || "".equals(source))
			return result;
		
		int posInit = source.indexOf(tokenBefore);
		if (-1 == posInit) return result;
		posInit = posInit += tokenBefore.length();
		
		int posEnd = source.indexOf(tokenAfter, posInit);
		if (-1 == posEnd) posEnd = source.length(); 
		
		result = source.substring(posInit, posEnd);
		
		//modify source string
		if (posEnd == source.length())
			sourceArray[0] = "";
		else
			sourceArray[0] = source.substring(posEnd);
		
		return result;
	}    

}
