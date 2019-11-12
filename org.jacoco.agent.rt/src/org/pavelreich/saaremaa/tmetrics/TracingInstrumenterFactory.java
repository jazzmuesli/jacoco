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

import org.jacoco.agent.rt.internal.IExceptionLogger;
import org.jacoco.agent.rt.internal.InstrumenterFactory;
import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.internal.instr.ClassInstrumenter;
import org.jacoco.core.internal.instr.IProbeArrayStrategy;
import org.jacoco.core.runtime.AgentOptions;
import org.jacoco.core.runtime.IRuntime;
import org.objectweb.asm.ClassVisitor;

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
		Logger logger = Logger.provideLogger(exceptionLogger,
				jacocoDestFileName);
		final ClassVisitor metricsVisitor = TestMetricsCollector
				.provideClassVisitor(nextVisitor, jacocoDestFileName, logger);
		return metricsVisitor;
	}
}
