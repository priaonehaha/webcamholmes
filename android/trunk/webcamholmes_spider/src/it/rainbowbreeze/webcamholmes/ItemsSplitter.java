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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * This class parses the file with all webcams and categories
 * and splits it in the different file needed for each personalized package
 *
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public class ItemsSplitter {

	//---------- Private fields
	private static final String FILENAME = File.separator + "items.xml";

	private final List<PackageDescriptor> mPackageDescriptors;



	//---------- Constructor
	/**
	 * Initialize the combinator
	 */
	public ItemsSplitter() {
		
		mPackageDescriptors = new ArrayList<PackageDescriptor>();
		
		//All
		mPackageDescriptors.add(new PackageDescriptor
				("full" + FILENAME,
				 "0"));
		//London
		mPackageDescriptors.add(new PackageDescriptor
				("london" + FILENAME,
				 "9"));
		//Rome
		mPackageDescriptors.add(new PackageDescriptor
				("rome" + FILENAME,
				 "2"));
		//Paris
		mPackageDescriptors.add(new PackageDescriptor
				("paris" + FILENAME,
				 "5"));
		//New York
		mPackageDescriptors.add(new PackageDescriptor
				("newyork" + FILENAME,
				 "11"));
		//Cervinia
		mPackageDescriptors.add(new PackageDescriptor
				("skysnow" + FILENAME,
				 "13"));
	}

	
	
	
	//---------- Public properties



	//---------- Public methods
	
	
	/**
	 * Split the source file with all items to different files
	 * for each application package
	 * 
	 * @param sourceFilePath
	 * @param destDirectory
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public void splitResource(
			String sourceFilePath,
			String destDirectory)
	throws IOException, ParserConfigurationException, SAXException
	{
		//read the items from file
		List<ItemWrapper> items = getItemsFromFile(sourceFilePath);
		
		//and call the true split method
		splitResource(items, destDirectory);
	}

	
	/**
	 * Split the source file with all items to different files
	 * for each application package
	 * 
	 * @param items
	 * @param destDirectory
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public void splitResource(
			List<ItemWrapper> items,
			String destDirectory)
	throws IOException, ParserConfigurationException, SAXException
	{
		boolean include;
		//items to add to the package items file
		List<ItemWrapper> itemsToAdd = new ArrayList<ItemWrapper>();
		List<String> childParentAliasIdToInclude = new ArrayList<String>();
		
		for (PackageDescriptor currentPackage : mPackageDescriptors) {

			//reset child categories to include and items to add to package items file
			childParentAliasIdToInclude.clear();
			itemsToAdd.clear();
			
			for (ItemWrapper item:items) {
				boolean includeFirstLevel = currentPackage.parentAliasIdsToInclude.contains(
						String.valueOf(item.getParentAliasId()));
				//if the item is in one of the categories allowed for the package
				//(fixed categories or child categories of fixed categories)
				include = includeFirstLevel
						|| childParentAliasIdToInclude.contains(String.valueOf(item.getParentAliasId()));
				
				if (include) {
					//check if the item is a category, in this case, add it
					//to the child categories to include
					if (item.isCategory()) {
						childParentAliasIdToInclude.add(String.valueOf(item.getAliasId()));
					}
					
					//check if the item is a first-level item
					if (includeFirstLevel) {
						//set root category for the item
						item.setParentAliasId(0);
					}
					
					itemsToAdd.add(item);
				}
			}
			
			//write the output file
			writePackageFile(destDirectory, currentPackage.pathToSave, itemsToAdd);	
		}
	}


	

	//---------- Private methods

	/**
	 * @param sourceFilePath
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	private List<ItemWrapper> getItemsFromFile(String sourceFilePath)
	throws ParserConfigurationException, SAXException, IOException {
		ItemsXmlParser parser = new ItemsXmlParser();
		return parser.parseDocument(sourceFilePath);
	}

	
	/**
	 * Write items in the package file
	 *
	 * @param basePath base path where the directories with splitted resources are created
	 * @param filePathToSave path (can countain path separator) of the file where resorces are saved
	 * @param itemsToAdd list of item to save
	 * @throws IOException 
	 */
	private void writePackageFile(String basePath, String filePathToSave, List<ItemWrapper> itemsToAdd)
	throws IOException
	{
		File file = new File(basePath, filePathToSave);
		createTree(file.getParent());
		FileOutputStream fos = new FileOutputStream(file);
		
		//build the string
		ItemsXmlParser parser = new ItemsXmlParser();
		String finalFileContent = parser.getXmlRepresentation(itemsToAdd);

		//write the final file
		fos.write(finalFileContent.getBytes());
		
		//and close all
		fos.close();
	}


	
	/**
	 * Recursively creates the tree specified
	 * @param finalPath
	 */
	private boolean createTree(String finalPath) {
		if (null == finalPath) return false;
		
		File file = new File(finalPath);
		if (file.exists()) return true;
		return file.mkdirs();
	}




	//---------- Private classes
	
	public class PackageDescriptor {

		/** path of the file where save items related to the package */
		public final String pathToSave;
		
		/** Alias category ids to include in this package */
		public final List<String> parentAliasIdsToInclude;
		
		
		/**
		 * Default constructor
		 * 
		 * @param pathToSave
		 * @param parentAliasIdsToInclude
		 */
		public PackageDescriptor(
				String pathToSave,
				String... parentAliasIdsToInclude)
		{
			this.pathToSave = pathToSave;
			this.parentAliasIdsToInclude = new ArrayList<String>();
			for(String aliasId:parentAliasIdsToInclude) {
				this.parentAliasIdsToInclude.add(aliasId);
			}
		}
	}
	
	

}
