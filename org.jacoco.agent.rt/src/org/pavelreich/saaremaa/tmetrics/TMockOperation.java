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

class TMockOperation extends TestingArtifact {

	private final SourceLocation sourceLocation;
	private final TargetLocation targetLocation;

	public TMockOperation(final SourceLocation sourceLocation,
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

	SourceLocation getSourceLocation() {
		return sourceLocation;
	}

	TargetLocation getTargetLocation() {
		return targetLocation;
	}

}
