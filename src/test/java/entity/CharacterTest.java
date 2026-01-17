package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterTest {

    private entity.Character createTestCharacter(
            int maxHp,
            int attackPower,
            int defense,
            double critChance,
            double critDamage,
            double dodgeChance
    ) {
        return new entity.Character(maxHp, attackPower, defense, critChance, critDamage, dodgeChance) {
            @Override
            public void useSpecialAbility(entity.Character target) {
            }
        };
    }

    @Test
    void constructorShouldClampAndInitializeStatsCorrectly() {
        entity.Character c = createTestCharacter(
                0,       // maxHp <= 0
                -10,     // attackPower < 0
                -5,      // defense < 0
                -0.5,    // critChance < 0
                0.5,     // critDamage < 1
                2.0      // dodgeChance > 1
        );

        assertEquals(1, c.getMaxHp());
        assertEquals(1, c.getHp());
        assertEquals(0, c.getAttackPower());
        assertEquals(0, c.getDefense());
        assertEquals(0.0, c.getCritChance(), 1e-9);
        assertEquals(1.0, c.getCritDamage(), 1e-9);
        assertEquals(1.0, c.getDodgeChance(), 1e-9);
        assertTrue(c.isAlive());
    }

    @Test
    void setHpShouldClampToRangeAndSetAliveFalseAtZero() {
        entity.Character c = createTestCharacter(50, 10, 0, 0.0, 1.5, 0.0);

        c.setHp(40);
        assertEquals(40, c.getHp());
        assertTrue(c.isAlive());

        c.setHp(200);  // เกิน maxHp
        assertEquals(50, c.getHp());
        assertTrue(c.isAlive());

        c.setHp(0);    // ตาย
        assertEquals(0, c.getHp());
        assertFalse(c.isAlive());
    }

    @Test
    void basicAttackShouldDealAttackMinusDefenseWhenNoCritAndNoDodge() {
        entity.Character attacker = createTestCharacter(
                100,
                20,   // attackPower
                0,    // defense
                0.0,  // critChance = 0
                1.5,
                0.0   // dodgeChance ของ attacker
        );

        entity.Character defender = createTestCharacter(
                100,
                10,
                5,    // defense
                0.0,
                1.5,
                0.0   // dodgeChance = 0
        );

        int hpBefore = defender.getHp();

        int damageDealt = attacker.basicAttack(defender);

        // netDamage = atk(20) - def(5) = 15
        assertEquals(15, damageDealt);
        assertEquals(hpBefore - 15, defender.getHp());
    }

    @Test
    void takeDamageShouldRespectDefenseAndNotGoBelowZero() {
        entity.Character c = createTestCharacter(
                50,
                10,
                5,     // defense
                0.0,
                1.5,
                0.0    // dodgeChance = 0
        );

        int hpBefore = c.getHp();
        int damage = c.takeDamage(20);  // net = 20 - 5 = 15

        assertEquals(15, damage);
        assertEquals(hpBefore - 15, c.getHp());
        assertTrue(c.isAlive());
    }

    @Test
    void takeDamageShouldMissWhenDodged() {
        entity.Character c = createTestCharacter(
                50,
                10,
                0,
                0.0,
                1.5,
                1.0    // dodgeChance = 1.0
        );

        int hpBefore = c.getHp();
        int damage = c.takeDamage(999);

        assertEquals(0, damage);
        assertEquals(hpBefore, c.getHp());
        assertTrue(c.isAlive());
    }

    @Test
    void healShouldIncreaseHpButNotOverMaxAndDoNothingWhenDead() {
        entity.Character c = createTestCharacter(
                50,
                10,
                0,
                0.0,
                1.5,
                0.0
        );

        c.takeDamage(20);  // defense=0 → hp = 30
        assertEquals(30, c.getHp());

        // heal 10 → 40
        c.heal(10);
        assertEquals(40, c.getHp());

        c.heal(20);
        assertEquals(50, c.getHp());

        c.takeDamage(100);
        assertFalse(c.isAlive());
        assertEquals(0, c.getHp());

        c.heal(10);
        assertEquals(0, c.getHp());
    }

    // ----------------- Poison tests ----------------- //

    @Test
    void applyPoisonShouldSetPoisonedStatus() {
        entity.Character c = createTestCharacter(
                50,
                10,
                0,
                0.0,
                1.5,
                0.0
        );

        assertFalse(c.isPoisoned());

        c.applyPoison(5, 3);

        assertTrue(c.isPoisoned());
    }

    @Test
    void tickStatusEffectsShouldDealPoisonDamageOverTurnsAndThenStop() {
        entity.Character c = createTestCharacter(
                50,
                10,
                0,
                0.0,
                1.5,
                0.0
        );

        c.applyPoison(5, 3);  // 5 dmg ต่อเทิร์น นาน 3 เทิร์น

        assertTrue(c.isPoisoned());

        int hp1 = c.getHp();

        c.tickStatusEffectsAtTurnStart();  // turn 1
        int hp2 = c.getHp();
        assertTrue(hp2 < hp1);

        c.tickStatusEffectsAtTurnStart();  // turn 2
        int hp3 = c.getHp();
        assertTrue(hp3 < hp2);

        c.tickStatusEffectsAtTurnStart();  // turn 3
        int hp4 = c.getHp();
        assertTrue(hp4 < hp3);

        c.tickStatusEffectsAtTurnStart();
        int hp5 = c.getHp();
        assertEquals(hp4, hp5);
    }

    @Test
    void clearPoisonShouldRemovePoisonStatus() {
        entity.Character c = createTestCharacter(
                50,
                10,
                0,
                0.0,
                1.5,
                0.0
        );

        c.applyPoison(5, 3);
        assertTrue(c.isPoisoned());

        c.clearPoison();
        assertFalse(c.isPoisoned());

        int hpBefore = c.getHp();
        c.tickStatusEffectsAtTurnStart();
        assertEquals(hpBefore, c.getHp());
    }

    @Test
    void takeDamageShouldUseDefenseFirstAndNotReduceHpWhenDamageLessThanDefense() {
        entity.Character c = createTestCharacter(
                50,   // maxHp
                10,
                20,   // defense
                0.0,
                1.5,
                0.0
        );

        int hpBefore = c.getHp();
        int defBefore = c.getDefense();

        int taken = c.takeDamage(15);  // 15 < 20 → เกราะรับหมด

        assertEquals(0, taken);                 // HP ไม่โดนเลย
        assertEquals(hpBefore, c.getHp());      // hp เท่าเดิม
        assertEquals(defBefore - 15, c.getDefense()); // def ลดลงเท่าดาเมจ
    }

    @Test
    void basicAttackShouldApplyCritMultiplierWhenCritChanceIsOne() {
        entity.Character attacker = createTestCharacter(
                100,
                20,   // atk
                0,
                1.0,  // critChance = 1
                2.0,  // คูณ 2
                0.0
        );

        entity.Character defender = createTestCharacter(
                100,
                0,
                0,
                0.0,
                1.5,
                0.0
        );

        int hpBefore = defender.getHp();

        int dealt = attacker.basicAttack(defender);

        assertEquals(40, dealt);                // 20 * 2.0
        assertEquals(hpBefore - 40, defender.getHp());
    }

    @Test
    void poisonCanKillCharacterAndSetAliveFalse() {
        Character c = createTestCharacter(
                10,   // maxHp
                0,
                0,
                0.0,
                1.5,
                0.0
        );

        c.applyPoison(5, 3);
        assertTrue(c.isPoisoned());

        // turn 1: hp 10 -> 5
        c.tickStatusEffectsAtTurnStart();
        assertEquals(5, c.getHp());
        assertTrue(c.isAlive());

        // turn 2: hp 5 -> 0 ตาย
        c.tickStatusEffectsAtTurnStart();
        assertEquals(0, c.getHp());
        assertFalse(c.isAlive());
    }
}
