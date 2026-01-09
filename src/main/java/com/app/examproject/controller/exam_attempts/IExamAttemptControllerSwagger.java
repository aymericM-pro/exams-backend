package com.app.examproject.controller.exam_attempts;

import com.app.examproject.domains.dto.exam_attempt.CreateExamAttemptRequest;
import com.app.examproject.domains.dto.exam_attempt.ExamAttemptResponse;
import com.app.examproject.domains.dto.exam_attempt.UpdateExamAttemptRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@Tag(name = "Exam Attempts", description = "APIs for managing exam attempts")
public interface IExamAttemptControllerSwagger {

    @Operation(summary = "Create a new exam attempt")
    @ApiResponse(responseCode = "200", description = "Exam attempt created")
    ResponseEntity<ExamAttemptResponse> create(
            @RequestBody(
                    required = true,
                    description = "Exam attempt creation payload",
                    content = @Content(
                            schema = @Schema(implementation = UpdateExamAttemptRequest.class)
                    )
            )
            CreateExamAttemptRequest request
    );

    @Operation(summary = "Get all exam attempts")
    @ApiResponse(responseCode = "200", description = "List of exam attempts")
    ResponseEntity<List<ExamAttemptResponse>> getAll();

    @Operation(summary = "Get exam attempt by id")
    @ApiResponse(responseCode = "200", description = "Exam attempt found")
    @ApiResponse(responseCode = "404", description = "Exam attempt not found")
    ResponseEntity<ExamAttemptResponse> getById(
            @Parameter(description = "Exam attempt ID", required = true)
            @PathVariable UUID id
    );

    @Operation(summary = "Get exam attempts by session")
    @ApiResponse(responseCode = "200", description = "Exam attempts found")
    ResponseEntity<List<ExamAttemptResponse>> getBySession(
            @Parameter(description = "Exam session ID", required = true)
            @PathVariable UUID examSessionId
    );

    @Operation(summary = "Update an exam attempt")
    @ApiResponse(responseCode = "200", description = "Exam attempt updated")
    @ApiResponse(responseCode = "404", description = "Exam attempt not found")
    ResponseEntity<ExamAttemptResponse> update(
            @Parameter(description = "Exam attempt ID", required = true)
            @PathVariable UUID id,
            @RequestBody(
                    required = true,
                    description = "Exam attempt update payload",
                    content = @Content(
                            schema = @Schema(implementation = UpdateExamAttemptRequest.class)
                    )
            )
            UpdateExamAttemptRequest request
    );

    @Operation(summary = "Delete an exam attempt")
    @ApiResponse(responseCode = "204", description = "Exam attempt deleted")
    @ApiResponse(responseCode = "404", description = "Exam attempt not found")
    ResponseEntity<Void> delete(
            @Parameter(description = "Exam attempt ID", required = true)
            @PathVariable UUID id
    );
}
