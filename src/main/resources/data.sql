INSERT INTO training_types (training_type_name) VALUES ('YOGA');
INSERT INTO training_types (training_type_name) VALUES ('FITNESS');
INSERT INTO training_types (training_type_name) VALUES ('ZUMBA');
INSERT INTO training_types (training_type_name) VALUES ('STRETCHING');
INSERT INTO training_types (training_type_name) VALUES ('RESISTANCE');

INSERT INTO users (id, first_name, last_name, username, password, is_active) VALUES (1, 'John', 'Doe', 'John.Doe', 'aB3dE5gH', true);
INSERT INTO users (id, first_name, last_name, username, password, is_active) VALUES (2, 'Jane', 'Smith', 'Jane.Smith', 'xY9zW2vU', true);
INSERT INTO users (id, first_name, last_name, username, password, is_active) VALUES (3, 'Robert', 'Brown', 'Robert.Brown', 'pPass123', true);
INSERT INTO users (id, first_name, last_name, username, password, is_active) VALUES (4, 'Alice', 'Johnson', 'Alice.Johnson', 'qWert456', true);
INSERT INTO users (id, first_name, last_name, username, password, is_active) VALUES (5, 'Max', 'Smith', 'Max.Smith', 'qWert678', true);

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
