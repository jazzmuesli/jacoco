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

/**
 * simplistic logger, we can't really use slf4j here.
 *
 * @author preich
 *
 */
public class Logger implements IExceptionLogger {

	private final PrintWriter printWriter;
	private final IExceptionLogger exceptionLogger;

	public Logger(final PrintWriter printWriter,
			final IExceptionLogger exceptionLogger) {
		this.printWriter = printWriter;
		this.exceptionLogger = exceptionLogger;
	}

	public void info(final String msg) {
		printWriter.println(new Date() + " INFO " + msg);
	}

	public static Logger provideLogger(final IExceptionLogger exceptionLogger2,
			final String jacocoDestFileName) {
		FileWriter fw;
		try {
			fw = new FileWriter(jacocoDestFileName.replace(".exec", ".log"),
					true);
			final PrintWriter printWriter = new PrintWriter(fw);
			return new Logger(printWriter, exceptionLogger2);
		} catch (final IOException e) {
			exceptionLogger2.logExeption(e);
		}
		return new Logger(new PrintWriter(System.out), exceptionLogger2);
	}

	@Override
	public void logExeption(final Exception ex) {
		exceptionLogger.logExeption(ex);
	}

	public static Logger defaultLogger() {
		return new Logger(new PrintWriter(System.out),
				IExceptionLogger.SYSTEM_ERR);
	}

	public void flush() {
		printWriter.flush();
	}
}
