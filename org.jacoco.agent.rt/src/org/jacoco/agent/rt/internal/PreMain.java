/*******************************************************************************
 * Copyright (c) 2009, 2019 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 *******************************************************************************/
package org.jacoco.agent.rt.internal;

import java.lang.instrument.Instrumentation;

/**
 * The agent which is referred as the <code>Premain-Class</code>. The agent
 * configuration is provided with the agent parameters in the command line.
 */
public final class PreMain {

	private PreMain() {
		// no instances
	}

	/**
	 * This method is called by the JVM to initialize Java agents.
	 *
	 * @param options
	 *            agent options
	 * @param inst
	 *            instrumentation callback provided by the JVM
	 * @throws Exception
	 *             in case initialization fails
	 */
	public static void premain(final String options, final Instrumentation inst)
			throws Exception {

		final PremainBuilder premainBuilder = new PremainBuilder();
		premainBuilder.premain(options, inst);
	}

}
