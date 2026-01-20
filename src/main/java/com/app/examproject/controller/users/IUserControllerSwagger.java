package com.app.examproject.controller.users;

import com.app.examproject.domains.dto.users.CreateUserRequest;
import com.app.examproject.domains.dto.users.UpdateUserRequest;
import com.app.examproject.domains.dto.users.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@Tag(
        name = IUserControllerSwagger.TAG_USERS,
        description = "APIs for managing users"
)
public interface IUserControllerSwagger {

    String TAG_USERS = "Users";

    String PARAM_ROLE = "role";
    String PARAM_FIRSTNAME = "firstname";
    String PARAM_ID = "id";
    String PARAM_PAGE = "page";
    String PARAM_SIZE = "size";
    String PARAM_SORT = "sort";

    String EXAMPLE_USER_RESPONSE = "/examples/users/user-response.json";
    String EXAMPLE_USER_SEARCH_RESPONSE = "/examples/users/user-search-response.json";

    /* ===================== SEARCH ===================== */

    @Operation(
            summary = "Search users",
            description = "Search users by optional criteria",
            parameters = {
                    @Parameter(
                            name = PARAM_ROLE,
                            in = QUERY,
                            description = "Filter users by role",
                            example = "ADMIN"
                    ),
                    @Parameter(
                            name = PARAM_FIRSTNAME,
                            in = QUERY,
                            description = "Filter users by firstname (minimum 2 characters)",
                            example = "John"
                    ),
                    @Parameter(
                            name = PARAM_PAGE,
                            in = QUERY,
                            description = "Page index (0-based)",
                            example = "0"
                    ),
                    @Parameter(
                            name = PARAM_SIZE,
                            in = QUERY,
                            description = "Page size",
                            example = "20"
                    ),
                    @Parameter(
                            name = PARAM_SORT,
                            in = QUERY,
                            description = "Sorting criteria (e.g. firstname,asc)",
                            example = "firstname,asc"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Users found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class),
                                    examples = @ExampleObject(
                                            name = "searchResult",
                                            summary = "Paginated users search result",
                                            externalValue = EXAMPLE_USER_SEARCH_RESPONSE
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid search parameters")
            }
    )
    ResponseEntity<Page<UserResponse>> search(
            @ParameterObject UserSearchParams params,
            @ParameterObject Pageable pageable
    );

    /* ===================== CREATE ===================== */

    @Operation(
            summary = "Create a user",
            description = "Creates a new user with role and credentials",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class),
                                    examples = @ExampleObject(
                                            name = "createdUser",
                                            summary = "Created user",
                                            externalValue = EXAMPLE_USER_RESPONSE
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Validation error"),
                    @ApiResponse(responseCode = "409", description = "Email already exists")
            }
    )
    ResponseEntity<UserResponse> create(
            @Valid @RequestBody CreateUserRequest request
    );

    /* ===================== CREATE MANY ===================== */

    @Operation(
            summary = "Create multiple users",
            description = "Creates multiple users at once",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Users created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Validation error")
            }
    )
    ResponseEntity<List<UserResponse>> createMany(
            @Valid @RequestBody List<CreateUserRequest> request
    );

    /* ===================== GET ALL ===================== */

    @Operation(
            summary = "Get all users",
            description = "Returns a paginated list of all users",
            parameters = {
                    @Parameter(
                            name = PARAM_PAGE,
                            in = QUERY,
                            description = "Page index (0-based)",
                            example = "0"
                    ),
                    @Parameter(
                            name = PARAM_SIZE,
                            in = QUERY,
                            description = "Page size",
                            example = "20"
                    ),
                    @Parameter(
                            name = PARAM_SORT,
                            in = QUERY,
                            description = "Sorting criteria (e.g. lastname,desc)",
                            example = "lastname,desc"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Users retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class),
                                    examples = @ExampleObject(
                                            name = "usersPage",
                                            summary = "Paginated users",
                                            externalValue = EXAMPLE_USER_SEARCH_RESPONSE
                                    )
                            )
                    )
            }
    )
    ResponseEntity<Page<UserResponse>> getAll(
            @ParameterObject Pageable pageable
    );

    /* ===================== GET BY ID ===================== */

    @Operation(
            summary = "Get user by id",
            description = "Returns a single user by its UUID",
            parameters = {
                    @Parameter(
                            name = PARAM_ID,
                            in = PATH,
                            description = "User UUID",
                            required = true,
                            example = "c1b3c4d5-9e6a-4a8f-9c0d-123456789abc"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class),
                                    examples = @ExampleObject(
                                            name = "user",
                                            summary = "User response",
                                            externalValue = EXAMPLE_USER_RESPONSE
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    ResponseEntity<UserResponse> getById(
            @PathVariable UUID id
    );

    /* ===================== UPDATE ===================== */

    @Operation(
            summary = "Update a user",
            description = "Updates an existing user",
            parameters = {
                    @Parameter(
                            name = PARAM_ID,
                            in = PATH,
                            description = "User UUID",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class),
                                    examples = @ExampleObject(
                                            name = "updatedUser",
                                            summary = "Updated user",
                                            externalValue = EXAMPLE_USER_RESPONSE
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Validation error"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    ResponseEntity<UserResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequest request
    );

    /* ===================== DELETE ===================== */

    @Operation(
            summary = "Delete a user",
            description = "Deletes a user by its UUID",
            parameters = {
                    @Parameter(
                            name = PARAM_ID,
                            in = PATH,
                            description = "User UUID",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "User deleted"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    ResponseEntity<Void> delete(
            @PathVariable UUID id
    );
}
