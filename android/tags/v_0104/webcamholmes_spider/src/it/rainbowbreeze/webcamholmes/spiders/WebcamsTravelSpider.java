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

import it.rainbowbreeze.webcamholmes.data.WebcamsTravelXmlParser;
import it.rainbowbreeze.webcamholmes.domain.ItemWrapper;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * Aggregate webcams from webcam.travel site
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public class WebcamsTravelSpider
	extends BaseSpider
{
	//---------- Private fields
	//query based on webcam 6km around the given location
	private static final String WEBSERVICE_BASE_ADDRESS = 
		"http://api.webcams.travel/rest?method=wct.webcams.list_nearby&devid=970e04b9d5005171dbb59c0aafe1bd27&radius=5&unit=km&per_page=60&lat=%s&lng=%s";

	//query bases on place name
	//"http://api.webcams.travel/rest?method=wct.search.webcams&devid=d0c0a0d5cfa782ed9dd9605425e829cd&per_page=50&query=%s";
	
	
//	private final String mPlaceToSearch;
	private final String mLongitude;
	private final String mLatitude;

	
	
	
	//---------- Constructor
	/**
	 * @param placeToSearch
	 * @param spiderName
	 * @param rootParentAliasId
	 * @param reservedAliasIdStart
	 * @param reservedAliasIdStop
	 * 
	 */
	public WebcamsTravelSpider(
			String latitude, 
			String longitude, 
			String spiderName,
			long rootParentAliasId,
			long reservedAliasIdStart,
			long reservedAliasIdStop)
	{
		super(spiderName, rootParentAliasId, reservedAliasIdStart, reservedAliasIdStop);
		mLatitude = latitude;
		mLongitude = longitude;
	}


	
	
	
	//---------- Public properties

	
	
	
	//---------- Public methods

	@Override
	public void parseResource(List<ItemWrapper> items) {
		//get webservice reply
		String webserviceRequestAddress = String.format(WEBSERVICE_BASE_ADDRESS, mLatitude, mLongitude);
		String webserviceReply = getPageContent(webserviceRequestAddress);
		
		//parse webcams
		WebcamsTravelXmlParser parser = new WebcamsTravelXmlParser(mRootParentAliasId);
		List<ItemWrapper> newWebcams = null;
		try {
			newWebcams = parser.parseDocument(webserviceReply);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (null != newWebcams && newWebcams.size() > 0)
			items.addAll(newWebcams);
		
	}

	
	
	
	//---------- Private methods
}
