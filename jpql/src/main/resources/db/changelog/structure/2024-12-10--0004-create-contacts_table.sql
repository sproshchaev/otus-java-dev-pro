CREATE TABLE contacts (
  id UUID PRIMARY KEY NOT NULL,
  value VARCHAR(70) NOT NULL,
  type VARCHAR(15) NOT NULL CHECK (type IN ('PHONE', 'EMAIL', 'TELEGRAM')),
  student_id UUID NOT NULL REFERENCES students (id),
  UNIQUE (type, student_id)
);