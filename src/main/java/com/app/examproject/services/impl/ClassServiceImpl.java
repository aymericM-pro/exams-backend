package com.app.examproject.services.impl;

import com.app.examproject.domains.dto.ClassMapper;
import com.app.examproject.domains.dto.classes.ClassResponse;
import com.app.examproject.domains.dto.classes.CreateClassRequest;
import com.app.examproject.domains.entities.ClassEntity;
import com.app.examproject.errors.BusinessException;
import com.app.examproject.errors.errors.ClassError;
import com.app.examproject.repositories.ClassRepository;
import com.app.examproject.services.ClassService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ClassServiceImpl implements ClassService {

    private final ClassRepository classRepository;
    private final ClassMapper classMapper;

    public ClassServiceImpl(
            ClassRepository classRepository,
            ClassMapper classMapper
    ) {
        this.classRepository = classRepository;
        this.classMapper = classMapper;
    }

    @Override
    public ClassResponse create(CreateClassRequest request) {
        validateCreate(request);

        ClassEntity entity = classMapper.fromCreate(request);
        ClassEntity saved = classRepository.save(entity);

        return classMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassResponse> getAll() {
        return classRepository.findAll()
                .stream()
                .map(classMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClassResponse getById(UUID id) {
        ClassEntity entity = classRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ClassError.CLASS_NOT_FOUND));

        return classMapper.toResponse(entity);
    }

    @Override
    public void delete(UUID id) {
        if (!classRepository.existsById(id)) {
            throw new BusinessException(ClassError.CLASS_NOT_FOUND);
        }
        classRepository.deleteById(id);
    }

    private void validateCreate(CreateClassRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            throw new BusinessException(ClassError.INVALID_REQUEST);
        }
    }
}
