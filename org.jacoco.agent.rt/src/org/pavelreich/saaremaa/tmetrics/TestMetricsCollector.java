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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.BsonValueCodecProvider;
import org.bson.codecs.DocumentCodecProvider;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.IterableCodec;
import org.bson.codecs.IterableCodecProvider;
import org.bson.codecs.ValueCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.json.JsonWriter;
import org.bson.json.JsonWriterSettings;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

/**
 * load list of prod/test classes to analyse from jacoco-classes.txt or
 * classes.txt
 *
 * collect test metrics:
 * <ul>
 * <li>TDATA new objects of relevant classes created</li>
 * <li>TINVOK invocations of prod/test classes.</li>
 * <li>TASSERT calls to org.junit.Assert, hamcrest, etc.</li>
 * <li>TMockField Mock fields</li>
 * <li>TMockOperation Mockito operations</li>
 * </ul>
 *
 * dump results to jacoco-result.json along the way log to jacoco.log
 *
 * @author preich
 *
 */
public class TestMetricsCollector {
	public static final Collection<TestingArtifact> occurences = new LinkedHashSet<TestingArtifact>();
	public static final int ASM_VERSION = Opcodes.ASM7;

	public static final Set<String> relevantClasses = new HashSet<String>();
	public static String baseFileName = "banana.exec";
	private static Logger LOG = Logger.defaultLogger();

	public static void main(final String[] args) {
		ClassReader reader;
		try {
			reader = new ClassReader(TestMetricsCollector.class.getName());
			final ClassVisitor visitor = TestMetricsCollector
					.provideClassVisitor(null, baseFileName, LOG);
			reader.accept(visitor, 0);
			final List<Document> dumpTestingArtifacts = dumpTestingArtifacts();
			for (final Document ta : dumpTestingArtifacts) {
				System.out.println(ta);
			}
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static List<Document> dumpTestingArtifacts()
			throws FileNotFoundException {
		final String fname = baseFileName.replaceAll(".exec", "-result.json");
		final PrintWriter jsonWriter = new PrintWriter(fname);
		LOG.info("dumpTestingArtifacts: " + fname);
		final JsonWriterSettings writerSettings = JsonWriterSettings.builder()
				.indent(true).build();
		final List<Document> docs = new ArrayList<Document>();
		for (final TestingArtifact s : occurences) {
			docs.add(s.toDocument());
		}
		LOG.info("docs: " + docs.size());

		final BsonTypeClassMap bsonTypeClassMap = new BsonTypeClassMap();
		final CodecRegistry registry = CodecRegistries.fromProviders(
				new ValueCodecProvider(), new DocumentCodecProvider(),
				new BsonValueCodecProvider(), new IterableCodecProvider());
		final IterableCodec codec = new IterableCodec(registry,
				bsonTypeClassMap);
		final BsonWriter bsonWriter = new JsonWriter(jsonWriter,
				writerSettings);
		bsonWriter.writeStartDocument();
		bsonWriter.writeStartArray("results");
		codec.encode(bsonWriter, docs, EncoderContext.builder().build());
		bsonWriter.writeEndArray();
		bsonWriter.writeEndDocument();

		bsonWriter.flush();
		jsonWriter.close();
		return docs;
	}

	/**
	 * provide ClassVisitor that collects test metrics and delegates to
	 * nextVisitor.
	 *
	 * @param nextVisitor
	 * @param jacocoDestFilename
	 * @param logger
	 * @return
	 */
	public static ClassVisitor provideClassVisitor(
			final ClassVisitor nextVisitor, final String jacocoDestFilename,
			final Logger logger) {
		try {
			logger.info("provideClassVisitor.jacocoDestFilename="
					+ jacocoDestFilename);
			TestMetricsCollector.baseFileName = jacocoDestFilename;
			TestMetricsCollector.LOG = logger;
			return new TestObservingClassVisitor(nextVisitor,
					new ClassAcceptor(jacocoDestFilename, logger));

		} catch (final Exception e) {
			logger.logExeption(e);
			return nextVisitor;
		}
	}

}
