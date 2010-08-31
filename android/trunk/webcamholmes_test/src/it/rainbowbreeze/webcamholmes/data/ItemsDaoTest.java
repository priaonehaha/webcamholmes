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
    }
    
    public void testInsertWebcam() {
        ItemWebcam webcam;
        ItemWebcam loadedWebcam;

        //insert first webcam
        webcam = new ItemWebcam(0, 0, "Paris - Tour Eiffel", "http://www.parislive.net/eiffelwebcam01.jpg", 5);
        long webcamId1 = mDao.insertWebcam(webcam);
        
        loadedWebcam = mDao.getWebcamById(webcamId1);
        assertEquals("Wrong id", webcamId1, loadedWebcam.getId());
        assertEquals("Wrong parentId", 0, loadedWebcam.getParentId());
        assertEquals("Wrong name", "Paris - Tour Eiffel", loadedWebcam.getName());
        assertEquals("Wrong url", "http://www.parislive.net/eiffelwebcam01.jpg", loadedWebcam.getImageUrl());
        assertEquals("Wrong interval", 5, loadedWebcam.getReloadInterval());
        
        //insert second webcam
        webcam = new ItemWebcam(0, 0, "Webcam 2", "http://amrc.ssec.wisc.edu/~amrc/webcam/b15k/20050216_02.jpg", 0);
        long webcamId2 = mDao.insertWebcam(webcam);
        
        loadedWebcam = mDao.getWebcamById(webcamId2);
        assertEquals("Wrong id", webcamId2, loadedWebcam.getId());
        assertEquals("Wrong parentId", 0, loadedWebcam.getParentId());
        assertEquals("Wrong name", "Webcam 2", loadedWebcam.getName());
        assertEquals("Wrong url", "http://amrc.ssec.wisc.edu/~amrc/webcam/b15k/20050216_02.jpg", loadedWebcam.getImageUrl());
        assertEquals("Wrong interval", 0, loadedWebcam.getReloadInterval());
        
        //delete webcams
        mDao.deleteWebcam(webcamId1);
        mDao.deleteWebcam(webcamId2);
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
        ItemWebcam webcam;
        ItemWebcam loadedWebcam;

        //insert first category
        webcam = new ItemWebcam(0, 0, "Paris - Tour Eiffel", "http://www.parislive.net/eiffelwebcam01.jpg", 5);
        long webcamId1 = mDao.insertWebcam(webcam);
        
        loadedWebcam = mDao.getWebcamById(webcamId1);
        assertEquals("Wrong id", webcamId1, loadedWebcam.getId());
        assertEquals("Wrong parentId", 0, loadedWebcam.getParentId());
        assertEquals("Wrong name", "Paris - Tour Eiffel", loadedWebcam.getName());
        assertEquals("Wrong url", "http://www.parislive.net/eiffelwebcam01.jpg", loadedWebcam.getImageUrl());
        assertEquals("Wrong interval", 5, loadedWebcam.getReloadInterval());
        
        //insert second webcam
        webcam = new ItemWebcam(0, 0, "Webcam 2", "http://amrc.ssec.wisc.edu/~amrc/webcam/b15k/20050216_02.jpg", 0);
        long webcamId2 = mDao.insertWebcam(webcam);
        
        loadedWebcam = mDao.getWebcamById(webcamId2);
        assertEquals("Wrong id", webcamId2, loadedWebcam.getId());
        assertEquals("Wrong parentId", 0, loadedWebcam.getParentId());
        assertEquals("Wrong name", "Webcam 2", loadedWebcam.getName());
        assertEquals("Wrong url", "http://amrc.ssec.wisc.edu/~amrc/webcam/b15k/20050216_02.jpg", loadedWebcam.getImageUrl());
        assertEquals("Wrong interval", 0, loadedWebcam.getReloadInterval());
        
        //delete webcams
        mDao.deleteWebcam(webcamId1);
        mDao.deleteWebcam(webcamId2);
    }
    
    public void testDeleteCategory() {
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
    
    
    public void testGetChildrenOfParentItem() {
    	
    }
}
