package com.tw.joi.delivery.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.tw.joi.delivery.domain.User;
import com.tw.joi.delivery.port.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldFetchUserByIdFromRepository() {
        String userId = "user101";
        User user = User.builder()
            .userId(userId)
            .firstName("John")
            .lastName("Doe")
            .build();

        when(userRepository.findById(userId)).thenReturn(user);

        User result = userService.fetchUserById(userId);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
    }
}
