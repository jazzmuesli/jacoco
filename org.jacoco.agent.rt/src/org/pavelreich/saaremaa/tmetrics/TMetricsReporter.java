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

import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jacoco.core.internal.instr.InstrSupport;

class TMetricsReporter {

	private final CSVPrinter printer;
	private String testClassName = null;
	private String prodClassName = null;

	TMetricsReporter(final String csvFname) throws IOException {
		this.printer = new CSVPrinter(new FileWriter(csvFname),
				CSVFormat.DEFAULT);
		printer.printRecord("testClassName", "testMethodName", "testClassLine",
				"metricType", "prodClassName", "prodMethodName");
	}

	static String getProdClassName(final String testClassName) {
		final String prodClassName = testClassName.replaceAll("_ESTest$", "")
				.replaceAll("Test$", "").replaceAll("\\.Test", ".");
		return prodClassName;
	}

	void process(final TestingArtifact s) throws IOException {
		if (s instanceof TInvok) {
			final TInvok tinvok = (TInvok) s;
			final SourceLocation sourceLocation = tinvok.getSourceLocation();
			final TargetLocation targetLocation = tinvok.getTargetLocation();
			if (tinvok.isJacocoInit()) {
				testClassName = targetLocation.getClassName();
				prodClassName = getProdClassName(testClassName);
			} else if (testClassName != null && isTest(testClassName)
					&& sourceLocation.getClassName().equals(testClassName)
					&& targetLocation.getClassName().equals(prodClassName)) {
				printer.printRecord(testClassName,
						sourceLocation.getMethodName(),
						sourceLocation.getCurrentLine(), "TINVOK",
						targetLocation.getClassName(),
						targetLocation.getMethodName());
			}

		} else if (s instanceof TAssert) {
			final TAssert tassert = (TAssert) s;
			final SourceLocation sourceLocation = tassert.getSourceLocation();
			if (sourceLocation.getClassName().contains("junit.framework")
					|| sourceLocation.getClassName().contains("org.junit")
					|| sourceLocation.getClassName().contains("org.hamcrest")) {
				return;
			}
			if (tassert.getTargetLocation().getMethodName()
					.equals(InstrSupport.INITMETHOD_NAME)) {
				return;
			}
			printer.printRecord(sourceLocation.getClassName(),
					sourceLocation.getMethodName(),
					sourceLocation.getCurrentLine(), "TASSERT",
					tassert.getTargetLocation().getClassName(),
					tassert.getTargetLocation().getMethodName());

		} else if (s instanceof TData) {
			final TData tData = (TData) s;
			final SourceLocation sourceLocation = tData.getSourceLocation();
			if (!sourceLocation.getClassName().endsWith("Test")) {
				return;
			}
			printer.printRecord(sourceLocation.getClassName(),
					sourceLocation.getMethodName(),
					sourceLocation.getCurrentLine(), "TDATA",
					tData.getDataType(), "");
		} else if (s instanceof TMockField) {
			final TMockField tMockField = (TMockField) s;
			printer.printRecord(tMockField.getClassName(), "", 0, "TMOCKFIELD",
					tMockField.getFieldType(), tMockField.getFieldName());
		} else if (s instanceof TMockOperation) {
			final TMockOperation tMockOperation = (TMockOperation) s;
			final SourceLocation sourceLocation = tMockOperation
					.getSourceLocation();
			final TargetLocation targetLocation = tMockOperation
					.getTargetLocation();
			if (tMockOperation.getTargetLocation().getMethodName()
					.equals(InstrSupport.INITMETHOD_NAME)
					|| sourceLocation.getClassName()
							.startsWith("org.mockito")) {
				return;
			}

			printer.printRecord(sourceLocation.getClassName(),
					sourceLocation.getMethodName(),
					sourceLocation.getCurrentLine(), "TMOCKOP",
					targetLocation.getClassName(),
					targetLocation.getMethodName());
		}
	}

	static boolean isTest(final String tcn) {
		return tcn.contains(".Test") || tcn.endsWith("Test")
				|| tcn.endsWith("TestCase");
	}

	public void flush() throws IOException {
		printer.flush();
	}
}
