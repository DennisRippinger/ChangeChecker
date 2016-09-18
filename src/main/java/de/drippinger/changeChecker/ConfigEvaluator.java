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

import org.apache.commons.lang3.StringUtils;

import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConfigEvaluator {

	public static final String BASIC_PROPERTY = "change\\.[a-zA-Z0-9_]+\\.";

	/**
	 * Evaluates the give properties to a set of basic property paths. This allows to add
	 * additional properties that follow a pattern. In this case based on {@value BASIC_PROPERTY}
	 *
	 * @param properties standard property file
	 * @return base paths matching {@value BASIC_PROPERTY}
	 */
	public Set<String> getBasicPropertyKeys(Properties properties) {
		return properties
				.entrySet()
				.stream()
				.map(x -> extractMinimalProperty(x.getKey().toString()))
				.filter(x -> StringUtils.isNotEmpty(x))
				.collect(Collectors.toSet());
	}

	private String extractMinimalProperty(String property) {
		Pattern pattern = Pattern.compile(BASIC_PROPERTY);

		Matcher matcher = pattern.matcher(property);
		if (matcher.find()) {
			return matcher.group(0);
		}

		return StringUtils.EMPTY;
	}

}
