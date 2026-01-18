package com.app.examproject.controller.exams;

import com.app.examproject.domains.dto.exams.CreateExamRequest;
import com.app.examproject.domains.dto.exams.ExamListItemResponse;
import com.app.examproject.domains.dto.exams.ExamResponse;
import com.app.examproject.domains.dto.exams.UpdateExamRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@Tag(name = "Exams", description = "APIs for managing exams")
public interface IExamControllerSwagger {

    @Operation(
            summary = "Create an exam",
            description = "Create a new exam for a given user",
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "ID of the user creating the exam",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "201", description = "Exam created"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    ResponseEntity<ExamResponse> create(
            @PathVariable UUID userId,
            CreateExamRequest request
    );

    @Operation(
            summary = "Get all exams",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Exams retrieved successfully",
                            content = @Content(
                                    schema = @Schema(implementation = ExamListItemResponse.class)
                            )
                    )
            }
    )
    ResponseEntity<List<ExamListItemResponse>> getAll();

    @Operation(
            summary = "Get exam by id",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID of the exam to retrieve",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Exam found"),
                    @ApiResponse(responseCode = "404", description = "Exam not found")
            }
    )
    ResponseEntity<ExamResponse> getById(
            @PathVariable UUID id
    );

    @Operation(
            summary = "Update an exam",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID of the exam to update",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Exam updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Exam not found")
            }
    )
    ResponseEntity<ExamResponse> update(
            @PathVariable UUID id,
            UpdateExamRequest request
    );

    @Operation(
            summary = "Delete an exam",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID of the exam to delete",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Exam deleted"),
                    @ApiResponse(responseCode = "404", description = "Exam not found")
            }
    )
    ResponseEntity<Void> delete(
            @PathVariable UUID id
    );
}
