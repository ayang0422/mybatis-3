/*
 *    Copyright 2009-2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.zheteng;

import com.zheteng.dao.UserMapper;
import com.zheteng.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.UUID;

/**
 * @author yangpeng
 * @version 1.0.0
 * @date 2022年03月25日
 */
@Slf4j
public class ATest {

  @Test
  public void testConnect() throws Exception {
    String resource = "mybatis-config.xml";
    InputStream inputStream = Resources.getResourceAsStream(resource);
    // 此时创建了一个sqlSessionFactory对象---> DefaultSqlSessionFactory对象
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    try (SqlSession session = sqlSessionFactory.openSession()) {
      // 第一种方式：通过mapper配置
      UserInfo info = session.selectOne("com.zheteng.UserMapper.selectUser", 1);
      log.info("xml---->{}",info);

      UserInfo info2 = session.selectOne("com.zheteng.UserMapper.selectUser", 1);
      log.info("是否使用了缓存---->{}",info == info2);

      session.update("com.zheteng.UserMapper.updateNickname", UUID.randomUUID().toString().replaceAll("-", ""));
      UserInfo info3 = session.selectOne("com.zheteng.UserMapper.selectUser", 1);
      log.info("更新后是否使用了缓存---->{}",info == info3);


      SqlSession session2 = sqlSessionFactory.openSession();
      UserInfo info4 = session2.selectOne("com.zheteng.UserMapper.selectUser", 1);
      log.info("不同session之间查询是否使用了缓存---->{}",info4 == info3);

      // 第二种方式，通过class文件
      // 第二种方法有很多优势，首先它不依赖于字符串字面值，会更安全一点；
      // 其次，如果你的 IDE 有代码补全功能，那么代码补全可以帮你快速选择到映射好的 SQL 语句。
      /*
      摘自mybatis官网：

      使用注解来映射简单语句会使代码显得更加简洁，但对于稍微复杂一点的语句，
      Java 注解不仅力不从心，还会让你本就复杂的 SQL 语句更加混乱不堪。
      因此，如果你需要做一些很复杂的操作，最好用 XML 来映射语句。
       */
      UserMapper mapper = session.getMapper(UserMapper.class);
      UserInfo user = mapper.selectById(1);
      log.info("class---->{}",user);

      SQL sql = new SQL().FROM("user_info").SELECT("name").WHERE("id=1");
    }
  }
}
