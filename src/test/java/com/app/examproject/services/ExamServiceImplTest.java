package com.app.examproject.services;

import com.app.examproject.TestFixtures;
import com.app.examproject.domains.ExamMapper;
import com.app.examproject.domains.dto.exams.CreateExamRequest;
import com.app.examproject.domains.dto.exams.ExamResponse;
import com.app.examproject.domains.dto.exams.UpdateExamRequest;
import com.app.examproject.domains.entities.ExamEntity;
import com.app.examproject.domains.entities.UserEntity;
import com.app.examproject.errors.BusinessException;
import com.app.examproject.errors.ExamError;
import com.app.examproject.errors.UserError;
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
    void create_should_save_exam_and_link_relations() {
        CreateExamRequest request = TestFixtures.createExamRequest();
        ExamEntity mappedEntity = TestFixtures.examEntity();
        UserEntity user = TestFixtures.userEntity(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(examMapper.fromCreate(request)).thenReturn(mappedEntity);
        when(examRepository.save(mappedEntity)).thenReturn(mappedEntity);
        when(examMapper.toResponse(mappedEntity)).thenReturn(TestFixtures.examResponse());

        ExamResponse response = examService.create(userId, request);

        assertThat(response).isNotNull();
        assertThat(mappedEntity.getUser()).isSameAs(user);

        mappedEntity.getQuestions().forEach(q -> {
            assertThat(q.getExam()).isSameAs(mappedEntity);
            q.getAnswers().forEach(a ->
                    assertThat(a.getQuestion()).isSameAs(q)
            );
        });

        verify(examRepository).save(mappedEntity);
    }

    @Test
    void create_should_throw_when_user_not_found() {
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
    void create_should_throw_when_request_invalid() {
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
    void getById_should_return_exam() {
        ExamEntity entity = TestFixtures.examEntity();

        when(examRepository.findById(examId)).thenReturn(Optional.of(entity));
        when(examMapper.toResponse(entity)).thenReturn(TestFixtures.examResponse());

        ExamResponse response = examService.getById(examId);

        assertThat(response).isNotNull();
        verify(examRepository).findById(examId);
    }

    @Test
    void getById_should_throw_when_not_found() {
        when(examRepository.findById(examId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> examService.getById(examId))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex ->
                        assertThat(((BusinessException) ex).getError())
                                .isEqualTo(ExamError.EXAM_NOT_FOUND)
                );

        verify(examRepository).findById(examId);
        verifyNoInteractions(examMapper);
    }


    @Test
    void update_should_replace_questions_and_save() {
        ExamEntity existing = TestFixtures.examEntity();
        ExamEntity updated = TestFixtures.examEntity();

        when(examRepository.findById(examId)).thenReturn(Optional.of(existing));
        when(examMapper.fromUpdate(any(UpdateExamRequest.class))).thenReturn(updated);
        when(examRepository.save(existing)).thenReturn(existing);
        when(examMapper.toResponse(existing)).thenReturn(TestFixtures.examResponse());

        ExamResponse response =
                examService.update(examId, TestFixtures.updateExamRequest());

        assertThat(response).isNotNull();
        verify(examRepository).save(existing);
    }

    @Test
    void delete_should_delete_exam() {
        when(examRepository.existsById(examId)).thenReturn(true);

        examService.delete(examId);

        verify(examRepository).deleteById(examId);
    }

    @Test
    void delete_should_throw_when_not_found() {
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
