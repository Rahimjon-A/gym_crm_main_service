INSERT INTO training_types (training_type_name) VALUES ('YOGA');
INSERT INTO training_types (training_type_name) VALUES ('FITNESS');
INSERT INTO training_types (training_type_name) VALUES ('ZUMBA');
INSERT INTO training_types (training_type_name) VALUES ('STRETCHING');
INSERT INTO training_types (training_type_name) VALUES ('RESISTANCE');

INSERT INTO users (first_name, last_name, username, password, is_active)
VALUES ('John', 'Doe', 'John.Doe', 'aB3dE5gH', true);

INSERT INTO users (first_name, last_name, username, password, is_active)
VALUES ('Jane', 'Smith', 'Jane.Smith', 'xY9zW2vU', true);

INSERT INTO trainees (date_of_birth, address, user_id)
VALUES ('1995-05-15', '123 Main St, New York', 1);

INSERT INTO trainers (specialization_id, user_id)
VALUES (2, 2);

INSERT INTO trainings (trainee_id, trainer_id, training_name, training_type_id, training_date, training_duration)
VALUES (1, 1, 'Morning Fitness Routine', 2, '2026-03-01', 60.0);