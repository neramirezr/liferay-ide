/*******************************************************************************
 * Copyright (c) 2010-2011 Liferay, Inc. All rights reserved.
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
package com.liferay.ide.eclipse.service.ui;

import com.liferay.ide.eclipse.portlet.core.PortletCore;
import com.liferay.ide.eclipse.portlet.core.job.BuildServiceJob;

import org.eclipse.core.resources.IFile;
import org.eclipse.sapphire.ui.SapphireActionHandler;
import org.eclipse.sapphire.ui.SapphireRenderingContext;


public class BuildServicesActionHandler extends SapphireActionHandler {

	@Override
	protected Object run(SapphireRenderingContext context) {
		IFile file = context.getPart().getModelElement().adapt(IFile.class);

		if (file != null && file.exists()) {
			BuildServiceJob job = PortletCore.createBuildServiceJob(file);

			job.schedule();
		}

		return null;
	}

}
