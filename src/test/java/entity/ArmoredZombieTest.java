package entity;

import interfaces.OrganDroppable;
import items.OrganParts;
import items.Weapon;
import items.WeaponType;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ArmoredZombieTest {

    @Test
    void initialStatsShouldMatchDesign() {
        ArmoredZombie z = new ArmoredZombie();

        assertEquals(250, z.getMaxHp());
        assertEquals(250, z.getHp());
        assertEquals(30, z.getAttackPower());
        assertEquals(20, z.getDefense());
        assertEquals(0.10, z.getCritChance(), 1e-9);
        assertEquals(1.5, z.getCritDamage(), 1e-9);
        assertEquals(0.0, z.getDodgeChance(), 1e-9);
        assertTrue(z.isAlive());
    }

    @Test
    void shouldIncreaseDefenseWhenPlayerUsesSharpWeapon() {
        ArmoredZombie z = new ArmoredZombie();
        Player p = new Player("Hunter");

        Weapon sharpWeapon = new Weapon(
                "Sharp Sword",
                10,           // attackBonus
                0.0,          // critChanceBonus
                0.0,          // critDamageBonus
                0,            // costHearts
                0,            // costBrains
                WeaponType.SHARP
        );
        p.equipWeapon(sharpWeapon);

        int defBefore = z.getDefense();

        z.useSpecialAbility(p);

        assertEquals(defBefore + 5, z.getDefense());
    }

    @Test
    void shouldNotChangeDefenseWhenPlayerUsesNonSharpWeapon() {
        ArmoredZombie z = new ArmoredZombie();
        Player p = new Player("Hunter");

        Weapon gun = new Weapon(
                "Pistol",
                10,
                0.0,
                0.0,
                0,
                0,
                WeaponType.GUN
        );
        p.equipWeapon(gun);

        int defBefore = z.getDefense();

        z.useSpecialAbility(p);

        assertEquals(defBefore, z.getDefense());
    }

    @Test
    void armoredZombieDropShouldBeWithinRangeAndShowSomeVariety() {
        ArmoredZombie z = new ArmoredZombie();

        assertInstanceOf(OrganDroppable.class, z, "ArmoredZombie should implement OrganDroppable");

        Set<Integer> heartValues = new HashSet<>();
        Set<Integer> brainValues = new HashSet<>();

        for (int i = 0; i < 100; i++) {
            OrganParts drop = z.getDropItem();
            int hearts = drop.getHearts();
            int brains = drop.getBrains();

            assertTrue(hearts >= 140 && hearts <= 160,
                    "Hearts should be in [140,160]");
            assertTrue(brains >= 50 && brains <= 70,
                    "Brains should be in [50,70]");

            heartValues.add(hearts);
            brainValues.add(brains);
        }

        assertTrue(heartValues.size() > 1, "Hearts should vary over many drops");
        assertTrue(brainValues.size() > 1, "Brains should vary over many drops");
    }
}