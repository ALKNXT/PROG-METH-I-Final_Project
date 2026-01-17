package items;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrganPartsTest {

    @Test
    void addHeartsAndBrainsShouldIncreaseCorrectly() {
        OrganParts parts = new OrganParts();

        parts.addHearts(2);
        parts.addBrains(1);

        assertEquals(2, parts.getHearts());
        assertEquals(1, parts.getBrains());
    }

    @Test
    void cannotAffordShouldReturnFalse() {
        OrganParts parts = new OrganParts();
        parts.addHearts(1);
        parts.addBrains(0);

        assertFalse(parts.canAfford(2, 0));  // ต้องการหัวใจ 2 แต่มี 1
    }

    @Test
    void payShouldDecreaseValuesWhenEnough() {
        OrganParts parts = new OrganParts();
        parts.addHearts(3);
        parts.addBrains(2);

        boolean result = parts.pay(2, 1);

        assertTrue(result);
        assertEquals(1, parts.getHearts());
        assertEquals(1, parts.getBrains());
    }

    @Test
    void payShouldNotChangeValuesWhenNotEnough() {
        OrganParts parts = new OrganParts();
        parts.addHearts(1);
        parts.addBrains(0);

        boolean result = parts.pay(2, 0);

        assertFalse(result);
        assertEquals(1, parts.getHearts());
        assertEquals(0, parts.getBrains());
    }
}