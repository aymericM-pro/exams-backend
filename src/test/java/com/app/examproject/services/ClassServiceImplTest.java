package com.app.examproject.services;

import com.app.examproject.TestFixtures;
import com.app.examproject.domains.ClassMapper;
import com.app.examproject.domains.dto.classes.ClassResponse;
import com.app.examproject.domains.dto.classes.CreateClassRequest;
import com.app.examproject.domains.entities.ClassEntity;
import com.app.examproject.domains.entities.UserEntity;
import com.app.examproject.errors.BusinessException;
import com.app.examproject.errors.errors.ClassError;
import com.app.examproject.repositories.ClassRepository;
import com.app.examproject.repositories.UserRepository;
import com.app.examproject.services.impl.ClassServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassServiceImplTest {

    @Mock
    ClassRepository classRepository;

    @Mock
    ClassMapper classMapper;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    ClassServiceImpl classService;

    UUID classId;
    UUID studentId;
    UUID professorId;

    @BeforeEach
    void setup() {
        classId = UUID.randomUUID();
        studentId = UUID.randomUUID();
        professorId = UUID.randomUUID();
    }

    // ================= CREATE =================

    @Test
    void testCreateClass() {
        CreateClassRequest request = new CreateClassRequest(
                "L3 Informatique",
                "2024-2025",
                List.of(studentId),
                List.of(professorId)
        );

        ClassEntity entity = TestFixtures.classEntity(classId);
        UserEntity student = TestFixtures.userEntity(studentId);
        UserEntity professor = TestFixtures.userEntity(professorId);

        ClassResponse expectedResponse = TestFixtures.classResponse(
                classId,
                List.of(studentId),
                List.of(professorId)
        );

        when(classMapper.fromCreate(request)).thenReturn(entity);
        when(userRepository.findAllById(List.of(studentId))).thenReturn(List.of(student));
        when(userRepository.findAllById(List.of(professorId))).thenReturn(List.of(professor));
        when(classRepository.save(entity)).thenReturn(entity);
        when(classMapper.toResponse(entity)).thenReturn(expectedResponse);

        ClassResponse result = classService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.classId()).isEqualTo(classId);
        assertThat(result.studentIds()).containsExactly(studentId);
        assertThat(result.professorIds()).containsExactly(professorId);

        assertThat(student.getStudentClass()).isEqualTo(entity);

        verify(classRepository).save(entity);
    }


    @Test
    void testCreateClassThrowsExceptionWhenInvalidRequest() {
        CreateClassRequest invalid = new CreateClassRequest(
                "   ",
                "2024",
                null,
                null
        );

        assertThatThrownBy(() -> classService.create(invalid))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex ->
                        assertThat(((BusinessException) ex).getError())
                                .isEqualTo(ClassError.INVALID_REQUEST)
                );

        verifyNoInteractions(classRepository);
        verifyNoInteractions(classMapper);
        verifyNoInteractions(userRepository);
    }

    // ================= READ =================

    @Test
    void testGetAllClasses() {
        ClassEntity entity = TestFixtures.classEntity(classId);

        ClassResponse response = TestFixtures.classResponse(classId, List.of(), List.of());

        when(classRepository.findAll()).thenReturn(List.of(entity));
        when(classMapper.toResponse(entity)).thenReturn(response);

        List<ClassResponse> result = classService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).classId()).isEqualTo(classId);

        verify(classRepository).findAll();
        verify(classMapper).toResponse(entity);
    }

    @Test
    void testGetClassById() {
        ClassEntity entity = TestFixtures.classEntity(classId);
        ClassResponse response = TestFixtures.classResponse(classId, List.of(), List.of());

        when(classRepository.findById(classId)).thenReturn(Optional.of(entity));
        when(classMapper.toResponse(entity)).thenReturn(response);

        ClassResponse result = classService.getById(classId);

        assertThat(result).isNotNull();
        assertThat(result.classId()).isEqualTo(classId);

        verify(classRepository).findById(classId);
    }

    @Test
    void testGetClassByIdThrowsExceptionWhenNotFound() {
        when(classRepository.findById(classId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> classService.getById(classId))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex ->
                        assertThat(((BusinessException) ex).getError())
                                .isEqualTo(ClassError.CLASS_NOT_FOUND)
                );

        verify(classRepository).findById(classId);
        verifyNoInteractions(classMapper);
    }

    // ================= DELETE =================

    @Test
    void testDeleteClass() {
        when(classRepository.existsById(classId)).thenReturn(true);

        classService.delete(classId);

        verify(classRepository).deleteById(classId);
    }

    @Test
    void testDeleteClassThrowsExceptionWhenNotFound() {
        when(classRepository.existsById(classId)).thenReturn(false);

        assertThatThrownBy(() -> classService.delete(classId))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex ->
                        assertThat(((BusinessException) ex).getError())
                                .isEqualTo(ClassError.CLASS_NOT_FOUND)
                );

        verify(classRepository).existsById(classId);
        verifyNoMoreInteractions(classRepository);
    }
}
