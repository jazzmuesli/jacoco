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

import java.lang.instrument.ClassFileTransformer;

import org.jacoco.agent.rt.internal.CoverageTransformer;
import org.jacoco.agent.rt.internal.IExceptionLogger;
import org.jacoco.agent.rt.internal.InstrumenterFactory;
import org.jacoco.agent.rt.internal.PremainBuilder;
import org.jacoco.core.runtime.AgentOptions;
import org.jacoco.core.runtime.IRuntime;

/**
 * TODO: move it to my own jagent. gather test metrics
 *
 * @author preich
 *
 */
public final class TestMetricsGatheringPreMainBuilder extends PremainBuilder {
	public static final IExceptionLogger exceptionLogger = IExceptionLogger.SYSTEM_ERR;

	@Override
	public Runnable createShutdownAction() {
		if (true) {
			return null;
		} else {
			return new Runnable() {

				@Override
				public void run() {
					try {
						TestMetricsCollector.dumpTestingArtifacts();
					} catch (final Exception e) {
						exceptionLogger.logExeption(e);
					}
				}

			};
		}
	}

	@Override
	public ClassFileTransformer createTransformer(final IRuntime runtime,
			final AgentOptions agentOptions) {
		final InstrumenterFactory instrumenterFactory = new TracingInstrumenterFactory(
				agentOptions, exceptionLogger);
		final CoverageTransformer transformer = new CoverageTransformer(runtime,
				agentOptions, exceptionLogger, instrumenterFactory);
		return transformer;
	}
}
