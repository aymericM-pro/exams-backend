package com.app.examproject.services;

import com.app.examproject.TestFixtures;
import com.app.examproject.domains.UserMapper;
import com.app.examproject.domains.dto.users.CreateUserRequest;
import com.app.examproject.domains.dto.users.UserResponse;
import com.app.examproject.domains.entities.Role;
import com.app.examproject.domains.entities.UserEntity;
import com.app.examproject.errors.BusinessException;
import com.app.examproject.errors.UserError;
import com.app.examproject.repositories.UserRepository;
import com.app.examproject.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserServiceImpl userService;

    UUID userId;

    @BeforeEach
    void setup() {
        userId = UUID.randomUUID();
    }

    @Test
    void testCreateUser() {
        CreateUserRequest request = TestFixtures.createUserRequest();
        UserEntity entity = TestFixtures.userEntity(userId);
        UserResponse expectedResponse = TestFixtures.userResponse(userId);

        when(userMapper.fromCreate(request)).thenReturn(entity);
        when(userRepository.save(entity)).thenReturn(entity);
        when(userMapper.toResponse(entity)).thenReturn(expectedResponse);

        UserResponse result = userService.create(request);

        assertThat(result).isNotNull();
        assertThat(entity.getRoles()).contains(Role.valueOf(request.role()));

        verify(userRepository).save(entity);
        verify(userMapper).toResponse(entity);
    }

    @Test
    void testGetUserById() {
        UserEntity entity = TestFixtures.userEntity(userId);

        when(userRepository.findById(userId))
                .thenReturn(java.util.Optional.of(entity));
        when(userMapper.toResponse(entity))
                .thenReturn(TestFixtures.userResponse(userId));

        UserResponse response = userService.getById(userId);

        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(userId);

        verify(userRepository).findById(userId);
    }

    @Test
    void testGetUserByIdThrowsExceptionWhenUserNotFound() {
        when(userRepository.findById(userId))
                .thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> userService.getById(userId))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getError())
                                .isEqualTo(UserError.USER_NOT_FOUND)
                );

        verify(userRepository).findById(userId);
        verifyNoInteractions(userMapper);
    }

    @Test
    void testUpdateUser() {
        UserEntity existing = TestFixtures.userEntity(userId);
        UserEntity updated = TestFixtures.userEntity(userId);
        UserResponse response = TestFixtures.userResponse(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existing));
        when(userMapper.fromUpdate(any())).thenReturn(updated);
        when(userRepository.save(updated)).thenReturn(updated);
        when(userMapper.toResponse(updated)).thenReturn(response);

        UserResponse result = userService.update(userId, TestFixtures.updateUserRequest());

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);

        verify(userRepository).save(updated);
    }

    @Test
    void testUpdateUserThrowsExceptionWhenUserNotFound() {
        when(userRepository.findById(userId))
                .thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() ->
                userService.update(userId, TestFixtures.updateUserRequest())
        )
                .isInstanceOf(BusinessException.class)
                .satisfies(ex ->
                        assertThat(((BusinessException) ex).getError())
                                .isEqualTo(UserError.USER_NOT_FOUND)
                );

        verify(userRepository).findById(userId);
        verifyNoInteractions(userMapper);
    }

    @Test
    void testDeleteUser() {
        when(userRepository.existsById(userId)).thenReturn(true);
        userService.delete(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void testDeleteUserThrowsExceptionWhenUserNotFound() {
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThatThrownBy(() -> userService.delete(userId))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex ->
                        assertThat(((BusinessException) ex).getError()).isEqualTo(UserError.USER_NOT_FOUND)
                );

        verify(userRepository).existsById(userId);
        verifyNoMoreInteractions(userRepository);
    }
}
