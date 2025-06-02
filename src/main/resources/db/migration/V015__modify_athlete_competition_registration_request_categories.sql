-- First, create new tables for the categories
CREATE TABLE athlete_competition_registration_individual_categories (
    request_id BIGINT NOT NULL,
    category_type VARCHAR(255) NOT NULL,
    CONSTRAINT pk_athlete_competition_registration_individual_categories PRIMARY KEY (request_id, category_type),
    CONSTRAINT fk_athlete_comp_regis_individual_categories_on_request FOREIGN KEY (request_id) REFERENCES athlete_competition_registration_requests (id) ON DELETE CASCADE
);

CREATE TABLE athlete_competition_registration_team_categories (
    request_id BIGINT NOT NULL,
    category_type VARCHAR(255) NOT NULL,
    CONSTRAINT pk_athlete_competition_registration_team_categories PRIMARY KEY (request_id, category_type),
    CONSTRAINT fk_athlete_competition_registration_team_categories_on_request FOREIGN KEY (request_id) REFERENCES athlete_competition_registration_requests (id) ON DELETE CASCADE
);

-- Migrate existing data
INSERT INTO athlete_competition_registration_individual_categories (request_id, category_type)
SELECT id, unnest(individual_categories::varchar[])
FROM athlete_competition_registration_requests
WHERE individual_categories IS NOT NULL;

INSERT INTO athlete_competition_registration_team_categories (request_id, category_type)
SELECT id, unnest(team_categories::varchar[])
FROM athlete_competition_registration_requests
WHERE team_categories IS NOT NULL;

-- Drop the old columns
ALTER TABLE athlete_competition_registration_requests
    DROP COLUMN individual_categories,
    DROP COLUMN team_categories; 