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

import org.junit.Test;

import java.util.Properties;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigEvaluatorTest {

	@Test
	public void test_GetBasicPropertyKeys() {

		// Given: Properties
		Properties properties = new Properties();

		properties.put("change.pageOne.1", "");
		properties.put("change.pageTwo.2", "");
		properties.put("change.pageThird.3", "");
		properties.put("something.else", "");

		ConfigEvaluator configEvaluator = new ConfigEvaluator();

		// When: evaluated
		Set<String> propertyKeys = configEvaluator.getBasicPropertyKeys(properties);

		// Then:
		assertThat(propertyKeys)
			.isNotNull()
			.isNotEmpty()
			.containsExactlyInAnyOrder("change.pageOne.", "change.pageTwo.", "change.pageThird.");

	}

}

