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

import it.rainbowbreeze.libs.log.LogFacility;
import it.rainbowbreeze.webcamholmes.domain.ItemCategory;
import it.rainbowbreeze.webcamholmes.domain.ItemToDisplay;
import it.rainbowbreeze.webcamholmes.domain.ItemWebcam;
import it.rainbowbreeze.webcamholmes.domain.WebcamHolmes.Category;
import it.rainbowbreeze.webcamholmes.domain.WebcamHolmes.Webcam;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Provider for categories and webcams
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 */
public class ItemsDao
{
	//---------- Private fields
    private static final String DATABASE_NAME = "webcamholmes.db";
    private static final int DATABASE_VERSION = 1;

    /**
     * Standard projection for the interesting columns of a webcam.
     */
    private static final String[] WEBCAM_FULL_PROJECTION = new String[] {
        Webcam._ID, // 0
        Webcam.PARENT_CATEGORY_ID, // 1
        Webcam.NAME, // 2
        Webcam.IMAGEURL, // 3
        Webcam.RELOAD_INTERVAL, // 4
        Webcam.PREFERRED, // 5
    };

    private static final String[] CATEGORY_FULL_PROJECTION = new String[] {
        Category._ID, // 0
        Category.PARENT_CATEGORY_ID, // 1
        Category.NAME, // 2
    };

    
    
    
	//---------- Constructor    

	

    
	//---------- Inner classes
    /**
     * This class helps open, create, and upgrade the database file.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + Webcam.TABLE_NAME + " ("
                    + Webcam._ID + " INTEGER PRIMARY KEY,"
                    + Webcam.PARENT_CATEGORY_ID + " INTEGER,"
                    + Webcam.NAME + " TEXT,"
                    + Webcam.IMAGEURL + " TEXT,"
                    + Webcam.RELOAD_INTERVAL + " SMALL,"
                    + Webcam.PREFERRED + " BOOLEAN"
                    + ");");
            db.execSQL("CREATE TABLE " + Category.TABLE_NAME + " ("
                    + Category._ID + " INTEGER PRIMARY KEY,"
                    + Category.PARENT_CATEGORY_ID + " INTEGER,"
                    + Category.NAME + " TEXT,"
                    + ");");
       }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            LogFacility.i("Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + Webcam.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Category.TABLE_NAME);
            onCreate(db);
        }

    }

    private DatabaseHelper mOpenHelper;




	//---------- Public properties
	
	/** Singleton */
	private static ItemsDao mInstance;
	public static ItemsDao instance()
	{
		if (null == mInstance) mInstance = new ItemsDao();
		return mInstance;
	}

	

    
	//---------- Events



	
	//---------- Public methods
	public ItemWebcam getWebcamById(long webcamId) {
//		//TODO
//		ItemWebcam webcam = new ItemWebcam("1", "Paris - Tour Eiffel", "http://www.parislive.net/eiffelwebcam01.jpg");
//		//ItemWebcam webcam = new ItemWebcam("1", "Test location", "http://amrc.ssec.wisc.edu/~amrc/webcam/b15k/20050216_02.jpg");
//		webcam.setReloadInterval(5);
//		return webcam;
		
        List<ItemToDisplay> webcams = getWebcamsFromDatabase(Webcam._ID + "=" + webcamId);

        //returns first webcam found. or null
        return webcams.size() > 0 ? (ItemWebcam) webcams.get(0) : null;
	}

	/**
	 * Returns all the children items of a category
	 * @param parentItemId
	 * @return
	 */
	public List<ItemToDisplay> getChildrenOfParentItem(long parentItemId) {
		//first get all the categories
		List<ItemToDisplay> items = getCategoriesFromDatabase(Category.PARENT_CATEGORY_ID + "=" + parentItemId);
		//then, add the webcams
		items.addAll(getWebcamsFromDatabase(Webcam.PARENT_CATEGORY_ID + "=" + parentItemId));

		return items;
	}
	
	
	/**
	 * Add a new webcam to the database
	 * @param webcam
	 */
	public long insertWebcam(ItemWebcam webcam) {
		
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(Webcam.PARENT_CATEGORY_ID, webcam.getParentId());
        values.put(Webcam.NAME, webcam.getName());
        values.put(Webcam.IMAGEURL, webcam.getImageUrl());
        values.put(Webcam.RELOAD_INTERVAL, webcam.getReloadInterval());
        values.put(Webcam.PREFERRED, webcam.getPreferred());

        long webcamId = db.insert(Webcam.TABLE_NAME, Webcam.NAME, values);

        return webcamId;
	}
	
	/**
	 * Add a new webcam to the database
	 * @param webcam
	 */
	public long insertCategory(ItemCategory category) {
		
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(Category.PARENT_CATEGORY_ID, category.getParentId());
        values.put(Category.NAME, category.getName());

        long categoryId = db.insert(Category.TABLE_NAME, Category.NAME, values);

        return categoryId;
	}

	
	public int deleteWebcam(long webcamId) {
		//TODO
		return 0;
	}

	public int deleteCategory(long categoryId) {
		//TODO
		return 0;
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
        Cursor cur = db.query(Webcam.TABLE_NAME,
        		WEBCAM_FULL_PROJECTION,
        		where,
        		null,
        		null,
        		null,
        		Webcam.DEFAULT_SORT_ORDER);
        
        if (cur.moveToFirst()) {
	        do {
	        	long id = cur.getLong(0);
	        	long parentCategoryId = cur.getLong(1);
	        	String name = cur.getString(2);
	        	String imageUrl = cur.getString(3);
	        	int reloadInterval = cur.getInt(4);
	        	ItemWebcam webcam = new ItemWebcam(id, parentCategoryId, name, imageUrl, reloadInterval);
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
        Cursor cur = db.query(Category.TABLE_NAME,
        		CATEGORY_FULL_PROJECTION,
        		where,
        		null,
        		null,
        		null,
        		Category.DEFAULT_SORT_ORDER);
        
        if (cur.moveToFirst()) {
	        do {
	        	long id = cur.getLong(0);
	        	long parentCategoryId = cur.getLong(1);
	        	String name = cur.getString(2);
	        	ItemCategory category = new ItemCategory(id, parentCategoryId, name);
	        	list.add(category);
	        } while (cur.moveToNext());
        }
        cur.close();
        cur = null;

        return list;
	}
	
	/**
	 * Return the latest id used for the webcam table
	 * @return
	 */
	private long getLatestWebcamId() {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cur = db.query(Webcam.TABLE_NAME,
        		new String[]{ Webcam._ID },
        		null,
        		null,
        		null,
        		null,
        		Webcam._ID + "DESC");

        long latestId = 0;
        
        if (cur.moveToFirst()) {
        	latestId = cur.getLong(0);
        }
        cur.close();
        cur = null;

        return latestId;
	}

	/**
	 * Return the latest id used for the category table
	 * @return
	 */
	private long getLatestCategoryId() {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cur = db.query(Webcam.TABLE_NAME,
        		new String[]{ Webcam._ID },
        		null,
        		null,
        		null,
        		null,
        		Category._ID + "DESC");

        long latestId = 0;
        
        if (cur.moveToFirst()) {
        	latestId = cur.getLong(0);
        }
        cur.close();
        cur = null;

        return latestId;
	}
}
