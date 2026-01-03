package com.app.examproject.controller;

import com.app.examproject.domains.dto.exams.CreateExamRequest;
import com.app.examproject.domains.dto.exams.ExamResponse;
import com.app.examproject.domains.dto.exams.UpdateExamRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@Tag(name = "Exams", description = "APIs for managing exams")
public interface IExamControllerSwagger {

    @Operation(
            summary = "Create an exam",
            description = "Create a new exam for a given user"
    )
    @ApiResponse(responseCode = "201", description = "Exam created")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    ResponseEntity<ExamResponse> create(
            @Parameter(
                    description = "ID of the user creating the exam",
                    required = true
            )
            @PathVariable UUID userId,

            @RequestBody(
                    required = true,
                    description = "Exam creation payload",
                    content = @Content(
                            schema = @Schema(implementation = CreateExamRequest.class)
                    )
            )
            CreateExamRequest request
    );

    @Operation(summary = "Get all exams")
    ResponseEntity<List<ExamResponse>> getAll();

    @Operation(summary = "Get exam by id")
    @ApiResponse(responseCode = "200", description = "Exam found")
    @ApiResponse(responseCode = "404", description = "Exam not found")
    ResponseEntity<ExamResponse> getById(
            @Parameter(description = "Exam ID", required = true)
            @PathVariable UUID id
    );

    @Operation(summary = "Update an exam")
    ResponseEntity<ExamResponse> update(
            @Parameter(description = "Exam ID", required = true)
            @PathVariable UUID id,

            @RequestBody(
                    required = true,
                    description = "Exam update payload",
                    content = @Content(
                            schema = @Schema(implementation = UpdateExamRequest.class)
                    )
            )
            UpdateExamRequest request
    );

    @Operation(summary = "Delete an exam")
    ResponseEntity<Void> delete(
            @Parameter(description = "Exam ID", required = true)
            @PathVariable UUID id
    );
}
