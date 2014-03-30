/*******************************************************************************
 *
 * My todo list everywhere
 *
 * Copyright (C) 2011 Davy Leggieri
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * Tou can try the demo at http://todo-list-everywhere.appspot.com
 *
 ******************************************************************************/



package com.wouterpot.makeyourshoppinglist.server;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class PMF {
	
    private static PersistenceManager pmInstance = null;
	private static boolean testing;

    private PMF() {
        throw new UnsupportedOperationException();
    }

    public static void open() {
    	if (pmInstance == null && !testing) {
    		pmInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional").getPersistenceManager();
    		pmInstance.currentTransaction().begin();
    	}
    }
    
    public static void test(String t) {
    	
    }

    // TODO: resolve this nicely with mocking
    public static void setTesting(boolean testing) {
		PMF.testing = testing;
    }
    
    public static <T> T makePersistent(T obj) {
    	return testing ? obj : pmInstance.makePersistent(obj);
    }
    
    public static void close() {
    	if (!testing) {
	    	pmInstance.currentTransaction().commit();
	    	pmInstance.close();
	    	pmInstance = null;
    	}
    }

	public static <T> List<T> retrieveAll(Class<T> classType) {
		Query newQuery = pmInstance.newQuery(classType);
		List<T> shoppingLists = (List<T>)newQuery.execute();
		pmInstance.retrieveAll(shoppingLists);
		return shoppingLists;
	}

	public static <T> void retrieve(T obj) {
		pmInstance.retrieve(obj);
	}

	public static <T> T getObjectById(Class<T> classType, String id) {
		return pmInstance.getObjectById(classType, id);
	}
}