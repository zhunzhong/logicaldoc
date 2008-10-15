package com.logicaldoc.web.document;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.icesoft.faces.context.effects.JavascriptContext;
import com.logicaldoc.core.CryptBean;
import com.logicaldoc.core.document.Document;
import com.logicaldoc.core.document.DownloadTicket;
import com.logicaldoc.core.document.History;
import com.logicaldoc.core.document.dao.DocumentDAO;
import com.logicaldoc.core.document.dao.DownloadTicketDAO;
import com.logicaldoc.core.document.dao.HistoryDAO;
import com.logicaldoc.core.security.Menu;
import com.logicaldoc.core.security.dao.MenuDAO;
import com.logicaldoc.util.Context;
import com.logicaldoc.web.SessionManagement;
import com.logicaldoc.web.StyleBean;
import com.logicaldoc.web.i18n.Messages;
import com.logicaldoc.web.navigation.MenuBarBean;
import com.logicaldoc.web.navigation.MenuItem;
import com.logicaldoc.web.navigation.PageContentBean;
import com.logicaldoc.web.upload.InputFileBean;
import com.logicaldoc.web.util.Constants;
import com.logicaldoc.web.util.FacesUtil;

/**
 * The <code>DocumentRecord</code> class contains the base information for an
 * entry in a data table. This class is meant to represent a model and should
 * only contain base document data <p/>
 * 
 * @author Marco Meschieri
 * @version $Id: DocumentRecord.java,v 1.17 2006/08/29 16:33:46 marco Exp $
 * @since 3.0
 */
public class DocumentRecord extends MenuBarBean {

	protected static Log log = LogFactory.getLog(DocumentRecord.class);

	public static final String GROUP_INDENT_STYLE_CLASS = "groupRowIndentStyle";

	public static final String GROUP_ROW_STYLE_CLASS = "groupRowStyle";

	public static final String CHILD_INDENT_STYLE_CLASS = "childRowIndentStyle";

	public static final String CHILD_ROW_STYLE_CLASS = "childRowStyle";

	// toggle for expand contract
	public static final String CONTRACT_IMAGE = "contract.png";

	public static final String EXPAND_IMAGE = "expand.png";

	public static final String SPACER_IMAGE = "spacer.gif";

	// style for column that holds expand/contract image toggle, in the sales
	// record row.
	private String indentStyleClass = CHILD_INDENT_STYLE_CLASS;

	// style for all other columns in the sales record row.
	private String rowStyleClass = CHILD_ROW_STYLE_CLASS;

	// Images used to represent expand/contract, spacer by default
	private String expandImage = SPACER_IMAGE; // arrow points right

	private String contractImage = SPACER_IMAGE; // arrow point down

	// image which will be drawn to screen
	private String expandContractImage = SPACER_IMAGE;

	// callback to list which contains all data in the dataTable. This callback
	// is needed so that a node can be set in the expanded state at construction
	// time.
	private List<DocumentRecord> parentDocumentsList;

	// indicates if node is in expanded state.
	private boolean expanded = false;

	// indicates if node is selected
	private boolean selected = false;

	protected Document document;

	private String documentPath;

	private long docId;

	/**
	 * <p>
	 * Creates a new <code>DocumentRecord</code>.
	 * </p>
	 * <p/>
	 * <p>
	 * The created DocumentRecord has no image states defined.
	 * </p>
	 * 
	 * @param menuId
	 * @param parentDocumentsList
	 * @param indentStyleClass
	 * @param rowStyleClass
	 */
	public DocumentRecord(long docId, List<DocumentRecord> parentDocumentsList, String indentStyleClass,
			String rowStyleClass) {
		this.docId = docId;
		this.parentDocumentsList = parentDocumentsList;
		if (indentStyleClass != null)
			this.indentStyleClass = indentStyleClass;
		if (rowStyleClass != null)
			this.rowStyleClass = rowStyleClass;
	}

	public DocumentRecord() {
		super();
	}

	public Document getDocument() {
		if (document == null)
			loadDocument();
		return document;
	}

	public boolean isDocumentFound() {
		return document != null;
	}

	public long getSize() {
		return getDocument().getFileSize() / 1024;
	}

	public long getDocId() {
		return docId;
	}

	/**
	 * Gets the description of the record
	 * 
	 * @return description of the record
	 */
	public String getDescription() {
		return getDocument().getTitle();
	}

	public String getDisplayDescription() {
		return StringUtils.abbreviate(getDocument().getTitle(), 68);
	}

	public String getDisplayFilename() {
		return StringUtils.abbreviate(getDocument().getFileName(), 68);
	}

	/**
	 * Gets the style class name used to define the first column of a document
	 * record row. This first column is where a expand/contract image is placed.
	 * 
	 * @return indent style class as defined in css file
	 */
	public String getIndentStyleClass() {
		return indentStyleClass;
	}

