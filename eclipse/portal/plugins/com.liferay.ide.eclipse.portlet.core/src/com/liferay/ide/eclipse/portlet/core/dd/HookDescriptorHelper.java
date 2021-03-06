/*******************************************************************************
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 *******************************************************************************/

package com.liferay.ide.eclipse.portlet.core.dd;

import com.liferay.ide.eclipse.core.ILiferayConstants;
import com.liferay.ide.eclipse.core.util.DescriptorHelper;
import com.liferay.ide.eclipse.portlet.core.operation.INewHookDataModelProperties;
import com.liferay.ide.eclipse.project.core.util.ProjectUtil;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.format.FormatProcessorXML;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Greg Amerson
 */
@SuppressWarnings( {
	"restriction"
})
public class HookDescriptorHelper extends DescriptorHelper implements INewHookDataModelProperties {

	public HookDescriptorHelper(IProject project) {
		super(project);
	}

	public IStatus addActionItems(final List<String[]> actionItems) {
		DOMModelEditOperation operation =
			new DOMModelEditOperation(getDescriptorFile(ILiferayConstants.LIFERAY_HOOK_XML_FILE)) {

				protected IStatus doExecute(IDOMDocument document) {
					return doAddActionItems(document, actionItems);
				}

			};

		IStatus status = operation.execute();

		if (!status.isOK()) {
			return status;
		}

		return status;
	}

	public IStatus addLanguageProperties(final List<String> languageProperties) {
		DOMModelOperation operation =
			new DOMModelEditOperation(getDescriptorFile(ILiferayConstants.LIFERAY_HOOK_XML_FILE)) {

			protected IStatus doExecute(IDOMDocument document) {
				return doAddLanguageProperties(document, languageProperties);
			}

			};

		IStatus status = operation.execute();

		if (!status.isOK()) {
			return status;
		}

		return status;
	}

	public IStatus doSetCustomJSPDir(IDOMDocument document, IDataModel model) {
		// <hook> element
		Element docRoot = document.getDocumentElement();

		String relativeJspFolderPath =
			ProjectUtil.getRelativePathFromDocroot(this.project, model.getStringProperty(CUSTOM_JSPS_FOLDER));

		Element customJspElement = null;

		// check for existing element
		NodeList nodeList = docRoot.getElementsByTagName("custom-jsp-dir");

		if (nodeList != null && nodeList.getLength() > 0) {
			customJspElement = (Element) nodeList.item(0);

			removeChildren(customJspElement);

			Node textNode = document.createTextNode(relativeJspFolderPath);

			customJspElement.appendChild(textNode);
		}
		else {
			// need to insert customJspElement before any <service>
			NodeList serviceTags = docRoot.getElementsByTagName("service");

			if (serviceTags != null && serviceTags.getLength() > 0) {
				customJspElement =
					insertChildElement(docRoot, serviceTags.item(0), "custom-jsp-dir", relativeJspFolderPath);
			}
			else {
				customJspElement = appendChildElement(docRoot, "custom-jsp-dir", relativeJspFolderPath);

				// append a newline text node
				docRoot.appendChild(document.createTextNode(System.getProperty("line.separator")));
			}
		}

		// format the new node added to the model;
		FormatProcessorXML processor = new FormatProcessorXML();

		processor.formatNode(customJspElement);

		return Status.OK_STATUS;
	}

	public String getCustomJSPFolder(final IDataModel model) {
		final String[] retval = new String[1];

		DOMModelOperation operation =
			new DOMModelEditOperation(getDescriptorFile(ILiferayConstants.LIFERAY_HOOK_XML_FILE)) {

			protected IStatus doExecute(IDOMDocument document) {
				retval[0] = readCustomJSPFolder(document, model);
				return Status.OK_STATUS;
			}
			};

		IStatus status = operation.execute();

		if (!status.isOK()) {
			return null;
		}

		return retval[0];
	}

	public String readCustomJSPFolder(IDOMDocument document, IDataModel model) {
		// <hook> element
		Element docRoot = document.getDocumentElement();

		Element customJspElement = null;

		// check for existing element
		NodeList nodeList = docRoot.getElementsByTagName("custom-jsp-dir");

		if (nodeList != null && nodeList.getLength() > 0) {
			customJspElement = (Element) nodeList.item(0);

			return customJspElement.getFirstChild().getNodeValue();
		}

		return null;
	}

