-- Rename athlete_id to athlete_user_id in individual_category_participation
ALTER TABLE individual_category_participation
    RENAME COLUMN athlete_user_id TO athlete_id;

-- Rename the foreign key constraint
ALTER TABLE individual_category_participation
    RENAME CONSTRAINT fk_individual_category_participation_on_athlete_user TO fk_individual_category_participation_on_athlete;
