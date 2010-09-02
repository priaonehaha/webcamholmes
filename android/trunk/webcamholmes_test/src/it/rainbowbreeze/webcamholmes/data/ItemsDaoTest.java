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
package it.rainbowbreeze.webcamholmes.data;

import java.util.List;

import it.rainbowbreeze.webcamholmes.domain.ItemCategory;
import it.rainbowbreeze.webcamholmes.domain.ItemToDisplay;
import it.rainbowbreeze.webcamholmes.domain.ItemWebcam;
import android.test.AndroidTestCase;

/**
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public class ItemsDaoTest extends AndroidTestCase {

    ItemsDao mDao;
    
    @Override
    protected void setUp() throws Exception {
        mDao = new ItemsDao(getContext());
        mDao.clearDatabaseComplete();
    }
    
    public void testInsertWebcam() {
        ItemWebcam webcam;
        ItemWebcam loadedWebcam;

        //insert first webcam
        webcam = new ItemWebcam(0, 43, "Paris - Tour Eiffel", "http://www.parislive.net/eiffelwebcam01.jpg", 5);
        long webcamId1 = mDao.insertWebcam(webcam);
        
        loadedWebcam = mDao.getWebcamById(webcamId1);
        assertEquals("Wrong id", webcamId1, loadedWebcam.getId());
        assertEquals("Wrong parentId", 43, loadedWebcam.getParentId());
        assertEquals("Wrong name", "Paris - Tour Eiffel", loadedWebcam.getName());
        assertEquals("Wrong url", "http://www.parislive.net/eiffelwebcam01.jpg", loadedWebcam.getImageUrl());
        assertEquals("Wrong interval", 5, loadedWebcam.getReloadInterval());
        
        //insert second webcam
        webcam = new ItemWebcam(0, 52, "Webcam 2", "http://amrc.ssec.wisc.edu/~amrc/webcam/b15k/20050216_02.jpg", 0);
        long webcamId2 = mDao.insertWebcam(webcam);
        
        loadedWebcam = mDao.getWebcamById(webcamId2);
        assertEquals("Wrong id", webcamId2, loadedWebcam.getId());
        assertEquals("Wrong parentId", 52, loadedWebcam.getParentId());
        assertEquals("Wrong name", "Webcam 2", loadedWebcam.getName());
        assertEquals("Wrong url", "http://amrc.ssec.wisc.edu/~amrc/webcam/b15k/20050216_02.jpg", loadedWebcam.getImageUrl());
        assertEquals("Wrong interval", 0, loadedWebcam.getReloadInterval());
    }
    
    public void testInsertWebcamWithFixedId() {
        ItemWebcam webcam;

        //insert first webcam
        long expectedId = 2354;
        webcam = new ItemWebcam(expectedId, 43, "Paris - Tour Eiffel", "http://www.parislive.net/eiffelwebcam01.jpg", 5);
        long webcamId = mDao.insertWebcam(webcam);
        
//        assertEquals("Ids are different", expectedId, webcamId);
        assertEquals("Ids are different", 1l, webcamId);
    }
    
    public void testDeleteWebcam() {
        ItemWebcam webcam;
        ItemWebcam loadedWebcam1;
        ItemWebcam loadedWebcam2;
    	
        //insert webcams
        webcam = new ItemWebcam(0, 0, "Paris - Tour Eiffel", "http://www.parislive.net/eiffelwebcam01.jpg", 5);
        long webcamId1 = mDao.insertWebcam(webcam);
        webcam = new ItemWebcam(0, 0, "Webcam 2", "http://amrc.ssec.wisc.edu/~amrc/webcam/b15k/20050216_02.jpg", 0);
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

    public void testInsertCategory() {
        ItemCategory category;
        ItemCategory loadedCategory;

        //insert first category
        category = new ItemCategory(0, 100, "Testcategory 1");
        long categoryId1 = mDao.insertCategory(category);
        
        loadedCategory = mDao.getCategoryById(categoryId1);
        assertEquals("Wrong id", categoryId1, loadedCategory.getId());
        assertEquals("Wrong parentId", 100, loadedCategory.getParentId());
        assertEquals("Wrong name", "Testcategory 1", loadedCategory.getName());
        
        //insert second category
        category = new ItemCategory(0, 123, "Testcategory 2");
        long categoryId2 = mDao.insertCategory(category);
        
        loadedCategory = mDao.getCategoryById(categoryId2);
        assertEquals("Wrong id", categoryId2, loadedCategory.getId());
        assertEquals("Wrong parentId", 123, loadedCategory.getParentId());
        assertEquals("Wrong name", "Testcategory 2", loadedCategory.getName());
    }
    
    public void testDeleteCategory() {
        ItemCategory category;
        ItemCategory loadedCategory1;
        ItemCategory loadedCategory2;
    	
        //insert categories
        category = new ItemCategory(0, 126, "Category Paris");
        long categoryId1 = mDao.insertCategory(category);
        category = new ItemCategory(0, 53, "Category London");
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
        
        webcam = new ItemWebcam(0, 54, "Paris - Tour Eiffel", "http://www.parislive.net/eiffelwebcam01.jpg", 5);
        long webcamId1 = mDao.insertWebcam(webcam);
        webcam = new ItemWebcam(0, 54, "Webcam 2", "http://amrc.ssec.wisc.edu/~amrc/webcam/b15k/20050216_02.jpg", 0);
        long webcamId2 = mDao.insertWebcam(webcam);
        webcam = new ItemWebcam(0, 123, "Webcam 3", "http://localhost.edu/20050216_02.jpg", 30);
        long webcamId3 = mDao.insertWebcam(webcam);
        category = new ItemCategory(0, 54, "Testcategory 1");
        long categoryId1 = mDao.insertCategory(category);
        category = new ItemCategory(0, 123, "Testcategory 1");
        long categoryId2 = mDao.insertCategory(category);
        
        items = mDao.getChildrenOfParentItem(2);
        assertNotNull("List of items retrieved cannot be null", items);
        assertEquals("Wrong number of items retrieved", 0, items.size());

        items = mDao.getChildrenOfParentItem(54);
        assertNotNull("List of items retrieved cannot be null", items);
        assertNotNull("List of items retrieved cannot be null", items);
        assertEquals("Wrong number of items retrieved", 3, items.size());

        items = mDao.getChildrenOfParentItem(123);
        assertNotNull("List of items retrieved cannot be null", items);
        assertNotNull("List of items retrieved cannot be null", items);
        assertEquals("Wrong number of items retrieved", 2, items.size());
        
    }
}
