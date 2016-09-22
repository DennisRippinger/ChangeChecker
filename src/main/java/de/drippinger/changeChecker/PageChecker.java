/**
 * Copyright 2016 Dennis Rippinger
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
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
	private final Properties properties;

	public PageChecker(FileHelper fileHelper, Properties properties) {
		this.fileHelper = fileHelper;
		this.properties = properties;
	}

	public void checkPage(String baseProperty) throws IOException {
		Document newDocument = fileHelper.getCurrentDocument(baseProperty);
		Document oldDocument = fileHelper.getCompareDocument(baseProperty);

		String cssSelector = properties.getProperty(baseProperty + ".cssSelector");

		Elements newSelect = newDocument.body().select(cssSelector);
		Elements oldSelect = oldDocument.body().select(cssSelector);

		// TODO Check if newSelect is empty -> Page has changed completely

		List<String> newElements = getDifferences(newSelect, oldSelect);

		if (isNotEmpty(newElements)) {
			log.info("Found new elements:");
			newElements.stream()
				.forEach(log::info);

			File fileOldDocument = fileHelper.getCompareFile(baseProperty);
			File fileOldDocumentArchive = fileHelper.getArchiveFile(baseProperty);

			fileHelper.moveToArchive(fileOldDocument, fileOldDocumentArchive);

			FileUtils.writeStringToFile(fileOldDocument, newDocument.html(), Charset.forName("UTF-8"));
		}
	}

	private List<String> getDifferences(Elements newSelect, Elements oldSelect) {
		List<String> newSelectStrings = toTextList(newSelect);
		List<String> oldSelectStrings = toTextList(oldSelect);

		List<String> newElements = newSelectStrings
			.stream()
			.filter(x -> isFalse(oldSelectStrings.contains(x)))
			.collect(Collectors.toList());

		return newElements;
	}

	private List<String> toTextList(Elements select) {
		return select
			.stream()
			.map(x -> x.ownText())
			.collect(Collectors.toList());
	}
}
