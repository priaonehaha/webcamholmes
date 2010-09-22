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

import it.rainbowbreeze.webcamholmes.domain.ItemWrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemsSplitter {

	//---------- Private fields
	private static final String FILENAME = File.separator + "items.xml";

	private final List<PackageDescriptor> mPackageDescriptors;

	private final String mBasePath;



	//---------- Constructor
	/**
	 * Initialize the combinator
	 */
	public ItemsSplitter(String basePath) {
		
		mBasePath = basePath;
		
		mPackageDescriptors = new ArrayList<PackageDescriptor>();
		
		//London
		mPackageDescriptors.add(new PackageDescriptor
				("london" + FILENAME,
				 "9"));
		//Rome
		mPackageDescriptors.add(new PackageDescriptor
				("rome" + FILENAME,
				 "5"));
		//Paris
		mPackageDescriptors.add(new PackageDescriptor
				("paris" + FILENAME,
				 "2"));
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
	 */
	public void splitResource(
			String sourceFilePath,
			String destDirectory)
	throws IOException
	{
		boolean include;
		//items to add to the package items file
		List<ItemWrapper> itemsToAdd = new ArrayList<ItemWrapper>();
		List<String> childParentAliasIdToInclude = new ArrayList<String>();
		
		for (PackageDescriptor currentPackage : mPackageDescriptors) {
			List<ItemWrapper> items = getItemsFromFile(sourceFilePath);

			//reset child categories to include and items to add to package items file
			childParentAliasIdToInclude.clear();
			itemsToAdd.clear();
			
			for (ItemWrapper item:items) {
				boolean includeFirstLevel = currentPackage.parentAliasIdsToInclude.contains(item.getParentAliasId());
				//if the item is in one of the categories allowed for the package
				//(fixed categories or child categories of fixed categories)
				include = includeFirstLevel
						|| childParentAliasIdToInclude.contains(item.getParentAliasId());
				
				if (include) {
					//check if the item is a category, in this case, add it
					//to the child categories to include
					if (item.isCategory()) {
						childParentAliasIdToInclude.add(item.getAliasId());
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
			writePackageFile(currentPackage.pathToSave, itemsToAdd);	
		}
	}


	

	//---------- Private methods

	/**
	 * @param sourceFilePath
	 */
	private List<ItemWrapper> getItemsFromFile(String sourceFilePath) {
		// TODO Auto-generated method stub
		return null;
	}

	
	/**
	 * Write items in the package file
	 * 
	 * @param pathToSave
	 * @param itemsToAdd
	 * @throws IOException 
	 */
	private void writePackageFile(String pathToSave, List<ItemWrapper> itemsToAdd)
	throws IOException
	{
		File file = new File(mBasePath, pathToSave);
		FileOutputStream fos = new FileOutputStream(file);
		
		//build the string
		StringBuilder sb = new StringBuilder();
		sb.append(XmlDictionary.OPENING_TAGS);
		for (ItemWrapper item:itemsToAdd) {
			sb.append(item.getXmlRepresentation());
		}
		sb.append(XmlDictionary.CLOSING_TAGS);

		//write the final file
		fos.write(sb.toString().getBytes());
		
		//and close all
		fos.close();
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
