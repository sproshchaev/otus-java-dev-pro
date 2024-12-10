CREATE TABLE students_courses (
  student_id UUID NOT NULL REFERENCES students (id),
  course_id UUID NOT NULL REFERENCES courses (id)
);