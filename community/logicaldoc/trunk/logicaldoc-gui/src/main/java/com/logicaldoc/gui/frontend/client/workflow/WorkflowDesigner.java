package com.logicaldoc.gui.frontend.client.workflow;

import com.google.gwt.core.client.GWT;
import com.logicaldoc.gui.common.client.beans.GUITransition;
import com.logicaldoc.gui.common.client.beans.GUIWFState;
import com.logicaldoc.gui.common.client.beans.GUIWorkflow;
import com.logicaldoc.gui.common.client.i18n.I18N;
import com.logicaldoc.gui.frontend.client.administration.AdminPanel;
import com.logicaldoc.gui.frontend.client.services.WorkflowService;
import com.logicaldoc.gui.frontend.client.services.WorkflowServiceAsync;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;
import com.smartgwt.client.widgets.Dialog;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VStack;

/**
 * Where the workflow diagram is drawn
 * 
 * @author Marco Meschieri - Logical Objects
 * @since 6.0
 */
public class WorkflowDesigner extends VStack implements WorkflowObserver {

	public final static int TYPE_TASK = 0;

	public final static int TYPE_END = 1;

	public final static int TYPE_JOIN = 2;

	public final static int TYPE_FORK = 3;

	protected WorkflowServiceAsync workflowService = (WorkflowServiceAsync) GWT.create(WorkflowService.class);

	// HStack or HLayout with Accordion e Drawing Panel
	private HLayout layout = new HLayout();

	private Accordion accordion = null;

	private GUIWorkflow workflow = null;

	private DrawingPanel drawingPanel = null;

	private ValuesManager vm = new ValuesManager();

	private boolean onlyVisualization = false;

	public WorkflowDesigner(GUIWorkflow workflow, boolean onlyDrawingPanel) {
		this.workflow = workflow;
		this.onlyVisualization = onlyDrawingPanel;

		setMembersMargin(5);

		if (!onlyVisualization) {
			addMember(new WorkflowToolstrip(this));
			addMember(new StateToolstrip(this));
		}

		if (this.workflow != null && !onlyVisualization) {
			accordion = new Accordion(workflow);
			layout.addMember(accordion);
		}

		if (this.workflow != null) {
			drawingPanel = new DrawingPanel(this);
			layout.addMember(drawingPanel);
			addMember(layout);
		}
	}

	@Override
	public void onStateSelect(GUIWFState state) {
		final GUIWFState wfState = state;

		if (wfState.getType() == TYPE_TASK) {
			TaskDialog taskDialog = new TaskDialog(this, workflow, wfState);
			taskDialog.show();
		} else {
			Dialog dialog = new Dialog();
			dialog.setWidth(200);

			String typeString = "";
			if (wfState.getType() == TYPE_JOIN) {
				typeString = I18N.message("join");
			} else if (wfState.getType() == TYPE_FORK) {
				typeString = I18N.message("fork");
			} else if (wfState.getType() == TYPE_END) {
				typeString = I18N.message("endstate");
			}

			SC.askforValue(I18N.message("editworkflowstate", typeString), "<b>" + I18N.message("name") + ":</b>",
					wfState.getName(), new ValueCallback() {
						@Override
						public void execute(String value) {
							if (value == null || "".equals(value.trim()))
								return;

							wfState.setName(value);

							GUIWFState[] states = new GUIWFState[workflow.getStates().length];
							int i = 0;
							for (GUIWFState state : workflow.getStates()) {
								if (!state.getId().equals(wfState.getId())) {
									states[i] = state;
									i++;
								} else {
									states[i] = wfState;
									i++;
								}
							}

							workflow.setStates(states);

							AdminPanel.get().setContent(new WorkflowDesigner(workflow, false));

						}
					}, dialog);
		}
	}

	public GUIWorkflow getWorkflow() {
		return workflow;
	}

	@Override
	public void onStateDelete(GUIWFState workflowState) {
		final GUIWFState wfState = workflowState;

		SC.ask(I18N.message("question"), I18N.message("confirmdelete"), new BooleanCallback() {
			@Override
			public void execute(Boolean value) {
				if (value) {
					GUIWFState[] states = new GUIWFState[workflow.getStates().length - 1];
					int i = 0;
					for (GUIWFState state : workflow.getStates()) {
						if (!state.getId().equals(wfState.getId())) {
							states[i] = state;
							i++;
						}
					}
					workflow.setStates(states);

					AdminPanel.get().setContent(new WorkflowDesigner(workflow, false));
				}
			}
		});
	}

	@Override
	public void onTransitionDelete(GUIWFState from, GUIWFState target) {
		final GUIWFState fromState = from;
		final GUIWFState targetState = target;

		SC.ask(I18N.message("question"), I18N.message("confirmdelete"), new BooleanCallback() {
			@Override
			public void execute(Boolean value) {
				if (value) {
					if (fromState.getTransitions().length == 1)
						fromState.setTransitions(null);
					else {
						GUITransition[] newTransitions = new GUITransition[fromState.getTransitions().length - 1];
						int i = 0;
						for (GUITransition transition : fromState.getTransitions()) {
							if (transition.getTargetState().getType() == GUIWFState.TYPE_UNDEFINED
									|| !transition.getTargetState().getId().equals(targetState.getId())) {
								newTransitions[i] = transition;
								i++;
							}
						}
						fromState.setTransitions(newTransitions);
					}

					GUIWFState[] states = new GUIWFState[workflow.getStates().length];
					int j = 0;
					for (GUIWFState state : workflow.getStates()) {
						if (!state.getId().equals(fromState.getId())) {
							states[j] = state;
							j++;
						} else {
							states[j] = fromState;
							j++;
						}
					}
					workflow.setStates(states);

					AdminPanel.get().setContent(new WorkflowDesigner(workflow, false));
				}
			}
		});
	}

