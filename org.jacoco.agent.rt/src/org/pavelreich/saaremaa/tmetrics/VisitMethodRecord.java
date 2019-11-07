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

class VisitMethodRecord extends VisitRecord {
	int access;
	String methodName;
	String descriptor;
	String signature;
	String[] exceptions;

	public VisitMethodRecord(final int access, final String methodName,
			final String descriptor, final String signature,
			final String[] exceptions) {
		this.access = access;
		this.methodName = methodName;
		this.descriptor = descriptor;
		this.signature = signature;
		this.exceptions = exceptions;
	}

}
