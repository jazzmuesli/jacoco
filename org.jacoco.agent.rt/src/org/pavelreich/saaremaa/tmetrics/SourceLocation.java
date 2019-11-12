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

}
