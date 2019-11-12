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

public class TMetricsReporter {

	private final CSVPrinter printer;
	private String testClassName = null;
	private String prodClassName = null;

	public TMetricsReporter(final String csvFname) throws IOException {
		this.printer = new CSVPrinter(new FileWriter(csvFname),
				CSVFormat.DEFAULT);
		printer.printRecord("testClassName", "testMethodName", "testClassLine",
				"metricType", "prodClassName", "prodMethodName");
	}

	void process(final TestingArtifact s) throws IOException {
		if (s instanceof TInvok) {
			final TInvok tinvok = (TInvok) s;
			final SourceLocation sourceLocation = tinvok.getSourceLocation();
			final TargetLocation targetLocation = tinvok.getTargetLocation();
			if (tinvok.isJacocoInit()) {
				testClassName = targetLocation.getClassName();
				prodClassName = testClassName.replaceAll("Test$", "");
			} else if (testClassName != null
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
			printer.printRecord(sourceLocation.getClassName(),
					sourceLocation.getMethodName(),
					sourceLocation.getCurrentLine(), "TDATA",
					tData.getDataType(), "");
		} else if (s instanceof TMockField) {
			final TMockField tMockField = (TMockField) s;
			printer.printRecord(tMockField.getClassName(), "", 0, "TMOCKFIELD",
					tMockField.getFieldType(), tMockField.getFieldName());
		} else if (s instanceof TMockOperation) {
			final TMockOperation tMockField = (TMockOperation) s;
			final SourceLocation sourceLocation = tMockField
					.getSourceLocation();
			final TargetLocation targetLocation = tMockField
					.getTargetLocation();
			if (tMockField.getTargetLocation().getMethodName()
					.equals(InstrSupport.INITMETHOD_NAME)
					|| sourceLocation.getClassName()
							.startsWith("org.mockito")) {
				return;
			}

			printer.printRecord(testClassName, sourceLocation.getMethodName(),
					sourceLocation.getCurrentLine(), "TMOCKOP",
					targetLocation.getClassName(),
					targetLocation.getMethodName());
		}
	}

	public void flush() throws IOException {
		printer.flush();
	}
}
