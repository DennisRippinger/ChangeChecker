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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.BooleanUtils.isFalse;

@Slf4j
public class PageChecker {

	private final FileHelper fileHelper;

	public PageChecker(FileHelper fileHelper) {
		this.fileHelper = fileHelper;
	}

	public void checkPage(Properties properties, String baseProperty) throws IOException {
		Document newDocument = fileHelper.getCurrentDocument(baseProperty);
		Document oldDocument = fileHelper.getCompareDocument(baseProperty);

		String cssSelector = properties.getProperty(baseProperty + "cssSelector");

		Elements newSelect = newDocument.body().select(cssSelector);
		Elements oldSelect = oldDocument.body().select(cssSelector);

		// TODO Check if newSelect is empty -> Page has changed completely

		List<Element> newElements = newSelect.stream()
				.filter(x -> isFalse(oldSelect.contains(x)))
				.collect(Collectors.toList());

		if (isNotEmpty(newElements)) {
			log.info("Found new elements:");
			newElements.stream()
					.forEach(x -> log.info(x.text()));

			File fileOldDocument = fileHelper.getCompareFile(baseProperty);
			File fileOldDocumentArchive = fileHelper.getArchiveFile(baseProperty);

			fileHelper.moveToArchive(fileOldDocument, fileOldDocumentArchive);

			FileUtils.writeStringToFile(fileOldDocument, newDocument.html(), Charset.forName("UTF-8"));
		}
	}
}
