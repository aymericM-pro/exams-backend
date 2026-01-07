CREATE TABLE exam_sessions (
    exam_session_id UUID PRIMARY KEY,
    start_at TIMESTAMP NOT NULL,
    end_at TIMESTAMP NOT NULL,
    exam_id UUID NOT NULL,
    class_id UUID NOT NULL,

    CONSTRAINT fk_exam_session_classes
        FOREIGN KEY (class_id)
        REFERENCES classes
        ON DELETE CASCADE,

    CONSTRAINT fk_exam_session_exam
        FOREIGN KEY (exam_id)
        REFERENCES exams
        ON DELETE CASCADE
);

CREATE TABLE exam_attempts
(
    attempt_id      UUID PRIMARY KEY,
    started_at      TIMESTAMP NOT NULL,
    submitted_at    TIMESTAMP,
    exam_session_id UUID      NOT NULL,
    user_id         UUID      NOT NULL,

    CONSTRAINT fk_exam_attempts_exam_session
        FOREIGN KEY (exam_session_id)
        REFERENCES exam_sessions
        ON DELETE CASCADE,

    CONSTRAINT fk_exam_attempts_users
        FOREIGN KEY (user_id)
        REFERENCES users
);

CREATE TABLE student_answers (
    student_answer_id UUID PRIMARY KEY,
    exam_attempt_id   UUID NOT NULL,
    question_id       UUID NOT NULL,

    CONSTRAINT fk_student_answers_exam_attempt
        FOREIGN KEY (exam_attempt_id)
        REFERENCES exam_attempts
        ON DELETE CASCADE,

    CONSTRAINT fk_student_answers_questions
        FOREIGN KEY (question_id)
        REFERENCES questions
        ON DELETE CASCADE,

    CONSTRAINT uk_student_answers_attempt_question
        UNIQUE (exam_attempt_id, question_id)
);

CREATE TABLE student_single_answers (
    student_answer_id UUID PRIMARY KEY,
    answer_id UUID NOT NULL,

    CONSTRAINT fk_single_answers_base
        FOREIGN KEY (student_answer_id)
        REFERENCES student_answers(student_answer_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_single_answer
        FOREIGN KEY (answer_id)
        REFERENCES answers(answer_id)
        ON DELETE CASCADE
);

CREATE TABLE student_text_answers (
      student_answer_id UUID PRIMARY KEY,
      content VARCHAR(2000) NOT NULL,

      CONSTRAINT fk_text_answers_base
          FOREIGN KEY (student_answer_id)
          REFERENCES student_answers(student_answer_id)
          ON DELETE CASCADE
);

CREATE TABLE student_multiple_answers (
    student_answer_id UUID PRIMARY KEY,

    CONSTRAINT fk_multiple_answers_base
        FOREIGN KEY (student_answer_id)
        REFERENCES student_answers(student_answer_id)
        ON DELETE CASCADE
);

CREATE TABLE student_multiple_answer_links (
   student_answer_id UUID NOT NULL,
   answer_id UUID NOT NULL,

   PRIMARY KEY (student_answer_id, answer_id),

   CONSTRAINT fk_multiple_answer_links_answer
       FOREIGN KEY (answer_id)
       REFERENCES answers(answer_id)
       ON DELETE CASCADE,

   CONSTRAINT fk_multiple_answer_links_student_answer
       FOREIGN KEY (student_answer_id)
       REFERENCES student_multiple_answers(student_answer_id)
       ON DELETE CASCADE
);

