package com.logicaldoc.core.document.dao;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.logicaldoc.core.AbstractCoreTCase;
import com.logicaldoc.core.document.History;
import com.logicaldoc.core.i18n.DateBean;

/**
 * Test case for <code>HibernateHistoryDAO</code>
 * 
 * @author Alessandro Gasparini - Logical Objects
 * @since 3.0
 */
public class HibernateHistoryDAOTest extends AbstractCoreTCase {

	// Instance under test
	private HistoryDAO dao;

	@Before
	public void setUp() throws Exception {
		super.setUp();

		// Retrieve the instance under test from spring context. Make sure that
		// it is an HibernateHistoryDAO
		dao = (HistoryDAO) context.getBean("HistoryDAO");
	}

	@Test
	public void testDelete() {
		Collection<History> histories = (Collection<History>) dao.findByUserId(1);
		Assert.assertNotNull(histories);
		Assert.assertEquals(2, histories.size());

		for (History history : histories) {
			boolean result = dao.delete(history.getId());
			Assert.assertTrue(result);
		}

		histories = (Collection<History>) dao.findByUserId(4);
		Assert.assertNotNull(histories);
		Assert.assertEquals(0, histories.size());
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testFindByDocId() {
		Collection histories = dao.findByDocId(1);
		Assert.assertNotNull(histories);
		Assert.assertEquals(1, histories.size());

		// Try with unexisting docId
		histories = dao.findByDocId(99);
		Assert.assertNotNull(histories);
		Assert.assertEquals(0, histories.size());
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testFindByUserId() {
		Collection histories = dao.findByUserId(1);
		Assert.assertNotNull(histories);
		Assert.assertEquals(2, histories.size());

		// Try with unexisting user
		histories = dao.findByUserId(99);
		Assert.assertNotNull(histories);
		Assert.assertEquals(0, histories.size());
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testFindByFolderId() {
		Collection histories = dao.findByFolderId(5);
		Assert.assertNotNull(histories);
		Assert.assertEquals(3, histories.size());

		// Try with unexisting folderId
		histories = dao.findByFolderId(99);
		Assert.assertNotNull(histories);
		Assert.assertEquals(0, histories.size());
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testCreateDocumentHistory() {
		History history = new History();
		history.setDocId(1L);
		history.setFolderId(5);
		history.setTitle("pippo");
		history.setVersion("2.0");

		history.setPath("/" + "paperino");

		history.setDate(new Date());
		history.setUserId(1);
		history.setUserName("mario");
		history.setEvent(History.EVENT_STORED);
		history.setComment("The document has been created.");

		dao.store(history);

		Collection histories = dao.findByDocId(1);
		Assert.assertNotNull(histories);
		Assert.assertEquals(2, histories.size());
	}

	@Test
	public void testStore() {
		History history = new History();
		history.setDocId(1L);
		history.setFolderId(5);
		history.setDate(DateBean.dateFromCompactString("20061220"));
		history.setUserName("sebastian");
		history.setUserId(3);
		history.setEvent("test History store");

		Assert.assertTrue(dao.store(history));

		History folderHistory = new History();
		folderHistory.setFolderId(5);
		folderHistory.setDate(DateBean.dateFromCompactString("20061220"));
		folderHistory.setUserName("sebastian");
		folderHistory.setUserId(3);
		folderHistory.setEvent("test History store");

		Assert.assertTrue(dao.store(folderHistory));

		// Test the stored history
		Collection<History> histories = (Collection<History>) dao.findByUserId(3);
		Assert.assertNotNull(histories);
		Assert.assertFalse(histories.isEmpty());

		Iterator<History> itHist = histories.iterator();
		History hStored = itHist.next();
		Assert.assertTrue(hStored.equals(folderHistory));
		Assert.assertEquals(hStored.getFolderId(), 5);
		Assert.assertEquals(hStored.getDate().getTime(), DateBean.dateFromCompactString("20061220").getTime());
		Assert.assertEquals(hStored.getUserName(), "sebastian");
		Assert.assertEquals(hStored.getEvent(), "test History store");
	}


	@SuppressWarnings("rawtypes")
	@Test
	public void testFindNotNotified() {
		Collection histories = dao.findNotNotified();
		Assert.assertNotNull(histories);
		Assert.assertEquals(2, histories.size());

		History history = dao.findById(1);
		dao.initialize(history);
		history.setNotified(1);
		dao.store(history);

		histories = dao.findNotNotified();
		Assert.assertNotNull(histories);
		Assert.assertEquals(1, histories.size());
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testCleanOldFolderHistories() {
		History history = dao.findById(3);
		Assert.assertNotNull(history);

		Collection histories = dao.findAll();
		Assert.assertNotNull(histories);
		Assert.assertEquals(3, histories.size());

		dao.cleanOldFolderHistories(5);

		history = dao.findById(3);
		Assert.assertNull(history);
		histories = dao.findAll();
		Assert.assertEquals(2, histories.size());
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testCleanOldDocumentHistories() {
		History history = dao.findById(1);
		Assert.assertNotNull(history);

		Collection histories = dao.findAll();
		Assert.assertNotNull(histories);
		Assert.assertEquals(3, histories.size());

		dao.cleanOldDocumentHistories(5);

		history = dao.findById(1);
		Assert.assertNull(history);
		histories = dao.findAll();
		Assert.assertEquals(1, histories.size());
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testFindByUserIdAndEvent() {
		Collection histories = dao.findByUserIdAndEvent(1, "data test 01");
		Assert.assertNotNull(histories);
		Assert.assertEquals(1, histories.size());

		histories = dao.findByUserIdAndEvent(1, "data test 02");
		Assert.assertNotNull(histories);
		Assert.assertEquals(1, histories.size());

		histories = dao.findByUserIdAndEvent(2, "data test 02");
		Assert.assertNotNull(histories);
		Assert.assertEquals(0, histories.size());

		// Try with unexisting user
		histories = dao.findByUserIdAndEvent(99, "data test 02");
		Assert.assertNotNull(histories);
		Assert.assertEquals(0, histories.size());
	}
}