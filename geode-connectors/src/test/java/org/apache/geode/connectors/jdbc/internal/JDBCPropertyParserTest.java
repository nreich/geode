/*
 *
 *  * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 *  * agreements. See the NOTICE file distributed with this work for additional information regarding
 *  * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 *  * "License"); you may not use this file except in compliance with the License. You may obtain a
 *  * copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License
 *  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *  * or implied. See the License for the specific language governing permissions and limitations under
 *  * the License.
 *
 */
package org.apache.geode.connectors.jdbc.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import java.util.Map;
import java.util.Properties;

import org.junit.Test;

public class JDBCPropertyParserTest {
  @Test
  public void returnsEmptyMapIfNoPropertiesPresent() {
    Properties props = new Properties();
    JDBCPropertyParser parser = new JDBCPropertyParser(props);
    Map<String, String> map = parser.getPropertiesMap("test", v -> v);
    assertThat(map).isEmpty();
  }

  @Test
  public void returnsMapWithKeyValuePair() {
    Properties props = new Properties();
    props.setProperty("name-key", "value");
    JDBCPropertyParser parser = new JDBCPropertyParser(props);
    Map<String, String> map = parser.getPropertiesMap("name", v -> v);
    assertThat(map).hasSize(1).containsKey("key").containsValue("value");
  }

  @Test
  public void returnsMapWithMultipleKeyValuePairs() {
    Properties props = new Properties();
    props.setProperty("name-key1", "value1");
    props.setProperty("name-key2", "value2");
    JDBCPropertyParser parser = new JDBCPropertyParser(props);
    Map<String, String> map = parser.getPropertiesMap("name", v -> v);
    assertThat(map).hasSize(2).containsExactly(entry("key1", "value1"), entry("key2", "value2"));
  }

  @Test
  public void returnsMapWithBooleanValues() {
    Properties props = new Properties();
    props.setProperty("name-key1", "true");
    props.setProperty("name-key2", "false");
    JDBCPropertyParser parser = new JDBCPropertyParser(props);
    Map<String, Boolean> map = parser.getPropertiesMap("name", Boolean::parseBoolean);
    assertThat(map).hasSize(2).containsExactly(entry("key1", true), entry("key2", false));
  }
}