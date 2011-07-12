package com.logicaldoc.gui.frontend.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.logicaldoc.gui.common.client.beans.GUITag;

public interface TagServiceAsync {

	void delete(String sid, String tag, AsyncCallback<Void> callback);

	void getTagCloud(AsyncCallback<GUITag[]> callback);

	void rename(String sid, String tag, String newTag, AsyncCallback<Void> callback);

	void addTag(String sid, String tag, AsyncCallback<Void> callback);

	void removeTag(String sid, String tag, AsyncCallback<Void> callback);

	void getMode(String sid, AsyncCallback<String> callback);

	void setMode(String sid, String mode, AsyncCallback<Void> callback);
}