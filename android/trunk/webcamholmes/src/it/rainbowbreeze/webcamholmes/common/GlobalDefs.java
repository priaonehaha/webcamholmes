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

package it.rainbowbreeze.webcamholmes.common;

import it.rainbowbreeze.libs.common.BaseGlobalDefs;

/**
 * Global constrains for the Application
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public interface GlobalDefs extends BaseGlobalDefs {
	/** keys for application preferences */
	public final static String APP_PREFERENCES_KEY = "WebcamHolmesPrefs";

	/** Application version, displayed to the user */
	public final static String APP_VERSION = "0.1b";

	/** Application version, for internal use */
	public final static String APP_VERSION_DESCRIPTION = "00.01.00b";

	/** address where send log */
	public final static String EMAIL_FOR_LOG = "devel@rainbowbreeze.it";

	public final static String LOG_TAG = "WebcamHolmes";
}
