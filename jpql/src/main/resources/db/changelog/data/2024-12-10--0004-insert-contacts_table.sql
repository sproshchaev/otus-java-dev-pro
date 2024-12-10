INSERT INTO contacts (id, value, type, student_id) VALUES
  (gen_random_uuid(), 'example@email.com', 'EMAIL', (SELECT id FROM students WHERE student_name = 'John Doe')),
  (gen_random_uuid(), '123-456-7890', 'PHONE', (SELECT id FROM students WHERE student_name = 'Jane Smith')),
  (gen_random_uuid(), 'example2@email.com', 'EMAIL', (SELECT id FROM students WHERE student_name = 'Michael Johnson')),
  (gen_random_uuid(), '987-654-3210', 'PHONE', (SELECT id FROM students WHERE student_name = 'Emily Davis')),
  (gen_random_uuid(), 'example3@email.com', 'EMAIL', (SELECT id FROM students WHERE student_name = 'David Wilson'));
