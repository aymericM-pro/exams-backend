package com.app.examproject.config;

import com.app.examproject.controller.generatorexam.GenerateExamRequest;
import com.app.examproject.controller.generatorexam.dtos.ExamAiMapper;
import com.app.examproject.controller.generatorexam.dtos.ExamMapper;
import com.app.examproject.domains.dto.exams.ExamResponse;
import com.app.examproject.services.impl.OpenAiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenAiService {
    private final OpenAiClient openAiClient;

    public ExamResponse generateExam(GenerateExamRequest request) {
        return ExamMapper.toResponse(
                ExamAiMapper.toModel(openAiClient.generateExam(request))
        );
    }
}
