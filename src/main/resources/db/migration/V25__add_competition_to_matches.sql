ALTER TABLE matches
    ADD competition_id BIGINT;

ALTER TABLE matches
    ALTER COLUMN competition_id SET NOT NULL;

ALTER TABLE matches
    ADD CONSTRAINT FK_MATCHES_ON_COMPETITION FOREIGN KEY (competition_id) REFERENCES competitions (id);

ALTER TABLE matches
    DROP COLUMN end_time;

ALTER TABLE matches
    DROP COLUMN start_time;

ALTER TABLE individual_match_winners
    DROP CONSTRAINT fk_indmatwin_on_athlete;

ALTER TABLE individual_match_winners
    DROP CONSTRAINT fk_indmatwin_on_individual_match_result;

ALTER TABLE team_match_winners
    DROP CONSTRAINT fk_teamatwin_on_team;

ALTER TABLE team_match_winners
    DROP CONSTRAINT fk_teamatwin_on_team_match_result;

CREATE TABLE individual_match_points
(
    match_id   BIGINT NOT NULL,
    points     BIGINT,
    athlete_id BIGINT NOT NULL,
    CONSTRAINT pk_individual_match_points PRIMARY KEY (match_id, athlete_id)
);

CREATE TABLE team_match_points
(
    match_id BIGINT NOT NULL,
    points   BIGINT,
    team_id  BIGINT NOT NULL,
    CONSTRAINT pk_team_match_points PRIMARY KEY (match_id, team_id)
);

ALTER TABLE individual_match_points
    ADD CONSTRAINT fk_individual_match_points_on_athlete FOREIGN KEY (athlete_id) REFERENCES athletes (user_id);

ALTER TABLE individual_match_points
    ADD CONSTRAINT fk_individual_match_points_on_individual_match_result FOREIGN KEY (match_id) REFERENCES individual_match_results (id);

ALTER TABLE team_match_points
    ADD CONSTRAINT fk_team_match_points_on_team FOREIGN KEY (team_id) REFERENCES teams (id);

ALTER TABLE team_match_points
    ADD CONSTRAINT fk_team_match_points_on_team_match_result FOREIGN KEY (match_id) REFERENCES team_match_results (id);

DROP TABLE individual_match_winners CASCADE;

DROP TABLE team_match_winners CASCADE;