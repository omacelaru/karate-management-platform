CREATE TABLE athlete_competition_registration_requests
(
    id                    BIGINT NOT NULL,
    competition_id        BIGINT,
    individual_categories VARCHAR(255),
    team_categories       VARCHAR(255),
    CONSTRAINT pk_athlete_competition_registration_requests PRIMARY KEY (id)
);

ALTER TABLE athlete_competition_registration_requests
    ADD CONSTRAINT FK_ATHLETE_COMPETITION_REGISTRATION_REQUESTS_ON_ID FOREIGN KEY (id) REFERENCES request_info (id);