	public IStatus setCustomJSPDir(final IDataModel model) {
		DOMModelOperation operation =
			new DOMModelEditOperation(getDescriptorFile(ILiferayConstants.LIFERAY_HOOK_XML_FILE)) {

				protected IStatus doExecute(IDOMDocument document) {
					return doSetCustomJSPDir(document, model);
				}

			};

		IStatus status = operation.execute();

		if (!status.isOK()) {
			return status;
		}

		return status;
	}

	public IStatus setPortalProperties(final IDataModel model, final String propertiesFile) {
		DOMModelOperation operation =
			new DOMModelEditOperation(getDescriptorFile(ILiferayConstants.LIFERAY_HOOK_XML_FILE)) {

				protected IStatus doExecute(IDOMDocument document) {
					return doSetPortalProperties(document, model, propertiesFile);
				}

			};

		IStatus status = operation.execute();

		if (!status.isOK()) {
			return status;
		}

		return status;
	}

	protected IStatus doAddActionItems(IDOMDocument document, List<String[]> actionItems) {
		// <hook> element
		Element docRoot = document.getDocumentElement();

		FormatProcessorXML processor = new FormatProcessorXML();

		Element newServiceElement = null;

		if (actionItems != null) {
			for (String[] actionItem : actionItems) {
				newServiceElement = appendChildElement(docRoot, "service");

				appendChildElement(newServiceElement, "service-type", actionItem[0]);

				appendChildElement(newServiceElement, "service-impl", actionItem[1]);

				processor.formatNode(newServiceElement);
			}
			if (newServiceElement != null) {
				// append a newline text node
				docRoot.appendChild(document.createTextNode(System.getProperty("line.separator")));

				processor.formatNode(newServiceElement);
			}
		}

		return Status.OK_STATUS;
	}

	protected IStatus doAddLanguageProperties(IDOMDocument document, List<String> languageProperties) {
		// <hook> element
		Element docRoot = document.getDocumentElement();

		FormatProcessorXML processor = new FormatProcessorXML();

		Element newLanguageElement = null;

		// check if we have existing custom_dir
		Node refChild = null;

		NodeList nodeList = docRoot.getElementsByTagName("custom-jsp-dir");

		if (nodeList != null && nodeList.getLength() > 0) {
			refChild = nodeList.item(0);
		}
		else {
			nodeList = docRoot.getElementsByTagName("service");

			if (nodeList != null && nodeList.getLength() > 0) {
				refChild = nodeList.item(0);
			}
		}

		if (languageProperties != null) {
			for (String languageProperty : languageProperties) {
				newLanguageElement = insertChildElement(docRoot, refChild, "language-properties", languageProperty);

				processor.formatNode(newLanguageElement);
			}
			if (newLanguageElement != null) {
				// append a newline text node
				docRoot.appendChild(document.createTextNode(System.getProperty("line.separator")));

				processor.formatNode(newLanguageElement);
			}
		}

		return Status.OK_STATUS;
	}

	protected IStatus doSetPortalProperties(IDOMDocument document, IDataModel model, String propertiesFile) {
		// <hook> element
		Element docRoot = document.getDocumentElement();

		// check for existing element
		Element portalPropertiesElement = null;

		NodeList nodeList = docRoot.getElementsByTagName("portal-properties");

		if (nodeList != null && nodeList.getLength() > 0) {
			portalPropertiesElement = (Element) nodeList.item(0);

			removeChildren(portalPropertiesElement);

			Node textNode = document.createTextNode(propertiesFile);

			portalPropertiesElement.appendChild(textNode);
		}
		else {
			portalPropertiesElement =
				insertChildElement(docRoot, docRoot.getFirstChild(), "portal-properties", propertiesFile);
		}

		// format the new node added to the model;
		FormatProcessorXML processor = new FormatProcessorXML();

		processor.formatNode(portalPropertiesElement);

		return Status.OK_STATUS;
	}

}
