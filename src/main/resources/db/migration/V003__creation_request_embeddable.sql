CREATE TABLE competition_creation_requests
(
    id       BIGINT NOT NULL,
    name     OID,
    location OID,
    date     date,
    CONSTRAINT pk_competition_creation_requests PRIMARY KEY (id)
);

CREATE TABLE competition_creation_requests_categories
(
    competition_creation_request_id BIGINT NOT NULL,
    categories_id                   BIGINT NOT NULL,
    CONSTRAINT pk_null_categories PRIMARY KEY (competition_creation_request_id, categories_id)
);

ALTER TABLE athlete_creation_requests
    ADD club_id BIGINT;

ALTER TABLE athlete_creation_requests
    ADD height INTEGER;

ALTER TABLE athlete_creation_requests
    ADD weight INTEGER;

ALTER TABLE coach_creation_requests
    ADD license_number VARCHAR;

ALTER TABLE coach_creation_requests
    ADD license_series VARCHAR;

ALTER TABLE coach_creation_requests
    ALTER COLUMN license_number SET NOT NULL;

ALTER TABLE referee_creation_requests
    ADD license_number VARCHAR;

ALTER TABLE referee_creation_requests
    ADD license_series VARCHAR;

ALTER TABLE referee_creation_requests
    ADD level VARCHAR(255);

ALTER TABLE referee_creation_requests
    ALTER COLUMN license_number SET NOT NULL;

ALTER TABLE coach_creation_requests
    ALTER COLUMN license_series SET NOT NULL;

ALTER TABLE referee_creation_requests
    ALTER COLUMN license_series SET NOT NULL;

ALTER TABLE organizer_creation_requests
    ADD trn_series VARCHAR;

ALTER TABLE organizer_creation_requests
    ALTER COLUMN trn_series SET NOT NULL;

ALTER TABLE competition_creation_requests
    ADD CONSTRAINT FK_COMPETITION_CREATION_REQUESTS_ON_ID FOREIGN KEY (id) REFERENCES request_info (id);

ALTER TABLE competition_creation_requests_categories
    ADD CONSTRAINT fk_comcrereqcat_on_competition_creation_request FOREIGN KEY (competition_creation_request_id) REFERENCES competition_creation_requests (id);