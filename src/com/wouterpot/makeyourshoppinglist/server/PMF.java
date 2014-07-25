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
import javax.jdo.Transaction;

public class PMF<T> {

	private static PersistenceManager pmInstance = null;

	private PMF() {
		throw new UnsupportedOperationException();
	}

	public static void open() {
		pmInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional").getPersistenceManager();
	}
	
	public static void begin() {
		pmInstance.currentTransaction().begin();
	}

	public static <T> T makePersistent(T obj) {
		return pmInstance.makePersistent(obj);
	}
	
	public static <T> T getObjectById(Class<T> classType, Object id) {
		return pmInstance.getObjectById(classType, id);
	}

	public static void commit() {
		pmInstance.currentTransaction().commit();
	}

	public static <T> List<T> retrieveAll(Class<T> classType) {
		Query newQuery = pmInstance.newQuery(classType);
		List<T> objects = (List<T>) newQuery.execute();
		pmInstance.retrieveAll(objects);
		return objects;
	}

	public static <T> void retrieve(T obj) {
		pmInstance.retrieve(obj);
	}

	public static <T> T getObjectById(Class<T> classType, String id) {
		return pmInstance.getObjectById(classType, id);
	}

	public static void close() {
		if (pmInstance != null) {
			pmInstance.flush();
			pmInstance.close();
			pmInstance = null;
		}
	}

	public static <T> T detachCopy(T obj) {
		return pmInstance.detachCopy(obj);		
	}
	
	public static <T> T[] detachCopyAll(T... objs) {
		return pmInstance.detachCopyAll(objs);		
	}

	public static void rollback() {
		Transaction currentTransaction = pmInstance.currentTransaction();
		if (currentTransaction.isActive())
			currentTransaction.rollback();
	}
}