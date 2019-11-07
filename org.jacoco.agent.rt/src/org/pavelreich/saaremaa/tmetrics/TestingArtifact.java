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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.Document;

public class TestingArtifact {
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public Document toDocument() {
		final Document d = new Document("type", getClass().getName())
				.append("metricType", getClass().getSimpleName());
		return d;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof TestingArtifact) {
			return EqualsBuilder.reflectionEquals(toDocument(),
					((TestingArtifact) obj).toDocument());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(toDocument());
	}

}
