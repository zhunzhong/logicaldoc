package com.logicaldoc.webdav.io.handler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.server.io.IOUtil;
import org.apache.jackrabbit.server.io.PropertyExportContext;
import org.apache.jackrabbit.server.io.PropertyImportContext;
import org.apache.jackrabbit.webdav.DavResource;

import com.logicaldoc.webdav.context.ExportContext;
import com.logicaldoc.webdav.context.ImportContext;
import com.logicaldoc.webdav.exception.WebDavStorageException;
import com.logicaldoc.webdav.io.manager.IOManager;
import com.logicaldoc.webdav.resource.model.Resource;
import com.logicaldoc.webdav.resource.service.ResourceService;
import com.logicaldoc.webdav.web.AbstractWebdavServlet;

/**
 * For more informations, please visit
 * {@link org.apache.jackrabbit.server.io.DefaultHandler}
 * 
 * @author Sebastian Wenzky
 * 
 */
public class DefaultHandler implements IOHandler {

	protected static Log log = LogFactory.getLog(AbstractWebdavServlet.class);

    private String collectionNodetype = JcrConstants.NT_FOLDER;
    private String defaultNodetype = JcrConstants.NT_FILE;
    /* IMPORTANT NOTE: for webDAV compliancy the default nodetype of the content
       node has been changed from nt:resource to nt:unstructured. */
    private String contentNodetype = JcrConstants.NT_UNSTRUCTURED;
    private ResourceService resourceService;
    
    public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}
    
    private IOManager ioManager;

    public DefaultHandler() {
    }

    public DefaultHandler(IOManager ioManager) {
        this.ioManager = ioManager;
    }

    public DefaultHandler(IOManager ioManager, String collectionNodetype, String defaultNodetype, String contentNodetype) {
        this.ioManager = ioManager;

        this.collectionNodetype = collectionNodetype;
        this.defaultNodetype = defaultNodetype;
        this.contentNodetype = contentNodetype;
    }

    public IOManager getIOManager() {
    	return ioManager;
    }

    public void setIOManager(IOManager ioManager) {
        this.ioManager = ioManager;
    }

    public String getName() {
        return getClass().getName();
    }
    
    public boolean canImport(ImportContext context, boolean isCollection) {
        if (context == null || context.isCompleted()) {
            return false;
        }
        Resource resource = context.getResource();
        log.warn("(resource != null) = " + (resource != null));
        return resource != null;
    }

    public boolean canImport(ImportContext context, DavResource resource) {
        if (resource == null) {
            return false;
        }
        boolean canImport = canImport(context, resource.isCollection());
        log.warn("canImport = " + canImport);
        return canImport;
    }

    public boolean importContent(ImportContext context, boolean isCollection) throws IOException {
        if (!canImport(context, isCollection)) {
        	log.warn(getName() + ": Cannot import " + context.getSystemId());
            throw new IOException(getName() + ": Cannot import " + context.getSystemId());
        }

        boolean success = false;
        try {
        	success = setContentData(context, isCollection);
        	log.warn("success = " + success);
        }
        catch (Exception e){        	
        	e.printStackTrace();
        	log.warn(e.getMessage(), e);
        } 

        return success;
    }

    public boolean importContent(ImportContext context, DavResource resource) throws IOException {
        if (!canImport(context, resource)) {
            throw new IOException(getName() + ": Cannot import " + context.getSystemId());
        }
        return importContent(context, resource.isCollection());
    }

    protected boolean importData(ImportContext context, boolean isCollection, Resource resource) throws IOException, WebDavStorageException {
    	return true;
    }

    public boolean canExport(ExportContext context, boolean isCollection) {
        if (context == null || context.isCompleted()) {
            return false;
        }
        return true;
    }

    public boolean canExport(ExportContext context, DavResource resource) {
        if (resource == null) {
            return false;
        }
        return canExport(context, resource.isCollection());
    }

    public boolean exportContent(ExportContext context, boolean isCollection) throws IOException {
        if (!canExport(context, isCollection)) {
            throw new IOException(getName() + ": Cannot export " );
        }
        try {
            if (context.hasStream()) 
                exportData(context, isCollection, context.getResource());
            
            return true;
        } catch (WebDavStorageException e) {
            // should never occur, since the proper structure of the content
            // node must be asserted in the 'canExport' call.
            throw new IOException(e.getMessage());
        }
    }

    public boolean exportContent(ExportContext context, DavResource resource) throws IOException {
        if (!canExport(context, resource)) {
            throw new IOException(getName() + ": Cannot export ");
        }
        return exportContent(context, resource.isCollection());
    }

    protected void exportData(ExportContext context, boolean isCollection,
			Resource resource) throws IOException, WebDavStorageException {
		try {
			InputStream is = resourceService.streamOut(resource);
			if (is != null)
				IOUtil.spool(is, context.getOutputStream());
		}
		catch(FileNotFoundException e){
			throw new IOException("Can´t find file " + resource.getName() + "(" + resource.getID() + ")");
		}
    }
    
    protected synchronized boolean setContentData(ImportContext context, boolean isCollection) throws WebDavStorageException {
        Resource resource = context.getResource();
        String name = context.getSystemId();

        Resource res = resourceService.getChildByName(resource, name);
        
        if (res == null){
        	resourceService.createResource(resource, name, isCollection, context);
        	return true;
        }
        else {
        	res.setRequestedPerson(resource.getRequestedPerson());
        	resourceService.updateResource(res, context);
        	return true;
        }
    }
    
    protected Resource getContentNode(ExportContext context, boolean isCollection) throws WebDavStorageException {
    	return context.getResource();
    }

    protected String getCollectionNodeType() {
        return collectionNodetype;
    }

    protected String getNodeType() {
        return defaultNodetype;
    }

    protected String getContentNodeType() {
        return contentNodetype;
    }
    public boolean canExport(PropertyExportContext context, boolean isCollection) {
        return canExport((ExportContext) context, isCollection);
    }

    public boolean exportProperties(PropertyExportContext exportContext, boolean isCollection) throws WebDavStorageException {
        if (!canExport(exportContext, isCollection)) {
            throw new WebDavStorageException("PropertyHandler " + getName() + " failed to export properties.");
        }
        
        return true;
    }

    public boolean canImport(PropertyImportContext context, boolean isCollection) {
    	return true;
    }

}