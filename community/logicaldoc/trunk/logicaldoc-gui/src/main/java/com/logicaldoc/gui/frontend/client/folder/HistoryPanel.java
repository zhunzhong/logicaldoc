package com.logicaldoc.gui.frontend.client.folder;

import com.google.gwt.user.client.Window;
import com.logicaldoc.gui.common.client.Session;
import com.logicaldoc.gui.common.client.beans.GUIFolder;
import com.logicaldoc.gui.common.client.data.FolderHistoryDS;
import com.logicaldoc.gui.common.client.formatters.DateCellFormatter;
import com.logicaldoc.gui.common.client.i18n.I18N;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;

/**
 * This panel shows the history of a folder
 * 
 * @author Marco Meschieri - Logical Objects
 * @since 6.0
 */
public class HistoryPanel extends FolderDetailTab {

	private FolderHistoryDS dataSource;

	private ListGrid listGrid;

	public HistoryPanel(final GUIFolder folder) {
		super(folder, null);

		ListGridField user = new ListGridField("user", I18N.message("user"), 100);
		ListGridField event = new ListGridField("event", I18N.message("event"), 200);
		ListGridField date = new ListGridField("date", I18N.message("date"), 110);
		date.setAlign(Alignment.CENTER);
		date.setType(ListGridFieldType.DATE);
		date.setCellFormatter(new DateCellFormatter());
		ListGridField comment = new ListGridField("comment", I18N.message("comment"));
		ListGridField title = new ListGridField("title", I18N.message("title"));
		ListGridField path = new ListGridField("path", I18N.message("path"));
		ListGridField sid = new ListGridField("sid", I18N.message("sid"));

		listGrid = new ListGrid();
		listGrid.setCanFreezeFields(true);
		listGrid.setAutoFetchData(true);
		dataSource = new FolderHistoryDS(folder.getId());
		listGrid.setDataSource(dataSource);
		listGrid.setFields(user, event, date, comment, title, path, sid);
		addMember(listGrid);

		listGrid.addCellDoubleClickHandler(new CellDoubleClickHandler() {
			@Override
			public void onCellDoubleClick(CellDoubleClickEvent event) {
				ListGridRecord record = event.getRecord();
				Window.open("download?sid=" + Session.get().getSid() + "&docId=" + folder.getId() + "&versionId="
						+ record.getAttribute("version") + "&open=true", "_blank", "");
			}
		});
	}

	@Override
	public void destroy() {
		super.destroy();
		if (dataSource != null)
			dataSource.destroy();
	}
}