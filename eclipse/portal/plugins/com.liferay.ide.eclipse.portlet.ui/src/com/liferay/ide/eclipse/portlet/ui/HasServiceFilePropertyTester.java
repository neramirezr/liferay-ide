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

package com.liferay.ide.eclipse.portlet.ui;

import com.liferay.ide.eclipse.core.ILiferayConstants;
import com.liferay.ide.eclipse.core.util.CoreUtil;
import com.liferay.ide.eclipse.project.core.util.ProjectUtil;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;

/**
 * @author Greg Amerson
 */
public class HasServiceFilePropertyTester extends PropertyTester {

	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (receiver instanceof IProject) {
			IProject project = (IProject) receiver;

			boolean isLiferayProject = ProjectUtil.isLiferayProject(project);

			if (isLiferayProject) {
				try {
					IFolder docroot = CoreUtil.getDocroot(project);

					if (docroot != null && docroot.exists()) {
						IFile serviceFile =
							docroot.getFile("WEB-INF/" + ILiferayConstants.LIFERAY_SERVICE_BUILDER_XML_FILE);

						if (serviceFile.exists()) {
							return true;
						}
					}
				}
				catch (Throwable t) {
					// ignore
				}
			}
		}

		return false;
	}

}
