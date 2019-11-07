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

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import org.jacoco.agent.rt.internal.IExceptionLogger;
import org.jacoco.agent.rt.internal.InstrumenterFactory;
import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.internal.instr.ClassInstrumenter;
import org.jacoco.core.internal.instr.IProbeArrayStrategy;
import org.jacoco.core.runtime.AgentOptions;
import org.jacoco.core.runtime.IRuntime;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceClassVisitor;

public class TracingInstrumenterFactory implements InstrumenterFactory {
	private final String jacocoDestFileName;
	private final IExceptionLogger exceptionLogger;

	public TracingInstrumenterFactory(final AgentOptions agentOptions,
			final IExceptionLogger exceptionLogger) {
		this.jacocoDestFileName = agentOptions.getDestfile();
		this.exceptionLogger = exceptionLogger;
	}

	@Override
	public Instrumenter create(final IRuntime runtime) {
		return new Instrumenter(runtime) {
			@Override
			protected ClassInstrumenter createClassInstrumenter(
					final ClassVisitor writer,
					final IProbeArrayStrategy strategy) {
				final ClassVisitor visitor = chainVisitor(writer);
				return super.createClassInstrumenter(visitor, strategy);
			}

		};
	}

	protected ClassVisitor chainVisitor(final ClassVisitor nextVisitor) {
		FileWriter fw;
		try {
			final ClassVisitor metricsVisitor = TestMetricsCollector
					.provideClassVisitor(nextVisitor, jacocoDestFileName,
							exceptionLogger);
			fw = new FileWriter(jacocoDestFileName.replace(".exec", ".txt"),
					true);
			final PrintWriter printWriter = new PrintWriter(fw);
			printWriter.println("banana:" + new Date());
			final TraceClassVisitor visitor = new TraceClassVisitor(
					metricsVisitor, new Textifier(), printWriter);
			return visitor;
		} catch (final IOException e) {
			throw new IllegalStateException(e);
		}
	}
}
