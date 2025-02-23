package ro.unibuc.fmi.karate_management_platform.models.competition.category.kumite;

import org.junit.jupiter.api.Test;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.kumite.individual.KumiteDivisionRange;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ro.unibuc.fmi.karate_management_platform.models.athelte.Gender.FEMALE;
import static ro.unibuc.fmi.karate_management_platform.models.athelte.Gender.MALE;
import static ro.unibuc.fmi.karate_management_platform.models.competition.category.kumite.individual.KumiteDivisionRange.*;

class KumiteDivisionRangeTest {
    @Test
    void testGetAvailableDivisionsForJuniorsMale() {
        Set<KumiteDivisionRange> divisions = getAvailableDivisions(AgeGroup.JUNIORS_15_17, MALE);
        assertFalse(divisions.isEmpty());
        assertEquals(4, divisions.size());
        assertTrue(divisions.contains(UNDER_55KG));
        assertTrue(divisions.contains(BETWEEN_60_65KG));
        assertTrue(divisions.contains(BETWEEN_55_60KG));
        assertTrue(divisions.contains(OVER_65KG));
    }

    @Test
    void testGetAvailableDivisionsForJuniorsFemale() {
        Set<KumiteDivisionRange> divisions = getAvailableDivisions(AgeGroup.JUNIORS_15_17, FEMALE);
        assertFalse(divisions.isEmpty());
        assertEquals(4, divisions.size());
        assertTrue(divisions.contains(UNDER_50KG));
        assertTrue(divisions.contains(BETWEEN_50_55KG));
        assertTrue(divisions.contains(BETWEEN_55_60KG));
        assertTrue(divisions.contains(OVER_60KG));
    }

    @Test
    void testGetAvailableDivisionsForSeniorsMale() {
        Set<KumiteDivisionRange> divisions = getAvailableDivisions(AgeGroup.SENIORS_18_34, MALE);
        assertFalse(divisions.isEmpty());
        assertEquals(4, divisions.size());
        assertTrue(divisions.contains(UNDER_65KG));
        assertTrue(divisions.contains(BETWEEN_65_70KG));
        assertTrue(divisions.contains(BETWEEN_70_75KG));
        assertTrue(divisions.contains(OVER_75KG));
    }

    @Test
    void testGetAvailableDivisionsForSeniorsFemale() {
        Set<KumiteDivisionRange> divisions = getAvailableDivisions(AgeGroup.SENIORS_18_34, FEMALE);
        assertFalse(divisions.isEmpty());
        assertEquals(4, divisions.size());
        assertTrue(divisions.contains(UNDER_55KG));
        assertTrue(divisions.contains(BETWEEN_55_60KG));
        assertTrue(divisions.contains(BETWEEN_60_65KG));
        assertTrue(divisions.contains(OVER_65KG));
    }

    @Test
    void testGetAvailableDivisionsByWeight() {
        KumiteDivisionRange division = getAvailableDivisions(AgeGroup.JUNIORS_15_17, MALE, (short) 54);
        assertEquals(UNDER_55KG, division);

        division = getAvailableDivisions(AgeGroup.SENIORS_18_34, MALE, (short) 70);
        assertEquals(BETWEEN_70_75KG, division);
    }

    @Test
    void testGetAvailableDivisionsByHeight() {
        KumiteDivisionRange division = getAvailableDivisions(AgeGroup.CHILDREN_7_9, MALE, (short) 135);
        assertEquals(BETWEEN_130_140CM, division);
    }


    @Test
    void testGetAvailableDivisions_WithoutValue() {
        AgeGroup ageGroup = AgeGroup.CHILDREN_7_9;

        Set<KumiteDivisionRange> divisions = getAvailableDivisions(ageGroup, MALE);

        assertTrue(divisions.contains(UNDER_130CM));
        assertTrue(divisions.contains(BETWEEN_130_140CM));
        assertTrue(divisions.contains(OVER_140CM));
        assertEquals(3, divisions.size());
    }

    @Test
    void testGetAvailableDivisions_WithValue_Height() {
        AgeGroup ageGroup = AgeGroup.CHILDREN_7_9;
        short height = 135;

        KumiteDivisionRange division = getAvailableDivisions(ageGroup, FEMALE, height);

        assertEquals(BETWEEN_130_140CM, division);
    }

    @Test
    void testGetAvailableDivisions_WithValue_Weight() {
        AgeGroup ageGroup = AgeGroup.JUNIORS_15_17;
        short weight = 52;

        KumiteDivisionRange division = getAvailableDivisions(ageGroup, FEMALE, weight);

        assertEquals(BETWEEN_50_55KG, division);
    }


    @Test
    void testGetAvailableDivisions_EdgeCase_MinValue() {
        AgeGroup ageGroup = AgeGroup.CHILDREN_U7;
        short height = 0;

        KumiteDivisionRange division = getAvailableDivisions(ageGroup, FEMALE, height);

        assertEquals(UNDER_110CM, division);
    }

    @Test
    void testGetAvailableDivisions_EdgeCase_MaxValue() {
        AgeGroup ageGroup = AgeGroup.VETERANS_65_PLUS;
        short weight = Short.MAX_VALUE;

        KumiteDivisionRange division = getAvailableDivisions(ageGroup, MALE, weight);

        assertEquals(OVER_75KG, division);
    }

    @Test
    void testGetAvailableDivisions_MultipleMatches() {
        AgeGroup ageGroup = AgeGroup.CHILDREN_7_9;
        short height = 130;

        KumiteDivisionRange division = getAvailableDivisions(ageGroup, FEMALE, height);

        assertEquals(BETWEEN_130_140CM, division);
    }
}