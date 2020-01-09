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

class SourceLocation {

	private final VisitClassRecord visitClassRecord;
	private final VisitMethodRecord visitMethodRecord;
	private final int currentLine;

	public SourceLocation(final VisitClassRecord visitClassRecord,
			final VisitMethodRecord visitMethodRecord, final int currentLine) {
		this.visitClassRecord = visitClassRecord;
		this.visitMethodRecord = visitMethodRecord;
		this.currentLine = currentLine;
	}

	@Override
	public String toString() {
		return visitClassRecord.name + ":" + getMethodName() + ":"
				+ currentLine;
	}

	public Document toDocument() {
		Document doc = new Document("type", getClass().getName())
				.append("className", getClassName())
				.append("methodName", getMethodName())
				.append("line", currentLine);
		if (visitMethodRecord.signature != null) {
			doc = doc.append("methodSignature", visitMethodRecord.signature);
		}
		return doc;
	}

	String getMethodName() {
		return visitMethodRecord.methodName;
	}

	String getClassName() {
		return visitClassRecord.name.replace('/', '.');
	}

	int getCurrentLine() {
		return currentLine;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + currentLine;
		result = prime * result + ((visitClassRecord == null) ? 0
				: visitClassRecord.hashCode());
		result = prime * result + ((visitMethodRecord == null) ? 0
				: visitMethodRecord.hashCode());
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
		final SourceLocation other = (SourceLocation) obj;
		if (currentLine != other.currentLine) {
			return false;
		}
		if (visitClassRecord == null) {
			if (other.visitClassRecord != null) {
				return false;
			}
		} else if (!visitClassRecord.equals(other.visitClassRecord)) {
			return false;
		}
		if (visitMethodRecord == null) {
			if (other.visitMethodRecord != null) {
				return false;
			}
		} else if (!visitMethodRecord.equals(other.visitMethodRecord)) {
			return false;
		}
		return true;
	}

}
