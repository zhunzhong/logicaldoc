package com.logicaldoc.core;

import java.io.File;
import java.io.InputStream;

import com.logicaldoc.core.document.Document;
import com.logicaldoc.core.searchengine.store.Storer;

public class DummyStorer implements Storer {

	@Override
	public void clean(long docId) {

	}

	@Override
	public void delete(long docId) {

	}

	@Override
	public File getDirectory(long docId) {
		return null;
	}

	@Override
	public File getFile(long docId, String filename) {
		return new File("pom.xml");
	}

	@Override
	public File getFile(Document doc, String fileVersion, String suffix) {
		return new File("pom.xml");
	}

	@Override
	public boolean store(InputStream stream, long docId, String filename) {
		return true;
	}

}
