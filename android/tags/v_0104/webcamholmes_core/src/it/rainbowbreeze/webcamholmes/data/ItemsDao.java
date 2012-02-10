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

import it.rainbowbreeze.libs.common.RainbowLogFacility;
import it.rainbowbreeze.webcamholmes.common.ResultOperation;
import it.rainbowbreeze.webcamholmes.domain.ItemCategory;
import it.rainbowbreeze.webcamholmes.domain.ItemToDisplay;
import it.rainbowbreeze.webcamholmes.domain.ItemWebcam;
import it.rainbowbreeze.webcamholmes.domain.WebcamHolmes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static it.rainbowbreeze.libs.common.RainbowContractHelper.*;

/**
 * Provider for categories and webcams
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 */
public class ItemsDao
{
	//---------- Private fields
    private static final String DATABASE_NAME = "webcamholmes.db";
    private static final int DATABASE_VERSION = 6;
    private final RainbowLogFacility mLogFacility;

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
    	WebcamHolmes.Webcam.FREEDATA1, // 8
    	WebcamHolmes.Webcam.FREEDATA2, // 9
    	WebcamHolmes.Webcam.FREEDATA3, // 10
    };

    private static final String[] CATEGORY_FULL_PROJECTION = new String[] {
    	WebcamHolmes.Category._ID, // 0
    	WebcamHolmes.Category.ALIAS_ID, // 1
    	WebcamHolmes.Category.PARENT_CATEGORY_ID, // 2
    	WebcamHolmes.Category.NAME, // 3
    	WebcamHolmes.Category.CREATED_BY_USER, // 4
    };

    
    
    
	//---------- Constructor    
    public ItemsDao(Context context, RainbowLogFacility logFacility) {
    	mLogFacility = checkNotNull(logFacility, "Log Facility");
    	mOpenHelper = new DatabaseHelper(context, mLogFacility);
	}
	

    
	//---------- Inner classes
    /**
     * This class helps open, create, and upgrade the database file.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        private final RainbowLogFacility mLogFacility;

        DatabaseHelper(Context context, RainbowLogFacility logFacility) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mLogFacility = logFacility;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + WebcamHolmes.Webcam.TABLE_NAME + " ("
                    + WebcamHolmes.Webcam._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + WebcamHolmes.Webcam.PARENT_CATEGORY_ID + " INTEGER NOT NULL,"
                    + WebcamHolmes.Webcam.NAME + " TEXT NOT NULL,"
                    + WebcamHolmes.Webcam.TYPE + " SMALL NOT NULL,"
                    + WebcamHolmes.Webcam.IMAGEURL + " TEXT NOT NULL,"
                    + WebcamHolmes.Webcam.RELOAD_INTERVAL + " SMALL,"
                    + WebcamHolmes.Webcam.PREFERRED + " BOOLEAN,"
                    + WebcamHolmes.Webcam.CREATED_BY_USER + " BOOLEAN, "
                    + WebcamHolmes.Webcam.SITE_URL + " TEXT,"
                    + WebcamHolmes.Webcam.USERNAME + " TEXT,"
                    + WebcamHolmes.Webcam.PASSWORD + " TEXT,"
                    + WebcamHolmes.Webcam.FREEDATA1 + " TEXT,"
                    + WebcamHolmes.Webcam.FREEDATA2 + " TEXT,"
                    + WebcamHolmes.Webcam.FREEDATA3 + " TEXT"
                    + ");");
            db.execSQL("CREATE TABLE " + WebcamHolmes.Category.TABLE_NAME + " ("
                    + WebcamHolmes.Category._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + WebcamHolmes.Category.ALIAS_ID + " INTEGER NOT NULL,"
                    + WebcamHolmes.Category.PARENT_CATEGORY_ID + " INTEGER NOT NULL,"
                    + WebcamHolmes.Category.NAME + " TEXT NOT NULL,"
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

	public ItemCategory getCategoryByAliasId(long aliasId) {
        List<ItemToDisplay> categories = getCategoriesFromDatabase(WebcamHolmes.Category.ALIAS_ID + "=" + aliasId);

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
        long webcamId = insertWebcamCore(db, webcam);
        return webcamId;
	}

	/**
	 * Add new webcams to the database
	 * @param webcam
	 * @return the numbers of added webcams
	 */
	public int insertWebcams(List<ItemWebcam> webcams) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int insertedItem = 0;
        for (ItemWebcam webcam:webcams) {
        	insertWebcamCore(db, webcam);
        	insertedItem++;
        }
        db.close();
        return insertedItem;
	}

	/**
	 * Add a new category to the database
	 * @param category
	 * @return the id of the new category
	 */
	public long insertCategory(ItemCategory category) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long categoryId = insertCategoryCore(db, category);
        db.close();
        return categoryId;
	}
	
	/**
	 * Add new categories to the database
	 * @param categories
	 * @return the number of categories added
	 */
	public int insertCategories(List<ItemCategory> categories) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int insertedItem = 0;
        for (ItemCategory category:categories) {
        	insertCategoryCore(db, category);
        	insertedItem++;
        }
        db.close();
        return insertedItem;
	}
	
	/**
	 * Add an entire list of {@link ItemToDisplay} to the database,
	 * opening the db connection only one time
	 * 
	 * @param items
	 */
	public void insertItem(List<ItemToDisplay> items) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        
        for (ItemToDisplay item:items) {
        	if (item instanceof ItemCategory)
        		insertCategoryCore(db, ItemCategory.class.cast(item));
        	if (item instanceof ItemWebcam)
        		insertWebcamCore(db, ItemWebcam.class.cast(item));
        }
        db.close();
	}

	/**
	 * Remov]e a webcam
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
        db.close();
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
        db.close();
		return count;
	}
	
	/**
	 * Set the preferred status of a webcam
	 * @param webcamId
	 * @param preferred
	 */
	public int setWebcamPreferredStatus(long webcamId, boolean preferred) {
		ContentValues values = new ContentValues();
		values.put(WebcamHolmes.Webcam.PREFERRED, preferred);
		return updateWebcamFields(webcamId, values);
	}

	/**
	 * Set the parent id of a category
	 * @param categoryId
	 * @param newParentId
	 * @return
	 */
	public int setCategoryParentId(long categoryId, long newParentId) {
		ContentValues values = new ContentValues();
		values.put(WebcamHolmes.Category.PARENT_CATEGORY_ID, newParentId);
		return updateCategoryFields(categoryId, values);
	}

	/**
	 * Set the parend id of a webcam
	 * @param webcamId
	 * @param newParentId
	 * @return
	 */
	public int setWebcamParentId(long webcamId, long newParentId) {
		ContentValues values = new ContentValues();
		values.put(WebcamHolmes.Webcam.PARENT_CATEGORY_ID, newParentId);
		return updateWebcamFields(webcamId, values);
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
        db.close();
        return count;
	}
	
	
	/**
	 * Import webcam and categories from a xml resource file</p>
	 * </p>
	 * Performance optimization: add all the categories in a single
	 * step, not one db open/insert/db close operation for each
	 * category. Same with webcams
	 * 
	 * @param context
	 * @param resourceId
	 * @return
	 */
	public ResultOperation<Integer> importFromResource(
			Context context, int resourceId){
		int totalAddedItems = 0;
		int addedItems;
		int processedItem;

		//obtains the list of elements from file
		ItemsXmlParser parser = new ItemsXmlParser();
		ResultOperation<List<ItemToDisplay>> resImport = parser.parseResource(context, resourceId);

		//errors
		if (resImport.hasErrors()) {
			return new ResultOperation<Integer>(resImport.getException(), resImport.getReturnCode());
		}
		
		//nothing to import
		List<ItemToDisplay> elements = resImport.getResult();
		if (null == elements || 0 == elements.size()) {
			return new ResultOperation<Integer>(0);
		}
		
		//creates categories list
		List<ItemCategory> categoriesToAdd = new ArrayList<ItemCategory>();
		for (ItemToDisplay item : elements) {
			if (item instanceof ItemCategory)
				categoriesToAdd.add(ItemCategory.class.cast(item));
		}
		//add all categories (with wrong parendId set)
		addedItems = insertCategories(categoriesToAdd);
		if (addedItems != categoriesToAdd.size()) {
			return new ResultOperation<Integer>(
					new Exception("Error while adding new categories. Expected " + categoriesToAdd.size() + ", added " + addedItems),
					ResultOperation.RETURNCODE_ERROR_GENERIC);
		}
		totalAddedItems+= addedItems;
		
		//second round, now that categories was added,
		//for each item, recalculate the true parentId
		Map<Long, Long> parentIdsCache = new HashMap<Long, Long>();
		for(ItemToDisplay item : elements) {
			long parentAliasId = item.getParentAliasId();
			if (0 != parentAliasId) {
				//check if the alias is in the cache
				if(!parentIdsCache.containsKey(parentAliasId)) {
					//find the category that matches the alias
					ItemCategory parentCategory = getCategoryByAliasId(parentAliasId);
					//and put it in the cache
					if (null != parentCategory)
						parentIdsCache.put(parentAliasId, parentCategory.getId());
					else
						//no alias found, point to root category
						parentIdsCache.put(parentAliasId, 0l);
				}
				item.setParentId(parentIdsCache.get(parentAliasId));
			}
		}
		
		//update parentId of categories
		for (ItemCategory category : categoriesToAdd) {
			processedItem = setCategoryParentId(category.getId(), category.getParentId());
			if (0 == processedItem) {
				return new ResultOperation<Integer>(
						new Exception("Error while updating id of category " + category.getAliasId() + " " + category.getName()),
						ResultOperation.RETURNCODE_ERROR_GENERIC);
			}
		}
		//free memory
		categoriesToAdd.clear();
		
		//create webcams list
		List<ItemWebcam> webcamsToAdd = new ArrayList<ItemWebcam>();
		for (ItemToDisplay item : elements) {
			if (item instanceof ItemWebcam) {
				webcamsToAdd.add(ItemWebcam.class.cast(item));
			}
		}
		//add webcams (with right parendId set)
		addedItems = insertWebcams(webcamsToAdd);
		if (addedItems != webcamsToAdd.size()) {
			return new ResultOperation<Integer>(
					new Exception("Error while adding new webcams. Expected " + webcamsToAdd.size() + ", added " + addedItems),
					ResultOperation.RETURNCODE_ERROR_GENERIC);
		}
		totalAddedItems+= addedItems;

		
		return new ResultOperation<Integer>(totalAddedItems);
	}
	
	/**
	 * Clean the database, but preserve user data (ie new webcam and categories)
	 */
	public void clearDatabasePreservingUserData() {
		
	}
	
	
	/**
	 * Return true if database is empty and initialization is needed
	 */
	public boolean isDatabaseEmpty() {
		boolean categoriesExist;
		boolean webcamsExist;
		Cursor cur;
		
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        cur = db.query(WebcamHolmes.Webcam.TABLE_NAME,
        		new String[]{WebcamHolmes.Webcam._ID},
        		null, null, null, null, null);
        webcamsExist = cur.moveToFirst();
        cur.close();
        
        cur = db.query(WebcamHolmes.Category.TABLE_NAME,
        		new String[]{WebcamHolmes.Category._ID},
        		null, null, null, null, null);
        categoriesExist = cur.moveToFirst();
        cur.close();
        db.close();
        
        return !(webcamsExist || categoriesExist);
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
	        	boolean preferred = cur.getInt(6) == 1;
	        	boolean userCreated = cur.getInt(7) == 1;
	        	String freeData1 = cur.getString(8);
	        	String freeData2 = cur.getString(9);
	        	String freeData3 = cur.getString(10);
	        	ItemWebcam webcam = new ItemWebcam(
	        			id, parentCategoryId,
	        			name, type,
	        			imageUrl, reloadInterval,
	        			preferred, userCreated,
	        			freeData1, freeData2, freeData3);
	        	list.add(webcam);
	        } while (cur.moveToNext());
        }
        cur.close();
        db.close();
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
	        	long aliasId = cur.getLong(1);
	        	long parentCategoryId = cur.getLong(2);
	        	String name = cur.getString(3);
	        	boolean userCreated = cur.getInt(4) == 1;
	        	ItemCategory category = new ItemCategory(id, aliasId, parentCategoryId, name, userCreated);
	        	list.add(category);
	        } while (cur.moveToNext());
        }
        cur.close();
        db.close();
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

	
	/**
	 * Changes values in a category item
	 * @param categoryId id of the category
	 * @param valuesToUpdate values to change
	 * @return number of items updated (generally 1)
	 */
	private int updateCategoryFields(long categoryId, ContentValues valuesToUpdate) {
		int count;
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		count = db.update(
				WebcamHolmes.Category.TABLE_NAME,
				valuesToUpdate,
        		WebcamHolmes.Category._ID + "=" + categoryId,
				null);
		db.close();
		return count;
	}
	
	/**
	 * Changes values in a webcam item
	 * @param categoryId id of the category
	 * @param valuesToUpdate values to change
	 * @return number of items updated (generally 1)
	 */
	private int updateWebcamFields(long webcamId, ContentValues valuesToUpdate) {
		int count;
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		count = db.update(
				WebcamHolmes.Webcam.TABLE_NAME,
				valuesToUpdate,
        		WebcamHolmes.Webcam._ID + "=" + webcamId,
				null);
		db.close();
		return count;
	}
	
	/**
	 * @param db
	 * @param webcam
	 * @return
	 */
	private long insertWebcamCore(SQLiteDatabase db, ItemWebcam webcam) {
		ContentValues values = new ContentValues();
        values.put(WebcamHolmes.Webcam.PARENT_CATEGORY_ID, webcam.getParentId());
        values.put(WebcamHolmes.Webcam.NAME, webcam.getName());
        values.put(WebcamHolmes.Webcam.TYPE, webcam.getType());
        values.put(WebcamHolmes.Webcam.IMAGEURL, webcam.getImageUrl());
        values.put(WebcamHolmes.Webcam.RELOAD_INTERVAL, webcam.getReloadInterval());
        values.put(WebcamHolmes.Webcam.PREFERRED, webcam.isPreferred());
        values.put(WebcamHolmes.Webcam.CREATED_BY_USER, webcam.isUserCreated());
        values.put(WebcamHolmes.Webcam.FREEDATA1, webcam.getFreeData1());
        values.put(WebcamHolmes.Webcam.FREEDATA2, webcam.getFreeData2());
        values.put(WebcamHolmes.Webcam.FREEDATA3, webcam.getFreeData3());

        long webcamId = db.insert(WebcamHolmes.Webcam.TABLE_NAME, WebcamHolmes.Webcam.NAME, values);
        webcam.setId(webcamId);

        return webcamId;
	}
	
	/**
	 * @param db
	 * @param category
	 * @return
	 */
	protected long insertCategoryCore(SQLiteDatabase db, ItemCategory category) {
		ContentValues values = new ContentValues();
        values.put(WebcamHolmes.Category.ALIAS_ID, category.getAliasId());
        values.put(WebcamHolmes.Category.PARENT_CATEGORY_ID, category.getParentId());
        values.put(WebcamHolmes.Category.NAME, category.getName());
        values.put(WebcamHolmes.Category.CREATED_BY_USER, category.isUserCreated());

        long categoryId = db.insert(WebcamHolmes.Category.TABLE_NAME, WebcamHolmes.Category.NAME, values);
        category.setId(categoryId);

        return categoryId;
	}
	
}
