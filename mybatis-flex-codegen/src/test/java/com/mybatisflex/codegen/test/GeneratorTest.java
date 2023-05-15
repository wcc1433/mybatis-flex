/**
 * Copyright (c) 2022-2023, Mybatis-Flex (fuhai999@gmail.com).
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
package com.mybatisflex.codegen.test;

import com.mybatisflex.codegen.Generator;
import com.mybatisflex.codegen.config.ColumnConfig;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.mybatisflex.codegen.config.TableConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.Test;

public class GeneratorTest {


    //   @Test
    public void testGenerator() {
        //配置数据源
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/jbootadmin?characterEncoding=utf-8");
        //        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/hh-vue?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&rewriteBatchedStatements=true&allowMultiQueries=true");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");

        //        JdbcTypeMapping.registerMapping(BigInteger.class, Long.class);
        //        JdbcTypeMapping.registerMapping(Integer.class, Long.class);

        GlobalConfig globalConfig = new GlobalConfig();

        //设置生成文件目录和根包
        globalConfig.getPackageConfig()
                .setSourceDir(System.getProperty("user.dir") + "/src/test/java")
                .setBasePackage("com.test");

        //设置只生成哪些表
        globalConfig.getStrategyConfig()
                .addGenerateTable("account", "account_session");

        //设置生成 entity
        globalConfig.enableEntity()
                .setWithLombok(true)
                .setClassPrefix("My")
                .setClassSuffix("Entity")
                .setSupperClass(BaseEntity.class);

        //设置生成 tableDef
        globalConfig.enableTableDef();

        //设置生成 mapper
        globalConfig.enableMapper()
                .setClassPrefix("Flex")
                .setClassSuffix("Dao")
                .setSupperClass(MyBaseMapper.class);

        TableConfig tableConfig = new TableConfig();
        tableConfig.setTableName("account");
        tableConfig.setUpdateListenerClass(MyUpdateListener.class);
        globalConfig.getStrategyConfig().addTableConfig(tableConfig);


        //可以单独配置某个列
        ColumnConfig columnConfig = new ColumnConfig();
        columnConfig.setColumnName("tenant_id");
        columnConfig.setLarge(true);
        columnConfig.setVersion(true);
        globalConfig.getStrategyConfig().addColumnConfig("account", columnConfig);


        //通过 datasource 和 globalConfig 创建代码生成器
        Generator generator = new Generator(dataSource, globalConfig);

        //开始生成代码
        generator.generate();
    }

    @Test
    public void testCodeGen() {
        //配置数据源
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test?characterEncoding=utf-8");
        dataSource.setUsername("root");
        dataSource.setPassword("12345678");

        GlobalConfig globalConfig = new GlobalConfig();

        //设置生成文件目录和根包
        globalConfig.getPackageConfig()
                .setSourceDir(System.getProperty("user.dir") + "/src/test/java")
                .setBasePackage("com.test");

        //设置表前缀和只生成哪些表
        globalConfig.getStrategyConfig()
                .setTablePrefix("sys_")
                .addGenerateTable("sys_user");

        //配置生成 entity
        globalConfig.enableEntity()
                .setWithLombok(true)
                .setSupperClass(BaseEntity.class);

        //配置生成 mapper
        globalConfig.enableMapper();
        //配置生成 service
        globalConfig.enableService();
        //配置生成 serviceImpl
        globalConfig.enableServiceImpl();
        //配置生成 controller
        globalConfig.enableController();
        //配置生成 tableDef
        globalConfig.enableTableDef();

        //通过 datasource 和 globalConfig 创建代码生成器
        Generator generator = new Generator(dataSource, globalConfig);

        //开始生成代码
        generator.generate();
    }

}
