package com.logicaldoc.gui.common.client.data;

import com.logicaldoc.gui.common.client.Session;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;

/**
 * Datasource to retrieve all documents tags. It is based on Xml parsing.
 * 
 * @author Marco Meschieri - Logical Objects
 * @since 6.0
 */
public class TagsDS extends DataSource {

	public TagsDS(String firstLetter) {
		setTitleField("word");
		setRecordXPath("/list/tag");
		DataSourceTextField index = new DataSourceTextField("index");
		index.setPrimaryKey(true);
		DataSourceTextField word = new DataSourceTextField("word");
		DataSourceTextField count = new DataSourceTextField("count");
		setFields(index, word, count);
		setDataURL("data/tags.xml?sid=" + Session.get().getSid()
				+ (firstLetter != null ? "&firstLetter=" + firstLetter : ""));
		setClientOnly(true);
	}
}