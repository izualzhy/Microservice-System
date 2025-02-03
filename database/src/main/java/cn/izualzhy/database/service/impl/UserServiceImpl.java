package cn.izualzhy.database.service.impl;

import cn.izualzhy.database.dao.UserDao;
import cn.izualzhy.database.dic.SexEnum;
import cn.izualzhy.database.pojo.User;
import cn.izualzhy.database.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User getUser(Long id) {
        return userDao.getUser(id);
    }

    @Override
    @Transactional
    public int insertUser(User user) {
        userDao.insertUser(new User(
                null,
                "mock-" + user.getUserName(),
                SexEnum.MALE,
                "mock-" + user.getNote()
        ));

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        userDao.insertUser(user);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        int a = 1/0;

        return a;
    }
}
