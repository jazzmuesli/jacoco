/*******************************************************************************
 * Copyright (c) 2009, 2019 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Pavel Reich
 *
 *******************************************************************************/
package org.pavelreich.saaremaa.tmetrics;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;

class TestObservingFieldVisitor extends FieldVisitor {

	private final VisitClassRecord visitClassRecord;
	private final VisitFieldRecord visitFieldRecord;
	private static final String MOCK_CLASS = "org/mockito/Mock";

	public TestObservingFieldVisitor(final VisitClassRecord visitClassRecord,
			final VisitFieldRecord visitFieldRecord,
			final FieldVisitor fieldVisitor) {
		super(TestMetricsCollector.ASM_VERSION, fieldVisitor);
		this.visitClassRecord = visitClassRecord;
		this.visitFieldRecord = visitFieldRecord;
	}

	@Override
	public AnnotationVisitor visitAnnotation(final String descriptor,
			final boolean visible) {
		if (descriptor.contains(MOCK_CLASS)) {
			TestMetricsCollector.occurences
					.add(new TMockField(visitClassRecord, visitFieldRecord));
		}
		return super.visitAnnotation(descriptor, visible);
	}
}
