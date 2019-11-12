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

class TMockField extends TestingArtifact {

	private final VisitClassRecord visitClassRecord;
	private final VisitFieldRecord visitFieldRecord;

	public TMockField(final VisitClassRecord visitClassRecord,
			final VisitFieldRecord visitFieldRecord) {
		this.visitClassRecord = visitClassRecord;
		this.visitFieldRecord = visitFieldRecord;
	}

	@Override
	public Document toDocument() {
		return super.toDocument().append("className", getClassName())
				.append("fieldName", getFieldName())
				.append("fieldType", getFieldType());
	}

	String getFieldType() {
		// transform to className
		// https://stackoverflow.com/questions/3442090/what-is-this-ljava-lang-object
		// I don't expect any other types
		final String type = visitFieldRecord.descriptor;
		return type.replaceAll("^L", "").replaceAll(";$", "").replace('/', '.');
	}

	String getFieldName() {
		return visitFieldRecord.name;
	}

	String getClassName() {
		return visitClassRecord.name.replace('/', '.');
	}
}
