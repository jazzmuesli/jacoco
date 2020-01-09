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

import java.util.Arrays;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + access;
		result = prime * result
				+ ((descriptor == null) ? 0 : descriptor.hashCode());
		result = prime * result + Arrays.hashCode(exceptions);
		result = prime * result
				+ ((methodName == null) ? 0 : methodName.hashCode());
		result = prime * result
				+ ((signature == null) ? 0 : signature.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final VisitMethodRecord other = (VisitMethodRecord) obj;
		if (access != other.access) {
			return false;
		}
		if (descriptor == null) {
			if (other.descriptor != null) {
				return false;
			}
		} else if (!descriptor.equals(other.descriptor)) {
			return false;
		}
		if (!Arrays.equals(exceptions, other.exceptions)) {
			return false;
		}
		if (methodName == null) {
			if (other.methodName != null) {
				return false;
			}
		} else if (!methodName.equals(other.methodName)) {
			return false;
		}
		if (signature == null) {
			if (other.signature != null) {
				return false;
			}
		} else if (!signature.equals(other.signature)) {
			return false;
		}
		return true;
	}

}
