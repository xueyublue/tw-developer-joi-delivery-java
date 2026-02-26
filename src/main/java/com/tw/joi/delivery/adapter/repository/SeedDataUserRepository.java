package com.tw.joi.delivery.adapter.repository;

import com.tw.joi.delivery.domain.User;
import com.tw.joi.delivery.port.UserRepository;
import com.tw.joi.delivery.seedData.SeedData;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class SeedDataUserRepository implements UserRepository {

    private final List<User> users = SeedData.users;

    @Override
    public User findById(String userId) {
        return users.stream()
            .filter(user -> userId.equals(user.getUserId()))
            .findFirst()
            .orElse(null);
    }

}
