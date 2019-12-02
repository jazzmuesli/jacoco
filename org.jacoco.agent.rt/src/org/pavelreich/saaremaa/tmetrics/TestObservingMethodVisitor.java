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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class TestObservingMethodVisitor extends MethodVisitor {

	private static final Set<String> ASSERT_CLASSES = new HashSet<String>(
			Arrays.asList("org.junit.Assert", "junit.framework.Assert",
					"org.hamcrest.Matchers", "org.hamcrest.MatcherAssert",
					"org.junit.jupiter.api.Assertions"));
	private static final Set<String> MOCKITO_CLASSES = new HashSet<String>(
			Arrays.asList("org.mockito.Mockito"));

	private final VisitClassRecord visitClassRecord;
	private final VisitMethodRecord visitMethodRecord;
	private int currentLine;
	private final ClassAcceptor relevantClassManager;

	public TestObservingMethodVisitor(final ClassAcceptor relevantClassManager,
			final VisitClassRecord visitClassRecord,
			final VisitMethodRecord visitMethodRecord,
			final MethodVisitor nextVisitor) {
		super(TestMetricsCollector.ASM_VERSION, nextVisitor);
		this.relevantClassManager = relevantClassManager;
		this.visitClassRecord = visitClassRecord;
		this.visitMethodRecord = visitMethodRecord;
	}

	@Override
	public void visitMethodInsn(final int opcode, final String owner,
			final String methodName, final String descriptor,
			final boolean isInterface) {
		final String ownerClassName = owner.replace('/', '.');
		if (ASSERT_CLASSES.contains(ownerClassName)) {
			TestMetricsCollector.occurences
					.add(new TAssert(getSourceLocation(), new TargetLocation(
							ownerClassName, methodName, descriptor)));
		}
		// junit3
		if (ownerClassName.equals(getSourceLocation().getClassName())
				&& methodName.toLowerCase().contains("assert")) {
			TestMetricsCollector.occurences
					.add(new TAssert(getSourceLocation(), new TargetLocation(
							ownerClassName, methodName, descriptor)));

		}
		if (MOCKITO_CLASSES.contains(ownerClassName)) {
			TestMetricsCollector.occurences.add(
					new TMockOperation(getSourceLocation(), new TargetLocation(
							ownerClassName, methodName, descriptor)));
		}
		final boolean isClassRelevant = relevantClassManager
				.isClassRelevant(ownerClassName);
		if (isClassRelevant) {
			if (opcode == Opcodes.INVOKEVIRTUAL
					|| opcode == Opcodes.INVOKESTATIC) {
				final SourceLocation sourceLocation = getSourceLocation();
				final TargetLocation targetLocation = new TargetLocation(
						ownerClassName, methodName, descriptor);
				TestMetricsCollector.occurences.add(
						new TInvok(sourceLocation, targetLocation, opcode));
			}
		}
		super.visitMethodInsn(opcode, owner, methodName, descriptor,
				isInterface);
	}

	protected SourceLocation getSourceLocation() {
		final SourceLocation sourceLocation = new SourceLocation(
				visitClassRecord, visitMethodRecord, currentLine);
		return sourceLocation;
	}

	@Override
	public String toString() {
		return visitClassRecord.name + "::" + visitMethodRecord.methodName;
	}

	@Override
	public void visitLineNumber(final int line, final Label start) {
		this.currentLine = line;
		super.visitLineNumber(line, start);
	}

	@Override
	public void visitTypeInsn(final int opcode, final String type) {
		if (relevantClassManager.isClassRelevant(type)
				&& opcode == Opcodes.NEW) {
			TestMetricsCollector.occurences
					.add(new TData(getSourceLocation(), type));
		}
		super.visitTypeInsn(opcode, type);
	}

}
