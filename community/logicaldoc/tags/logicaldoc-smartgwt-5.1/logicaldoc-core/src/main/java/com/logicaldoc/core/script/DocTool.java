package com.logicaldoc.core.script;

import com.logicaldoc.core.document.Document;
import com.logicaldoc.core.document.History;
import com.logicaldoc.util.Context;
import com.logicaldoc.util.config.ContextProperties;

/**
 * Utility methods to handle documents from within Velocity
 * 
 * @author Marco Meschieri - LogicalDOC
 * @since 7.3
 */
public class DocTool {

	public String download(long docId) {
		ContextProperties config = Context.get().getProperties();
		String url = config.getProperty("server.url");
		if (!url.endsWith("/"))
			url += "/";
		url += "download?docId=" + docId;
		return url;
	}

	public String display(long tenantId, long docId) {
		ContextProperties config = Context.get().getProperties();
		String url = config.getProperty("server.url");
		if (!url.endsWith("/"))
			url += "/";
		url += "?tenantId=" + tenantId + "&docId=" + docId;
		return url;
	}

	public String download(Document doc) {
		return download(doc.getId());
	}

	public String download(History history) {
		return download(history.getDocId());
	}

	public String display(Document doc) {
		return display(doc.getTenantId(), doc.getId());
	}

	public String display(History history) {
		return display(history.getTenantId(), history.getDocId());
	}
}
