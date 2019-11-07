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

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

class TestObservingClassVisitor extends ClassVisitor {
	private VisitClassRecord visitClassRecord;
	private final ClassAcceptor relevantClassManager;

	public TestObservingClassVisitor(final ClassVisitor nextVisitor,
			final ClassAcceptor relevantClassManager) {
		super(TestMetricsCollector.ASM_VERSION, nextVisitor);
		this.relevantClassManager = relevantClassManager;

	}

	@Override
	public FieldVisitor visitField(final int access, final String name,
			final String descriptor, final String signature,
			final Object value) {
		return new TestObservingFieldVisitor(visitClassRecord,
				new VisitFieldRecord(access, name, descriptor, signature,
						value),
				super.visitField(access, name, descriptor, signature, value));
	}

	@Override
	public void visit(final int version, final int access, final String name,
			final String signature, final String superName,
			final String[] interfaces) {
		this.visitClassRecord = new VisitClassRecord(version, access, name,
				signature, superName, interfaces);
		super.visit(version, access, name, signature, superName, interfaces);
	}

	@Override
	public void visitEnd() {
		this.visitClassRecord = null;
		super.visitEnd();
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String methodName,
			final String descriptor, final String signature,
			final String[] exceptions) {
		final MethodVisitor nextVisitor = super.visitMethod(access, methodName,
				descriptor, signature, exceptions);
		final VisitMethodRecord visitMethodRecord = new VisitMethodRecord(
				access, methodName, descriptor, signature, exceptions);
		return new TestObservingMethodVisitor(relevantClassManager,
				visitClassRecord, visitMethodRecord, nextVisitor);
	}

}