	@Override
	public void onDraggedStateDelete(GUIWFState from, GUIWFState target) {
		final GUIWFState fromState = from;
		final GUIWFState targetState = target;
		final GUIWorkflow workflow = this.getWorkflow();
		SC.ask(I18N.message("question"), I18N.message("confirmdelete"), new BooleanCallback() {
			@Override
			public void execute(Boolean value) {
				if (value) {
					GUITransition[] newTransitions = new GUITransition[fromState.getTransitions().length];
					int i = 0;
					for (GUITransition transition : fromState.getTransitions()) {
						if (transition.getTargetState().getType() == GUIWFState.TYPE_UNDEFINED
								|| !transition.getTargetState().getId().equals(targetState.getId())) {
							newTransitions[i] = transition;
							i++;
						} else {
							GUIWFState target = new GUIWFState();
							target.setId("" + (workflow.getStates().length + 1));
							target.setType(GUIWFState.TYPE_UNDEFINED);
							newTransitions[i] = new GUITransition(transition.getText(), target);
							i++;
						}
					}
					fromState.setTransitions(newTransitions);

					GUIWFState[] states = new GUIWFState[workflow.getStates().length];
					int j = 0;
					for (GUIWFState state : workflow.getStates()) {
						if (!state.getId().equals(fromState.getId())) {
							states[j] = state;
							j++;
						} else {
							states[j] = fromState;
							j++;
						}
					}
					workflow.setStates(states);

					AdminPanel.get().setContent(new WorkflowDesigner(workflow, false));
				}
			}
		});
	}

	@Override
	public void onAddTransition(GUIWFState fromState, GUIWFState targetState, String transitionText) {
		GUITransition[] newTransitions = null;
		if (targetState == null || fromState.getType() == GUIWFState.TYPE_FORK) {
			// Adding a new transition without a dragged state, so put an empty
			// drop area
			if (fromState.getTransitions() != null)
				newTransitions = new GUITransition[fromState.getTransitions().length + 1];
			else
				newTransitions = new GUITransition[1];
		} else {
			// Dragging a workflow state into an existing transition, so into an
			// empty drop area
			if (fromState.getTransitions() != null)
				newTransitions = new GUITransition[fromState.getTransitions().length];
			else
				newTransitions = new GUITransition[1];
		}

		if (fromState.getTransitions() != null) {
			GUITransition[] transitions = fromState.getTransitions();
			for (int i = 0; i < transitions.length; i++) {
				if (transitionText != null && transitionText.equals(transitions[i].getText())) {
					// Associate the targetState to an existing transition
					GUITransition t = new GUITransition(transitionText, targetState);
					newTransitions[i] = t;
				} else {
					newTransitions[i] = transitions[i];
				}
			}
		} else {
			// Associate the targetState to an existing transition
			GUITransition t = new GUITransition(transitionText, targetState);
			newTransitions[0] = t;
		}

		if (targetState == null) {
			// The user has clicked the 'add transition' link into the workflow
			// state element.
			GUIWFState target = new GUIWFState();
			target.setId("" + (this.getWorkflow().getStates().length + 1));
			target.setType(GUIWFState.TYPE_UNDEFINED);
			newTransitions[newTransitions.length - 1] = new GUITransition(transitionText, target);
		} else if (fromState.getType() == GUIWFState.TYPE_FORK) {
			newTransitions[newTransitions.length - 1] = new GUITransition(transitionText, targetState);
		}
		// else
		// // The user has dragged a new workflow state element.
		// newTransitions[newTransitions.length - 1] = new
		// GUITransition(targetState.getName(), targetState);
		fromState.setTransitions(newTransitions);

		GUIWFState[] states = new GUIWFState[workflow.getStates().length];
		int j = 0;
		for (GUIWFState state : workflow.getStates()) {
			if (!state.getId().equals(fromState.getId())) {
				states[j] = state;
				j++;
			} else {
				states[j] = fromState;
				j++;
			}
		}
		workflow.setStates(states);

		AdminPanel.get().setContent(new WorkflowDesigner(workflow, false));
	}

	public void reloadDrawingPanel() {
		removeMember(layout);
		accordion.destroy();
		drawingPanel.destroy();

		accordion = new Accordion(workflow);
		drawingPanel = new DrawingPanel(this);
		layout.addMember(accordion);
		layout.addMember(drawingPanel);
		addMember(layout);
	}

	public Accordion getAccordion() {
		return accordion;
	}

	@Override
	public void onAddState(GUIWorkflow wfl, int type) {
		workflow = wfl;

		GUIWFState[] newStates = null;
		if (workflow.getStates() != null && workflow.getStates().length > 0) {
			newStates = new GUIWFState[workflow.getStates().length + 1];
			int j = 0;
			for (GUIWFState state : workflow.getStates()) {
				newStates[j] = state;
				j++;
			}
		} else {
			newStates = new GUIWFState[1];
		}

		GUIWFState newState = new GUIWFState("" + (newStates.length), I18N.message("taskwithnoname"), type);
		newStates[newStates.length - 1] = newState;
		if (workflow.getStartStateId().equals("0"))
			if (newState.getType() == GUIWFState.TYPE_TASK) {
				workflow.setStartStateId(newState.getId());
			}

		workflow.setStates(newStates);

		AdminPanel.get().setContent(new WorkflowDesigner(workflow, false));
	}

	public boolean isOnlyVisualization() {
		return onlyVisualization;
	}
}