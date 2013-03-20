package com.logicaldoc.gui.frontend.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.logicaldoc.gui.common.client.beans.GUIDocument;

public interface GDocsServiceAsync {

	void checkin(String sid, long docId, String comment, boolean major, AsyncCallback<Void> callback);

	void upload(String sid, long docId, AsyncCallback<String> callback);

	void delete(String sid, String resourceId, AsyncCallback<Void> callback);

	void saveSettings(String sid, String username, String password, AsyncCallback<Void> callback);

	void loadSettings(String sid, AsyncCallback<String[]> callback);

	void create(String sid, String title, String type, AsyncCallback<String> callback);

	void importDocuments(String sid, String[] resourceIds, long targetFolderId, String type,
			AsyncCallback<Void> callback);

	void exportDocuments(String sid, long[] ids, AsyncCallback<String[]> callback);

	void search(String sid, String expression, AsyncCallback<GUIDocument[]> callback);

}
