DROP TABLE IF EXISTS trainings CASCADE;
DROP TABLE IF EXISTS trainers CASCADE;
DROP TABLE IF EXISTS trainees CASCADE;
DROP TABLE IF EXISTS training_types CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       is_active BOOLEAN NOT NULL
);

CREATE TABLE training_types (
                                id BIGSERIAL PRIMARY KEY,
                                training_type_name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE trainees (
                          id BIGINT PRIMARY KEY,
                          date_of_birth DATE,
                          address VARCHAR(255),
                          CONSTRAINT fk_trainee_user FOREIGN KEY (id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE trainers (
                          id BIGINT PRIMARY KEY,
                          specialization_id BIGINT NOT NULL,
                          CONSTRAINT fk_trainer_user FOREIGN KEY (id) REFERENCES users (id) ON DELETE CASCADE,
                          CONSTRAINT fk_trainer_specialization FOREIGN KEY (specialization_id) REFERENCES training_types (id)
);

CREATE TABLE trainings (
                           id BIGSERIAL PRIMARY KEY,
                           trainee_id BIGINT NOT NULL,
                           trainer_id BIGINT NOT NULL,
                           training_name VARCHAR(255) NOT NULL,
                           training_type_id BIGINT NOT NULL,
                           training_date DATE NOT NULL,
                           training_duration DOUBLE PRECISION NOT NULL,
                           CONSTRAINT fk_training_trainee FOREIGN KEY (trainee_id) REFERENCES trainees (id) ON DELETE CASCADE,
                           CONSTRAINT fk_training_trainer FOREIGN KEY (trainer_id) REFERENCES trainers (id),
                           CONSTRAINT fk_training_type FOREIGN KEY (training_type_id) REFERENCES training_types (id)
);
