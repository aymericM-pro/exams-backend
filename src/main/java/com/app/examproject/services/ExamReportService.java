package com.app.examproject.services;

import com.app.examproject.domains.dto.report.ExamReportResponse;

import java.util.UUID;

public interface ExamReportService {
    ExamReportResponse getReport(UUID attemptId);
}