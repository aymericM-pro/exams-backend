package com.app.examproject.controller.student_exam;

import com.app.examproject.domains.dto.correct.CreateExamCorrectionRequest;
import com.app.examproject.domains.dto.correct.ExamCorrectionResponse;
import com.app.examproject.domains.dto.exam_attempt.ExamAttemptResponse;
import com.app.examproject.domains.dto.report.ExamReportResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Student Exam", description = "APIs for students taking exams")
public interface IStudentExamControllerSwagger {

    @Operation(
            summary = "Start an exam session",
            description = "Starts an exam session for a given user.",
            parameters = {
                    @Parameter(
                            name = "sessionId",
                            description = "Exam session ID",
                            required = true
                    ),
                    @Parameter(
                            name = "userId",
                            description = "User ID starting the exam",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Exam attempt successfully started",
                            content = @Content(
                                    schema = @Schema(implementation = ExamAttemptResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Exam session not found")
            }
    )
    ResponseEntity<ExamAttemptResponse> start(UUID sessionId, UUID userId);

    @Operation(
            summary = "Submit exam answers",
            description = "Submits all answers for an exam attempt.",
            parameters = {
                    @Parameter(
                            name = "attemptId",
                            description = "Exam attempt ID",
                            required = true
                    )
            },
            requestBody = @RequestBody(
                    required = true,
                    description = "Exam answers payload",
                    content = @Content(
                            schema = @Schema(implementation = SubmitExamAnswersRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Answers successfully submitted"),
                    @ApiResponse(responseCode = "404", description = "Exam attempt not found")
            }
    )
    ResponseEntity<Void> submitExamAnswers(UUID attemptId, SubmitExamAnswersRequest request);

    @Operation(
            summary = "Get exam report",
            description = "Returns the exam report for a given attempt.",
            parameters = {
                    @Parameter(
                            name = "attemptId",
                            description = "Exam attempt ID",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Exam report retrieved",
                            content = @Content(
                                    schema = @Schema(implementation = ExamReportResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Exam attempt not found")
            }
    )
    ResponseEntity<ExamReportResponse> getReport(UUID attemptId);

    @Operation(
            summary = "Correct an exam",
            description = "Corrects an exam attempt by a professor.",
            parameters = {
                    @Parameter(
                            name = "attemptId",
                            description = "Exam attempt ID",
                            required = true
                    ),
                    @Parameter(
                            name = "professorId",
                            description = "Professor ID",
                            required = true
                    )
            },
            requestBody = @RequestBody(
                    required = true,
                    description = "Exam correction payload",
                    content = @Content(
                            schema = @Schema(implementation = CreateExamCorrectionRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Exam successfully corrected",
                            content = @Content(
                                    schema = @Schema(implementation = ExamCorrectionResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Exam attempt not found")
            }
    )
    ResponseEntity<ExamCorrectionResponse> correctExam(
            UUID attemptId,
            UUID professorId,
            CreateExamCorrectionRequest request
    );
}
