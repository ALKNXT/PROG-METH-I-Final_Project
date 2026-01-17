package items;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArmorTest {

    @Test
    void constructorShouldSetAllFields() {
        Armor jacket = new Armor(
                "Kevlar Jacket",
                5,       // defenseBonus
                0.05,    // dodgeChanceBonus
                1,       // costHearts
                0        // costBrains
        );

        assertEquals("Kevlar Jacket", jacket.getName());
        assertEquals(5, jacket.getDefenseBonus());
        assertEquals(0.05, jacket.getDodgeChanceBonus());
        assertEquals(1, jacket.getCostHearts());
        assertEquals(0, jacket.getCostBrains());
    }
}