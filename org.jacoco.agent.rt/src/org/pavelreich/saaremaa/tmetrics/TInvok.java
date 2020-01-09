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
import org.jacoco.core.internal.instr.InstrSupport;

class TInvok extends TestingArtifact {

	private final SourceLocation sourceLocation;
	private final TargetLocation targetLocation;
	private final int opcode;

	public TInvok(final SourceLocation sourceLocation,
			final TargetLocation targetLocation, final int opcode) {
		this.sourceLocation = sourceLocation;
		this.targetLocation = targetLocation;
		this.opcode = opcode;
	}

	@Override
	public Document toDocument() {
		return super.toDocument()
				.append("sourceLocation", sourceLocation.toDocument())
				.append("targetLocation", targetLocation.toDocument())
				.append("opcode", opcode);
	}

	TargetLocation getTargetLocation() {
		return targetLocation;
	}

	SourceLocation getSourceLocation() {
		return sourceLocation;
	}

	boolean isJacocoInit() {
		return targetLocation != null && InstrSupport.INITMETHOD_NAME
				.equals(targetLocation.getMethodName());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + opcode;
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
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final TInvok other = (TInvok) obj;
		if (opcode != other.opcode) {
			return false;
		}
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
