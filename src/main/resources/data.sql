INSERT INTO training_types (training_type_name) VALUES ('YOGA');
INSERT INTO training_types (training_type_name) VALUES ('FITNESS');
INSERT INTO training_types (training_type_name) VALUES ('ZUMBA');
INSERT INTO training_types (training_type_name) VALUES ('STRETCHING');
INSERT INTO training_types (training_type_name) VALUES ('RESISTANCE');

-- pass: aB3dE5gH
INSERT INTO users (id, first_name, last_name, username, password, is_active) VALUES (1, 'John', 'Doe', 'John.Doe', '$2a$10$jfEXZVNAl.BJtPdy1hW2oezj6Tzb.YErNBNdhWq4.57i6CxuYebfW', true);
-- pas: xY9zW2vU
INSERT INTO users (id, first_name, last_name, username, password, is_active) VALUES (2, 'Jane', 'Smith', 'Jane.Smith', '$2a$10$bYl0D6EwALj5g.6SKjAzg.x7iqoBTIf8ZKUfTgoZVJT8LKI2jLKCy', true);
-- pass: pPass123
INSERT INTO users (id, first_name, last_name, username, password, is_active) VALUES (3, 'Robert', 'Brown', 'Robert.Brown', '$2a$10$HDIG1Qf799HdU0owU.sv/.6odfPaBHvd0nRTZ19jXArV4uOrOIWb2', true);
-- pass: qWert456
INSERT INTO users (id, first_name, last_name, username, password, is_active) VALUES (4, 'Alice', 'Johnson', 'Alice.Johnson', '$2a$10$7fKRTzr3QSGqkXjd0h.ouOYL6bXZX0woXuWrCSFHUor6Srfh0PWL6', true);
-- pass: qWert678
INSERT INTO users (id, first_name, last_name, username, password, is_active) VALUES (5, 'Max', 'Smith', 'Max.Smith', '$2a$10$sosrwC0athYTDGucgR6/Me3cVQgL6WNpdy5jsZuKGP60B3pU4cnyG', true);

INSERT INTO trainees (id, date_of_birth, address)
VALUES (1, '1995-05-15', '123 Main St, New York');

INSERT INTO trainees (id, date_of_birth, address)
VALUES (4, NULL, NULL);

INSERT INTO trainers (id, specialization_id)
VALUES (2, 2);

INSERT INTO trainers (id, specialization_id)
VALUES (5, 3);

INSERT INTO trainers (id, specialization_id)
VALUES (3, 1);

INSERT INTO trainings (trainee_id, trainer_id, training_name, training_type_id, training_date, training_duration)
VALUES (1, 2, 'Morning Fitness Routine', 2, '2026-03-01', 60.0);

INSERT INTO trainings (trainee_id, trainer_id, training_name, training_type_id, training_date, training_duration)
VALUES (4, 3, 'Intro to Yoga', 1, '2026-04-10', 45.0);

SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('trainings_id_seq', (SELECT MAX(id) FROM trainings));
