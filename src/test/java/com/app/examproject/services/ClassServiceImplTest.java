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

    @Test
    void testCreateClass() {
        CreateClassRequest request = new CreateClassRequest(
                "L3 Informatique",
                "2024-2025",
                List.of(studentId),
                List.of(professorId)
        );

        ClassEntity entity = TestFixtures.classEntity(null);
        ClassEntity persisted = TestFixtures.classEntity(classId);

        UserEntity student = TestFixtures.userEntity(studentId);
        UserEntity professor = TestFixtures.userEntity(professorId);

        ClassResponse expectedResponse = TestFixtures.classResponse(
                classId,
                List.of(studentId),
                List.of(professorId)
        );

        when(classMapper.fromCreate(request)).thenReturn(entity);

        when(classRepository.save(entity)).thenReturn(persisted);
        when(classRepository.save(persisted)).thenReturn(persisted);

        when(userRepository.findAllById(List.of(studentId))).thenReturn(List.of(student));
        when(userRepository.findAllById(List.of(professorId))).thenReturn(List.of(professor));

        when(classMapper.toResponse(persisted)).thenReturn(expectedResponse);

        // WHEN
        ClassResponse result = classService.create(request);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.classId()).isEqualTo(classId);
        assertThat(result.studentIds()).containsExactly(studentId);
        assertThat(result.professorIds()).containsExactly(professorId);

        assertThat(student.getStudentClass()).isEqualTo(persisted);

        verify(classRepository, times(2)).save(any(ClassEntity.class));
        verify(userRepository).findAllById(List.of(studentId));
        verify(userRepository).findAllById(List.of(professorId));
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

    @Test
    void testDeleteClass() {
        // GIVEN
        ClassEntity classEntity = TestFixtures.classEntity(classId);

        UserEntity student1 = TestFixtures.userEntity(studentId);
        UserEntity student2 = TestFixtures.userEntity(UUID.randomUUID());
        UserEntity professor = TestFixtures.userEntity(professorId);

        // relations initiales
        classEntity.getStudents().addAll(List.of(student1, student2));
        classEntity.getProfessors().add(professor);

        student1.setStudentClass(classEntity);
        student2.setStudentClass(classEntity);

        when(classRepository.findById(classId)).thenReturn(Optional.of(classEntity));

        // WHEN
        classService.delete(classId);

        // THEN
        assertThat(student1.getStudentClass()).isNull();
        assertThat(student2.getStudentClass()).isNull();

        assertThat(classEntity.getStudents()).isEmpty();
        assertThat(classEntity.getProfessors()).isEmpty();

        verify(classRepository).findById(classId);
        verify(classRepository).delete(classEntity);

        verifyNoInteractions(userRepository);
    }

    @Test
    void testDeleteClassThrowsExceptionWhenNotFound() {
        when(classRepository.findById(classId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> classService.delete(classId))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex ->
                        assertThat(((BusinessException) ex).getError())
                                .isEqualTo(ClassError.CLASS_NOT_FOUND)
                );

        verify(classRepository).findById(classId);
        verify(classRepository, never()).delete(any());
        verifyNoInteractions(userRepository);
    }

}
