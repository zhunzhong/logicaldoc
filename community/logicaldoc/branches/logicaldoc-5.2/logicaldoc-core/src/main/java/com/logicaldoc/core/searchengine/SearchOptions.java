package com.logicaldoc.core.searchengine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Search options
 * 
 * @author Michael Scholz
 */
public class SearchOptions implements Serializable, Comparable<SearchOptions> {

	private static final long serialVersionUID = 1L;

	public static final int TYPE_FULLTEXT = 0;

	public static final int TYPE_TAG = 1;

	protected int maxHits = 40;

	private int type = TYPE_FULLTEXT;

	protected String expression = "";

	protected String name = "";

	protected String description = "";

	private Object[] parameters = null;

	private long userId = -1;

	public Object[] getParameters() {
		return parameters;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

	/** Creates a new instance of SearchOptions */
	public SearchOptions(int type) {
		this.type = type;
	}

	/** Necessary constructor for the Search Web Service */
	protected SearchOptions() {
	}

	public void setExpression(String expr) {
		this.expression = expr;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public static SearchOptions read(File file) throws FileNotFoundException, IOException, ClassNotFoundException {
		SearchOptions searchOptions = null;
		// Deserialize from a file
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
		try {
			// Deserialize the object
			searchOptions = (SearchOptions) in.readObject();
		} finally {
			in.close();
		}
		return searchOptions;
	}

	public void write(File file) throws FileNotFoundException, IOException {
		// Serialize to a file
		ObjectOutput out = new ObjectOutputStream(new FileOutputStream(file));
		try {
			out.writeObject(this);
		} finally {
			out.flush();
			out.close();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int compareTo(SearchOptions o) {
		return this.getName().compareTo(o.getName());
	}

	public int getMaxHits() {
		return maxHits;
	}

	public void setMaxHits(int maxHits) {
		this.maxHits = maxHits;
	}

	public boolean isFulltext() {
		return getType() == TYPE_FULLTEXT;
	}

	public String getExpression() {
		return expression;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}