package cn.izualzhy.dao;

import cn.izualzhy.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MyBatisUserDao {
    public User getUser(Long id);
}
