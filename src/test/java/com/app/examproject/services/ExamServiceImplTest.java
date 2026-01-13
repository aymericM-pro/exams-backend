package com.app.examproject.services;

import com.app.examproject.TestFixtures;
import com.app.examproject.domains.ExamMapper;
import com.app.examproject.domains.dto.exams.CreateExamRequest;
import com.app.examproject.domains.dto.exams.ExamListItemResponse;
import com.app.examproject.domains.dto.exams.ExamResponse;
import com.app.examproject.domains.dto.exams.UpdateExamRequest;
import com.app.examproject.domains.entities.ExamEntity;
import com.app.examproject.domains.entities.UserEntity;
import com.app.examproject.commons.errors.BusinessException;
import com.app.examproject.commons.errors.errors.ExamError;
import com.app.examproject.commons.errors.errors.UserError;
import com.app.examproject.repositories.ExamRepository;
import com.app.examproject.repositories.UserRepository;
import com.app.examproject.services.impl.ExamServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamServiceImplTest {

    @Mock
    ExamRepository examRepository;

    @Mock
    ExamMapper examMapper;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    ExamServiceImpl examService;

    UUID examId;
    UUID userId;

    @BeforeEach
    void setup() {
        examId = UUID.randomUUID();
        userId = UUID.randomUUID();
    }

    @Test
    void testCreateExam() {
        CreateExamRequest request = TestFixtures.createExamRequest();
        ExamEntity entity = TestFixtures.examEntity();
        UserEntity user = TestFixtures.userEntity(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(examMapper.fromCreate(request)).thenReturn(entity);
        when(examRepository.save(entity)).thenReturn(entity);
        when(examMapper.toResponse(entity)).thenReturn(TestFixtures.examResponse());

        ExamResponse response = examService.create(userId, request);

        assertThat(response).isNotNull();
        assertThat(entity.getUser()).isSameAs(user);

        entity.getQuestions().forEach(q -> {
            assertThat(q.getExam()).isSameAs(entity);
            q.getAnswers().forEach(a ->
                    assertThat(a.getQuestion()).isSameAs(q)
            );
        });

        verify(examRepository).save(entity);
    }

    @Test
    void testCreateExamUserNotFound() {
        CreateExamRequest request = TestFixtures.createExamRequest();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> examService.create(userId, request))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex ->
                        assertThat(((BusinessException) ex).getError())
                                .isEqualTo(UserError.USER_NOT_FOUND)
                );

        verify(userRepository).findById(userId);
        verifyNoInteractions(examMapper);
        verifyNoInteractions(examRepository);
    }

    @Test
    void testCreateExamInvalidRequest() {
        CreateExamRequest request =
                new CreateExamRequest("", "desc", "S1", List.of());

        assertThatThrownBy(() -> examService.create(userId, request))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex ->
                        assertThat(((BusinessException) ex).getError())
                                .isEqualTo(ExamError.INVALID_REQUEST)
                );

        verifyNoInteractions(userRepository);
        verifyNoInteractions(examMapper);
        verifyNoInteractions(examRepository);
    }

    @Test
    void testGetAllExams() {
        ExamEntity entity = TestFixtures.examEntity();
        ExamListItemResponse listItem =
                TestFixtures.examListItemResponse();

        when(examRepository.findAll()).thenReturn(List.of(entity));
        when(examMapper.toListItemResponse(entity)).thenReturn(listItem);

        List<ExamListItemResponse> result = examService.getAll();

        assertThat(result)
                .hasSize(1)
                .containsExactly(listItem);

        verify(examRepository).findAll();
        verify(examMapper).toListItemResponse(entity);
        verify(examMapper, never()).toResponse(any());
    }

    @Test
    void testGetExamById() {
        ExamEntity entity = TestFixtures.examEntity();

        when(examRepository.findExamEntityByExamId(examId))
                .thenReturn(Optional.of(entity));
        when(examMapper.toResponse(entity))
                .thenReturn(TestFixtures.examResponse());

        ExamResponse response = examService.getById(examId);

        assertThat(response).isNotNull();

        verify(examRepository).findExamEntityByExamId(examId);
        verify(examMapper).toResponse(entity);
    }

    @Test
    void testGetExamByIdNotFound() {
        when(examRepository.findExamEntityByExamId(examId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> examService.getById(examId))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex ->
                        assertThat(((BusinessException) ex).getError())
                                .isEqualTo(ExamError.EXAM_NOT_FOUND)
                );

        verify(examRepository).findExamEntityByExamId(examId);
        verifyNoInteractions(examMapper);
    }

    @Test
    void testUpdateExam() {
        ExamEntity existing = TestFixtures.examEntity();
        ExamEntity updated = TestFixtures.examEntity();

        when(examRepository.findById(examId))
                .thenReturn(Optional.of(existing));
        when(examMapper.fromUpdate(any(UpdateExamRequest.class)))
                .thenReturn(updated);
        when(examRepository.save(existing))
                .thenReturn(existing);
        when(examMapper.toResponse(existing))
                .thenReturn(TestFixtures.examResponse());

        ExamResponse response =
                examService.update(examId, TestFixtures.updateExamRequest());

        assertThat(response).isNotNull();
        verify(examRepository).save(existing);
    }

    @Test
    void testDeleteExam() {
        when(examRepository.existsById(examId)).thenReturn(true);

        examService.delete(examId);

        verify(examRepository).deleteById(examId);
    }

    @Test
    void testDeleteExamNotFound() {
        when(examRepository.existsById(examId)).thenReturn(false);

        assertThatThrownBy(() -> examService.delete(examId))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex ->
                        assertThat(((BusinessException) ex).getError())
                                .isEqualTo(ExamError.EXAM_NOT_FOUND)
                );

        verify(examRepository).existsById(examId);
        verifyNoMoreInteractions(examRepository);
    }
}
