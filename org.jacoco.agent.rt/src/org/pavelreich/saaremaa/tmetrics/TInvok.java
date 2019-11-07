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
}
