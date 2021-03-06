package com.logicaldoc.gui.frontend.client.folder;

import java.util.LinkedHashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.logicaldoc.gui.common.client.Session;
import com.logicaldoc.gui.common.client.i18n.I18N;
import com.logicaldoc.gui.common.client.log.Log;
import com.logicaldoc.gui.common.client.util.ItemFactory;
import com.logicaldoc.gui.frontend.client.services.AuditService;
import com.logicaldoc.gui.frontend.client.services.AuditServiceAsync;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * This is the form used for the workflow task setting.
 * 
 * @author Matteo Caruso - Logical Objects
 * @since 6.0
 */
public class SubscriptionDialog extends Window {
	private AuditServiceAsync service = (AuditServiceAsync) GWT.create(AuditService.class);

	public SubscriptionDialog(final ListGridRecord selection) {
		setHeaderControls(HeaderControls.HEADER_LABEL, HeaderControls.CLOSE_BUTTON);

		setTitle(I18N.message("subscription") + " - " + selection.getAttributeAsString("name"));

		final String[] events;
		String buf = selection.getAttributeAsString("events");
		if (buf == null || buf.isEmpty())
			events = null;
		else {
			events = buf.substring(1, buf.length()).split(",");
		}

		final long subscriptionId = Long.parseLong(selection.getAttributeAsString("id"));

		setWidth(290);
		setHeight(300);
		setMembersMargin(6);
		setCanDragResize(true);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();
		setPadding(6);
		setAutoSize(true);

		final DynamicForm form = new DynamicForm();
		form.setTitleOrientation(TitleOrientation.TOP);
		form.setNumCols(1);

		SelectItem notifyon = new SelectItem("notifyon", I18N.message("notifyon"));
		notifyon.setWidth(280);
		LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		vals.put("all", I18N.message("allevents"));
		vals.put("selection", I18N.message("selectedevents"));
		notifyon.setValueMap(vals);
		notifyon.setValue((events == null || events.length == 0) ? "all" : "selection");

		final SelectItem event;
		event = ItemFactory.newEventsSelector("event", I18N.message("event"), true, false, false);

		event.setEndRow(true);
		event.setDisabled(events == null || events.length == 0);
		if (events != null) {
			event.setValues(events);
		}

		notifyon.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent e) {
				if ("selection".equals(form.getValueAsString("notifyon")))
					event.setDisabled(false);
				else
					event.setDisabled(true);
			}
		});

		ButtonItem save = new ButtonItem();
		save.setTitle(I18N.message("save"));
		save.setAutoFit(true);
		save.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String[] events = null;
				if ("selection".equals(form.getValueAsString("notifyon"))) {
					String buf = form.getValues().get("event").toString().trim().toLowerCase();
					buf = buf.replace('[', ' ');
					buf = buf.replace(']', ' ');
					buf = buf.replace(" ", "");
					events = buf.split(",");
				}

				service.update(Session.get().getSid(), subscriptionId, events, new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
						Log.serverError(caught);
					}

					@Override
					public void onSuccess(Void ret) {
						Log.info(I18N.message("settingssaved"), null);

						String buf = form.getValues().get("event").toString().trim().toLowerCase();
						buf = buf.replace('[', ',');
						buf = buf.replace(']', ',');
						buf = buf.replace(" ", "");

						selection.setAttribute("events", buf);
					}
				});
				destroy();
			}
		});

		form.setItems(notifyon, event, save);
		addItem(form);
	}

	/**
	 * Constructor used to subscribe a user to folders or documents
	 */
	public SubscriptionDialog(final Long folderId, final long[] docIds) {
		setHeaderControls(HeaderControls.HEADER_LABEL, HeaderControls.CLOSE_BUTTON);

		if (folderId != null)
			setTitle(I18N.message("foldersubscription"));
		else
			setTitle(I18N.message("documentsubscription"));

		setWidth(290);
		setHeight(360);
		setMembersMargin(6);
		setCanDragResize(true);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();
		setPadding(6);
		setAutoSize(true);

		final DynamicForm form = new DynamicForm();
		form.setTitleOrientation(TitleOrientation.TOP);
		form.setNumCols(1);

		SelectItem option = new SelectItem("option", I18N.message("subscriptionoption"));
		option.setWidth(280);
		LinkedHashMap<String, String> options = new LinkedHashMap<String, String>();
		options.put("current", I18N.message("subscribecurrent"));
		options.put("subfolders", I18N.message("subscribesubfolders"));
		option.setValueMap(options);
		option.setValue("current");

		SelectItem notifyon = new SelectItem("notifyon", I18N.message("notifyon"));
		notifyon.setWidth(280);
		LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		vals.put("all", I18N.message("allevents"));
		vals.put("selection", I18N.message("selectedevents"));
		notifyon.setValueMap(vals);
		notifyon.setValue("all");

		final SelectItem event;
		if (folderId != null)
			event = ItemFactory.newEventsSelector("event", I18N.message("event"), true, false, false);
		else
			event = ItemFactory.newEventsSelector("event", I18N.message("event"), false, false, false);

		event.setEndRow(true);
		event.setDisabled(true);

		notifyon.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent e) {
				if ("selection".equals(form.getValueAsString("notifyon")))
					event.setDisabled(false);
				else
					event.setDisabled(true);
			}
		});

		ButtonItem subscribe = new ButtonItem();
		subscribe.setTitle(I18N.message("subscribe"));
		subscribe.setAutoFit(true);
		subscribe.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String[] events = null;
				if ("selection".equals(form.getValueAsString("notifyon"))) {
					String buf = form.getValues().get("event").toString().trim().toLowerCase();
					buf = buf.replace('[', ' ');
					buf = buf.replace(']', ' ');
					buf = buf.replace(" ", "");
					events = buf.split(",");
				}

				if (folderId != null)
					service.subscribeFolder(Session.get().getSid(), folderId,
							form.getValueAsString("option").equals("current"), events, new AsyncCallback<Void>() {
								@Override
								public void onFailure(Throwable caught) {
									Log.serverError(caught);
								}

								@Override
								public void onSuccess(Void ret) {
									Log.info(I18N.message("foldersubscribed"), null);
									Session.get().getUser()
											.setSubscriptions(Session.get().getUser().getSubscriptions() + 1);
								}
							});
				else
					service.subscribeDocuments(Session.get().getSid(), docIds, events, new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable caught) {
							Log.serverError(caught);
						}

						@Override
						public void onSuccess(Void ret) {
							Log.info(I18N.message("foldersubscribed"), null);
							Session.get().getUser().setSubscriptions(Session.get().getUser().getSubscriptions() + 1);
						}
					});
				destroy();
			}
		});

		if (folderId != null)
			form.setItems(option, notifyon, event, subscribe);
		else
			form.setItems(notifyon, event, subscribe);
		addItem(form);
	}
}
