package com.app.examproject.controller.users;

import com.app.examproject.domains.dto.users.CreateUserRequest;
import com.app.examproject.domains.dto.users.UpdateUserRequest;
import com.app.examproject.domains.dto.users.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Tag(
        name = "Users",
        description = "APIs for managing users"
)
public interface IUserControllerSwagger {

    @Operation(
            summary = "Create a user",
            description = "Creates a new user with role and credentials"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User created",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    ResponseEntity<UserResponse> create(
            @Valid
            @RequestBody(required = true)
            CreateUserRequest request
    );

    @Operation(
            summary = "Create many users",
            description = "Creates multiple users at once"
    )
    ResponseEntity<List<UserResponse>> createMany(
            @Valid
            @RequestBody(required = true)
            List<CreateUserRequest> request
    );


    @Operation(
            summary = "Get all users (paginated)",
            description = "Returns a paginated list of users"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Users retrieved successfully",
                    content = @Content(
                            schema = @Schema(implementation = UserResponse.class)
                    )
            )
    })
    ResponseEntity<Page<UserResponse>> getAll(
            @Parameter(
                    description = "Page number (0-based)",
                    example = "0"
            )
            @RequestParam(defaultValue = "0")
            int page,

            @Parameter(
                    description = "Page size",
                    example = "20"
            )
            @RequestParam(defaultValue = "20")
            int size,

            @Parameter(
                    description = "Sort criteria: field,direction (asc|desc)",
                    example = "userId,asc"
            )
            @RequestParam(defaultValue = "userId,asc")
            String[] sort
    );


    @Operation(
            summary = "Get user by id",
            description = "Returns a single user by its UUID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User found",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    ResponseEntity<UserResponse> getById(
            @PathVariable
            @Parameter(
                    description = "User UUID",
                    required = true,
                    example = "c1b3c4d5-9e6a-4a8f-9c0d-123456789abc"
            )
            UUID id
    );

    @Operation(
            summary = "Update a user",
            description = "Updates an existing user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User updated",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    ResponseEntity<UserResponse> update(
            @PathVariable
            @Parameter(description = "User UUID", required = true)
            UUID id,

            @Valid
            @RequestBody(required = true)
            UpdateUserRequest request
    );

    @Operation(
            summary = "Delete a user",
            description = "Deletes a user by its UUID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    ResponseEntity<Void> delete(
            @PathVariable
            @Parameter(description = "User UUID", required = true)
            UUID id
    );
}
