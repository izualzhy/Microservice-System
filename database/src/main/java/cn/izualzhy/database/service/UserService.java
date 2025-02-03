package cn.izualzhy.database.service;

import cn.izualzhy.database.pojo.User;

public interface UserService {
    public User getUser(Long id);
    public int insertUser(User user);
}
