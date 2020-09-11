package org.rdz.custom;

import org.rdz.common.entity.User;
import org.rdz.common.inter.UserService;
import org.rdz.netty.client.Client;

import java.util.List;

public class MyCustom {
    public static void main(String[] args) {
        Client client = new Client();
        client.startClient();

        UserService userService = (UserService) client.getBean(UserService.class);
        List<User> user = userService.getUsers(null);
        System.out.println(user);
        List<User> user2 = userService.getUsers(null);
        System.out.println(user2);
    }
}
