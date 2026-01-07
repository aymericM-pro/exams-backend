package com.app.examproject.services.impl;

import com.app.examproject.domains.ClassMapper;
import com.app.examproject.domains.dto.classes.ClassResponse;
import com.app.examproject.domains.dto.classes.CreateClassRequest;
import com.app.examproject.domains.entities.ClassEntity;
import com.app.examproject.domains.entities.UserEntity;
import com.app.examproject.errors.BusinessException;
import com.app.examproject.errors.errors.ClassError;
import com.app.examproject.errors.errors.UserError;
import com.app.examproject.repositories.ClassRepository;
import com.app.examproject.repositories.UserRepository;
import com.app.examproject.services.ClassService;
import org.springframework.data.repository.query.ListQueryByExampleExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ClassServiceImpl implements ClassService {

    private final ClassRepository classRepository;
    private final ClassMapper classMapper;
    private final UserRepository userRepository;

    public ClassServiceImpl(
            ClassRepository classRepository,
            ClassMapper classMapper,
            UserRepository userRepository
    ) {
        this.classRepository = classRepository;
        this.classMapper = classMapper;
        this.userRepository = userRepository;
    }

    @Override
    public ClassResponse create(CreateClassRequest request) {
        validateCreate(request);

        ClassEntity entity = classMapper.fromCreate(request);

        ClassEntity persistedClass = classRepository.save(entity);

        if (request.studentIds() != null && !request.studentIds().isEmpty()) {
            List<UserEntity> students = userRepository.findAllById(request.studentIds());

            if (students.size() != request.studentIds().size()) {
                throw new BusinessException(UserError.USER_NOT_FOUND);
            }

            for (UserEntity student : students) {
                persistedClass.addStudent(student);
            }
        }

        if (request.professorIds() != null && !request.professorIds().isEmpty()) {
            List<UserEntity> professors = userRepository.findAllById(request.professorIds());

            if (professors.size() != request.professorIds().size()) {
                throw new BusinessException(ClassError.INVALID_REQUEST);
            }

            for (UserEntity professor : professors) {
                persistedClass.addProfessor(professor);
            }
        }

        ClassEntity saved = classRepository.save(persistedClass);
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
        ClassEntity entity = classRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ClassError.CLASS_NOT_FOUND));

        // Détacher les étudiants
        for (UserEntity student : entity.getStudents()) {
            student.setStudentClass(null);
        }
        entity.getStudents().clear();

        // Détacher les professeurs
        entity.getProfessors().clear();

        classRepository.delete(entity);
    }

    private void validateCreate(CreateClassRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            throw new BusinessException(ClassError.INVALID_REQUEST);
        }
    }
}
