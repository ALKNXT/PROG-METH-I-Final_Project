package entity;

import interfaces.OrganDroppable;
import items.OrganParts;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BossZombieTest {

    @Test
    void initialStatsShouldMatchDesign() {
        BossZombie boss = new BossZombie();

        assertEquals(350, boss.getMaxHp());
        assertEquals(350, boss.getHp());
        assertEquals(25, boss.getAttackPower());
        assertEquals(12, boss.getDefense());
        assertEquals(0.20, boss.getCritChance(), 1e-9);
        assertEquals(1.5, boss.getCritDamage(), 1e-9);
        assertEquals(0.01, boss.getDodgeChance(), 1e-9);
        assertTrue(boss.isAlive());
        assertFalse(boss.isEnraged());
    }

    @Test
    void enrageShouldIncreaseAttackOnceWhenHpBelowHalf() {
        BossZombie boss = new BossZombie();

        boss.setHp(boss.getMaxHp() / 2);

        assertTrue(boss.getHp() <= boss.getMaxHp() / 2);

        int atkBefore = boss.getAttackPower();

        boss.enrage();

        assertTrue(boss.isEnraged(), "Boss should be enraged after enrage() below 50% HP");
        assertEquals((int) (atkBefore * 1.5), boss.getAttackPower());

        int atkAfterFirstEnrage = boss.getAttackPower();
        boss.enrage();
        assertEquals(atkAfterFirstEnrage, boss.getAttackPower(),
                "Attack should not increase again when already enraged");
    }

    @Test
    void enrageShouldDoNothingWhenHpAboveHalfOrDead() {
        BossZombie boss = new BossZombie();

        assertTrue(boss.getHp() > boss.getMaxHp() / 2);
        int atkBefore = boss.getAttackPower();
        boss.enrage();
        assertFalse(boss.isEnraged());
        assertEquals(atkBefore, boss.getAttackPower());

        boss.takeDamage(9999);
        assertFalse(boss.isAlive());

        boss.enrage();
        assertEquals(atkBefore, boss.getAttackPower());
        assertFalse(boss.isEnraged());
    }

    @Test
    void useSpecialAbilityShouldDealDamageGreaterThanBasicAttack() {
        BossZombie boss = new BossZombie();
        Player target = new Player("Hunter");

        boss.setCritChance(0.0);
        target.setDodgeChance(0.0);

        int hpBefore1 = target.getHp();
        int basicDamage = boss.basicAttack(target);
        int hpAfter1 = target.getHp();
        assertEquals(basicDamage, hpBefore1 - hpAfter1);

        int hpBefore2 = target.getHp();
        boss.useSpecialAbility(target);
        int hpAfter2 = target.getHp();
        int skillDamage = hpBefore2 - hpAfter2;

        assertTrue(skillDamage >= basicDamage,
                "Toxic Smash should deal at least as much damage as a basic attack");
    }

    @Test
    void bossZombieShouldImplementOrganDroppableAndDropFixedReward() {
        BossZombie boss = new BossZombie();

        assertInstanceOf(OrganDroppable.class, boss, "BossZombie should implement OrganDroppable");

        OrganParts drop = boss.getDropItem();

        assertEquals(100, drop.getHearts());
        assertEquals(100, drop.getBrains());
    }
}