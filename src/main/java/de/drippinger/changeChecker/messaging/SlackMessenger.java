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
package de.drippinger.changeChecker.messaging;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.helper.Validate;

import java.io.IOException;
import java.util.Properties;

@Slf4j
public class SlackMessenger {

	private SlackSession session;

	public SlackMessenger(Properties config) {

		session = SlackSessionFactory.createWebSocketSlackSession(config.getProperty("slack.oauth"));
		try {
			session.connect();
		} catch (IOException e) {
			log.error("Could not connect to Slack", e.getMessage());
		}
	}

	public void sendMessageToAChannel(String message) {
		Validate.notNull(session, "No Slack session available");

		SlackChannel channel = session.findChannelByName("general");
		session.sendMessage(channel, message);
	}
}
