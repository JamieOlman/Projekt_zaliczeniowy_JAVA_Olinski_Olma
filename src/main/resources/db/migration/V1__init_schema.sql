CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       role VARCHAR(50) NOT NULL DEFAULT 'USER'
);

CREATE TABLE courses (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL
);

CREATE TABLE assignments (
                             id BIGSERIAL PRIMARY KEY,
                             title VARCHAR(255) NOT NULL,
                             description TEXT,
                             assignment_type VARCHAR(50),
                             priority VARCHAR(50),
                             course_id BIGINT,
                             user_id BIGINT,
                             CONSTRAINT fk_assignments_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE SET NULL,
                             CONSTRAINT fk_assignments_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE course_users (
                              course_id BIGINT NOT NULL,
                              user_id BIGINT NOT NULL,
                              PRIMARY KEY (course_id, user_id),
                              CONSTRAINT fk_cu_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
                              CONSTRAINT fk_cu_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE certificates (
                              id BIGSERIAL PRIMARY KEY,
                              username VARCHAR(255) NOT NULL,
                              course_id BIGINT NOT NULL,
                              certificate_text VARCHAR(512) NOT NULL,
                              CONSTRAINT fk_certificates_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);