ALTER TABLE individual_kata_scores
    DROP COLUMN score;

ALTER TABLE individual_kata_scores
    ADD score DOUBLE PRECISION;

ALTER TABLE team_kata_scores
    DROP COLUMN score;

ALTER TABLE team_kata_scores
    ADD score DOUBLE PRECISION;