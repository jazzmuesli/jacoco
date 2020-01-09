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

public class TAssert extends TestingArtifact {

	private final SourceLocation sourceLocation;
	private final TargetLocation targetLocation;

	public TAssert(final SourceLocation sourceLocation,
			final TargetLocation targetLocation) {
		this.sourceLocation = sourceLocation;
		this.targetLocation = targetLocation;

	}

	@Override
	public Document toDocument() {
		return super.toDocument()
				.append("sourceLocation", sourceLocation.toDocument())
				.append("targetLocation", targetLocation.toDocument());
	}

	public SourceLocation getSourceLocation() {
		return sourceLocation;
	}

	public TargetLocation getTargetLocation() {
		return targetLocation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((sourceLocation == null) ? 0 : sourceLocation.hashCode());
		result = prime * result
				+ ((targetLocation == null) ? 0 : targetLocation.hashCode());
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
		final TAssert other = (TAssert) obj;
		if (sourceLocation == null) {
			if (other.sourceLocation != null) {
				return false;
			}
		} else if (!sourceLocation.equals(other.sourceLocation)) {
			return false;
		}
		if (targetLocation == null) {
			if (other.targetLocation != null) {
				return false;
			}
		} else if (!targetLocation.equals(other.targetLocation)) {
			return false;
		}
		return true;
	}

}
