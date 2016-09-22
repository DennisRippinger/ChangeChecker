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

import org.jsoup.Jsoup;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PageCheckerTest {

	private static final String BASE_STRING = "testProject";

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Mock
	private FileHelper fileHelper;

	private Properties properties;

	private PageChecker pageChecker;

	@Before
	public void setUp() throws IOException {
		properties = new Properties();
		properties.put(BASE_STRING + ".cssSelector", "tbody tr th:nth-child(2)");

		File fileTableA = new File(this.getClass().getResource("/Table_A.html").getFile());
		File fileTableB = new File(this.getClass().getResource("/Table_B.html").getFile());

		when(fileHelper.getCompareDocument(any())).thenReturn(Jsoup.parse(fileTableA, null));
		when(fileHelper.getCurrentDocument(any())).thenReturn(Jsoup.parse(fileTableB, null));
		when(fileHelper.getCompareFile(any())).thenReturn(temporaryFolder.newFile("compare_file.html"));

		pageChecker = new PageChecker(fileHelper, properties);
	}

	@Test
	public void test_checkPage() throws Exception {
		pageChecker.checkPage(BASE_STRING);

	}
}
