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

class VisitClassRecord extends VisitRecord {
	int version;
	int access;
	String name;
	String signature;
	String superName;
	String[] interfaces;

	public VisitClassRecord(final int version, final int access,
			final String name, final String signature, final String superName,
			final String[] interfaces) {
		this.version = version;
		this.access = access;
		this.name = name;
		this.signature = signature;
		this.superName = superName;
		this.interfaces = interfaces;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + access;
		result = prime * result + Arrays.hashCode(interfaces);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((signature == null) ? 0 : signature.hashCode());
		result = prime * result
				+ ((superName == null) ? 0 : superName.hashCode());
		result = prime * result + version;
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
		final VisitClassRecord other = (VisitClassRecord) obj;
		if (access != other.access) {
			return false;
		}
		if (!Arrays.equals(interfaces, other.interfaces)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (signature == null) {
			if (other.signature != null) {
				return false;
			}
		} else if (!signature.equals(other.signature)) {
			return false;
		}
		if (superName == null) {
			if (other.superName != null) {
				return false;
			}
		} else if (!superName.equals(other.superName)) {
			return false;
		}
		if (version != other.version) {
			return false;
		}
		return true;
	}

}
