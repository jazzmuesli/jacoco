/*******************************************************************************
 * Copyright (c) 2009, 2019 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *
 *******************************************************************************/
package org.pavelreich.saaremaa.tmetrics;

import org.bson.Document;

class TargetLocation {

	private final String className;
	private final String methodName;
	private final String descriptor;

	public TargetLocation(final String ownerClassName, final String methodName,
			final String descriptor) {
		this.className = ownerClassName;
		this.methodName = methodName;
		this.descriptor = descriptor;
	}

	@Override
	public String toString() {
		return className + ":" + methodName;
	}

	public Document toDocument() {
		return new Document("type", getClass().getName())
				.append("className", className).append("descriptor", descriptor)
				.append("methodName", methodName);
	}
}