	/**
	 * Gets the style class name used to define all other columns in the
	 * document record row, except the first column.
	 * 
	 * @return style class as defined in css file
	 */
	public String getRowStyleClass() {
		return rowStyleClass;
	}

	/**
	 * Gets the image which will represent either the expanded or contracted
	 * state of the <code>DocumentRecord</code>.
	 * 
	 * @return name of image to draw
	 */
	public String getExpandContractImage() {
		return expandContractImage;
	}

	public boolean isSelected() {
		return selected;
	}

	public boolean isCheckedOut() {
		return getDocument().getStatus() == Document.DOC_CHECKED_OUT;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getIcon() {
		return getDocument().getIcon();
	}

	public Date getSourceDate() {
		return getDocument().getSourceDate();
	}

	@Override
	public boolean equals(Object arg0) {
		if ((arg0 == null) || (document == null)) {
			return false;
		} else {
			return document.equals(((DocumentRecord) arg0).getDocument());
		}
	}

	public String getDocumentPath() {
		if (documentPath == null) {
			try {
				Menu folder = getDocument().getFolder();
				Collection<Menu> parentColl = folder.getParents();
				ArrayList<Menu> parentList = new ArrayList<Menu>(parentColl);

				StringBuilder sb = new StringBuilder();
				boolean first = true;
				for (Menu menu : parentList) {
					if (!first)
						sb.append(" / ");

					if (menu.getMenuId() == Menu.MENUID_HOME)
						continue;
					if (menu.getMenuId() == Menu.MENUID_DOCUMENTS) {
						// Decoding the root of documents using the resource
						// boundle
						String menuText = Messages.getMessage(menu.getMenuText());
						sb.append(menuText);
					} else {
						sb.append(menu.getMenuText());
					}
					first = false;
				}
				documentPath = sb.toString();

			} catch (Throwable th) {
				logger.warn("Exception getDocumentPath() " + th.getMessage(), th);
			}
		}
		return documentPath;
	}

	/**
	 * Creates the context menu associated with this record
	 * 
	 * @see com.logicaldoc.web.navigation.MenuBarBean#createMenuItems()
	 */
	@SuppressWarnings("unchecked")
	protected void createMenuItems() {
		model.clear();

		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map session = facesContext.getExternalContext().getSessionMap();
		String username = (String) session.get(Constants.AUTH_USERNAME);

		Menu folder = getDocument().getFolder();
		Document document = getDocument();

		if (folder.isWriteable()) {
			if ((document.getStatus() == Document.DOC_CHECKED_OUT) && username.equals(document.getCheckoutUser())) {
				model.add(createMenuItem(" " + Messages.getMessage("msg.jsp.checkin"), "checkin-" + folder.getMenuId(),
						null, "#{documentRecord.checkin}", null, StyleBean.getImagePath("checkin.png"), true, null,
						null));
			} else if (document.getStatus() == Document.DOC_CHECKED_IN) {
				model.add(createMenuItem(" " + Messages.getMessage("msg.jsp.checkout"), "checkout-"
						+ folder.getMenuId(), null, "#{documentRecord.checkout}", null, StyleBean
						.getImagePath("checkout.png"), true, null, null));
			}

			model.add(createMenuItem(" " + Messages.getMessage("msg.jsp.foldercontent.edit"), "edit-"
					+ folder.getMenuId(), null, "#{documentRecord.edit}", null, StyleBean
					.getImagePath("document_edit.png"), true, null, null));
		}

		model.add(createMenuItem(" " + Messages.getMessage("msg.jsp.versions"), "versions-" + folder.getMenuId(), null,
				"#{documentRecord.versions}", null, StyleBean.getImagePath("versions.png"), true, "_blank", null));
		model
				.add(createMenuItem(" " + Messages.getMessage("msg.jsp.similardocs"), "similar-" + folder.getMenuId(),
						null, "#{searchForm.searchSimilar}", null, StyleBean.getImagePath("similar.png"), true,
						"_blank", null));
		model.add(createMenuItem(" " + Messages.getMessage("msg.jsp.discuss"), "articles-" + folder.getMenuId(), null,
				"#{documentRecord.articles}", null, StyleBean.getImagePath("comments.png"), true, "_blank", null));
		model.add(createMenuItem(" " + Messages.getMessage("msg.jsp.sendasemail"), "sendasmail-" + folder.getMenuId(),
				null, "#{documentRecord.sendAsEmail}", null, StyleBean.getImagePath("editmail.png"), true, "_blank",
				null));
		model.add(createMenuItem(" " + Messages.getMessage("msg.jsp.sendticket"), "sendticket-" + folder.getMenuId(),
				null, "#{documentRecord.sendAsTicket}", null, StyleBean.getImagePath("ticket.png"), true, "_blank",
				null));
		model.add(createMenuItem(" " + Messages.getMessage("msg.jsp.foldercontent.info"), "info-" + folder.getMenuId(),
				null, "#{documentRecord.info}", null, StyleBean.getImagePath("info.png"), true, "_blank", null));
		model.add(createMenuItem(" " + Messages.getMessage("msg.jsp.history"), "history-" + folder.getMenuId(), null,
				"#{documentRecord.history}", null, StyleBean.getImagePath("history.png"), true, "_blank", null));
	}

	public String noaction() {
		return null;
	}

	public String edit() {
		// Show the proper panel
		DocumentNavigation documentNavigation = ((DocumentNavigation) FacesUtil.accessBeanFromFacesContext(
				"documentNavigation", FacesContext.getCurrentInstance(), log));
		documentNavigation.setSelectedPanel(new PageContentBean("updateDocument"));

		// Now initialize the form
		DocumentEditForm docForm = ((DocumentEditForm) FacesUtil.accessBeanFromFacesContext("documentForm",
				FacesContext.getCurrentInstance(), log));
		docForm.reset();
		docForm.init(this);
		docForm.setReadOnly(false);
		JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "window.location.reload();");
		return null;
	}

