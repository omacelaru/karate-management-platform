-- First add the duration columns if they don't exist
ALTER TABLE individual_category_participation
    ADD COLUMN IF NOT EXISTS duration_minutes INTEGER NOT NULL DEFAULT 0;

ALTER TABLE team_category_participation
    ADD COLUMN IF NOT EXISTS duration_minutes INTEGER NOT NULL DEFAULT 0;

-- Update durations for individual kata categories (4 minutes)
UPDATE individual_category_participation icp
SET duration_minutes = 4
WHERE icp.id IN (
    SELECT id FROM kata_individual_category
);

-- Update durations for individual kumite categories (4 minutes)
UPDATE individual_category_participation icp
SET duration_minutes = 4
WHERE icp.id IN (
    SELECT id FROM kumite_individual_category
);

-- Update durations for team kata categories (5 minutes)
UPDATE team_category_participation tcp
SET duration_minutes = 5
WHERE tcp.id IN (
    SELECT id FROM kata_team_category
);

-- Update durations for team kumite categories (9 minutes)
UPDATE team_category_participation tcp
SET duration_minutes = 9
WHERE tcp.id IN (
    SELECT id FROM kumite_team_category
);