package items;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WeaponTest {

    @Test
    void constructorShouldSetAllFields() {
        Weapon pistol = new Weapon(
                "Old Pistol",
                10,              // attackBonus
                0.05,            // critChanceBonus
                0.3,             // critDamageBonus
                2,               // costHearts
                0,               // costBrains
                WeaponType.GUN   // weaponType
        );

        assertEquals("Old Pistol", pistol.getName());
        assertEquals(10, pistol.getAttackBonus());
        assertEquals(0.05, pistol.getCritChanceBonus());
        assertEquals(0.3, pistol.getCritDamageBonus());
        assertEquals(2, pistol.getCostHearts());
        assertEquals(0, pistol.getCostBrains());
        assertEquals(WeaponType.GUN, pistol.getWeaponType());
    }
}