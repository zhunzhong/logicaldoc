package com.logicaldoc.core.document.dao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.logicaldoc.core.AbstractCoreTestCase;
import com.logicaldoc.core.document.Document;
import com.logicaldoc.core.document.Version;
import com.logicaldoc.core.document.Version.VERSION_TYPE;
import com.logicaldoc.core.searchengine.store.Storer;
import com.logicaldoc.core.security.Menu;
import com.logicaldoc.core.security.User;
import com.logicaldoc.core.security.dao.MenuDAO;
import com.logicaldoc.util.CharsetDetector;
import com.logicaldoc.util.io.FileUtil;

/**
 * Test case for <code>HibernateDocumentDAO</code>
 * 
 * @author Marco Meschieri - Logical Objects
 * @since 3.0
 */
public class HibernateDocumentDAOTest extends AbstractCoreTestCase {
	
	protected static Log log = LogFactory.getLog(HibernateDocumentDAOTest.class);

	// Instance under test
	private DocumentDAO dao;

	private MenuDAO menuDao;

	private Storer storer;

	public HibernateDocumentDAOTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();

		// Retrieve the instance under test from spring context. Make sure that
		// it is an HibernateDocumentDAO
		dao = (DocumentDAO) context.getBean("DocumentDAO");
		menuDao = (MenuDAO) context.getBean("MenuDAO");
		storer = (Storer) context.getBean("Storer");
	}

	public void testDelete() {
		assertTrue(dao.delete(1));
		Document doc = dao.findById(1);
		assertNull(doc);
	}

	public void testFindAll() {
		Collection<Document> documents = dao.findAll();
		assertNotNull(documents);
		assertEquals(2, documents.size());
	}

	public void testFindById() {
		Document doc = dao.findById(1);
		assertNotNull(doc);
		dao.initialize(doc);
		assertEquals(1, doc.getId());
		assertEquals("testDocname", doc.getTitle());
		assertNotNull(doc.getFolder());
		assertEquals(103, doc.getFolder().getId());

		// Try with unexisting document
		doc = dao.findById(99);
		assertNull(doc);
	}

	public void testFindByCustomId() {
		Document doc = dao.findByCustomId("a");
		assertNotNull(doc);
		dao.initialize(doc);
		assertEquals(1, doc.getId());
		assertEquals("testDocname", doc.getTitle());
		assertNotNull(doc.getFolder());
		assertEquals(103, doc.getFolder().getId());

		// Try with unexisting document
		doc = dao.findByCustomId("xx");
		assertNull(doc);
	}

	public void testFindByUserId() {
		Collection<Long> ids = dao.findByUserId(3);
		assertNotNull(ids);
		assertEquals(2, ids.size());
		assertTrue(ids.contains(new Long(2)));

		// Try with a user without documents
		ids = dao.findByUserId(5);
		assertNotNull(ids);
		assertEquals(0, ids.size());
	}

	public void testFindDocIdByFolder() {
		Collection<Long> ids = dao.findDocIdByFolder(103);
		assertNotNull(ids);
		assertEquals(2, ids.size());
		assertTrue(ids.contains(new Long(2)));

		ids = dao.findDocIdByFolder(1111);
		assertNotNull(ids);
		assertEquals(0, ids.size());
	}

	public void testFindByFolder() {
		Collection<Document> docs = dao.findByFolder(103);
		assertNotNull(docs);
		assertEquals(2, docs.size());
		assertTrue(docs.contains(dao.findById(2)));

		docs = dao.findByFolder(1111);
		assertNotNull(docs);
		assertEquals(0, docs.size());
	}

	public void testFindIndexed() {
		List<Document> docs = dao.findByIndexed(1);
		assertNotNull(docs);
		assertEquals(1, docs.size());
		assertEquals(1, docs.get(0).getId());

		docs = dao.findByIndexed(0);
		assertNotNull(docs);
		assertEquals(1, docs.size());
		assertEquals(2, docs.get(0).getId());
	}

	public void testFindLastModifiedByUserId() {
		Collection<Document> coll = dao.findLastModifiedByUserId(1, 10);
		assertNotNull(coll);
		assertEquals(2, coll.size());

		coll = dao.findLastModifiedByUserId(3, 10);
		assertNotNull(coll);
		assertEquals(1, coll.size());
	}

	public void testFindDocIdByTag() {
		Collection<Long> ids = dao.findDocIdByTag("abc");
		assertNotNull(ids);
		assertEquals(1, ids.size());
		assertEquals(new Long(1), ids.iterator().next());

		ids = dao.findDocIdByTag("xxx");
		assertNotNull(ids);
		assertEquals(0, ids.size());
	}

	public void testStore() throws IOException {
		Document doc = new Document();
		Menu menu = menuDao.findById(Menu.MENUID_HOME);
		doc.setFolder(menu);
		doc.setPublisher("admin");
		doc.setPublisherId(1);
		doc.setTitle("test");
		doc.addTag("pippo");
		doc.addTag("pluto");
		doc.setValue("att_1", "val 1");
		doc.setFileName("test.txt");
		doc.setFileVersion("1.0");

		// Prepare the document file for digest computation
		File docFile = storer.getFile(5L, doc.getFileVersion());
		FileUtils.forceMkdir(docFile.getParentFile());
		Writer out = new FileWriter(docFile);
		out.write("Questo file serve per fare il test del digest su un documento");
		out.flush();
		out.close();
		assertTrue(docFile.exists());
		String digest = FileUtil.computeDigest(docFile);
		System.out.println("Saved file " + docFile.getPath());

		// Try a long tag
		doc.addTag("123456789123456789123456789");
		User user = new User();
		user.setId(1);
		Version version = Version.create(doc, user, "comment", Version.EVENT_CHECKIN, VERSION_TYPE.OLD_VERSION);

		assertTrue(docFile.exists());
		assertTrue(dao.store(doc));

		assertTrue(docFile.exists());
		assertEquals(5, doc.getId());
		doc = dao.findById(5);
		assertNotNull(doc);
		dao.initialize(doc);

		dao.store(doc);

		docFile = storer.getFile(doc, doc.getFileVersion(), null);
		FileUtils.forceMkdir(docFile.getParentFile());
		out = new FileWriter(docFile);
		out.write("Questo file serve per fare il test del digest su un documento");
		out.flush();
		out.close();
		assertTrue(docFile.exists());

		assertEquals(5, doc.getId());
		assertEquals(3, doc.getTags().size());
		assertTrue(doc.getTags().contains("pluto"));
		assertTrue(doc.getTags().contains("123456789123456789123456789"));
		assertEquals("val 1", doc.getValue("att_1"));
		assertTrue(docFile.exists());
		assertNotNull(doc.getDigest());
		assertEquals(doc.getDigest(), digest);

		// Try to change the version comment
		doc = dao.findById(5);
		dao.initialize(doc);
		version.setComment("xxxx");
		version.setVersion("1.0");
		version.setUserId(1);
		dao.store(doc);
		doc = dao.findById(5);
		dao.initialize(doc);

		// Load an existing document and modify it
		doc = dao.findById(1);
		assertNotNull(doc);
		dao.initialize(doc);
		assertEquals("testDocname", doc.getTitle());
		assertEquals(3, doc.getTags().size());
		doc.setTitle("xxxx");
		assertTrue(dao.store(doc));
		doc = dao.findById(1);
		assertNotNull(doc);
		dao.initialize(doc);
		assertEquals(1, doc.getId());
		assertEquals("xxxx", doc.getTitle());
		assertEquals(3, doc.getTags().size());
	}
	
	
	public void testStoreGreekDocument() throws IOException {
		
		Document doc = new Document();
		Menu menu = menuDao.findById(Menu.MENUID_HOME);
		doc.setFolder(menu);
		doc.setPublisher("admin");
		doc.setPublisherId(1);
		
		 File file = new File("C:/Users/alle/Desktop/LogicalDoc45RC3_Greek_problem/\u03B7 \u03B1\u03BD\u03B1\u03B6\u03AE\u03C4\u03B7\u03C3\u03B7 \u03B5\u03C1\u03B3\u03B1\u03C3\u03AF\u03B1\u03C2.pdf");
		
		 File dir = new File("C:/Users/alle/Desktop/LogicalDoc45RC3_Greek_problem/ccc");
		 String[] files = dir.list( new SuffixFileFilter(".pdf") );
		 for (int i = 0; i < files.length; i++) {
		     System.out.println(files[i]);
		 }
		 if (files.length > 0)
			 file = new File(files[0]);
		
		String fileName = file.getName();
		
		System.out.println("fileName : " + fileName);
		log.fatal("fileName : " + fileName);
		String baseName = FilenameUtils.getBaseName(fileName);
		System.out.println("baseName : " + baseName);
		log.fatal("baseName : " + baseName);
		doc.setTitle(baseName);
		
		String[] encodings = CharsetDetector.detectEncodings(fileName);
		if (encodings != null && encodings.length > 0) {
			for (int i = 0; i < encodings.length; i++) {
				System.out.println(encodings[i]);
			}
		}
		
		
		String conv = new String(fileName.getBytes(), "UTF-8");
		System.out.println("conv: " + conv);
		log.fatal("conv: " + conv);
		
		String xxx = URLEncoder.encode(fileName, "UTF-8");
		System.out.println("xxx: " + xxx);
		log.fatal("xxx: " + xxx);
		
		String htmlEsc = StringEscapeUtils.escapeHtml(fileName);
		System.out.println("htmlEsc: " + htmlEsc);
		log.fatal("htmlEsc: " + htmlEsc);
		
		doc.setFileName(fileName);
		doc.setFileVersion("1.0");

		// Create a new version for the document
		User user = new User();
		user.setId(1);
		Version.create(doc, user, "comment", Version.EVENT_CHECKIN, VERSION_TYPE.OLD_VERSION);

		assertTrue(dao.store(doc));
		dao.initialize(doc);
		long dID = doc.getId();

		// Load the document and verify it
		doc = null;
		doc = dao.findById(dID);
		assertNotNull(doc);
		dao.initialize(doc);
		assertEquals(dID, doc.getId());
		assertEquals(baseName, doc.getTitle());
	}
	

	public void testFindTagss() {
		Collection<String> tags = dao.findTags("a", 1);
		assertNotNull(tags);
		assertEquals(2, tags.size());
		assertTrue(tags.contains("abc"));
	}

	public void testFindDocIdByUserIdAndTag() {
		Collection<Long> ids = dao.findDocIdByUserIdAndTag(1, "abc");
		assertNotNull(ids);
		assertEquals(1, ids.size());
		assertEquals(new Long(1), ids.iterator().next());

		ids = dao.findDocIdByUserIdAndTag(1, "xxx");
		assertNotNull(ids);
		assertEquals(0, ids.size());

		ids = dao.findDocIdByUserIdAndTag(99, "abc");
		assertNotNull(ids);
		assertEquals(0, ids.size());
	}

	public void testFindLastDownloadsByUserId() {
		Collection<Document> documents = dao.findLastDownloadsByUserId(1, 10);
		assertNotNull(documents);
		assertEquals(2, documents.size());
	}

	public void testFindByTitleAndParentFolderId() {
		Collection<Document> documents = dao.findByTitleAndParentFolderId(103, "testDocname", null);
		assertNotNull(documents);
		assertEquals(1, documents.size());
	}

	public void testFindLinkedDocuments() {
		Collection<Document> docs = dao.findLinkedDocuments(1, null, null);
		assertNotNull(docs);
		assertEquals(1, docs.size());
		assertEquals(1, docs.iterator().next().getId());

		docs = dao.findLinkedDocuments(2, "xyz", null);
		assertNotNull(docs);
		assertEquals(1, docs.size());
		assertEquals(2, docs.iterator().next().getId());

		docs = dao.findLinkedDocuments(2, "xyz", 1);
		assertNotNull(docs);
		assertEquals(1, docs.size());
		assertEquals(2, docs.iterator().next().getId());

		docs = dao.findLinkedDocuments(2, "xyz", 2);
		assertNotNull(docs);
		assertEquals(0, docs.size());
	}

	public void testFindDeletedDocIds() {
		List<Long> coll = dao.findDeletedDocIds();
		assertNotNull(coll);
		assertEquals(2, coll.size());
		assertTrue(coll.contains(new Long(3)));
		assertTrue(coll.contains(new Long(4)));
	}

	public void testFindDeletedDocs() {
		List<Document> coll = dao.findDeletedDocs();
		assertNotNull(coll);
		assertEquals(2, coll.size());
	}

	public void testGetTotalSize() {
		assertEquals(368391L, dao.getTotalSize(true));
		assertEquals(123701L, dao.getTotalSize(false));
	}

	public void testGetDocumentCount() {
		assertEquals(4L, dao.getDocumentCount(true));
		assertEquals(2L, dao.getDocumentCount(false));
	}

	public void testRestore() {
		assertNull(dao.findById(4));
		dao.restore(4);
		assertNotNull(dao.findById(4));
		assertNotNull(menuDao.findById(1100));
		assertNotNull(menuDao.findById(1000));
	}

	public void testMakeImmutable() {
		dao.makeImmutable(2);
		assertEquals(1, dao.findById(2).getImmutable());
	}

	public void testFindLockedByUserId() {
		assertEquals(2, dao.findLockedByUserId(3).size());
		assertEquals(0, dao.findLockedByUserId(1).size());
		assertEquals(0, dao.findLockedByUserId(987541).size());
	}

	public void testFindByLockUserAndStatus() {
		assertEquals(2, dao.findByLockUserAndStatus(3L, null).size());
		assertEquals(2, dao.findByLockUserAndStatus(3L, Document.DOC_CHECKED_OUT).size());
		assertEquals(2, dao.findByLockUserAndStatus(null, Document.DOC_CHECKED_OUT).size());
		assertEquals(0, dao.findByLockUserAndStatus(1L, null).size());
		assertEquals(0, dao.findByLockUserAndStatus(1L, Document.DOC_CHECKED_OUT).size());
		assertEquals(0, dao.findByLockUserAndStatus(987541L, null).size());
	}
}