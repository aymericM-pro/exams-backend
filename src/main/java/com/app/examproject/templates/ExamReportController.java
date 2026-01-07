package com.app.examproject.templates;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ExamReportController {

    private final PdfGeneratorService pdfGeneratorService;

    @PostMapping("/exam")
    public ResponseEntity<byte[]> generateExamReport(@RequestBody ExamReportRequest request) {

        byte[] pdf = pdfGeneratorService.generateExamReport(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=exam-report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

}
