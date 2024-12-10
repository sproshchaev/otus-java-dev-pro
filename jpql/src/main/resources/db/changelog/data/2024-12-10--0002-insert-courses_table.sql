INSERT INTO courses (id, name, cost, category_id) VALUES
  (gen_random_uuid(), 'Java Basic', 110, (SELECT id FROM categories WHERE name = 'Development')),
  (gen_random_uuid(), 'Java Pro', 170, (SELECT id FROM categories WHERE name = 'Development')),
  (gen_random_uuid(), 'Android Mobile', 150, (SELECT id FROM categories WHERE name = 'Development')),
  (gen_random_uuid(), 'IOs Mobile', 190, (SELECT id FROM categories WHERE name = 'Development')),
  (gen_random_uuid(), 'Microservices', 200, (SELECT id FROM categories WHERE name = 'Architecture')),
  (gen_random_uuid(), 'Docker', 70, (SELECT id FROM categories WHERE name = 'DevOps')),
  (gen_random_uuid(), 'Ansible', 80, (SELECT id FROM categories WHERE name = 'DevOps')),
  (gen_random_uuid(), 'Kubernates', 180, (SELECT id FROM categories WHERE name = 'DevOps')),
  (gen_random_uuid(), 'System Analysis', 140, (SELECT id FROM categories WHERE name = 'Analysis')),
  (gen_random_uuid(), 'Business Analysis', 140, (SELECT id FROM categories WHERE name = 'Analysis')),
  (gen_random_uuid(), 'Figma', 200, (SELECT id FROM categories WHERE name = 'Design'));