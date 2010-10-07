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

import it.rainbowbreeze.webcamholmes.data.ItemsXmlParser;
import it.rainbowbreeze.webcamholmes.domain.ItemWrapper;
import it.rainbowbreeze.webcamholmes.spiders.BBCLondonTrafficSpider;
import it.rainbowbreeze.webcamholmes.spiders.BaseSpider;
import it.rainbowbreeze.webcamholmes.spiders.WebcamsTravelSpider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

/**
 * @author rainbowbreeze
 *
 */
public class App {
	
	/*
	 * The file with all the webcams (from core project)
	 */
	//private static final String itemsFile = "../webcamholmes_core/res/xml/items.xml";
	private static final String itemsFile = "res/items.xml";
	/**
	 * @param args
	 * @throws Exception 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws Exception {

		
		//create the spiders list
		List<BaseSpider> spiders = new ArrayList<BaseSpider>();
		//London AliasId is 9
		spiders.add(new BBCLondonTrafficSpider("London traffic", 9, 101, 105));
		spiders.add(new WebcamsTravelSpider("London", "Webcas.Travel London", 9, 106, 110));
		spiders.add(new WebcamsTravelSpider("Paris", "Webcas.Travel Paris", 5, 111, 115));
		spiders.add(new WebcamsTravelSpider("Rome", "Webcas.Travel Rome", 2, 116, 120));
		
		//read existing and manually created webcams
		System.out.println("Reading manually created items...");
		ItemsXmlParser parser = new ItemsXmlParser();
		List<ItemWrapper> items = parser.parseDocument(itemsFile);
		System.out.println("Total items: " + items.size());
		
		//grabbing new webcams
		System.out.println("Spidering for new webcams...");
		for(BaseSpider spider:spiders) {
			spider.parseResource(items);
			System.out.println("Total items after " + spider.getName() + ": " + items.size());
		}
		
		//build the final list
		System.out.println("Splitting items...");
		ItemsSplitter splitter = new ItemsSplitter();
		splitter.splitResource(items, "res");
		System.out.println("Done!");
		
	}

}
