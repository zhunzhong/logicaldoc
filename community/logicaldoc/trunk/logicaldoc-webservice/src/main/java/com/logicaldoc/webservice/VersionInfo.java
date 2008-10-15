package com.logicaldoc.webservice;

/**
 * Version informations
 * 
 * @author Marco Meschieri
 * @version $Id:$
 * @since 3.0
 */
public class VersionInfo {
	private String version;

	private String date;

	private String comment;

	private String user;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}