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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageUrlProvider implements IImageUrlProvider
{
	//---------- Constructor
	public ImageUrlProvider() {
	}




	//---------- Private fields




	//---------- Public properties




	//---------- Public methods
	
	/*(non-Javadoc)
	 * @see it.rainbowbreeze.webcamholmes.data.IImageUrlProvider#loadBitmap(java.lang.String)
	 */
	public Bitmap loadBitmap(String imagePath)
		throws MalformedURLException, IOException
	{
		URL imageUrl = new URL(imagePath);
	    final URLConnection conn = imageUrl.openConnection();
	    conn.connect();
	    final BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
	    final Bitmap bitmap = BitmapFactory.decodeStream(bis);
	    bis.close();
	    return bitmap;
	}

	


	//---------- Private methods
}
