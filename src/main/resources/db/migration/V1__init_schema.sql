CREATE TABLE users (
       user_id UUID PRIMARY KEY,
       email VARCHAR(150) NOT NULL UNIQUE,
       password VARCHAR(255) NOT NULL
);

CREATE TABLE user_roles (
        user_id UUID NOT NULL,
        role VARCHAR(50) NOT NULL,
        PRIMARY KEY (user_id, role),
        CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE exams (
       exam_id UUID PRIMARY KEY,
       title VARCHAR(200) NOT NULL,
       description VARCHAR(1000) NOT NULL,
       semester VARCHAR(10) NOT NULL,
       created_at TIMESTAMP NOT NULL,
       user_id UUID NOT NULL,
       CONSTRAINT fk_exams_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE questions (
       question_id UUID PRIMARY KEY,
       title VARCHAR(2000) NOT NULL,
       type VARCHAR(50) NOT NULL,
       exam_id UUID NOT NULL,
       CONSTRAINT fk_questions_exam FOREIGN KEY (exam_id) REFERENCES exams(exam_id) ON DELETE CASCADE
);

CREATE TABLE answers (
     answer_id UUID PRIMARY KEY,
     text VARCHAR(2000) NOT NULL,
     correct BOOLEAN NOT NULL,
     question_id UUID NOT NULL,
     CONSTRAINT fk_answers_question FOREIGN KEY (question_id) REFERENCES questions(question_id) ON DELETE CASCADE
);

CREATE TABLE users_exams (
     user_id UUID NOT NULL,
     exam_id UUID NOT NULL,
     PRIMARY KEY (user_id, exam_id),
     CONSTRAINT fk_users_exams_user FOREIGN KEY (user_id) REFERENCES users(user_id),
     CONSTRAINT fk_users_exams_exam FOREIGN KEY (exam_id) REFERENCES exams(exam_id)
);
