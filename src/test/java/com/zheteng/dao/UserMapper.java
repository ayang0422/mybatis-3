package com.zheteng.dao;

import com.zheteng.entity.UserInfo;
import org.apache.ibatis.annotations.Select;

/**
 * @author yangpeng
 * @version 1.0.0
 * @date 2022年03月25日
 */
public interface UserMapper {

  @Select("select * from user_info where id = #{id}")
  UserInfo selectById(Integer id);
}
