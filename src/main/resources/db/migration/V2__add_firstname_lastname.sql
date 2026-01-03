ALTER TABLE users ADD COLUMN first_name VARCHAR(100);
ALTER TABLE users ADD COLUMN last_name  VARCHAR(100);

UPDATE users
SET first_name = 'UNKNOWN',
    last_name  = 'UNKNOWN'
WHERE first_name IS NULL OR last_name IS NULL;

ALTER TABLE users ALTER COLUMN first_name SET NOT NULL;
ALTER TABLE users ALTER COLUMN last_name  SET NOT NULL;
