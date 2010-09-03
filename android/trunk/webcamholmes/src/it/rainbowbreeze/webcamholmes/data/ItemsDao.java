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

import it.rainbowbreeze.libs.log.BaseLogFacility;
import it.rainbowbreeze.webcamholmes.domain.ItemCategory;
import it.rainbowbreeze.webcamholmes.domain.ItemToDisplay;
import it.rainbowbreeze.webcamholmes.domain.ItemWebcam;
import it.rainbowbreeze.webcamholmes.domain.WebcamHolmes;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static it.rainbowbreeze.libs.common.ContractHelper.*;

/**
 * Provider for categories and webcams
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 */
public class ItemsDao
{
	//---------- Private fields
    private static final String DATABASE_NAME = "webcamholmes.db";
    private static final int DATABASE_VERSION = 3;
    private final BaseLogFacility mLogFacility;

    /**
     * Standard projection for the interesting columns of a webcam.
     */
    private static final String[] WEBCAM_FULL_PROJECTION = new String[] {
    	WebcamHolmes.Webcam._ID, // 0
    	WebcamHolmes.Webcam.PARENT_CATEGORY_ID, // 1
    	WebcamHolmes.Webcam.NAME, // 2
    	WebcamHolmes.Webcam.TYPE, // 3
    	WebcamHolmes.Webcam.IMAGEURL, // 4
    	WebcamHolmes.Webcam.RELOAD_INTERVAL, // 5
    	WebcamHolmes.Webcam.PREFERRED, // 6
    	WebcamHolmes.Webcam.CREATED_BY_USER, // 7
    };

    private static final String[] CATEGORY_FULL_PROJECTION = new String[] {
    	WebcamHolmes.Category._ID, // 0
    	WebcamHolmes.Category.PARENT_CATEGORY_ID, // 1
    	WebcamHolmes.Category.NAME, // 2
    	WebcamHolmes.Category.CREATED_BY_USER, // 3
    };

    
    
    
	//---------- Constructor    
    public ItemsDao(Context context, BaseLogFacility logFacility) {
    	mLogFacility = checkNotNull(logFacility, "Log Facility");
    	mOpenHelper = new DatabaseHelper(context, mLogFacility);
	}
	

    
	//---------- Inner classes
    /**
     * This class helps open, create, and upgrade the database file.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        private final BaseLogFacility mLogFacility;

        DatabaseHelper(Context context, BaseLogFacility logFacility) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mLogFacility = logFacility;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + WebcamHolmes.Webcam.TABLE_NAME + " ("
                    + WebcamHolmes.Webcam._ID + " INTEGER PRIMARY KEY,"
                    + WebcamHolmes.Webcam.PARENT_CATEGORY_ID + " INTEGER,"
                    + WebcamHolmes.Webcam.NAME + " TEXT,"
                    + WebcamHolmes.Webcam.TYPE + " INTEGER,"
                    + WebcamHolmes.Webcam.IMAGEURL + " TEXT,"
                    + WebcamHolmes.Webcam.RELOAD_INTERVAL + " SMALL,"
                    + WebcamHolmes.Webcam.PREFERRED + " BOOLEAN,"
                    + WebcamHolmes.Webcam.CREATED_BY_USER + " BOOLEAN"
                    + ");");
            db.execSQL("CREATE TABLE " + WebcamHolmes.Category.TABLE_NAME + " ("
                    + WebcamHolmes.Category._ID + " INTEGER PRIMARY KEY,"
                    + WebcamHolmes.Category.PARENT_CATEGORY_ID + " INTEGER,"
                    + WebcamHolmes.Category.NAME + " TEXT,"
                    + WebcamHolmes.Category.CREATED_BY_USER + " BOOLEAN"
                    + ");");
       }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            mLogFacility.i("Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + WebcamHolmes.Webcam.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + WebcamHolmes.Category.TABLE_NAME);
            onCreate(db);
        }

    }

    private DatabaseHelper mOpenHelper;




	//---------- Public properties



    
	//---------- Events



	
	//---------- Public methods
    /**
     * Retrieves a webcam by its id
     * @param webcamId
     * @return webcam object or null
     */
    public ItemWebcam getWebcamById(long webcamId) {
        List<ItemToDisplay> webcams = getWebcamsFromDatabase(WebcamHolmes.Webcam._ID + "=" + webcamId);

        //returns first webcam found or null
        return webcams.size() > 0 ? (ItemWebcam) webcams.get(0) : null;
	}

    /**
     * Retrieves a category by its id
     * @param categoryId
     * @return category object or null
     */
	public ItemCategory getCategoryById(long categoryId) {
        List<ItemToDisplay> categories = getCategoriesFromDatabase(WebcamHolmes.Category._ID + "=" + categoryId);

        //returns first category found or null
        return categories.size() > 0 ? (ItemCategory) categories.get(0) : null;
	}

