package entity;

import interfaces.OrganDroppable;
import items.OrganParts;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RottenZombieTest {

    @Test
    void initialStatsShouldMatchDesign() {
        RottenZombie z = new RottenZombie();

        assertEquals(170, z.getMaxHp());
        assertEquals(170, z.getHp());
        assertEquals(25, z.getAttackPower());
        assertEquals(5, z.getDefense());
        assertEquals(0.10, z.getCritChance(), 1e-9);
        assertEquals(1.5, z.getCritDamage(), 1e-9);
        assertEquals(0.0, z.getDodgeChance(), 1e-9);
        assertTrue(z.isAlive());
    }

    @Test
    void rottenZombieDropShouldBeInRangeAndVary() {
        RottenZombie z = new RottenZombie();

        assertInstanceOf(OrganDroppable.class, z, "RottenZombie should implement OrganDroppable");

        Set<Integer> heartValues = new HashSet<>();
        Set<Integer> brainValues = new HashSet<>();

        for (int i = 0; i < 100; i++) {
            OrganParts drop = z.getDropItem();
            int hearts = drop.getHearts();
            int brains = drop.getBrains();

            assertTrue(hearts >= 70 && hearts <= 90,
                    "Hearts should be in [70,90], but was " + hearts);

            assertTrue(brains >= 25 && brains <= 35,
                    "Brains should be in [25,35], but was " + brains);

            heartValues.add(hearts);
            brainValues.add(brains);
        }

        assertTrue(heartValues.size() > 1,
                "Hearts should not be constant over many drops");
        assertTrue(brainValues.size() > 1,
                "Brains should not be constant over many drops");
    }

    @Test
    void basicAttackShouldPoisonTargetOnHit() {
        RottenZombie z = new RottenZombie();
        Player target = new Player("Hunter");

        z.setCritChance(0.0);
        target.setDodgeChance(0.0);

        assertFalse(target.isPoisoned(), "Target should not be poisoned before attack");

        int damage = z.basicAttack(target);

        assertTrue(damage > 0, "RottenZombie should deal positive damage to Player");
        assertTrue(target.isPoisoned(), "Target should be poisoned after being hit by RottenZombie");
    }

    @Test
    void poisonedTargetShouldLoseHpWhenTickStatusEffectsAtTurnStart() {
        RottenZombie z = new RottenZombie();
        Player target = new Player("Hunter");

        z.setCritChance(0.0);
        target.setDodgeChance(0.0);

        z.basicAttack(target);
        assertTrue(target.isPoisoned(), "Target should be poisoned after attack");

        int hpBefore = target.getHp();

        target.tickStatusEffectsAtTurnStart();
        int hpAfter = target.getHp();

        assertTrue(hpAfter < hpBefore,
                "HP after tickStatusEffectsAtTurnStart should be lower when poisoned");
    }
}