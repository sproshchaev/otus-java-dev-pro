INSERT INTO students_courses (student_id, course_id) VALUES
  ((SELECT id FROM students WHERE student_name = 'John Doe'), (SELECT id FROM courses WHERE name = 'Java Basic')),
  ((SELECT id FROM students WHERE student_name = 'Jane Smith'), (SELECT id FROM courses WHERE name = 'Android Mobile')),
  ((SELECT id FROM students WHERE student_name = 'Michael Johnson'), (SELECT id FROM courses WHERE name = 'Microservices')),
  ((SELECT id FROM students WHERE student_name = 'Emily Davis'), (SELECT id FROM courses WHERE name = 'Docker')),
  ((SELECT id FROM students WHERE student_name = 'David Wilson'), (SELECT id FROM courses WHERE name = 'Figma'));
