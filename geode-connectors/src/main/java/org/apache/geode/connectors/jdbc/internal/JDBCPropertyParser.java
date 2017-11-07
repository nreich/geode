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

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

class JDBCPropertyParser {
  private final Properties properties;

  private static final String PROPERTY_PREFIX_SEPARATOR = "-";
  private static final String COMPOUND_VALUE_SEPARATOR = ":";


  JDBCPropertyParser(Properties properties) {
    this.properties = properties;
  }

  <V> Map<String, V> getPropertiesMap(String propertyPrefix, Function<String, V> valueParser) {
    Map<String, V> map = new HashMap<>();
    for(String propertyName : properties.stringPropertyNames()) {
      if (propertyName.startsWith(propertyPrefix)) {
        V value = valueParser.apply(properties.getProperty(propertyName));
        int prefixEnd = propertyName.indexOf(PROPERTY_PREFIX_SEPARATOR);
        String key = propertyName.substring(prefixEnd + 1);
        map.put(key, value);
      }
    }
    return map;
  }
}
