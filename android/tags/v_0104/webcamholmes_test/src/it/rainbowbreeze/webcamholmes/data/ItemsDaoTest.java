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

import java.util.ArrayList;
import java.util.List;

import it.rainbowbreeze.libs.common.RainbowLogFacility;
import it.rainbowbreeze.webcamholmes.R;
import it.rainbowbreeze.webcamholmes.common.ResultOperation;
import it.rainbowbreeze.webcamholmes.domain.ItemCategory;
import it.rainbowbreeze.webcamholmes.domain.ItemToDisplay;
import it.rainbowbreeze.webcamholmes.domain.ItemWebcam;
import android.test.AndroidTestCase;
import android.text.TextUtils;
import android.util.Log;

/**
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public class ItemsDaoTest extends AndroidTestCase {

	//---------- Constructor

	
	
	
	//---------- Private fields
    private ItemsDao mDao;
	private RainbowLogFacility logFacility = new RainbowLogFacility("WEBCAMHOLMES_TEST");

	
	
	
	//---------- Test initialization
    @Override
    protected void setUp() throws Exception {
        mDao = new ItemsDao(getContext(), logFacility);
        mDao.clearDatabaseComplete();
    }

	
	
	
	//---------- Test cases	
    public void testInsertWebcam() {
        ItemWebcam webcam;

        //insert first webcam
        webcam = createWebcam1();
        long webcamId1 = mDao.insertWebcam(webcam);
        compareWithWebcam1(webcamId1);
        
        //insert second webcam
        webcam = createWebcam2();
        long webcamId2 = mDao.insertWebcam(webcam);
        compareWithWebcam2(webcamId2);
    }
        
    public void testInsertWebcams(){
    	List<ItemWebcam> webcams = new ArrayList<ItemWebcam>();
    	webcams.add(createWebcam1());
    	webcams.add(createWebcam2());
    	webcams.add(createWebcam3());
    	
    	int added = mDao.insertWebcams(webcams);
    	assertEquals("Wrong numbers of added webcams", 3, added);
    	long webcamId1 = webcams.get(0).getId();
    	compareWithWebcam1(webcamId1);
    	long webcamId2 = webcams.get(1).getId();
    	compareWithWebcam2(webcamId2);
    	long webcamId3 = webcams.get(2).getId();
    	compareWithWebcam3(webcamId3);
    }

	public void testDeleteWebcam() {
        ItemWebcam webcam;
        ItemWebcam loadedWebcam1;
        ItemWebcam loadedWebcam2;
    	
        //insert webcams
        webcam = ItemWebcam.Factory.getSystemWebcam(0, "Paris - Tour Eiffel", "http://www.parislive.net/eiffelwebcam01.jpg", 5);
        long webcamId1 = mDao.insertWebcam(webcam);
        webcam = ItemWebcam.Factory.getSystemWebcam(0, "Webcam 2", "http://amrc.ssec.wisc.edu/~amrc/webcam/b15k/20050216_02.jpg", 0);
        long webcamId2 = mDao.insertWebcam(webcam);
        
        //check if webcams could be retrieved
        loadedWebcam1 = mDao.getWebcamById(webcamId1);
        loadedWebcam2 = mDao.getWebcamById(webcamId2);
        assertNotNull("Webcam1 doesn't exists", loadedWebcam1);
        assertNotNull("Webcam2 doesn't exists", loadedWebcam2);
        //delete first webcam
        mDao.deleteWebcam(webcamId1);
        loadedWebcam1 = mDao.getWebcamById(webcamId1);
        loadedWebcam2 = mDao.getWebcamById(webcamId2);
        assertNull("Webcam1 still exists", loadedWebcam1);
        assertNotNull("Webcam2 doesn't exists", loadedWebcam2);
        //delete second webcam
        mDao.deleteWebcam(webcamId2);
        loadedWebcam1 = mDao.getWebcamById(webcamId1);
        loadedWebcam2 = mDao.getWebcamById(webcamId2);
        assertNull("Webcam1 still exists", loadedWebcam1);
        assertNull("Webcam2 still exists", loadedWebcam2);
    }
    
    public void testGetWebcamById_NoWebcam(){
    	ItemWebcam webcam = mDao.getWebcamById(1243123);
    	assertNull("Webcam exists", webcam);
    }

    public void testGetCategoryById_NoCategory(){
    	ItemCategory category = mDao.getCategoryById(1243123);
    	assertNull("Category exists", category);
    }
    
    public void testGetCategoryByAliasId() {
    	ItemCategory category;
    	
    	category = mDao.getCategoryByAliasId(1243123);
    	assertNull("Category exists", category);
    	
    	category = ItemCategory.Factory.getSystemCategory(342, 421, "Test Alias Id 1");
    	long categoryId1 = mDao.insertCategory(category);
    	category = ItemCategory.Factory.getUserCategory(23, 421, "Test Alias Id 2");
    	long categoryId2 = mDao.insertCategory(category);
    	
    	category = mDao.getCategoryByAliasId(342);
    	assertNotNull("Catogory doesn't exists", category);
    	assertEquals("Wrong category id", categoryId1, category.getId());
    	assertEquals("Wrong category name", "Test Alias Id 1", category.getName());

    	category = mDao.getCategoryByAliasId(23);
    	assertNotNull("Catogory doesn't exists", category);
    	assertEquals("Wrong category id", categoryId2, category.getId());
    	assertEquals("Wrong category name", "Test Alias Id 2", category.getName());
    }

    public void testInsertCategory() {
        ItemCategory category;

        //insert first category
        category = createCategory1();
        long categoryId1 = mDao.insertCategory(category);
        compareWithCategory1(categoryId1);
        
        //insert second category
        category = createCategory2();
        long categoryId2 = mDao.insertCategory(category);
        compareWithCategory2(categoryId2);
    }
    
    public void testInsertCategories(){
    	List<ItemCategory> categories = new ArrayList<ItemCategory>();
    	categories.add(createCategory1());
    	categories.add(createCategory2());
    	
    	int added = mDao.insertCategories(categories);
    	assertEquals("Wrong numbers of added categories", 2, added);
    	long categoryId1 = categories.get(0).getId();
    	compareWithCategory1(categoryId1);
    	long categoryId2 = categories.get(1).getId();
    	compareWithCategory2(categoryId2);
    }

    public void testDeleteCategory() {
        ItemCategory category;
        ItemCategory loadedCategory1;
        ItemCategory loadedCategory2;
    	
        //insert categories
        category = ItemCategory.Factory.getSystemCategory(126, "Category Paris");
        long categoryId1 = mDao.insertCategory(category);
        category = ItemCategory.Factory.getSystemCategory(53, "Category London");
        long categoryId2 = mDao.insertCategory(category);
        
        //check if categories could be retrieved
        loadedCategory1 = mDao.getCategoryById(categoryId1);
        loadedCategory2 = mDao.getCategoryById(categoryId2);
        assertNotNull("Category1 doesn't exists", loadedCategory1);
        assertNotNull("Category2 doesn't exists", loadedCategory2);
        //delete first category
        mDao.deleteCategory(categoryId1);
        loadedCategory1 = mDao.getCategoryById(categoryId1);
        loadedCategory2 = mDao.getCategoryById(categoryId2);
        assertNull("Category1 still exists", loadedCategory1);
        assertNotNull("Category2 doesn't exists", loadedCategory2);
        //delete second Category
        mDao.deleteCategory(categoryId2);
        loadedCategory1 = mDao.getCategoryById(categoryId1);
        loadedCategory2 = mDao.getCategoryById(categoryId2);
        assertNull("Category still exists", loadedCategory1);
        assertNull("Category still exists", loadedCategory2);
    }
    
    
    public void testGetChildrenOfParentItem() {
    	ItemWebcam webcam;
        ItemCategory category;
        List<ItemToDisplay> items;
        
        webcam = ItemWebcam.Factory.getSystemWebcam(54, "Paris - Tour Eiffel", "http://www.parislive.net/eiffelwebcam01.jpg", 5);
        long webcamId1 = mDao.insertWebcam(webcam);
        webcam = ItemWebcam.Factory.getSystemWebcam(54, "Webcam 2", "http://amrc.ssec.wisc.edu/~amrc/webcam/b15k/20050216_02.jpg", 0);
        long webcamId2 = mDao.insertWebcam(webcam);
        webcam = ItemWebcam.Factory.getSystemWebcam(123, "Webcam 3", "http://localhost.edu/20050216_02.jpg", 30);
        long webcamId3 = mDao.insertWebcam(webcam);
        category = ItemCategory.Factory.getSystemCategory(54, "Testcategory 1");
        long categoryId1 = mDao.insertCategory(category);
        category = ItemCategory.Factory.getSystemCategory(123, "Testcategory 1");
        long categoryId2 = mDao.insertCategory(category);
        
        items = mDao.getChildrenOfParentItem(2);
        assertNotNull("List of items retrieved cannot be null", items);
        assertEquals("Wrong number of items retrieved", 0, items.size());

        items = mDao.getChildrenOfParentItem(54);
        assertNotNull("List of items retrieved cannot be null", items);
        assertNotNull("List of items retrieved cannot be null", items);
        assertEquals("Wrong number of items retrieved", 3, items.size());
        assertEquals("Wrong item id", categoryId1, items.get(0).getId());
        assertEquals("Wrong item id", webcamId1, items.get(1).getId());
        assertEquals("Wrong item id", webcamId2, items.get(2).getId());

        items = mDao.getChildrenOfParentItem(123);
        assertNotNull("List of items retrieved cannot be null", items);
        assertNotNull("List of items retrieved cannot be null", items);
        assertEquals("Wrong number of items retrieved", 2, items.size());
        assertEquals("Wrong item id", categoryId2, items.get(0).getId());
        assertEquals("Wrong item id", webcamId3, items.get(1).getId());
    }
    
    public void testSetWebcamPreferredStatus() {
    	ItemWebcam webcam;
    	int count;
    	
    	count = mDao.setWebcamPreferredStatus(11231, true);
    	assertEquals("Wrong count operation", 0, count);
    	
        webcam = ItemWebcam.Factory.getSystemWebcam(54, "Paris - Tour Eiffel", "http://www.parislive.net/eiffelwebcam01.jpg", 5);
        long webcamId = mDao.insertWebcam(webcam);
    	
        webcam = mDao.getWebcamById(webcamId);
        assertFalse("Wrong preferred status", webcam.isPreferred());

        count = mDao.setWebcamPreferredStatus(webcamId, true);
    	assertEquals("Wrong count operation", 1, count);
        webcam = mDao.getWebcamById(webcamId);
        assertTrue("Wrong preferred status", webcam.isPreferred());

        count = mDao.setWebcamPreferredStatus(webcamId, false);
    	assertEquals("Wrong count operation", 1, count);
        webcam = mDao.getWebcamById(webcamId);
        assertFalse("Wrong preferred status", webcam.isPreferred());
    }
    
    public void testSetWebcamParendId() {
    	ItemWebcam webcam;
    	int res;
    	
    	//non existing item
    	res = mDao.setWebcamParentId(329382, 221);
    	assertEquals("Wrong item processed", 0, res);
    	
        webcam = ItemWebcam.Factory.getSystemWebcam(54, "Paris - Tour Eiffel", "http://www.parislive.net/eiffelwebcam01.jpg", 5);
        long webcamId = mDao.insertWebcam(webcam);
    	
        webcam = mDao.getWebcamById(webcamId);
        assertEquals("Wrong parendId", 54, webcam.getParentId());

        //change the parent
        res = mDao.setWebcamParentId(webcamId, 192);
    	assertEquals("Wrong item processed", 1, res);
        webcam = mDao.getWebcamById(webcamId);
        assertEquals("Wrong parendId", 192, webcam.getParentId());
    }
    
    public void testSetCategoryParendId() {
    	ItemCategory category;
    	int res;
    	
    	//non existing item
    	res = mDao.setCategoryParentId(329382, 221);
    	assertEquals("Wrong item processed", 0, res);
    	
        category = ItemCategory.Factory.getUserCategory(192, "Traffic main road");
        long categoryId = mDao.insertCategory(category);
    	
        category = mDao.getCategoryById(categoryId);
        assertEquals("Wrong parendId", 192, category.getParentId());

        //change the parent
        res = mDao.setCategoryParentId(categoryId, 291);
    	assertEquals("Wrong item processed", 1, res);
        category = mDao.getCategoryById(categoryId);
        assertEquals("Wrong parendId", 291, category.getParentId());
    }
    
    
    public void testGetParendIdOfCategory() {
    	ItemWebcam webcam;
        ItemCategory category;

        category = ItemCategory.Factory.getSystemCategory(0, "Testcategory 1");
        long categoryId1 = mDao.insertCategory(category);
        category = ItemCategory.Factory.getSystemCategory(0, "Testcategory 1");
        long categoryId2 = mDao.insertCategory(category);
        category = ItemCategory.Factory.getSystemCategory(categoryId1, "Testcategory 1.1");
        long categoryId11 = mDao.insertCategory(category);
        category = ItemCategory.Factory.getSystemCategory(categoryId11, "Testcategory 1.2");
        long categoryId12 = mDao.insertCategory(category);
        
        webcam = ItemWebcam.Factory.getSystemWebcam(0, "Webcam 3", "http://localhost.edu/20050216_02.jpg", 30);
        mDao.insertWebcam(webcam);
        webcam = ItemWebcam.Factory.getSystemWebcam(categoryId1, "Paris - Tour Eiffel", "http://www.parislive.net/eiffelwebcam01.jpg", 5);
        mDao.insertWebcam(webcam);
        webcam = ItemWebcam.Factory.getSystemWebcam(categoryId11, "Webcam 2", "http://amrc.ssec.wisc.edu/~amrc/webcam/b15k/20050216_02.jpg", 0);
        mDao.insertWebcam(webcam);
        webcam = ItemWebcam.Factory.getSystemWebcam(categoryId12, "Webcam 3", "http://localhost.edu/20050216_02.jpg", 30);
        mDao.insertWebcam(webcam);
    	
        assertEquals(0, mDao.getParentIdOfCategory(0));
        assertEquals(0, mDao.getParentIdOfCategory(categoryId1));
        assertEquals(categoryId1, mDao.getParentIdOfCategory(categoryId11));
        assertEquals(categoryId11, mDao.getParentIdOfCategory(categoryId12));
        assertEquals(categoryId11, mDao.getParentIdOfCategory(categoryId12));
        assertEquals(0, mDao.getParentIdOfCategory(categoryId2));
    }
    
    
    public void testIsDatabaseEmpty() {
    	assertTrue("Database not empty", mDao.isDatabaseEmpty());
    	
    	//insert a webcam
        ItemWebcam webcam = ItemWebcam.Factory.getSystemWebcam(0, "Webcam 3", "http://localhost.edu/20050216_02.jpg", 30);
        mDao.insertWebcam(webcam);
    	assertFalse("Database is empty", mDao.isDatabaseEmpty());
    	
    	mDao.clearDatabaseComplete();
    	assertTrue("Database not empty", mDao.isDatabaseEmpty());
        ItemCategory category = ItemCategory.Factory.getSystemCategory(0, "Testcategory 1");
        mDao.insertCategory(category);
    	assertFalse("Database is empty", mDao.isDatabaseEmpty());
    }
    
    
    public void testImportFromResource() {
        List<ItemToDisplay> items;
        
    	ResultOperation<Integer> res = mDao.importFromResource(getContext(), R.xml.testimportdata1);
    	assertFalse("Errors during import", res.hasErrors());
    	assertTrue("No elements added", res.getResult() > 0);
    	
    	//check for root children
        items = mDao.getChildrenOfParentItem(0);
        for (ItemToDisplay item:items)
        	Log.e("WEBCAMTEST", item.getName());
        assertNotNull("List of items retrieved cannot be null", items);
        assertEquals("Wrong number of items retrieved", 3, items.size());
        
        //checks elements and their values
        long categoryId = 0;
        for (ItemToDisplay item : items) {
        	if (item instanceof ItemCategory) {
        		categoryId = item.getId();
        		assertEquals("Wrong category name", "Traffic", item.getName());
        		assertTrue("Wrong user created", ((ItemCategory) item).isUserCreated());
        	}
        }
        
    	//check for children of first category
        items = mDao.getChildrenOfParentItem(categoryId);
        assertNotNull("List of items retrieved cannot be null", items);
        assertEquals("Wrong number of items retrieved", 3, items.size());
    	
        //checks elements and their values
        for (ItemToDisplay item : items) {
        	if (item instanceof ItemCategory && item.getName().equals("Major road")) {
        	} else if (item instanceof ItemWebcam && item.getName().equals("Paris - Tour Eiffel")) {
    		} else if (item instanceof ItemWebcam && item.getName().equals("Paris from webcam.travel")) {
        	} else {
        		assertFalse("Not expected item", true);
        	}
        }
    }
    
    
    public void testClearDatabaseComplete() {
		long categoryId = mDao.insertCategory(ItemCategory.Factory.getSystemCategory(0, "Italy places"));
		assertNotSame("Cannot create category", 0, categoryId);
		ItemCategory category = mDao.getCategoryById(categoryId);
		assertNotNull("Cannot retrieve category", category);
		
		long webcamId1 = mDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(AN) Riviera del Conero", "http://www.damablu.it/video.jpg", 10));
		assertNotSame("Cannot create webcam", 0, webcamId1);
		long webcamId2 = mDao.insertWebcam(ItemWebcam.Factory.getUserWebcam(categoryId, "(AO) Aosta - Arco d'Augusto", "http://www.regione.vda.it/Bollettino_neve/Images/ao2.jpg", 60));
		assertNotSame("Cannot create webcam", 0, webcamId2);
		long webcamId3 = mDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(AO) Valtournenche - piste da sci", "http://www.regione.vda.it/Bollettino_neve/Images/valtour.jpg", 60));
		assertNotSame("Cannot create webcam", 0, webcamId3);
		
		mDao.clearDatabaseComplete();
		assertNull("Category still exists", mDao.getCategoryById(categoryId));
		assertNull("Webcam 1 still exists", mDao.getWebcamById(webcamId1));
		assertNull("Webcam 2 still exists", mDao.getWebcamById(webcamId2));
		assertNull("Webcam 3 still exists", mDao.getWebcamById(webcamId3));
    }
    
    

    
	//---------- Private methods
	private ItemWebcam createWebcam1() {
		return ItemWebcam.Factory.getSystemWebcam(43, "Paris - Tour Eiffel", "http://www.parislive.net/eiffelwebcam01.jpg", 5);
	}

	/**
	 * @param webcamId
	 */
	private void compareWithWebcam1(long webcamId) {
		ItemWebcam webcam;
		webcam = mDao.getWebcamById(webcamId);
        assertEquals("Wrong id", webcamId, webcam.getId());
        assertEquals("Wrong parentId", 43, webcam.getParentId());
        assertEquals("Wrong name", "Paris - Tour Eiffel", webcam.getName());
        assertEquals("Wrong webcam type", 1, webcam.getType());
        assertEquals("Wrong url", "http://www.parislive.net/eiffelwebcam01.jpg", webcam.getImageUrl());
        assertEquals("Wrong interval", 5, webcam.getReloadInterval());
        assertFalse("Wrong preferred", webcam.isPreferred());
        assertFalse("Wrong user created", webcam.isUserCreated());
		assertTrue("Wrong freeData1", TextUtils.isEmpty(webcam.getFreeData1()));
		assertTrue("Wrong freeData2", TextUtils.isEmpty(webcam.getFreeData2()));
		assertTrue("Wrong freeData3", TextUtils.isEmpty(webcam.getFreeData3()));
	}
    
    private ItemWebcam createWebcam2() {
    	ItemWebcam webcam;
		webcam = ItemWebcam.Factory.getUserWebcam(52, "Webcam 2", "http://amrc.ssec.wisc.edu/~amrc/webcam/b15k/20050216_02.jpg", 0);
        webcam.setPreferred(true);
        return webcam;
	}

    private void compareWithWebcam2(long webcamId) {
		ItemWebcam webcam = mDao.getWebcamById(webcamId);
        assertEquals("Wrong id", webcamId, webcam.getId());
        assertEquals("Wrong parentId", 52, webcam.getParentId());
        assertEquals("Wrong name", "Webcam 2", webcam.getName());
        assertEquals("Wrong webcam type", 1, webcam.getType());
        assertEquals("Wrong url", "http://amrc.ssec.wisc.edu/~amrc/webcam/b15k/20050216_02.jpg", webcam.getImageUrl());
        assertEquals("Wrong interval", 0, webcam.getReloadInterval());
        assertTrue("Wrong preferred", webcam.isPreferred());
        assertTrue("Wrong user created", webcam.isUserCreated());
		assertTrue("Wrong freeData1", TextUtils.isEmpty(webcam.getFreeData1()));
		assertTrue("Wrong freeData2", TextUtils.isEmpty(webcam.getFreeData2()));
		assertTrue("Wrong freeData3", TextUtils.isEmpty(webcam.getFreeData3()));
	}

	private ItemWebcam createWebcam3() {
		return ItemWebcam.Factory.getWebcamTravelWebcam(59, "Paris from webcam.travel", "http://webcam.travel/2323523.jpg", "User2", "http://forum.webcam.travel/user2", "http://webcam.travel/13243432");
	}

	/**
	 * @param webcamId
	 */
	private void compareWithWebcam3(long webcamId) {
		ItemWebcam webcam;
		webcam = mDao.getWebcamById(webcamId);
        assertEquals("Wrong id", webcamId, webcam.getId());
        assertEquals("Wrong parentId", 59, webcam.getParentId());
        assertEquals("Wrong name", "Paris from webcam.travel", webcam.getName());
        assertEquals("Wrong webcam type", 2, webcam.getType());
        assertEquals("Wrong url", "http://webcam.travel/2323523.jpg", webcam.getImageUrl());
        assertEquals("Wrong interval", 30, webcam.getReloadInterval());
        assertFalse("Wrong preferred", webcam.isPreferred());
        assertFalse("Wrong user created", webcam.isUserCreated());
		assertEquals("Wrong freeData1", "User2", webcam.getFreeData1());
		assertEquals("Wrong freeData2", "http://forum.webcam.travel/user2", webcam.getFreeData2());
		assertEquals("Wrong freeData3", "http://webcam.travel/13243432", webcam.getFreeData3());
	}
    
    private ItemCategory createCategory1() {
		return ItemCategory.Factory.getUserCategory(100, 200, "Testcategory 1");
	}

    private ItemCategory createCategory2() {
		return ItemCategory.Factory.getSystemCategory(123, 456, "Testcategory 2");
	}

	private void compareWithCategory1(long categoryId) {
        ItemCategory loadedCategory = mDao.getCategoryById(categoryId);
        assertNotNull(loadedCategory);
        assertEquals("Wrong id", categoryId, loadedCategory.getId());
        assertEquals("Wrong aliasId", 100, loadedCategory.getAliasId());
        assertEquals("Wrong parentId", 200, loadedCategory.getParentId());
        assertEquals("Wrong name", "Testcategory 1", loadedCategory.getName());
        assertTrue("Wrong user created", loadedCategory.isUserCreated());
	}

    private void compareWithCategory2(long categoryId2) {
        ItemCategory loadedCategory = mDao.getCategoryById(categoryId2);
        assertNotNull(loadedCategory);
        assertEquals("Wrong id", categoryId2, loadedCategory.getId());
        assertEquals("Wrong aliasId", 123, loadedCategory.getAliasId());
        assertEquals("Wrong parentId", 456, loadedCategory.getParentId());
        assertEquals("Wrong name", "Testcategory 2", loadedCategory.getName());
        assertFalse("Wrong user created", loadedCategory.isUserCreated());
	}
}