	/**
	 * Returns all the children items of a category
	 * @param parentItemId
	 * @return
	 */
	public List<ItemToDisplay> getChildrenOfParentItem(long parentItemId) {
		//first get all the categories
		List<ItemToDisplay> items = getCategoriesFromDatabase(WebcamHolmes.Category.PARENT_CATEGORY_ID + "=" + parentItemId);
		//then, add the webcams
		items.addAll(getWebcamsFromDatabase(WebcamHolmes.Webcam.PARENT_CATEGORY_ID + "=" + parentItemId));

		return items;
	}
	
	
	/**
	 * Add a new webcam to the database
	 * @param webcam
	 * @return the id of the new webcam
	 */
	public long insertWebcam(ItemWebcam webcam) {
		
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(WebcamHolmes.Webcam.PARENT_CATEGORY_ID, webcam.getParentId());
        values.put(WebcamHolmes.Webcam.NAME, webcam.getName());
        values.put(WebcamHolmes.Webcam.TYPE, webcam.getType());
        values.put(WebcamHolmes.Webcam.IMAGEURL, webcam.getImageUrl());
        values.put(WebcamHolmes.Webcam.RELOAD_INTERVAL, webcam.getReloadInterval());
        values.put(WebcamHolmes.Webcam.PREFERRED, webcam.getPreferred());
        values.put(WebcamHolmes.Webcam.CREATED_BY_USER, webcam.isUserCreated());

        long webcamId = db.insert(WebcamHolmes.Webcam.TABLE_NAME, WebcamHolmes.Webcam.NAME, values);
        webcam.setId(webcamId);

        return webcamId;
	}
	
	/**
	 * Add a new webcam to the database
	 * @param webcam
	 * @return the id of the new category
	 */
	public long insertCategory(ItemCategory category) {
		
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(WebcamHolmes.Category.PARENT_CATEGORY_ID, category.getParentId());
        values.put(WebcamHolmes.Category.NAME, category.getName());
        values.put(WebcamHolmes.Category.CREATED_BY_USER, category.isUserCreated());

        long categoryId = db.insert(WebcamHolmes.Category.TABLE_NAME, WebcamHolmes.Category.NAME, values);
        category.setId(categoryId);

        return categoryId;
	}


	/**
	 * Remove a webcam
	 * @param webcamId the id of the webcam to delete
	 * @return the deleted webcam (1 if success, 0 if no webcams were found)
	 */
	public int deleteWebcam(long webcamId) {
		int count;
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        count = db.delete(
        		WebcamHolmes.Webcam.TABLE_NAME,
        		WebcamHolmes.Webcam._ID + "=" + webcamId,
                null);
		return count;
	}

	/**
	 * Remove a category
	 * @param categoryId the id of the category to delete
	 * @return the deleted category (1 if success, 0 if no categories were found)
	 */
	public int deleteCategory(long categoryId) {
		int count;
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        count = db.delete(
        		WebcamHolmes.Category.TABLE_NAME,
        		WebcamHolmes.Category._ID + "=" + categoryId,
                null);
		return count;
	}
	

	/**
	 * Completely clean the database (used in tests)
	 */
	public int clearDatabaseComplete() {
		int count;
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		//delete all webcams
		count = db.delete(
        		WebcamHolmes.Webcam.TABLE_NAME,
        		null,
                null);
		//delete all categories
        count += db.delete(
        		WebcamHolmes.Category.TABLE_NAME,
        		null,
                null);
        return count;
	}
	
	/**
	 * Clean the database, but preserve user data (ie new webcam and categories)
	 */
	public void clearDatabasePreservingUserData() {
		
	}



	
	//---------- Private methods
	/**
	 * Read the requested webcams from the database
	 * 
	 * @param where
	 */
	private List<ItemToDisplay> getWebcamsFromDatabase(String where) {
		List<ItemToDisplay> list = new ArrayList<ItemToDisplay>();
		
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cur = db.query(WebcamHolmes.Webcam.TABLE_NAME,
        		WEBCAM_FULL_PROJECTION,
        		where,
        		null,
        		null,
        		null,
        		WebcamHolmes.Webcam.DEFAULT_SORT_ORDER);
        
        if (cur.moveToFirst()) {
	        do {
	        	long id = cur.getLong(0);
	        	long parentCategoryId = cur.getLong(1);
	        	String name = cur.getString(2);
	        	int type = cur.getInt(3);
	        	String imageUrl = cur.getString(4);
	        	int reloadInterval = cur.getInt(5);
	        	boolean preferred = Boolean.getBoolean(cur.getString(6));
	        	boolean userCreated = Boolean.getBoolean(cur.getString(7));
	        	ItemWebcam webcam = new ItemWebcam(
	        			id, parentCategoryId,
	        			name, type,
	        			imageUrl, reloadInterval,
	        			preferred, userCreated);
	        	list.add(webcam);
	        } while (cur.moveToNext());
        }
        cur.close();
        cur = null;

        return list;
	}
	
	/**
	 * Read the requested categories from the database
	 * 
	 * @param where
	 */
	private List<ItemToDisplay> getCategoriesFromDatabase(String where) {
		List<ItemToDisplay> list = new ArrayList<ItemToDisplay>();
		
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cur = db.query(WebcamHolmes.Category.TABLE_NAME,
        		CATEGORY_FULL_PROJECTION,
        		where,
        		null,
        		null,
        		null,
        		WebcamHolmes.Category.DEFAULT_SORT_ORDER);
        
        if (cur.moveToFirst()) {
	        do {
	        	long id = cur.getLong(0);
	        	long parentCategoryId = cur.getLong(1);
	        	String name = cur.getString(2);
	        	boolean userCreated = Boolean.getBoolean(cur.getString(3));
	        	ItemCategory category = new ItemCategory(id, parentCategoryId, name, userCreated);
	        	list.add(category);
	        } while (cur.moveToNext());
        }
        cur.close();
        cur = null;

        return list;
	}

	/**
	 * Returns the father id of the father category of the item
	 * @param itemId
	 * @return
	 */
	public long getParentIdOfCategory(long categoryId) {
		if (0 == categoryId) return 0;

		long parentId = 0;
		ItemCategory category = getCategoryById(categoryId);
		if (null != category) {
			parentId = category.getParentId();
		}
		return parentId;
	}
	
}
