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

import java.io.IOException;
import java.net.MalformedURLException;

import android.graphics.Bitmap;

public interface IImageUrlProvider {

	//---------- Public methods
	/**
	 * Load an image from an URL and put it in a Bitmap
	 * 
	 * @param imagePath
	 * @return the bitmap with the image or null if some errors happened
	 * @throws MalformedURLException 
	 * @throws IOException 
	 */
	public abstract Bitmap loadBitmap(final String imagePath) throws MalformedURLException, IOException;

}