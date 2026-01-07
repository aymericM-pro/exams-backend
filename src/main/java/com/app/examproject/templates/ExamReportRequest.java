package com.app.examproject.templates;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ExamReportRequest {

    private String examName;
    private String className;
    private String semester;
    private LocalDate examDate;
    private String teacherName;

    private GlobalStats globalStats;
    private List<QuestionStats> questions;
    private List<StudentRanking> ranking;

    private String presentation;
    private String organisation;
    private String pedagogicalSummary;

    @Data
    public static class GlobalStats {
        private double average;
        private double median;
        private double min;
        private double max;
        private double successRate;
    }

    @Data
    public static class QuestionStats {
        private String question;
        private String type;
        private String successRate;
        private String averageScore;
    }

    @Data
    public static class StudentRanking {
        private int rank;
        private String studentName;
        private double score;
    }
}
