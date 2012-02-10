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
package it.rainbowbreeze.webcamholmes.data;

import it.rainbowbreeze.webcamholmes.common.ResultOperation;
import it.rainbowbreeze.webcamholmes.domain.ItemCategory;
import it.rainbowbreeze.webcamholmes.domain.ItemToDisplay;
import it.rainbowbreeze.webcamholmes.domain.ItemWebcam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.content.res.Resources.NotFoundException;

/**
 * Parse XML file with webcam and categories
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public class ItemsXmlParser implements ItemsXmlDictionary {
	//---------- Private fields




	//---------- Constructor
	/**
	 * This class cannot be used outside of ItemsDao
	 */
	protected ItemsXmlParser()
	{ }




	//---------- Public properties




	//---------- Public methods
	
	/**
	 * Parse an xml resource file and returns a list with the items contained
	 * 
	 * @param context
	 * @param resourceId Resource Id of the xml file to import
	 */
	public ResultOperation<List<ItemToDisplay>> parseResource(Context context, int resourceId) {
		long cat_aliasId = 0;
		long cat_parentAliasId = 0;
		String cat_name = null;
		boolean cat_isCreatedByUser = false;

		long web_parentAliasId = 0;
		int web_type = 0;
		String web_name = null;
		String web_imageUrl = null;
		int web_reloadInterval = 0;
		boolean web_isPreferred = false;
		boolean web_isCreatedByUser = false;
		String web_freedata1 = null;
		String web_freedata2 = null;
		String web_freedata3 = null;
		
		
		boolean processingCategory = false;
		boolean processingWebcam = false;

		List<ItemToDisplay> elements = null;
		
		XmlResourceParser parser;
		try {
			Resources res = context.getResources();
			parser = res.getXml(resourceId);
		} catch (NotFoundException e) {
			return new ResultOperation<List<ItemToDisplay>>(e, ResultOperation.RETURNCODE_ERROR_IMPORT_FROM_RESOURCE);
		}
		
		try {
			parser.next();
			int eventType = parser.getEventType();
			
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String tagName = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					elements = new ArrayList<ItemToDisplay>();
					break;
					
				case XmlPullParser.START_TAG:
					tagName = parser.getName();
	
					if (tagName.equalsIgnoreCase(XMLNODE_ITEMS)) {
						processingCategory = false;
						processingWebcam = false;
						
					} else if (tagName.equalsIgnoreCase(XMLNODE_CATEGORY)) {
						//reset data
						cat_aliasId = 0;
						cat_parentAliasId = 0;
						cat_name = "";
						cat_isCreatedByUser = false;
						processingCategory = true;
						
					} else if (tagName.equalsIgnoreCase(XMLNODE_WEBCAM)) {
						//reset data
						web_parentAliasId = 0;
						web_type = 1;
						web_name = "";
						web_imageUrl = "";
						web_reloadInterval = 0;
						web_isPreferred = false;
						web_isCreatedByUser = false;
						web_freedata1 = "";
						web_freedata2 = "";
						web_freedata3 = "";
						processingWebcam = true;

					} else if (processingCategory) {
						//category tags
						if (tagName.equalsIgnoreCase(XMLNODE_CATEGORY_ALIAS_ID)) {
							cat_aliasId = Long.valueOf(parser.nextText());
						} else if (tagName.equalsIgnoreCase(XMLNODE_CATEGORY_PARENT_ALIAS_ID)) {
							cat_parentAliasId = Long.valueOf(parser.nextText());
						} else if (tagName.equalsIgnoreCase(XMLNODE_CATEGORY_NAME)) {
							cat_name = parser.nextText();
						} else if (tagName.equalsIgnoreCase(XMLNODE_CATEGORY_USER_CREATED)) {
							cat_isCreatedByUser = Boolean.parseBoolean(parser.nextText());
						}

					} else if (processingWebcam) {
						//webcam tags
						if (tagName.equalsIgnoreCase(XMLNODE_WEBCAM_PARENT_ALIAS_ID)) {
							web_parentAliasId = Long.valueOf(parser.nextText());
						} else if (tagName.equalsIgnoreCase(XMLNODE_WEBCAM_NAME)) {
							web_name = parser.nextText();
						} else if (tagName.equalsIgnoreCase(XMLNODE_WEBCAM_TYPE)) {
							web_type = Integer.valueOf(parser.nextText());
						} else if (tagName.equalsIgnoreCase(XMLNODE_WEBCAM_IMAGE_URL)) {
							web_imageUrl = parser.nextText();
						} else if (tagName.equalsIgnoreCase(XMLNODE_WEBCAM_RELOAD_INTERVAL)) {
							web_reloadInterval = Integer.valueOf(parser.nextText());
						} else if (tagName.equalsIgnoreCase(XMLNODE_WEBCAM_PREFFERED)) {
							web_isPreferred = Boolean.parseBoolean(parser.nextText());
						} else if (tagName.equalsIgnoreCase(XMLNODE_WEBCAM_USER_CREATED)) {
							web_isCreatedByUser = Boolean.parseBoolean(parser.nextText());
						} else if (tagName.equalsIgnoreCase(XMLNODE_WEBCAM_FREE_DATA_1)) {
							web_freedata1 = parser.nextText();
						} else if (tagName.equalsIgnoreCase(XMLNODE_WEBCAM_FREE_DATA_2)) {
							web_freedata2 = parser.nextText();
						} else if (tagName.equalsIgnoreCase(XMLNODE_WEBCAM_FREE_DATA_3)) {
							web_freedata3 = parser.nextText();
						}
					}
					break;

				case XmlPullParser.END_TAG:
					tagName = parser.getName();
					if (tagName.equalsIgnoreCase(XMLNODE_CATEGORY)) {
						//create new category with gathered data
						ItemCategory category = new ItemCategory(0, cat_aliasId, 0, cat_name, cat_isCreatedByUser);
						category.setParentAliasId(cat_parentAliasId);
						elements.add(category);
						//reset all values
						category = null;
						processingCategory = false;

					} else if (tagName.equalsIgnoreCase(XMLNODE_WEBCAM)) {
						//create new category with gathered data
						ItemWebcam webcam = new ItemWebcam(0, 0, web_name, web_type, web_imageUrl, web_reloadInterval, web_isPreferred, web_isCreatedByUser, web_freedata1, web_freedata2, web_freedata3);
						webcam.setParentAliasId(web_parentAliasId);
						elements.add(webcam);
						//reset all values
						webcam = null;
						processingWebcam = false;
					}
					break;
				}
				
				eventType = parser.next();
			}
			
		} catch (XmlPullParserException e) {
			return new ResultOperation<List<ItemToDisplay>>(e, ResultOperation.RETURNCODE_ERROR_IMPORT_FROM_RESOURCE);
		} catch (IOException e) {
			return new ResultOperation<List<ItemToDisplay>>(e, ResultOperation.RETURNCODE_ERROR_IMPORT_FROM_RESOURCE);
		}
		
		return new ResultOperation<List<ItemToDisplay>>(elements);
	}
	

	//---------- Private methods
}
