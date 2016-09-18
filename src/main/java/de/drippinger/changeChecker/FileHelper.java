/**
 * Copyright 2016 Dennis Rippinger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.drippinger.changeChecker;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class FileHelper {

	private final Properties properties;

	private static String COMPARE_FILE = "compare_document.html";

	public FileHelper(Properties properties) {
		this.properties = properties;
	}

	public Document getCurrentDocument(String baseProperty) throws IOException {
		return Jsoup.connect(properties.getProperty(baseProperty + ".url")).get();
	}

	public Document getCompareDocument(String baseProperty) throws IOException {
		return Jsoup
			.connect(properties.getProperty(baseProperty + ".projectId")
				+ File.pathSeparator
				+ COMPARE_FILE).get();
	}

	public void moveToArchive(File fileOldDocument, File fileOldDocumentArchive) throws IOException {
		FileUtils.copyFile(fileOldDocument, fileOldDocumentArchive);
	}

	public File getCompareFile(String base) {
		return new File(base + File.pathSeparator + COMPARE_FILE);
	}

	public File getArchiveFile(String base) {
		String dateTime = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(Instant.now());

		return new File(base + File.pathSeparator + dateTime + ".html");
	}

}
