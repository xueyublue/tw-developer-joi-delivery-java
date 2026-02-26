package com.tw.joi.delivery.port;

import com.tw.joi.delivery.domain.User;

public interface UserRepository {

    User findById(String userId);

}
