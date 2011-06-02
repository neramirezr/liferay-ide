/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

package com.liferay.ide.eclipse.server.aws.ui;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.wst.common.project.facet.core.runtime.IRuntime;
import org.eclipse.wst.common.project.facet.ui.IDecorationsProvider;

/**
 * @author Greg Amerson
 */
@SuppressWarnings("rawtypes")
public class BeanstalkRuntimeDecorationsProvider implements IAdapterFactory {

	public class WebsphereDecorationsProvider implements IDecorationsProvider {

		// private IRuntime runtime;

		public WebsphereDecorationsProvider(IRuntime adaptableObject) {
			// runtime = adaptableObject;
		}

		public ImageDescriptor getIcon() {
			return AWSUIPlugin.imageDescriptorFromPlugin(AWSUIPlugin.PLUGIN_ID, "icons/aws-box.gif");
		}

	}

	private static final Class<?>[] ADAPTER_TYPES = {
		IDecorationsProvider.class
	};

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (IDecorationsProvider.class.equals(adapterType)) {
			return new WebsphereDecorationsProvider((IRuntime) adaptableObject);
		}
		else {
			return null;
		}
	}

	public Class<?>[] getAdapterList() {
		return ADAPTER_TYPES;
	}

}