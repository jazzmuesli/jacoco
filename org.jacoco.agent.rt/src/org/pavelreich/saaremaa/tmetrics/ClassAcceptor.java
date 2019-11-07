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
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

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
	private final Logger logger;
	private final String jacocoDestFilename;
	private final String packageName = TestMetricsCollector.class.getPackage()
			.getName();

	public ClassAcceptor(final String jacocoDestFilename, final Logger logger) {
		this.logger = logger;
		this.jacocoDestFilename = jacocoDestFilename;
		loadClasses(new File(
				jacocoDestFilename.replaceAll(".exec", "-classes.txt")));
		loadClasses(new File("classes.txt"));
		logger.info("loaded " + relevantClasses.size() + " classes in total");
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
