package com.app.examproject.services.impl;

import com.app.examproject.controller.users.UserSearchParams;
import com.app.examproject.domains.UserMapper;
import com.app.examproject.domains.dto.users.CreateUserRequest;
import com.app.examproject.domains.dto.users.UpdateUserRequest;
import com.app.examproject.domains.dto.users.UserResponse;
import com.app.examproject.domains.entities.Role;
import com.app.examproject.domains.entities.UserEntity;
import com.app.examproject.commons.errors.BusinessException;
import com.app.examproject.commons.errors.errors.UserError;
import com.app.examproject.repositories.UserRepository;
import com.app.examproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Page<UserResponse> search(UserSearchParams params, Pageable pageable) {
        return userRepository.search(
                params.getRoleEnum(),
                params.getFirstname(),
                pageable
        ).map(userMapper::toResponse);
    }


    @Override
    public List<UserResponse> createMany(List<CreateUserRequest> requests) {
        List<UserEntity> users = requests.stream()
                .map(request -> {
                    UserEntity user = userMapper.fromCreate(request);
                    user.setRoles(Set.of(Role.valueOf(request.role())));
                    return user;
                })
                .toList();

        List<UserEntity> savedUsers = userRepository.saveAll(users);

        return savedUsers.stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse create(CreateUserRequest request) {
        UserEntity user = userMapper.fromCreate(request);
        user.setRoles(Set.of(Role.valueOf(request.role())));
        UserEntity saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    @Override
    public Page<UserResponse> getAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(UUID id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse update(UUID id, UpdateUserRequest request) {
        UserEntity existing = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));

        UserEntity updated = userMapper.fromUpdate(request);
        updated.setUserId(existing.getUserId());

        UserEntity saved = userRepository.save(updated);
        return userMapper.toResponse(saved);
    }

    @Override
    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new BusinessException(UserError.USER_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }
}
