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

/**
 * Dictionary class with all XML element used in category and webcam parsing
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public interface ItemsXmlDictionary {
	//---------- Private fields
	
	final static String XML_OPENING =
		"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
		"<!--\n" +  
		"Copyright (C) 2010 Alfredo Morresi\n" +
		"This file is part of WebcamHolmes project.\n" +
		"\n" +
		"This program is free software; you can redistribute it and/or modify it under\n" +
		"the terms of the GNU General Public License as published by the Free Software\n" +
		"Foundation; either version 3 of the License, or (at your option) any later\n" +
		"version.\n" +
		"\n" +
		"This program is distributed in the hope that it will be useful, but WITHOUT\n" +
		"ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS\n" +
		"FOR A PARTICULAR PURPOSE. See the GNU General Public License for more\n" +
		"details.\n" +
		"\n" +
		"You should have received a copy of the GNU General Public License along with\n" +
		"this program; If not, see <http://www.gnu.org/licenses/>.\n" +
		"-->\n" +
		"\n";

	final static String XML_CLOSING = "";
	
	final static String XMLNODE_ITEMS = "Items";

	final static String XMLNODE_CATEGORY = "Category";
	final static String XMLNODE_CATEGORY_ALIAS_ID = "AliasId";
	final static String XMLNODE_CATEGORY_PARENT_ALIAS_ID = "ParentAliasId";
	final static String XMLNODE_CATEGORY_NAME = "Name";
	final static String XMLNODE_CATEGORY_USER_CREATED = "UserCreated";

	final static String XMLNODE_WEBCAM = "Webcam";
	final static String XMLNODE_WEBCAM_PARENT_ALIAS_ID = "ParentAliasId";
	final static String XMLNODE_WEBCAM_NAME = "Name";
	final static String XMLNODE_WEBCAM_TYPE = "Type";
	final static String XMLNODE_WEBCAM_RELOAD_INTERVAL = "ReloadInterval";
	final static String XMLNODE_WEBCAM_IMAGE_URL = "ImageUrl";
	final static String XMLNODE_WEBCAM_PREFFERED = "Preferred";
	final static String XMLNODE_WEBCAM_USER_CREATED = "UserCreated";
	final static String XMLNODE_WEBCAM_FREE_DATA_1 = "FreeData1";
	final static String XMLNODE_WEBCAM_FREE_DATA_2 = "FreeData2";
	final static String XMLNODE_WEBCAM_FREE_DATA_3 = "FreeData3";



	//---------- Constructor




	//---------- Public properties




	//---------- Public methods
	

	
	//---------- Private methods
}
