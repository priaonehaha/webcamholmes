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

package it.rainbowbreeze.webcamholmes;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * @author rainbowbreeze
 *
 */
public class App {
	
	/*
	 * The file with all the webcams (from core project)
	 */
	private static final String itemsFile = "../webcamholmes_core/res/xml/items.xml";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println("Generating resource files...");
		ItemsSplitter splitter = new ItemsSplitter();
		try {
			splitter.splitResource(itemsFile, "res");
			System.out.println("Done!");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		System.out.println("");

		
//		ItemsXmlParser parser = new ItemsXmlParser();
//		List<ItemWrapper> items = parser.parseDocument("res/items.xml");
//		System.out.println(items.size());
	}

}
