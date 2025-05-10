package cn.izualzhy.service.impl;

import cn.izualzhy.dao.MyBatisUserDao;
import cn.izualzhy.pojo.User;
import cn.izualzhy.service.MyBatisUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyBatisUserServiceImpl implements MyBatisUserService {
   @Autowired
   private MyBatisUserDao myBatisUserDao = null;

   @Override
   public User getUser(Long id) {
      return myBatisUserDao.getUser(id);
   }
}