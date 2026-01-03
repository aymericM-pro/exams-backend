CREATE TABLE classes (
     class_id UUID PRIMARY KEY,
     name VARCHAR(100) NOT NULL,
     graduation_year VARCHAR(50)
);

ALTER TABLE users ADD COLUMN class_id UUID;
ALTER TABLE users ADD CONSTRAINT fk_users_class FOREIGN KEY (class_id) REFERENCES classes(class_id);

CREATE TABLE class_professors (
      class_id UUID NOT NULL,
      professor_id UUID NOT NULL,
      PRIMARY KEY (class_id, professor_id),
      CONSTRAINT fk_class_professors_class FOREIGN KEY (class_id) REFERENCES classes(class_id) ON DELETE CASCADE,
      CONSTRAINT fk_class_professors_professor FOREIGN KEY (professor_id) REFERENCES users(user_id) ON DELETE CASCADE
);
