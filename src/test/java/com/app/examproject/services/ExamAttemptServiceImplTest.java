package com.app.examproject.services;

import com.app.examproject.domains.ExamAttemptMapper;
import com.app.examproject.domains.dto.exam_attempt.ExamAttemptResponse;
import com.app.examproject.domains.dto.exam_attempt.UpdateExamAttemptRequest;
import com.app.examproject.domains.entities.ExamAttemptEntity;
import com.app.examproject.errors.BusinessException;
import com.app.examproject.errors.errors.ExamAttemptError;
import com.app.examproject.repositories.ExamAttemptRepository;
import com.app.examproject.services.impl.ExamAttemptServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamAttemptServiceImplTest {

    @Mock
    ExamAttemptRepository examAttemptRepository;

    @Mock
    ExamAttemptMapper examAttemptMapper;

    @InjectMocks
    ExamAttemptServiceImpl examAttemptService;

    UUID attemptId;
    UUID sessionId;

    @BeforeEach
    void setup() {
        attemptId = UUID.randomUUID();
        sessionId = UUID.randomUUID();
    }

    // ================= GET BY ID =================

    @Test
    void testGetByIdOk() {
        ExamAttemptEntity entity = new ExamAttemptEntity();
        entity.setExamAttemptId(attemptId);

        ExamAttemptResponse response = mock(ExamAttemptResponse.class);

        when(examAttemptRepository.findById(attemptId))
                .thenReturn(Optional.of(entity));
        when(examAttemptMapper.toResponse(entity))
                .thenReturn(response);

        ExamAttemptResponse result = examAttemptService.getById(attemptId);

        assertThat(result).isSameAs(response);

        verify(examAttemptRepository).findById(attemptId);
        verify(examAttemptMapper).toResponse(entity);
    }

    @Test
    void testGetByIdNotFound() {
        when(examAttemptRepository.findById(attemptId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> examAttemptService.getById(attemptId))
                .isInstanceOf(BusinessException.class)
                .satisfies(e ->
                        assertThat(((BusinessException) e).getError())
                                .isEqualTo(ExamAttemptError.EXAM_ATTEMPT_NOT_FOUND)
                );

        verify(examAttemptRepository).findById(attemptId);
        verifyNoInteractions(examAttemptMapper);
    }

    // ================= GET BY SESSION =================

    @Test
    void testGetBySession() {
        ExamAttemptEntity a1 = new ExamAttemptEntity();
        ExamAttemptEntity a2 = new ExamAttemptEntity();

        ExamAttemptResponse r1 = mock(ExamAttemptResponse.class);
        ExamAttemptResponse r2 = mock(ExamAttemptResponse.class);

        when(examAttemptRepository.findByExamSession_ExamSessionId(sessionId))
                .thenReturn(List.of(a1, a2));
        when(examAttemptMapper.toResponse(a1)).thenReturn(r1);
        when(examAttemptMapper.toResponse(a2)).thenReturn(r2);

        List<ExamAttemptResponse> result =
                examAttemptService.getBySession(sessionId);

        assertThat(result)
                .hasSize(2)
                .containsExactly(r1, r2);

        verify(examAttemptRepository).findByExamSession_ExamSessionId(sessionId);
        verify(examAttemptMapper).toResponse(a1);
        verify(examAttemptMapper).toResponse(a2);
    }

    // ================= UPDATE =================

    @Test
    void testUpdateOk() {
        ExamAttemptEntity existing = new ExamAttemptEntity();
        existing.setExamAttemptId(attemptId);

        UpdateExamAttemptRequest request =
                new UpdateExamAttemptRequest(Instant.now());

        ExamAttemptResponse response = mock(ExamAttemptResponse.class);

        when(examAttemptRepository.findById(attemptId))
                .thenReturn(Optional.of(existing));
        when(examAttemptRepository.save(existing))
                .thenReturn(existing);
        when(examAttemptMapper.toResponse(existing))
                .thenReturn(response);

        ExamAttemptResponse result =
                examAttemptService.update(attemptId, request);

        assertThat(result).isSameAs(response);

        verify(examAttemptRepository).findById(attemptId);
        verify(examAttemptMapper).update(request, existing);
        verify(examAttemptRepository).save(existing);
        verify(examAttemptMapper).toResponse(existing);
    }

    @Test
    void testUpdateNotFound() {
        UpdateExamAttemptRequest request =
                new UpdateExamAttemptRequest(Instant.now());

        when(examAttemptRepository.findById(attemptId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> examAttemptService.update(attemptId, request))
                .isInstanceOf(BusinessException.class)
                .satisfies(e ->
                        assertThat(((BusinessException) e).getError())
                                .isEqualTo(ExamAttemptError.EXAM_ATTEMPT_NOT_FOUND)
                );

        verify(examAttemptRepository).findById(attemptId);
        verifyNoInteractions(examAttemptMapper);
    }

    // ================= DELETE =================

    @Test
    void testDeleteOk() {
        when(examAttemptRepository.existsById(attemptId))
                .thenReturn(true);

        examAttemptService.delete(attemptId);

        verify(examAttemptRepository).existsById(attemptId);
        verify(examAttemptRepository).deleteById(attemptId);
    }

    @Test
    void testDeleteNotFound() {
        when(examAttemptRepository.existsById(attemptId))
                .thenReturn(false);

        assertThatThrownBy(() -> examAttemptService.delete(attemptId))
                .isInstanceOf(BusinessException.class)
                .satisfies(e ->
                        assertThat(((BusinessException) e).getError())
                                .isEqualTo(ExamAttemptError.EXAM_ATTEMPT_NOT_FOUND)
                );

        verify(examAttemptRepository).existsById(attemptId);
        verify(examAttemptRepository, never()).deleteById(any());
    }
}

