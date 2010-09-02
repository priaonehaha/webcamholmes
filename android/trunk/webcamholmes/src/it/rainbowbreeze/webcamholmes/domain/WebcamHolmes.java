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
package it.rainbowbreeze.webcamholmes.domain;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Convenience definitions for WebcamHolmesProvider
 * Still not a true provider, but useful for
 * webcam managing
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 */
public class WebcamHolmes {
    public static final String AUTHORITY = "it.rainbowbreeze.provider.WebcamHolmes";
	
    // This class cannot be instantiated
    private WebcamHolmes() {}


    /**
     * Webcam table
     */
    public static final class Webcam implements BaseColumns {
        // This class cannot be instantiated
        private Webcam() {}

        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/webcams");

//        /**
//         * The MIME type of {@link #CONTENT_URI} providing a directory of webcams.
//         */
//        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.note";
//
//        /**
//         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single note.
//         */
//        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.note";

        /**
         * The table name
         */
        public static final String TABLE_NAME = "Webcam";

        /**
         * The id of the category at which the webcam belongs
         * <P>Type: INTEGER (long)</P>
         */
        public static final String PARENT_CATEGORY_ID = "parentCategoryId";

        /**
         * The name of the webcam
         * <P>Type: TEXT</P>
         */
        public static final String NAME = "name";

        /**
         * The url of the webcam
         * <P>Type: TEXT</P>
         */
        public static final String IMAGEURL = "imageUrl";

        /**
         * The reload interval
         * <P>Type: INT</P>
         */
        public static final String RELOAD_INTERVAL = "reloadInterval";

        /**
         * If the webcam is one of the preferred
         * <P>Type: BOOLEAN</P>
         */
        public static final String PREFERRED = "preferred";
    
        /**
         * Type of webcam. Manage how to load images
         * <P>Type: INT</P>
         */
        public static final String TYPE = "webcamTypes";

        /**
         * If the webcam was created by user or by the system
         * <P>Type: BOOLEAN</P>
         */
        public static final String CREATED_BY_USER = "userCreated";

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = NAME;
    }



    /**
     * Category table
     */
    public static final class Category implements BaseColumns {
        // This class cannot be instantiated
        private Category() {}

        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/category");

//        /**
//         * The MIME type of {@link #CONTENT_URI} providing a directory of webcams.
//         */
//        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.note";
//
//        /**
//         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single note.
//         */
//        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.note";

        /**
         * The table name
         */
        public static final String TABLE_NAME = "Category";

        /**
         * The id of the category at which the category belongs
         * <P>Type: INT</P>
         */
        public static final String PARENT_CATEGORY_ID = "parentCategoryId";

        /**
         * The name of the category
         * <P>Type: TEXT</P>
         */
        public static final String NAME = "name";

        /**
         * If the category was created by user or by the system
         * <P>Type: BOOLEAN</P>
         */
        public static final String CREATED_BY_USER = "userCreated";

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = NAME;

    }
}