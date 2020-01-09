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

class TData extends TestingArtifact {
	private final SourceLocation sourceLocation;
	private final String type;

	public TData(final SourceLocation sourceLocation, final String type) {
		this.sourceLocation = sourceLocation;
		this.type = type;
	}

	@Override
	public Document toDocument() {
		return super.toDocument()
				.append("sourceLocation", sourceLocation.toDocument())
				.append("dataType", type);
	}

	public SourceLocation getSourceLocation() {
		return sourceLocation;
	}

	String getDataType() {
		return type.replace('/', '.');
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((sourceLocation == null) ? 0 : sourceLocation.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final TData other = (TData) obj;
		if (sourceLocation == null) {
			if (other.sourceLocation != null) {
				return false;
			}
		} else if (!sourceLocation.equals(other.sourceLocation)) {
			return false;
		}
		if (type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!type.equals(other.type)) {
			return false;
		}
		return true;
	}

}
