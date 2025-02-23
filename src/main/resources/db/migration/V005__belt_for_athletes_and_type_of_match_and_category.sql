ALTER TABLE athlete_creation_requests
    ADD belt VARCHAR(255);

ALTER TABLE athlete_creation_requests
    ALTER COLUMN belt SET NOT NULL;

ALTER TABLE athletes
    ADD belt VARCHAR(30);

ALTER TABLE athletes
    ALTER COLUMN belt SET NOT NULL;

ALTER TABLE kata_category
    ADD category_type VARCHAR(30);

ALTER TABLE kata_category
    ALTER COLUMN category_type SET NOT NULL;

ALTER TABLE kumite_category
    ADD category_type VARCHAR(30);

ALTER TABLE kumite_category
    ALTER COLUMN category_type SET NOT NULL;

ALTER TABLE matches
    ADD match_type VARCHAR(255);

ALTER TABLE matches
    ALTER COLUMN match_type SET NOT NULL;

ALTER TABLE competition_creation_requests
    ALTER COLUMN categories_ids SET NOT NULL;

ALTER TABLE competition_creation_requests
    ALTER COLUMN date SET NOT NULL;

ALTER TABLE competition_creation_requests
    ALTER COLUMN location TYPE VARCHAR(255) USING (location::VARCHAR(255));

ALTER TABLE competition_creation_requests
    ALTER COLUMN location SET NOT NULL;

ALTER TABLE competition_creation_requests
    ALTER COLUMN name TYPE VARCHAR(255) USING (name::VARCHAR(255));

ALTER TABLE competition_creation_requests
    ALTER COLUMN name SET NOT NULL;