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

import java.util.List;
import java.util.UUID;

@Tag(name = "Exam Attempts", description = "APIs for managing exam attempts")
public interface IExamAttemptControllerSwagger {

    @Operation(
            summary = "Create a new exam attempt",
            requestBody = @RequestBody(
                    required = true,
                    description = "Exam attempt creation payload",
                    content = @Content(
                            schema = @Schema(implementation = CreateExamAttemptRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Exam attempt created"),
                    @ApiResponse(responseCode = "400", description = "Invalid request payload")
            }
    )
    ResponseEntity<ExamAttemptResponse> create(CreateExamAttemptRequest request);

    @Operation(
            summary = "Get all exam attempts",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of exam attempts")
            }
    )
    ResponseEntity<List<ExamAttemptResponse>> getAll();

    @Operation(
            summary = "Get exam attempt by id",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Exam attempt ID",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Exam attempt found"),
                    @ApiResponse(responseCode = "404", description = "Exam attempt not found")
            }
    )
    ResponseEntity<ExamAttemptResponse> getById(UUID id);

    @Operation(
            summary = "Get exam attempts by session",
            parameters = {
                    @Parameter(
                            name = "examSessionId",
                            description = "Exam session ID",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Exam attempts found")
            }
    )
    ResponseEntity<List<ExamAttemptResponse>> getBySession(UUID examSessionId);

    @Operation(
            summary = "Update an exam attempt",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Exam attempt ID",
                            required = true
                    )
            },
            requestBody = @RequestBody(
                    required = true,
                    description = "Exam attempt update payload",
                    content = @Content(
                            schema = @Schema(implementation = UpdateExamAttemptRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Exam attempt updated"),
                    @ApiResponse(responseCode = "404", description = "Exam attempt not found")
            }
    )
    ResponseEntity<ExamAttemptResponse> update(UUID id, UpdateExamAttemptRequest request);

    @Operation(
            summary = "Delete an exam attempt",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Exam attempt ID",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Exam attempt deleted"),
                    @ApiResponse(responseCode = "404", description = "Exam attempt not found")
            }
    )
    ResponseEntity<Void> delete(UUID id);
}
