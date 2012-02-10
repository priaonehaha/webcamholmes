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

import it.rainbowbreeze.webcamholmes.domain.ItemWebcam;
import it.rainbowbreeze.webcamholmes.domain.ItemWrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public class WebcamsTravelXmlParser
	extends DefaultHandler
{
	//---------- Private fields
	private final static String XMLNODE_WEBCAM = "webcam";
	private final static String XMLNODE_USER = "user";
	private final static String XMLNODE_USER_URL = "user_url";
	private final static String XMLNODE_TITLE = "title";
	private final static String XMLNODE_WEBCAM_ID = "webcamid";
	private final static String XMLNODE_URL = "url";
	private static final String BASE_WEBCAM_IMAGE_URL = "http://images.webcams.travel/webcam/";

	private String mUser;
	private String mUserUrl;
	private String mTitle;
	private String mWebcamSiteUrl;
	private String mWebcamId;

	private List<ItemWrapper> mItems;
	private String mTempVal;
	
	private final long mParentAliasId;




	//---------- Constructor
	public WebcamsTravelXmlParser(long parentAliasId) {
		mParentAliasId = parentAliasId;
	}




	//---------- Public properties




	//---------- Public methods
	
	/**
	 * Parse the WebcamHolmes xml document and retrieve a list of {@link ItemWrapper}
	 * 
	 * @param contentToParse
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 */
	public List<ItemWrapper> parseDocument(String contentToParse)
	throws ParserConfigurationException, SAXException, IOException {
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();

		//get a new instance of parser
		SAXParser sp = spf.newSAXParser();
		
		//get an inputstream from content
		InputStream is = new ByteArrayInputStream(contentToParse.getBytes("UTF-8"));

		mItems = new ArrayList<ItemWrapper>();
		//parse the file and also register this class for call backs
		sp.parse(is, this);

		return mItems;
	}
	
	
	@Override
	public void startElement(
			String uri, String localName, String qName, Attributes attributes)
	throws SAXException {
		//reset
		mTempVal = "";
			
		if (qName.equalsIgnoreCase(XMLNODE_WEBCAM)) {
			//reset webcam values
			mTitle = "";
			mUserUrl = "";
			mWebcamId = "";
			mUser = "";
			mWebcamSiteUrl = "";

		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
	throws SAXException {
		mTempVal = new String(ch,start,length);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		//webcam data was processed
		if (qName.equalsIgnoreCase(XMLNODE_WEBCAM)) {
			String imageUrl = BASE_WEBCAM_IMAGE_URL + mWebcamId + ".jpg";
			ItemWebcam webcam = ItemWebcam.Factory.getWebcamTravelWebcam(
					0, mTitle, imageUrl, mUser, mUserUrl, mWebcamSiteUrl);
			webcam.setParentAliasId(mParentAliasId);
			//add webcam to the list
			mItems.add(new ItemWrapper(webcam));

		//webcam data is in progress
		} else {
			if (qName.equalsIgnoreCase(XMLNODE_URL)) {
				mWebcamSiteUrl = mTempVal;
			} else if (qName.equalsIgnoreCase(XMLNODE_TITLE)) {
				mTitle = mTempVal;
			} else if (qName.equalsIgnoreCase(XMLNODE_USER)) {
				mUser = mTempVal;
			} else if (qName.equalsIgnoreCase(XMLNODE_USER_URL)) {
				mUserUrl = mTempVal;
			} else if (qName.equalsIgnoreCase(XMLNODE_WEBCAM_ID)) {
				mWebcamId = mTempVal;
			}
		}
	}
	



	//---------- Private methods
}

