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
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FileHelperTest {

	private static final String BASE_STRING = "testProject";

	private Properties properties;

	private FileHelper fileHelper;


	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Before
	public void setUp() throws IOException {
		properties = new Properties();

		File compareDocument = new File(this.getClass().getResource("/compare_document.html").getFile());
		File testProjectFolder = temporaryFolder.newFolder("testProject");
		FileUtils.copyFileToDirectory(compareDocument, testProjectFolder);
		properties.put(BASE_STRING + ".projectId", testProjectFolder.getPath());

		fileHelper = new FileHelper(properties);
	}

	@Test
	public void test_getArchiveFile() throws Exception {
		File archiveFile = fileHelper.getArchiveFile(BASE_STRING);

		String date = DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now());

		assertThat(archiveFile)
			.isNotNull()
			.doesNotExist()
			.satisfies(x -> x.getName().startsWith(date));
	}

	@Test
	public void test_getCompareFile() throws Exception {

		Document document = fileHelper.getCompareDocument(BASE_STRING);
		Element testID = document.getElementById("simpleTestID");

		assertThat(testID.text())
			.isNotNull()
			.isNotEmpty()
			.isEqualTo("Simple Document for testing propose");
	}
}