	/**
	 * Executes the checkout and the related document's download
	 */
	public String checkout() {

		String username = SessionManagement.getUsername();
		MenuDAO mdao = (MenuDAO) Context.getInstance().getBean(MenuDAO.class);
		DocumentDAO ddao = (DocumentDAO) Context.getInstance().getBean(DocumentDAO.class);
		Menu folder = getDocument().getFolder();

		if (SessionManagement.isValid()) {
			try {
				if (mdao.isWriteEnable(folder.getMenuId(), username)) {
					if (document.getStatus() == Document.DOC_CHECKED_IN) {
						document.setCheckoutUser(username);
						document.setStatus(Document.DOC_CHECKED_OUT);
						ddao.store(document);

						/* create historycheckout entry */
						History history = new History();
						history.setDocId(document.getId());
						history.setDate(new Date());
						history.setUsername(username);
						history.setEvent(History.CHECKOUT);

						HistoryDAO historyDAO = (HistoryDAO) Context.getInstance().getBean(HistoryDAO.class);
						historyDAO.store(history);

						JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "alert('"
								+ Messages.getMessage("msg.checkout.alert") + "');");

						try {
							// create a new menu to replace the checkout
							MenuItem checkinMenuItem = createMenuItem(Messages.getMessage("msg.jsp.checkin"),
									"checkin-" + document.getId(), null, "#{documentRecord.checkin}", null, StyleBean
											.getImagePath("checkin.png"), true, null, null);
							// replacing the old menu at the same index
							model.set(0, checkinMenuItem);
						} catch (Throwable e) {
						}

					} else {
						Messages.addLocalizedError("errors.noaccess");
					}
				} else {
					Messages.addLocalizedError("errors.noaccess");
				}
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
				Messages.addLocalizedError("errors.error");
			}
		} else {
			return "login";
		}

