package com.app.examproject.controller.classes;

import com.app.examproject.domains.dto.classes.ClassResponse;
import com.app.examproject.domains.dto.classes.CreateClassRequest;
import com.app.examproject.domains.dto.users.StudentResponse;
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

@Tag(name = "Classes", description = "APIs for managing classes")
public interface IClassControllerSwagger {

    @Operation(summary = "Create a class",
            description = "Creates a new class with the provided details.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Class successfully created",
                            content = @Content(
                                    schema = @Schema(implementation = ClassResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request payload"
                    )
            }
    )
    ResponseEntity<ClassResponse> create(
            @RequestBody(
                    required = true,
                    description = "Class creation payload",
                    content = @Content(
                            schema = @Schema(implementation = CreateClassRequest.class)
                    )
            )
            CreateClassRequest request
    );

    @Operation(summary = "Get all classes")
    ResponseEntity<List<ClassResponse>> getAll();

    @Operation(summary = "Get class by id")
    @ApiResponse(responseCode = "404", description = "Class not found")
    ResponseEntity<ClassResponse> getById(
            @Parameter(description = "Class ID", required = true)
            @PathVariable UUID id
    );

    @Operation(summary = "Delete a class")
    ResponseEntity<Void> delete(
            @Parameter(description = "Class ID", required = true)
            @PathVariable UUID id
    );

    @Operation(summary = "Get students by class ID")
    ResponseEntity<List<StudentResponse>> getStudentsByClass(
            @Parameter(description = "Class ID", required = true)
            @PathVariable UUID classId
    );

    @Operation(
            summary = "Remove student from class",
            description = "Removes a student from the specified class.",
            parameters = {
                    @Parameter(
                            name = "classId",
                            description = "ID of the class",
                            required = true
                    ),
                    @Parameter(
                            name = "studentId",
                            description = "ID of the student to be removed",
                            required = true
                    ),
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Student successfully removed from class"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Class or student not found"
                    )
            }
    )
    ResponseEntity<Void> removeStudentFromClass(
            @PathVariable UUID classId,
            @PathVariable UUID studentId
    );
}
