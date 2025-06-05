

CREATE TABLE individual_kata_scores
(
    match_id   BIGINT NOT NULL,
    score      BIGINT,
    athlete_id BIGINT NOT NULL,
    CONSTRAINT pk_individual_kata_scores PRIMARY KEY (match_id, athlete_id)
);

CREATE TABLE individual_kumite_scores
(
    match_id   BIGINT NOT NULL,
    score      BIGINT,
    athlete_id BIGINT NOT NULL,
    CONSTRAINT pk_individual_kumite_scores PRIMARY KEY (match_id, athlete_id)
);

CREATE TABLE individual_match_results
(
    id BIGINT NOT NULL,
    CONSTRAINT pk_individual_match_results PRIMARY KEY (id)
);

CREATE TABLE individual_match_winners
(
    athlete_id BIGINT NOT NULL,
    match_id   BIGINT NOT NULL
);

CREATE TABLE team_kata_scores
(
    match_id BIGINT NOT NULL,
    score    BIGINT,
    team_id  BIGINT NOT NULL,
    CONSTRAINT pk_team_kata_scores PRIMARY KEY (match_id, team_id)
);

CREATE TABLE team_kumite_scores
(
    match_id BIGINT NOT NULL,
    score    BIGINT,
    team_id  BIGINT NOT NULL,
    CONSTRAINT pk_team_kumite_scores PRIMARY KEY (match_id, team_id)
);

CREATE TABLE team_match_results
(
    id BIGINT NOT NULL,
    CONSTRAINT pk_team_match_results PRIMARY KEY (id)
);

CREATE TABLE team_match_winners
(
    match_id BIGINT NOT NULL,
    team_id  BIGINT NOT NULL
);

ALTER TABLE matches
    ADD age_group SMALLINT;

ALTER TABLE matches
    ADD end_time time WITHOUT TIME ZONE;

ALTER TABLE matches
    ADD gender SMALLINT;

ALTER TABLE matches
    ADD start_time time WITHOUT TIME ZONE;

ALTER TABLE matches
    ALTER COLUMN age_group SET NOT NULL;

ALTER TABLE matches
    ALTER COLUMN end_time SET NOT NULL;

ALTER TABLE matches
    ALTER COLUMN gender SET NOT NULL;

ALTER TABLE matches
    ALTER COLUMN start_time SET NOT NULL;

ALTER TABLE individual_kata_matches
    DROP CONSTRAINT FK_INDIVIDUAL_KATA_MATCHES_ON_ID;
ALTER TABLE individual_kata_matches
    ADD CONSTRAINT FK_INDIVIDUAL_KATA_MATCHES_ON_ID FOREIGN KEY (id) REFERENCES individual_match_results (id);

ALTER TABLE individual_kumite_matches
    DROP CONSTRAINT FK_INDIVIDUAL_KUMITE_MATCHES_ON_ID;
ALTER TABLE individual_kumite_matches
    ADD CONSTRAINT FK_INDIVIDUAL_KUMITE_MATCHES_ON_ID FOREIGN KEY (id) REFERENCES individual_match_results (id);

ALTER TABLE individual_match_results
    ADD CONSTRAINT FK_INDIVIDUAL_MATCH_RESULTS_ON_ID FOREIGN KEY (id) REFERENCES matches (id);

ALTER TABLE matches
    ADD CONSTRAINT FK_MATCHES_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE team_kata_matches
    DROP CONSTRAINT FK_TEAM_KATA_MATCHES_ON_ID;
ALTER TABLE team_kata_matches
    ADD CONSTRAINT FK_TEAM_KATA_MATCHES_ON_ID FOREIGN KEY (id) REFERENCES team_match_results (id);

ALTER TABLE team_kumite_matches
    DROP CONSTRAINT FK_TEAM_KUMITE_MATCHES_ON_ID;
ALTER TABLE team_kumite_matches
    ADD CONSTRAINT FK_TEAM_KUMITE_MATCHES_ON_ID FOREIGN KEY (id) REFERENCES team_match_results (id);

ALTER TABLE team_match_results
    ADD CONSTRAINT FK_TEAM_MATCH_RESULTS_ON_ID FOREIGN KEY (id) REFERENCES matches (id);

ALTER TABLE individual_kata_scores
    ADD CONSTRAINT fk_individual_kata_scores_on_athlete FOREIGN KEY (athlete_id) REFERENCES athletes (user_id);

ALTER TABLE individual_kata_scores
    ADD CONSTRAINT fk_individual_kata_scores_on_individual_kata_match FOREIGN KEY (match_id) REFERENCES individual_kata_matches (id);

ALTER TABLE individual_kumite_scores
    ADD CONSTRAINT fk_individual_kumite_scores_on_athlete FOREIGN KEY (athlete_id) REFERENCES athletes (user_id);

ALTER TABLE individual_kumite_scores
    ADD CONSTRAINT fk_individual_kumite_scores_on_individual_kumite_match FOREIGN KEY (match_id) REFERENCES individual_kumite_matches (id);

ALTER TABLE individual_match_winners
    ADD CONSTRAINT fk_indmatwin_on_athlete FOREIGN KEY (athlete_id) REFERENCES athletes (user_id);

ALTER TABLE individual_match_winners
    ADD CONSTRAINT fk_indmatwin_on_individual_match_result FOREIGN KEY (match_id) REFERENCES individual_match_results (id);

ALTER TABLE team_kata_scores
    ADD CONSTRAINT fk_team_kata_scores_on_team FOREIGN KEY (team_id) REFERENCES teams (id);

ALTER TABLE team_kata_scores
    ADD CONSTRAINT fk_team_kata_scores_on_team_kata_match FOREIGN KEY (match_id) REFERENCES team_kata_matches (id);

ALTER TABLE team_kumite_scores
    ADD CONSTRAINT fk_team_kumite_scores_on_team FOREIGN KEY (team_id) REFERENCES teams (id);

ALTER TABLE team_kumite_scores
    ADD CONSTRAINT fk_team_kumite_scores_on_team_kumite_match FOREIGN KEY (match_id) REFERENCES team_kumite_matches (id);

ALTER TABLE team_match_winners
    ADD CONSTRAINT fk_teamatwin_on_team FOREIGN KEY (team_id) REFERENCES teams (id);

ALTER TABLE team_match_winners
    ADD CONSTRAINT fk_teamatwin_on_team_match_result FOREIGN KEY (match_id) REFERENCES team_match_results (id);

ALTER TABLE individual_kumite_matches
    DROP COLUMN athlete_left_id;

ALTER TABLE individual_kumite_matches
    DROP COLUMN athlete_right_id;

ALTER TABLE matches
    DROP COLUMN result;

ALTER TABLE matches
    DROP COLUMN scheduled_time;

ALTER TABLE team_kumite_matches
    DROP COLUMN team_left_id;

ALTER TABLE team_kumite_matches
    DROP COLUMN team_right_id;