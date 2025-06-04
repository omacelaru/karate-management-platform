-- Create the base category_participation table
CREATE TABLE category_participation (
    id BIGSERIAL PRIMARY KEY,
    competition_id BIGINT NOT NULL,
    duration_minutes INTEGER NOT NULL,
    CONSTRAINT fk_category_participation_competition FOREIGN KEY (competition_id) REFERENCES competition(id)
);

-- Migrate data from individual_category_participation
INSERT INTO category_participation (id, competition_id, duration_minutes)
SELECT id, competition_id, duration_minutes
FROM individual_category_participation
ON CONFLICT (id) DO NOTHING;

-- Migrate data from team_category_participation
INSERT INTO category_participation (id, competition_id, duration_minutes)
SELECT id, competition_id, duration_minutes
FROM team_category_participation
ON CONFLICT (id) DO NOTHING;

-- Modify individual_category_participation to inherit from category_participation
ALTER TABLE individual_category_participation
    DROP COLUMN competition_id,
    DROP COLUMN duration_minutes,
    ADD CONSTRAINT fk_individual_category_participation_base FOREIGN KEY (id) REFERENCES category_participation(id);

-- Modify team_category_participation to inherit from category_participation
ALTER TABLE team_category_participation
    DROP COLUMN competition_id,
    DROP COLUMN duration_minutes,
    ADD CONSTRAINT fk_team_category_participation_base FOREIGN KEY (id) REFERENCES category_participation(id); 