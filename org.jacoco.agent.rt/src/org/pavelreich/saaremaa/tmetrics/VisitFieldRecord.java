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

class VisitFieldRecord extends VisitRecord {
	int access;
	String name;
	String descriptor;
	String signature;
	Object value;

	public VisitFieldRecord(final int access, final String name,
			final String descriptor, final String signature,
			final Object value) {
		this.access = access;
		this.name = name;
		this.descriptor = descriptor;
		this.signature = signature;
		this.value = value;
	}

}
