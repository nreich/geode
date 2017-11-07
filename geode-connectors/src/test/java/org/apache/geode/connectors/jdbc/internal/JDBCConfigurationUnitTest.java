/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.geode.connectors.jdbc.internal;

import static org.assertj.core.api.Assertions.*;

import java.util.Properties;

import org.apache.geode.connectors.jdbc.internal.JDBCConfiguration;
import org.apache.geode.test.junit.categories.UnitTest;
import org.junit.*;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;

@Category(UnitTest.class)
public class JDBCConfigurationUnitTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void testMissingAllRequiredProperties() {
    Properties props = new Properties();
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("missing required properties: [url]");
    new JDBCConfiguration(props);
  }

  @Test
  public void testURLProperty() {
    Properties props = new Properties();
    props.setProperty("url", "myUrl");
    JDBCConfiguration config = new JDBCConfiguration(props);
    assertThat(config.getURL()).isEqualTo("myUrl");
  }

  @Test
  public void testDefaultUser() {
    Properties props = new Properties();
    props.setProperty("url", "");
    JDBCConfiguration config = new JDBCConfiguration(props);
    assertThat(config.getUser()).isNull();
  }

  @Test
  public void testDefaultPassword() {
    Properties props = new Properties();
    props.setProperty("url", "");
    JDBCConfiguration config = new JDBCConfiguration(props);
    assertThat(config.getPassword()).isNull();
  }

  @Test
  public void testUser() {
    Properties props = new Properties();
    props.setProperty("url", "");
    props.setProperty("user", "myUser");
    JDBCConfiguration config = new JDBCConfiguration(props);
    assertThat(config.getUser()).isEqualTo("myUser");
  }

  @Test
  public void testPassword() {
    Properties props = new Properties();
    props.setProperty("url", "");
    props.setProperty("password", "myPassword");
    JDBCConfiguration config = new JDBCConfiguration(props);
    assertThat(config.getPassword()).isEqualTo("myPassword");
  }

  @Test
  public void testDefaultValueClassName() {
    Properties props = new Properties();
    props.setProperty("url", "");
    JDBCConfiguration config = new JDBCConfiguration(props);
    assertThat(config.getValueClassName("foo")).isNull();
  }

  @Test
  public void testValueClassNameWithRegionNames() {
    Properties props = new Properties();
    props.setProperty("url", "");
    props.setProperty("valueClassName-reg1", "cn1");
    props.setProperty("valueClassName-reg2", "pack2.cn2");
    props.setProperty("valueClassName-foo", "myPackage.myDomainClass");
    JDBCConfiguration config = new JDBCConfiguration(props);
    assertThat(config.getValueClassName("foo")).isEqualTo("myPackage.myDomainClass");
    assertThat(config.getValueClassName("reg1")).isEqualTo("cn1");
    assertThat(config.getValueClassName("reg2")).isEqualTo("pack2.cn2");
  }

  @Test
  public void testDefaultIsKeyPartOfValue() {
    Properties props = new Properties();
    props.setProperty("url", "");
    JDBCConfiguration config = new JDBCConfiguration(props);
    assertThat(config.getIsKeyPartOfValue("foo")).isEqualTo(false);
  }

  @Test
  public void testIsKeyPartOfValueWithRegionNames() {
    Properties props = new Properties();
    props.setProperty("url", "");
    props.setProperty("isKeyPartOfValue-reg1", "true");
    props.setProperty("isKeyPartOfValue-reg2", "false");
    JDBCConfiguration config = new JDBCConfiguration(props);
    assertThat(config.getIsKeyPartOfValue("foo")).isEqualTo(false);
    assertThat(config.getIsKeyPartOfValue("reg1")).isEqualTo(true);
    assertThat(config.getIsKeyPartOfValue("reg2")).isEqualTo(false);
  }

  @Test
  public void testDefaultRegionToTableMap() {
    Properties props = new Properties();
    props.setProperty("url", "");
    JDBCConfiguration config = new JDBCConfiguration(props);
    assertThat(config.getTableForRegion("foo")).isEqualTo("foo");
  }

  @Test
  public void testRegionToTableMap() {
    Properties props = new Properties();
    props.setProperty("url", "");
    props.setProperty("regionToTable-reg1", "table1");
    props.setProperty("regionToTable-reg2", "table2");
    JDBCConfiguration config = new JDBCConfiguration(props);
    assertThat(config.getTableForRegion("reg1")).isEqualTo("table1");
    assertThat(config.getTableForRegion("reg2")).isEqualTo("table2");
  }

  @Test
  public void testDefaultFieldToColumnMap() {
    Properties props = new Properties();
    props.setProperty("url", "");
    JDBCConfiguration config = new JDBCConfiguration(props);
    assertThat(config.getColumnForRegionField("reg1", "field1")).isEqualTo("field1");
  }

  @Test
  public void testFieldToColumnMap() {
    Properties props = new Properties();
    props.setProperty("url", "");
    props.setProperty("fieldToColumn", "field1:column1");
    JDBCConfiguration config = new JDBCConfiguration(props);
    assertThat(config.getColumnForRegionField("reg1", "field1")).isEqualTo("column1");
  }

  @Test
  public void testFieldToColumnMapWithMoreThanOne() {
    Properties props = new Properties();
    props.setProperty("url", "");
    props.setProperty("fieldToColumn",
        "reg0:field2:othercolumn2, reg1:field1:column1, field2:column2, reg3:field1:othercolumn1");
    JDBCConfiguration config = new JDBCConfiguration(props);
    assertThat(config.getColumnForRegionField("reg1", "field1")).isEqualTo("column1");
    assertThat(config.getColumnForRegionField("reg3", "field1")).isEqualTo("othercolumn1");
    assertThat(config.getColumnForRegionField("reg0", "field2")).isEqualTo("othercolumn2");
    assertThat(config.getColumnForRegionField("regAny", "field2")).isEqualTo("column2");
    assertThat(config.getColumnForRegionField("regOther", "field2")).isEqualTo("column2");
  }

  @Test(expected = IllegalArgumentException.class)
  public void verifyDuplicateFieldThrows() {
    Properties props = new Properties();
    props.setProperty("url", "");
    props.setProperty("fieldToColumn", "field1:column1, field1:column2");
    new JDBCConfiguration(props);
  }

  @Test(expected = IllegalArgumentException.class)
  public void verifyDuplicateRegionFieldThrows() {
    Properties props = new Properties();
    props.setProperty("url", "");
    props.setProperty("fieldToColumn", "reg1:field1:column1, reg1:field1:column2");
    new JDBCConfiguration(props);
  }
}
