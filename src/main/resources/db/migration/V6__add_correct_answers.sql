CREATE TABLE correct_answers (
    correct_answer_id UUID PRIMARY KEY,
    student_answer_id UUID NOT NULL,
    correct BOOLEAN NOT NULL,
    score DOUBLE PRECISION,
    comment TEXT,
    corrected_by UUID NOT NULL,
    corrected_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_correction_answer
        FOREIGN KEY (student_answer_id)
        REFERENCES student_answers(student_answer_id)
        ON DELETE CASCADE
)