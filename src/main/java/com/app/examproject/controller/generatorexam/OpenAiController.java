package com.app.examproject.controller.generatorexam;

import com.app.examproject.config.OpenAiService;
import com.app.examproject.domains.dto.exams.ExamResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI", description = "Génération d’examens via OpenAI")
public class OpenAiController {

    private final OpenAiService openAiService;

    @Operation(
            summary = "Générer un examen",
            description = "Génère un examen à partir d’un thème, d’un nombre de questions et d’un niveau"
    )
    @PostMapping("/exam")
    public ResponseEntity<ExamResponse> generateExam(
            @RequestBody GenerateExamRequest request
    ) {
        return ResponseEntity.ok(openAiService.generateExam(request));
    }
}
