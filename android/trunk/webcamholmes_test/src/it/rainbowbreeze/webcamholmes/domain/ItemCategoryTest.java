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

/**
 * 
 */

package it.rainbowbreeze.webcamholmes.domain;

import junit.framework.TestCase;

/**
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public class ItemCategoryTest extends TestCase {
	//---------- Constructor

	//---------- Private fields

	//---------- Public properties

	//---------- Test methods
	public void testFactoryMethods() {
		ItemCategory category;
		
		category = ItemCategory.Factory.getSystemCategory(100, "Cat di prova");
		assertEquals("Wrong id", 0, category.getId());
		assertEquals("Wrong alias id", 0, category.getAliasId());
		assertEquals("Wrong parent id", 100, category.getParentId());
		assertEquals("Wrong name", "Cat di prova", category.getName());
		assertFalse("Wrong user created", category.isUserCreated());

		category = ItemCategory.Factory.getSystemCategory(123, 140, "Cat di prova 1");
		assertEquals("Wrong id", 0, category.getId());
		assertEquals("Wrong alias id", 123, category.getAliasId());
		assertEquals("Wrong parent id", 140, category.getParentId());
		assertEquals("Wrong name", "Cat di prova 1", category.getName());
		assertFalse("Wrong user created", category.isUserCreated());

		category = ItemCategory.Factory.getUserCategory(93, "Cat di prova 3");
		assertEquals("Wrong id", 0, category.getId());
		assertEquals("Wrong alias id", 0, category.getAliasId());
		assertEquals("Wrong parent id", 93, category.getParentId());
		assertEquals("Wrong name", "Cat di prova 3", category.getName());
		assertTrue("Wrong user created", category.isUserCreated());

		category = ItemCategory.Factory.getUserCategory(452, 420, "Cat di prova 4");
		assertEquals("Wrong id", 0, category.getId());
		assertEquals("Wrong alias id", 452, category.getAliasId());
		assertEquals("Wrong parent id", 420, category.getParentId());
		assertEquals("Wrong name", "Cat di prova 4", category.getName());
		assertTrue("Wrong user created", category.isUserCreated());
	}
	

	//---------- Private methods

}