		return null;
	}

	/**
	 * Executes the checkin and the related document's download
	 */
	@SuppressWarnings("deprecation")
	public String checkin() {
		if (SessionManagement.isValid()) {
			// Show the proper panel
			Application application = FacesContext.getCurrentInstance().getApplication();
			DocumentNavigation documentNavigation = ((DocumentNavigation) application.createValueBinding(
					"#{documentNavigation}").getValue(FacesContext.getCurrentInstance()));
			documentNavigation.setSelectedPanel(new PageContentBean("checkin"));

			// Now initialize the edit form
			DocumentEditForm docForm = ((DocumentEditForm) application.createValueBinding("#{documentForm}").getValue(
					FacesContext.getCurrentInstance()));
			docForm.reset();
			docForm.init(this);

			InputFileBean fileForm = ((InputFileBean) application.createValueBinding("#{inputFile}").getValue(
					FacesContext.getCurrentInstance()));
			fileForm.reset();
		} else {
			return "login";
		}

		return null;
	}

	/**
	 * Shows all versions of this document
	 */
	public String versions() {
		String username = SessionManagement.getUsername();
		MenuDAO mdao = (MenuDAO) Context.getInstance().getBean(MenuDAO.class);
		Menu folder = getDocument().getFolder();

		if (SessionManagement.isValid()) {
			try {
				if (mdao.isWriteEnable(folder.getMenuId(), username)) {
					VersionsRecordsManager versionsManager = ((VersionsRecordsManager) FacesUtil
							.accessBeanFromFacesContext("versionsRecordsManager", FacesContext.getCurrentInstance(),
									log));
					versionsManager.selectDocument(document);

					DocumentNavigation documentNavigation = ((DocumentNavigation) FacesUtil.accessBeanFromFacesContext(
							"documentNavigation", FacesContext.getCurrentInstance(), log));
					documentNavigation.setSelectedPanel(new PageContentBean("versions"));
				} else {
					Messages.addError(Messages.getMessage("errors.noaccess"));
				}
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
				Messages.addError(Messages.getMessage("errors.error"));
			}
		} else {
			return "login";
		}

		return null;
	}

	/**
	 * Shows the articles for this document
	 */
	public String articles() {
		if (SessionManagement.isValid()) {
			try {

				ArticlesRecordsManager articlesManager = ((ArticlesRecordsManager) FacesUtil
						.accessBeanFromFacesContext("articlesRecordsManager", FacesContext.getCurrentInstance(), log));
				articlesManager.selectDocument(document);

				DocumentNavigation documentNavigation = ((DocumentNavigation) FacesUtil.accessBeanFromFacesContext(
						"documentNavigation", FacesContext.getCurrentInstance(), log));
				documentNavigation.setSelectedPanel(new PageContentBean("articles"));
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
				Messages.addError(Messages.getMessage("errors.error"));
			}
		} else {
			return "login";
		}

		return null;
	}

	/**
	 * Shows the history of this document
	 */
	public String history() {
		if (SessionManagement.isValid()) {

			DocumentNavigation documentNavigation = ((DocumentNavigation) FacesUtil.accessBeanFromFacesContext(
					"documentNavigation", FacesContext.getCurrentInstance(), log));
			documentNavigation.setSelectedPanel(new PageContentBean("history"));

			HistoryRecordsManager manager = ((HistoryRecordsManager) FacesUtil.accessBeanFromFacesContext(
					"historyRecordsManager", FacesContext.getCurrentInstance(), log));
			manager.selectDocument(this.getDocument());
		} else {
			return "login";
		}

		return null;
	}

	public String info() {
		// All goes as edit action
		edit();

		// Now initialize the form

		DocumentEditForm docForm = ((DocumentEditForm) FacesUtil.accessBeanFromFacesContext("documentForm",
				FacesContext.getCurrentInstance(), log));
		docForm.init(this);
		docForm.setReadOnly(true);

		return null;
	}

	public String sendAsEmail() {
		// Show the proper panel
		DocumentNavigation documentNavigation = ((DocumentNavigation) FacesUtil.accessBeanFromFacesContext(
				"documentNavigation", FacesContext.getCurrentInstance(), log));
		documentNavigation.setSelectedPanel(new PageContentBean("email"));

		// Now initialize the form
		EMailForm emailForm = ((EMailForm) FacesUtil.accessBeanFromFacesContext("emailForm", FacesContext
				.getCurrentInstance(), log));
		emailForm.reset();
		emailForm.setSelectedDocument(getDocument());

		// TODO Reimplement since now documents have no associated menu

		// emailForm.getAttachments().add(getDocument());
		emailForm.setAuthor(SessionManagement.getUser().getEmail());

		return null;
	}

	public String sendAsTicket() {
		// Show the proper panel

		DocumentNavigation documentNavigation = ((DocumentNavigation) FacesUtil.accessBeanFromFacesContext(
				"documentNavigation", FacesContext.getCurrentInstance(), log));
		documentNavigation.setSelectedPanel(new PageContentBean("email"));

		// Now initialize the form
		EMailForm emailForm = ((EMailForm) FacesUtil.accessBeanFromFacesContext("emailForm", FacesContext
				.getCurrentInstance(), log));
		emailForm.reset();
		emailForm.setSelectedDocument(getDocument());
		emailForm.setAuthor(SessionManagement.getUser().getEmail());

		String username = SessionManagement.getUsername();
		Date date = new Date();
		String temp = DateFormat.getDateInstance().format(date) + username;
		String ticketid = CryptBean.cryptString(temp);
		DownloadTicket ticket = new DownloadTicket();
		ticket.setTicketId(ticketid);
		ticket.setDocId(docId);
		ticket.setUsername(username);

		DownloadTicketDAO ticketDao = (DownloadTicketDAO) Context.getInstance().getBean(DownloadTicketDAO.class);
		ticketDao.store(ticket);

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		request.getRequestURL();

		String address = "http://";
		address += (request.getServerName() + ":");
		address += request.getServerPort();
		address += request.getContextPath();
		address += ("/download-ticket?ticketId=" + ticketid);
		emailForm.setText("URL: " + address);

		return null;
	}

	/**
	 * Utility method used by document lazy loading
	 */
	protected void loadDocument() {
		DocumentDAO docDao = (DocumentDAO) Context.getInstance().getBean(DocumentDAO.class);
		this.document = docDao.findByPrimaryKey(docId);
	}
}