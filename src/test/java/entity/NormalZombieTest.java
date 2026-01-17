package entity;

import interfaces.OrganDroppable;
import items.OrganParts;
import items.WeaponType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NormalZombieTest {

    @Test
    void initialStatsShouldMatchDesign() {
        NormalZombie z = new NormalZombie();

        assertEquals(100, z.getMaxHp());
        assertEquals(100, z.getHp());
        assertEquals(15, z.getAttackPower());
        assertEquals(5, z.getDefense());
        assertEquals(0.10, z.getCritChance(), 1e-9);
        assertEquals(1.5, z.getCritDamage(), 1e-9);
        assertEquals(0.05, z.getDodgeChance(), 1e-9);
        assertTrue(z.isAlive());
    }

    @Test
    void damageMultiplierShouldBeOneForAllWeaponTypesByDefault() {
        NormalZombie z = new NormalZombie();

        for (WeaponType type : WeaponType.values()) {
            assertEquals(1.0, z.getDamageMultiplier(type),
                    "NormalZombie should use damage multiplier = 1 for type: " + type);
        }
    }

    @Test
    void specialAbilityShouldRegenHpWhenBelowHalf() {
        NormalZombie z = new NormalZombie();

        z.setDodgeChance(0.0);
        z.takeDamage(60);
        int hpBefore = z.getHp();
        assertTrue(hpBefore < z.getMaxHp() / 2);

        z.useSpecialAbility(null);

        int hpAfter = z.getHp();

        assertTrue(hpAfter > hpBefore,
                "HP after special ability should be higher than before");
        assertTrue(hpAfter <= z.getMaxHp(),
                "HP should not exceed maxHp");
    }

    @Test
    void specialAbilityShouldDoNothingWhenHpAboveOrEqualHalf() {
        NormalZombie z = new NormalZombie();

        int hpBefore = z.getHp();
        z.useSpecialAbility(null);
        assertEquals(hpBefore, z.getHp());

        z.setHp(z.getMaxHp() / 2);
        hpBefore = z.getHp();
        z.useSpecialAbility(null);

        assertEquals(hpBefore, z.getHp());
    }

    @Test
    void normalZombieShouldImplementOrganDroppableAndDropValidOrganParts() {
        NormalZombie z = new NormalZombie();

        assertInstanceOf(OrganDroppable.class, z, "NormalZombie should implement OrganDroppable");

        for (int i = 0; i < 50; i++) {
            OrganParts drop = z.getDropItem();
            int hearts = drop.getHearts();
            int brains = drop.getBrains();

            assertTrue(hearts >= 40 && hearts <= 50,
                    "Hearts should be between 40 and 50, but was " + hearts);
            assertTrue(brains >= 15 && brains <= 18,
                    "Brains should be between 15 and 18, but was " + brains);
        }
    }
}