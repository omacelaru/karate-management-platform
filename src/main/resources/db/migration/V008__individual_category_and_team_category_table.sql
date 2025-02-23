CREATE TABLE individual_category_athletes
(
    athlete_id             BIGINT NOT NULL,
    individual_category_id BIGINT NOT NULL,
    CONSTRAINT pk_individual_category_athletes PRIMARY KEY (athlete_id, individual_category_id)
);

CREATE TABLE team_category_teams
(
    team_category_id BIGINT NOT NULL,
    team_id          BIGINT NOT NULL,
    CONSTRAINT pk_team_category_teams PRIMARY KEY (team_category_id, team_id)
);

ALTER TABLE individual_category_athletes
    ADD CONSTRAINT fk_indcatath_on_athlete FOREIGN KEY (athlete_id) REFERENCES athletes (user_id);

ALTER TABLE individual_category_athletes
    ADD CONSTRAINT fk_indcatath_on_kata_individual_category FOREIGN KEY (individual_category_id) REFERENCES kata_individual_category (id);

ALTER TABLE team_category_teams
    ADD CONSTRAINT fk_teacattea_on_kata_team_category FOREIGN KEY (team_category_id) REFERENCES kata_team_category (id);

ALTER TABLE team_category_teams
    ADD CONSTRAINT fk_teacattea_on_team FOREIGN KEY (team_id) REFERENCES teams (id);