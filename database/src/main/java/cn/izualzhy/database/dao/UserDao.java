package cn.izualzhy.database.dao;

import cn.izualzhy.database.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
   public User getUser(Long id);
   public void insertUser(User user);
}