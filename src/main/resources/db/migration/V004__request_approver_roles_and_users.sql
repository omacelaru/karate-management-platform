ALTER TABLE coach_creation_request_approver_roles
    DROP CONSTRAINT fk3gx8al4l5glftsd437cmwhab8;

ALTER TABLE athlete_creation_requests_approver_users
    DROP CONSTRAINT fkbo1n3ixdb281bryeb99himcvl;

ALTER TABLE athlete_creation_requests_approver_users
    DROP CONSTRAINT fkcetes35kumay3sd3o820j6j15;

CREATE TABLE request_approver_roles
(
    request_id    BIGINT       NOT NULL,
    approver_role VARCHAR(255) NOT NULL
);

CREATE TABLE request_approver_users
(
    request_id BIGINT NOT NULL,
    user_id    BIGINT NOT NULL,
    CONSTRAINT pk_request_approver_users PRIMARY KEY (request_id, user_id)
);

ALTER TABLE request_approver_users
    ADD CONSTRAINT fk_reqappuse_on_request_info FOREIGN KEY (request_id) REFERENCES request_info (id);

ALTER TABLE request_approver_users
    ADD CONSTRAINT fk_reqappuse_on_user FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE request_approver_roles
    ADD CONSTRAINT fk_request_approver_roles_on_request_info FOREIGN KEY (request_id) REFERENCES request_info (id);

DROP TABLE athlete_creation_requests_approver_users CASCADE;

DROP TABLE coach_creation_request_approver_roles CASCADE;