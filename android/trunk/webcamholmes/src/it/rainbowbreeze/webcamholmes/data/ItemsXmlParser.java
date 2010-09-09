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

import it.rainbowbreeze.webcamholmes.domain.ItemCategory;
import it.rainbowbreeze.webcamholmes.domain.ItemWebcam;
import it.rainbowbreeze.webcamholmes.domain.WebcamHolmes.Category;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

/**
 * Parse XML file with webcam and categories
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public class ItemsXmlParser {
	//---------- Private fields
	private final static String XMLNODE_CATEGORY = "Category";
	private final static String XMLNODE_CATEGORY_ID = "Id";
	private final static String XMLNODE_CATEGORY_PARENT_CATEGORY_ID = "ParentCategoryId";
	private final static String XMLNODE_CATEGORY_NAME = "Name";
	private final static String XMLNODE_CATEGORY_CREATED_BY_USER = "UserCreated";

	private final static String XMLNODE_WEBCAM = "Webcam";




	//---------- Constructor




	//---------- Public properties
	public void parseResource(Context context, int resourceId) {
		ItemCategory category;
		ItemWebcam webcam;

		StringBuffer stringBuffer = new StringBuffer();
		Resources res = context.getResources();
		XmlResourceParser parser = res.getXml(resourceId);
		try {
			parser.next();
			int eventType = parser.getEventType();
			
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String name = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
					
				case XmlPullParser.START_TAG:
					name = parser.getName();
					
//					if (name.equalsIgnoreCase(XMLNODE_CATEGORY)) {
//					} else if (name.equalsIgnoreCase(XMLNODE_PARAMETERVALUE)) {
//						provider.setParameterValue(parametersIndex, parser.nextText());
//					} else if (name.equalsIgnoreCase(XMLNODE_PARAMETERFORMAT)) {
//						provider.setParameterFormat(parametersIndex, Integer.valueOf(parser.nextText()));
//					}
					break;
					
				case XmlPullParser.END_TAG:
					break;
				
				}
				
				eventType = parser.next();
			}
			
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}




	//---------- Events




	//---------- Public methods
	

	//---------- Private methods
}
