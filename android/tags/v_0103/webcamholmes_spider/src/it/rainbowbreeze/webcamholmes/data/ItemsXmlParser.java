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

import it.rainbowbreeze.webcamholmes.domain.ItemCategory;
import it.rainbowbreeze.webcamholmes.domain.ItemWebcam;
import it.rainbowbreeze.webcamholmes.domain.ItemWrapper;

import java.io.IOException;
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
public class ItemsXmlParser
	extends DefaultHandler
	implements ItemsXmlDictionary
{
	//---------- Private fields
	private String mTempVal;
	private boolean mProcessingCategory;
	private long mCatAliasId;
	private long mCatParentAliasId;
	private String mCatName;
	private boolean mCatIsCreatedByUser;

	private boolean mProcessingWebcam;
	private long mWebParentAliasId;
	private int mWebType;
	private String mWebName;
	private String mWebImageUrl;
	private int mWebReloadInterval;
	private boolean mWebIsPreferred;
	private boolean mWebIsCreatedByUser;

	private List<ItemWrapper> mItems;




	//---------- Constructor




	//---------- Public properties




	//---------- Events




	//---------- Public methods
	
	/**
	 * Parse the WebcamHolmes xml document and retrieve a list of {@link ItemWrapper}
	 * 
	 * @param absoluteFilePath
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 */
	public List<ItemWrapper> parseDocument(String absoluteFilePath)
	throws ParserConfigurationException, SAXException, IOException {
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();

		//get a new instance of parser
		SAXParser sp = spf.newSAXParser();

		mItems = new ArrayList<ItemWrapper>();
		//parse the file and also register this class for call backs
		sp.parse(absoluteFilePath, this);

		return mItems;
	}
	
	
	@Override
	public void startElement(
			String uri, String localName, String qName, Attributes attributes)
	throws SAXException {
		//reset
		mTempVal = "";
		if (qName.equalsIgnoreCase(XMLNODE_CATEGORY)) {
			mProcessingCategory = true;
			//reset category values
			mCatAliasId = 0;
			mCatParentAliasId = 0;
			mCatName = null;
			mCatIsCreatedByUser = false;			
			
		} else if (qName.equalsIgnoreCase(XMLNODE_WEBCAM)) {
			mProcessingWebcam = true;
			//reset webcam values
			mWebParentAliasId = 0;
			mWebType = 1;
			mWebName = "";
			mWebImageUrl = "";
			mWebReloadInterval = 0;
			mWebIsPreferred = false;
			mWebIsCreatedByUser = false;

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

		//category data was processed
		if (qName.equalsIgnoreCase(XMLNODE_CATEGORY)) {
			//add category to the list
			ItemCategory category = new ItemCategory(0, mCatAliasId, 0, mCatName, mCatIsCreatedByUser);
			category.setParentAliasId(mCatParentAliasId);
			mItems.add(new ItemWrapper(category));
			//reset values
			mProcessingCategory = false;

		//webcam data was processed
		} else if (qName.equalsIgnoreCase(XMLNODE_WEBCAM)) {
			ItemWebcam webcam = new ItemWebcam(0, 0, mWebName, mWebType, mWebImageUrl, mWebReloadInterval, mWebIsPreferred, mWebIsCreatedByUser);
			webcam.setParentAliasId(mWebParentAliasId);
			//add webcam to the list
			mItems.add(new ItemWrapper(webcam));
			//reset values
			mProcessingCategory = false;

		//category data is in progress
		} else if (mProcessingCategory) {
			if (qName.equalsIgnoreCase(XMLNODE_CATEGORY_ALIAS_ID)) {
				mCatAliasId = Long.parseLong(mTempVal);
			} else if (qName.equalsIgnoreCase(XMLNODE_CATEGORY_PARENT_ALIAS_ID)) {
				mCatParentAliasId = Long.parseLong(mTempVal);
			} else if (qName.equalsIgnoreCase(XMLNODE_CATEGORY_NAME)) {
				mCatName = mTempVal;
			} else if (qName.equalsIgnoreCase(XMLNODE_CATEGORY_USER_CREATED)) {
				mCatIsCreatedByUser = Boolean.parseBoolean(mTempVal);
			}
			
		//webcam data is in progress
		} else if (mProcessingWebcam) {
			if (qName.equalsIgnoreCase(XMLNODE_WEBCAM_CREATED_BY_USER)) {
				mWebIsCreatedByUser = Boolean.parseBoolean(mTempVal);
			} else if (qName.equalsIgnoreCase(XMLNODE_WEBCAM_IMAGE_URL)) {
				mWebImageUrl = mTempVal;
			} else if (qName.equalsIgnoreCase(XMLNODE_WEBCAM_NAME)) {
				mWebName = mTempVal;
			} else if (qName.equalsIgnoreCase(XMLNODE_WEBCAM_PARENT_ALIAS_ID)) {
				mWebParentAliasId = Long.parseLong(mTempVal);
			} else if (qName.equalsIgnoreCase(XMLNODE_WEBCAM_PREFFERED)) {
				mWebIsPreferred = Boolean.parseBoolean(mTempVal);
			} else if (qName.equalsIgnoreCase(XMLNODE_WEBCAM_RELOAD_INTERVAL)) {
				mWebReloadInterval = Integer.parseInt(mTempVal);
			} else if (qName.equalsIgnoreCase(XMLNODE_WEBCAM_TYPE)) {
				mWebType = Integer.parseInt(mTempVal);
			}
		}
	}
	
	
	/**
	 * Get the XML representation of the list of items
	 */
	public String getXmlRepresentation(List<ItemWrapper> itemsToAdd) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(ItemsXmlDictionary.XML_OPENING);
	
		sb.append("\n");
		openNode(sb, XMLNODE_ITEMS);

		for (ItemWrapper item:itemsToAdd) {
			if (item.isCategory()) {
				appendCategoryXmlRepresentation(sb, ItemCategory.class.cast(item.getItem()));
			} else if (item.isWebcam()) {
				appendWebcamXmlRepresentation(sb, ItemWebcam.class.cast(item.getItem()));
			}
		}

		sb.append("\n");
		closeNode(sb, XMLNODE_ITEMS);

		sb.append(ItemsXmlDictionary.XML_CLOSING);

		return sb.toString();
	}

	
	
	
	//---------- Private methods
	private void openNode(StringBuilder sb, String content)
	{ sb.append("<").append(content).append(">"); }
	
	private void closeNode(StringBuilder sb, String content)
	{ sb.append("</").append(content).append(">"); }
	
	private void wrapValue(StringBuilder sb, String nodeDesc, String nodeValue) {  
		sb.append("\n\t");
		openNode(sb, nodeDesc);
		sb.append(nodeValue.replace("&", "&amp;"));
		closeNode(sb, nodeDesc);
	}
	private void wrapValue(StringBuilder sb, String nodeDesc, boolean nodeValue)
	{ wrapValue(sb, nodeDesc, String.valueOf(nodeValue)); }
	private void wrapValue(StringBuilder sb, String nodeDesc, long nodeValue)
	{ wrapValue(sb, nodeDesc, String.valueOf(nodeValue)); }
	private void wrapValue(StringBuilder sb, String nodeDesc, int nodeValue)
	{ wrapValue(sb, nodeDesc, String.valueOf(nodeValue)); }
	
	/**
	 * Create a XML representation of a category
	 * @param category
	 * @param sb
	 */
	private void appendCategoryXmlRepresentation(StringBuilder sb, ItemCategory category) {
		sb.append("\n");
		openNode(sb, XMLNODE_CATEGORY);
		wrapValue(sb, XMLNODE_CATEGORY_ALIAS_ID, category.getAliasId());
		wrapValue(sb, XMLNODE_CATEGORY_USER_CREATED, category.isUserCreated());
		wrapValue(sb, XMLNODE_CATEGORY_NAME, category.getName());
		wrapValue(sb, XMLNODE_CATEGORY_PARENT_ALIAS_ID, category.getParentAliasId());
		sb.append("\n");
		closeNode(sb, XMLNODE_CATEGORY);
	}
	
	/**
	 * Create a XML representation of a webcam
	 * @param webcam
	 * @param sb
	 */
	private void appendWebcamXmlRepresentation(StringBuilder sb, ItemWebcam webcam){
		sb.append("\n");
		openNode(sb, XMLNODE_WEBCAM);
		wrapValue(sb, XMLNODE_WEBCAM_CREATED_BY_USER, webcam.isUserCreated());
		wrapValue(sb, XMLNODE_WEBCAM_IMAGE_URL, webcam.getImageUrl());
		wrapValue(sb, XMLNODE_WEBCAM_NAME, webcam.getName());
		wrapValue(sb, XMLNODE_WEBCAM_PARENT_ALIAS_ID, webcam.getParentAliasId());
		wrapValue(sb, XMLNODE_WEBCAM_PREFFERED, webcam.isPreferred());
		wrapValue(sb, XMLNODE_WEBCAM_RELOAD_INTERVAL, webcam.getReloadInterval());
		wrapValue(sb, XMLNODE_WEBCAM_TYPE, webcam.getType());
		sb.append("\n");
		closeNode(sb, XMLNODE_WEBCAM);
	}




}

