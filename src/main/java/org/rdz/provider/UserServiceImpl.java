package org.rdz.provider;

import org.rdz.common.entity.User;
import org.rdz.common.inter.UserService;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {
    @Override
    public User addUser(User user) {
        user.setId("123");
        return user;
    }

    @Override
    public int deleteUser(String id) {
        return 1;
    }

    @Override
    public List<User> getUsers(User user) {
        List<User> list = new ArrayList<>();
        list.add(new User().setId("123").setName("张三").setAge(20));
        list.add(new User().setId("456").setName("李四").setAge(20));
        return list;
    }

    @Override
    public User getUser(String id) {
        return new User().setId(id).setName("张三").setAge(20);
    }
}
