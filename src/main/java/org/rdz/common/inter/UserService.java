package org.rdz.common.inter;

import org.rdz.common.entity.User;

import java.util.List;

public interface UserService {
    User addUser(User user);

    int deleteUser(String id);

    List<User> getUsers(User user);

    User getUser(String id);
}
