CREATE TABLE team_competitions (
    team_id BIGINT NOT NULL,
    competition_id BIGINT NOT NULL,
    PRIMARY KEY (team_id, competition_id),
    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE,
    FOREIGN KEY (competition_id) REFERENCES competitions(id) ON DELETE CASCADE
);

INSERT INTO team_competitions (team_id, competition_id)
SELECT DISTINCT team_id, competition_id
FROM team_category_participation;