package entity;

import interfaces.OrganDroppable;
import items.OrganParts;
import items.Weapon;
import items.WeaponType;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RunnerZombieTest {

    @Test
    void initialStatsShouldMatchDesign() {
        RunnerZombie z = new RunnerZombie();

        assertEquals(200, z.getMaxHp());
        assertEquals(200, z.getHp());
        assertEquals(30, z.getAttackPower());
        assertEquals(10, z.getDefense());
        assertEquals(0.25, z.getCritChance(), 1e-9);
        assertEquals(1.75, z.getCritDamage(), 1e-9);
        assertEquals(0.40, z.getDodgeChance(), 1e-9);
        assertTrue(z.isAlive());
    }

    @Test
    void useSpecialAbilityShouldIncreaseDodgeWhenPlayerUsesMeleeWeapon() {
        RunnerZombie z = new RunnerZombie();
        Player p = new Player("Hunter");

        Weapon heavy = new Weapon(
                "War Hammer",
                10,
                0.0,
                0.0,
                0,
                0,
                WeaponType.HEAVY
        );
        p.equipWeapon(heavy);

        double dodgeBefore = z.getDodgeChance();

        z.useSpecialAbility(p);

        double dodgeAfter = z.getDodgeChance();

        assertTrue(dodgeAfter > dodgeBefore,
                "Dodge chance should increase when player uses HEAVY weapon");
        assertEquals(dodgeBefore + 0.1, dodgeAfter, 1e-9);
    }

    @Test
    void useSpecialAbilityShouldNotChangeDodgeWhenPlayerUsesNonMeleeWeaponOrNoWeapon() {
        RunnerZombie z1 = new RunnerZombie();
        RunnerZombie z2 = new RunnerZombie();
        Player p = new Player("Hunter");

        double dodgeBefore1 = z1.getDodgeChance();
        z1.useSpecialAbility(p);
        double dodgeAfter1 = z1.getDodgeChance();
        assertEquals(dodgeBefore1, dodgeAfter1,
                "Dodge should not change when player has no weapon");

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

        double dodgeBefore2 = z2.getDodgeChance();
        z2.useSpecialAbility(p);
        double dodgeAfter2 = z2.getDodgeChance();

        assertEquals(dodgeBefore2, dodgeAfter2,
                "Dodge should not change when player uses non-melee weapon (GUN)");
    }

    @Test
    void runnerZombieDropShouldBeInRangeAndVary() {
        RunnerZombie z = new RunnerZombie();

        assertInstanceOf(OrganDroppable.class, z, "RunnerZombie should implement OrganDroppable");

        Set<Integer> heartValues = new HashSet<>();
        Set<Integer> brainValues = new HashSet<>();

        for (int i = 0; i < 100; i++) {
            OrganParts drop = z.getDropItem();
            int hearts = drop.getHearts();
            int brains = drop.getBrains();

            assertTrue(hearts >= 280 && hearts <= 320,
                    "Hearts should be in [280,320], but was " + hearts);

            assertTrue(brains >= 120 && brains <= 150,
                    "Brains should be in [120,150], but was " + brains);

            heartValues.add(hearts);
            brainValues.add(brains);
        }

        assertTrue(heartValues.size() > 1,
                "Hearts should not be constant over many drops");
        assertTrue(brainValues.size() > 1,
                "Brains should not be constant over many drops");
    }
}