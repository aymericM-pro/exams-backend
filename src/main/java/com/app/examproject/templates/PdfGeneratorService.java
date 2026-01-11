package com.app.examproject.templates;

import com.app.examproject.domains.dto.classes.pdf.ClassStudentsPdfRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class PdfGeneratorService {

    private final TemplateEngine templateEngine;

    public byte[] generateExamReport(ExamReportRequest request) {

        Context context = new Context();
        context.setVariable("report", request);

        String html = templateEngine.process("exam-report", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate exam report PDF", e);
        }
    }

    public byte[] generateClassStudentsPdf(ClassStudentsPdfRequest request) {

        Context context = new Context();

        // Header / meta
        context.setVariable("className", request.className());
        context.setVariable("promotion", request.promotion());
        context.setVariable("academicYear", request.academicYear());
        context.setVariable("graduationYear", request.graduationYear());

        // Data
        context.setVariable("professors", request.professors());
        context.setVariable("students", request.students());

        String html = templateEngine.process("class-students", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate class students PDF", e);
        }
    }
}
