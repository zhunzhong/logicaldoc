package com.logicaldoc.util.config;

import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

/**
 * Configurator for the web.xml file
 * 
 * @author Marco Meschieri
 * @author Sebastian Wenzky
 * @version $Id:$
 * @since 3.0
 */
public class WebConfigurator extends XMLBean {
	public WebConfigurator() {
		super(System.getProperty("logicaldoc.app.rootdir") + "/WEB-INF/web.xml");
	}

	/**
	 * Check for existing element within a XML-document
	 * @param elements List of Elements which have as child a <pre>name</pre> tag 
	 * @param match_text The text for looking up whether exists
	 * @param name The tag that should be right there for checking this value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Element elementLookUp(List elements, String match_text, String name){
		for (Iterator iterator = elements.iterator(); iterator.hasNext();) {
			Element elem = (Element) iterator.next();
			Element elementName = elem.getChild(match_text, elem.getNamespace());
			if (elementName != null && elementName.getText().trim().equals(name)) {
				// The element already exists
				return elem;
			}
		}
		
		return null;
	}
	
	/**
	 * Adds a init parameter to the servlet
	 * @param clazz The classname
	 * @param name Name of the Parameter
	 * @param value Value of the Parameter
	 * @param description Description
	 */
	@SuppressWarnings("unchecked")
	public void addInitParam(String servletName, String param_name, String param_value, String param_description){
		List servlets = getRootElement().getChildren("servlet", getRootElement().getNamespace());
		Element servlet = this.elementLookUp(servlets, "servlet-name", servletName);
		
		if(servlet == null)
			throw new IllegalStateException(
					"The servlet "
							+ servletName
							+ " has not been found. Have you already written the servlet?");
		
		Element initParam = this.elementLookUp(servlet.getChildren(), "param-name", param_name);
		
		if(initParam != null)
			return;

		
		Element paramElement = new Element("init-param", getRootElement().getNamespace());
		
		//the name
		Element param = new Element("param-name", getRootElement().getNamespace());
		param.setText(param_name);
		//paramElement.addContent("\n ");
		paramElement.getChildren().add(param);
		
		param = new Element("param-value", getRootElement().getNamespace());
		param.setText(param_value);
		//paramElement.addContent("\n ");
		paramElement.getChildren().add(param);
		
		if(param_description != null && param_description.equals("") != true){ 
			param = new Element("description", getRootElement().getNamespace());			
			param.setText(param_description);
			//paramElement.addContent("\n ");
			paramElement.getChildren().add(param);
		}
		
		Element loadOnStartUpElem = (Element) servlet.getChildren().get(
				servlet.getChildren().size() - 1);

		// sorting the elements in that way, that element "load-on-startup"
		// always stays at the tail
		if(loadOnStartUpElem.getName().equals("load-on-startup")){
			servlet.getChildren().remove(loadOnStartUpElem);
			servlet.addContent(paramElement);
			servlet.addContent(loadOnStartUpElem);
		}
		else 
			servlet.getChildren().add(paramElement);
		
		
	}
	/**
	 * Adds a new servlet mapping to the deployment descriptor. If the mapping
	 * already exists no modifications are committed.
	 * 
	 * @param name The servlet name
	 * @param clazz The servlet class fully qualified name
	 */
	public void addServlet(String name, String clazz){
		this.addServlet(name, clazz, -1);
	}
	
	/**
	 * Adds a new servlet mapping to the deployment descriptor. If the mapping
	 * already exists no modifications are committed.
	 * 
	 * @param name The servlet name
	 * @param clazz The servlet class fully qualified name
	 * @param load_on startup 
	 */
	@SuppressWarnings("unchecked")
	public void addServlet(String name, String clazz, int load_on_startup) {
		// Search for the specified servlet
		List servlets = getRootElement().getChildren("servlet", getRootElement().getNamespace());
		
		if(this.elementLookUp(servlets, "servlet-name", name) != null)
			return;

		// Retrieve the last <servlet> element
		Element lastServlet = (Element) servlets.get(servlets.size() - 1);

		List children = getRootElement().getChildren();

		// Find the index of the element to add the new element after.
		int index = children.indexOf(lastServlet);

		// Prepare the new mapping
		Element servlet = new Element("servlet", getRootElement().getNamespace());
		Element servletNameElement = new Element("servlet-name", getRootElement().getNamespace());
		servletNameElement.setText(name);
		Element servletClass = new Element("servlet-class", getRootElement().getNamespace());
		servletClass.setText(clazz);
		servlet.addContent("\n ");
		servlet.addContent(servletNameElement);
		servlet.addContent("\n ");
		servlet.addContent(servletClass);
		servlet.addContent("\n ");
		
		// Add the new element to the next index along.
		// This does cover the case where indexOf returned -1.
		children.add(index + 1, servlet);
		writeXMLDoc();
	}

	/**
	 * Adds a new servlet mapping to the deployment descriptor. If the mapping
	 * already exists no modifications are committed.
	 * 
	 * @param servlet The name of the servlet
	 * @param pattern The mapping pattern
	 */
	@SuppressWarnings("unchecked")
	public void addServletMapping(String servlet, String pattern) {
		// Search for the specified mapping
		List mappings = getRootElement().getChildren("servlet-mapping", getRootElement().getNamespace());
		for (Iterator iterator = mappings.iterator(); iterator.hasNext();) {
			Element elem = (Element) iterator.next();
			Element servletName = elem.getChild("servlet-name", elem.getNamespace());
			if (servletName.getText().trim().equals(servlet)) {
				// The mapping already exists
				return;
			}
		}

		// Retrieve the last <servlet-mapping> element
		Element lastMapping = (Element) mappings.get(mappings.size() - 1);

		List children = getRootElement().getChildren();
		// Find the index of the element to add the new element after.
		int index = children.indexOf(lastMapping);

		// Prepare the new mapping
		Element servletMapping = new Element("servlet-mapping", getRootElement().getNamespace());
		Element servletName = new Element("servlet-name", getRootElement().getNamespace());
		servletName.setText(servlet);
		Element servletPattern = new Element("url-pattern", getRootElement().getNamespace());
		servletPattern.setText(pattern);
		servletMapping.addContent("\n ");
		servletMapping.addContent(servletName);
		servletMapping.addContent("\n ");
		servletMapping.addContent(servletPattern);
		servletMapping.addContent("\n ");

		// Add the new element to the next index along.
		// This does cover the case where indexOf returned -1.
		children.add(index + 1, servletMapping);
		writeXMLDoc();
	}

	public void setDisplayName(String displayName) {
		// Retrieve the <display-name> element
		Element element = getRootElement().getChild("display-name", getRootElement().getNamespace());
		element.setText(displayName);
		writeXMLDoc();
	}

	public String getDisplayName() {
		// Retrieve the <display-name> element
		Element element = getRootElement().getChild("display-name", getRootElement().getNamespace());
		return element.getText();
	}

	public void setDescription(String description) {
		// Retrieve the <display-name> element
		Element element = getRootElement().getChild("description", getRootElement().getNamespace());
		element.setText(description);
		writeXMLDoc();
	}
}