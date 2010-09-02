/**
 * Copyright (C) 2010 Alfredo Morresi
 * 
 * This file is part of SmsForFree project.
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
package it.rainbowbreeze.webcamholmes.logic;

import it.rainbowbreeze.libs.common.BaseResultOperation;
import it.rainbowbreeze.libs.data.BaseAppPreferencesDao;
import it.rainbowbreeze.libs.log.BaseLogFacility;
import it.rainbowbreeze.libs.logic.BaseLogicManager;
import it.rainbowbreeze.webcamholmes.common.ResultOperation;
import it.rainbowbreeze.webcamholmes.data.ItemsDao;
import it.rainbowbreeze.webcamholmes.domain.ItemCategory;
import it.rainbowbreeze.webcamholmes.domain.ItemWebcam;
import android.content.Context;

import static it.rainbowbreeze.libs.common.ContractHelper.*;

/**
 * @author Alfredo "Rainbowbreeze" Morresi
 */
public class LogicManager extends BaseLogicManager {

	//---------- Private fields
	private ItemsDao mItemsDao;
	
	

	//---------- Constructor
	/**
	 * @param logFacility
	 * @param globalDefs
	 * @param appPreferencesDao
	 */
	public LogicManager(
			BaseLogFacility logFacility,
			BaseAppPreferencesDao appPreferencesDao,
			String currentAppVersion,
			ItemsDao itemsDao)
	{
		super(logFacility, appPreferencesDao, currentAppVersion);
		mItemsDao = checkNotNull(itemsDao);
	}
	
	
	
	//---------- Public properties
	/* (non-Javadoc)
	 * @see it.rainbowbreeze.libs.logic.BaseLogicManager#executeBeginTask(android.content.Context)
	 */
	@Override
	public BaseResultOperation<Void> executeBeginTask(Context context) {
		super.executeBeginTask(context);
		
		//TODO remove when tests finish
		if (null == mItemsDao.getWebcamById(1)) {
			//test deleted all webcams
			createWebcam010000();
		}
		
		return new ResultOperation<Void>();
	}


	/* (non-Javadoc)
	 * @see it.rainbowbreeze.libs.logic.BaseLogicManager#executeEndTast(android.content.Context)
	 */
	@Override
	public BaseResultOperation<Void> executeEndTast(Context context) {
		return super.executeEndTast(context);
	}
	
	
	
	//---------- Public methods

	
	
	
	//---------- Private methods
	/* (non-Javadoc)
	 * @see it.rainbowbreeze.libs.logic.BaseLogicManager#executeUpgradeTasks(java.lang.String)
	 */
	@Override
	protected BaseResultOperation<Void> executeUpgradeTasks(String startingAppVersion) {
		BaseResultOperation<Void> res =super.executeUpgradeTasks(startingAppVersion);
		if (res.hasErrors()) return res;
		
		createWebcam010000();
		
		return res;
	}
	

	/**
	 * Creates webcam for version 01.00.00 of the app
	 */
	private void createWebcam010000() {
		ItemCategory category;
		long categoryId;

		mBaseLogFacility.v("Adding new webcam for version 1.0");
		//add new item to the list
		
		category = ItemCategory.Factory.getSystemCategory(0, "Traffic - Australia");
		categoryId = mItemsDao.insertCategory(category);
		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Sidney - Anzac Bridge", "http://www.rta.nsw.gov.au/trafficreports/cameras/camera_images/anzacbr.jpg", 20));
		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Sidney - William St.", "http://www.rta.nsw.gov.au/trafficreports/cameras/camera_images/williamst.jpg", 20));
		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Sidney - Harbour Bridge", "http://www.rta.nsw.gov.au/trafficreports/cameras/camera_images/harbourbridge.jpg", 20));
		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Sidney - Anzac Bridge", "http://www.rta.nsw.gov.au/trafficreports/cameras/camera_images/georgest.jpg", 20));
		
		category = ItemCategory.Factory.getSystemCategory(0, "Traffic - Italy");
		categoryId = mItemsDao.insertCategory(category);
		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "A4 Torino-Trieste uscita Bergamo", "http://get.edidomus.it/vp/cam1/image.jpg", 5));
		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "A14 Trezzo sull'Adda", "http://get.edidomus.it/vp/cam23/image.jpg", 5));
		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Milano - Tang Est Rubattino", "http://get.edidomus.it/vp/cam4/image.jpg", 5));
		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Milano - Tang Est Mecenate", "http://get.edidomus.it/vp/cam6/image.jpg", 5));
	}
}
