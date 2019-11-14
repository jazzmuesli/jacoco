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

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jacoco.agent.rt.internal.IExceptionLogger;

/**
 * is class X relevant as a prod or test class?
 *
 * Load list of classes from jacoco-classes.txt or classes.txt
 *
 * @author preich
 *
 */
public class ClassAcceptor {
	public static final Set<String> relevantClasses = new HashSet<String>();
	private final String jacocoDestFilename;
	private final String packageName = TestMetricsCollector.class.getPackage()
			.getName();
	private final Logger logger;

	public static void main(final String[] args) {
		final ClassAcceptor acceptor = new ClassAcceptor("target/jacoco.exec",
				new Logger(new PrintWriter(System.out),
						IExceptionLogger.SYSTEM_ERR));
	}

	public ClassAcceptor(final String jacocoDestFilename, final Logger logger) {
		this.logger = logger;
		this.jacocoDestFilename = jacocoDestFilename;
		loadClasses(new File(
				jacocoDestFilename.replaceAll(".exec", "-classes.txt")));
		loadClasses(new File("classes.txt"));
		loadClassCSV(new File("."));
		logger.info("loaded " + relevantClasses.size() + " classes in total");
	}

	private void loadClassCSV(final File f) {
		if (f == null || !f.exists()) {
			return;
		}
		f.listFiles(new FileFilter() {

			@Override
			public boolean accept(final File pathname) {
				if (pathname.isDirectory()) {
					loadClassCSV(pathname);
				}
				if (pathname.getName().contains("class.csv")) {
					CSVParser parser;
					try {
						parser = CSVParser.parse(pathname,
								Charset.defaultCharset(),
								CSVFormat.DEFAULT.withDelimiter(';')
										.withFirstRecordAsHeader());
						Integer classId = parser.getHeaderMap().get("class");
						if (classId == null) {
							logger.info(
									"class not found in semicolon-separated "
											+ parser.getHeaderMap().keySet());
							parser = CSVParser.parse(pathname,
									Charset.defaultCharset(), CSVFormat.DEFAULT
											.withFirstRecordAsHeader());
							classId = parser.getHeaderMap().get("class");
							logger.info("Loading " + pathname
									+ " as comma-separated, found class as "
									+ classId);
						} else {
							logger.info("Loading " + pathname
									+ " as semicolon-separated");
						}
						final List<CSVRecord> records = parser.getRecords();
						int i = 0;
						for (final CSVRecord record : records) {
							final String className = record.get("class");
							if (className != null
									&& !className.trim().isEmpty()) {
								if (!relevantClasses.add(className)) {
									i++;
								}
							}

						}
						logger.info(
								"Loaded " + i + " classes from " + pathname);
						parser.close();
					} catch (final Exception e) {
						logger.logExeption(e);
					}

				}
				return false;
			}
		});
	}

	private void loadClasses(final File f) {
		if (f.exists()) {
			Scanner scnr;
			try {
				scnr = new Scanner(new FileInputStream(f));
				int i = 0;
				while (scnr.hasNextLine()) {
					final String name = scnr.nextLine();
					relevantClasses.add(name.trim());
					i++;
				}
				scnr.close();
				logger.info("Loaded " + i + " classes from " + f);
			} catch (final Exception e) {
				logger.logExeption(e);
			}
		} else {
			logger.info("file " + f + " not present");
		}
	}

	/**
	 * consider all classes from this package to be relvant.
	 *
	 * @param ownerClassName
	 * @return
	 */
	public boolean isClassRelevant(final String ownerClassName) {
		final String replacedName = ownerClassName.replace('/', '.');
		final boolean isClassRelevant = replacedName.startsWith(packageName);
		final String withoutInnerClass = replacedName.replaceAll("\\$.*", "");
		return isClassRelevant || relevantClasses.contains(withoutInnerClass);
	}

}
