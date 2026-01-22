package com.app.examproject.controller.exam_sessions;

import com.app.examproject.domains.dto.exams_sessions.CreateExamSessionRequest;
import com.app.examproject.domains.dto.exams_sessions.ExamSessionResponse;
import com.app.examproject.domains.dto.exams_sessions.UpdateExamSessionRequest;
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

@Tag(name = "Exam Sessions", description = "APIs for managing exam sessions")
public interface IExamSessionControllerSwagger {

    @Operation(
            summary = "Create an exam session",
            requestBody = @RequestBody(
                    required = true,
                    description = "Exam session creation payload",
                    content = @Content(
                            schema = @Schema(implementation = CreateExamSessionRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Exam session created"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    ResponseEntity<ExamSessionResponse> create(CreateExamSessionRequest request);

    @Operation(
            summary = "Get all exam sessions",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of exam sessions")
            }
    )
    ResponseEntity<List<ExamSessionResponse>> getAll();

    @Operation(
            summary = "Get exam session by id",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Exam session ID",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Exam session found"),
                    @ApiResponse(responseCode = "404", description = "Exam session not found")
            }
    )
    ResponseEntity<ExamSessionResponse> getById(UUID id);

    @Operation(
            summary = "Update an exam session",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Exam session ID",
                            required = true
                    )
            },
            requestBody = @RequestBody(
                    required = true,
                    description = "Exam session update payload",
                    content = @Content(
                            schema = @Schema(implementation = UpdateExamSessionRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Exam session updated"),
                    @ApiResponse(responseCode = "404", description = "Exam session not found")
            }
    )
    ResponseEntity<ExamSessionResponse> update(UUID id, UpdateExamSessionRequest request);

    @Operation(
            summary = "Delete an exam session",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Exam session ID",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Exam session deleted"),
                    @ApiResponse(responseCode = "404", description = "Exam session not found")
            }
    )
    ResponseEntity<Void> delete(UUID id);
}
