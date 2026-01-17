package entity;

import items.WeaponType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ZombieTest {

    private Zombie createTestZombie(
            int maxHp,
            int attackPower,
            int defense,
            double critChance,
            double critDamage,
            double dodgeChance
    ) {
        return new Zombie(maxHp, attackPower, defense, critChance, critDamage, dodgeChance) {
            @Override
            public void useSpecialAbility(Character target) {
            }
        };
    }

    @Test
    void zombieShouldStartAliveWithCorrectStats() {
        Zombie z = createTestZombie(
                100,    // maxHp
                20,     // attackPower
                5,      // defense
                0.10,   // critChance
                1.5,    // critDamage
                0.05    // dodgeChance
        );

        assertTrue(z.isAlive());
        assertEquals(100, z.getMaxHp());
        assertEquals(100, z.getHp());
        assertEquals(20, z.getAttackPower());
        assertEquals(5, z.getDefense());
        assertEquals(0.10, z.getCritChance(), 1e-9);
        assertEquals(1.5, z.getCritDamage(), 1e-9);
        assertEquals(0.05, z.getDodgeChance(), 1e-9);
    }

    @Test
    void getDamageMultiplierShouldReturnOneForAnyWeaponTypeByDefault() {
        Zombie z = createTestZombie(100, 10, 5, 0.0, 1.5, 0.0);

        for (WeaponType type : WeaponType.values()) {
            assertEquals(1.0, z.getDamageMultiplier(type),
                    "Base Zombie.getDamageMultiplier should return 1 for type " + type);
        }
    }

    @Test
    void basicAttackShouldDealAttackMinusDefenseWhenNoCritAndNoDodge() {
        // attacker: atk 20, no crit
        Zombie attacker = createTestZombie(
                100,   // maxHp
                20,    // attackPower
                0,     // defense
                0.0,   // critChance = 0
                1.5,   // critDamage
                0.0    // dodgeChance ของ attacker
        );

        // defender: def 5, no dodge
        Zombie defender = createTestZombie(
                100,   // maxHp
                10,    // attackPower
                5,     // defense
                0.0,   // critChance
                1.5,   // critDamage
                0.0    // dodgeChance = 0
        );

        int hpBefore = defender.getHp();

        int damageDealt = attacker.basicAttack(defender);

        // เน็ตดาเมจควร = atk(20) - def(5) = 15
        assertEquals(15, damageDealt);
        assertEquals(hpBefore - 15, defender.getHp());
    }

    @Test
    void takeDamageShouldReduceHpAndKillZombieAtZero() {
        Zombie z = createTestZombie(
                50,    // maxHp
                10,
                0,     // defense 0
                0.0,   // critChance
                1.5,
                0.0    // dodgeChance = 0
        );

        assertTrue(z.isAlive());

        int damageTaken = z.takeDamage(100);

        assertEquals(50, damageTaken);
        assertEquals(0, z.getHp());
        assertFalse(z.isAlive());
    }

    @Test
    void takeDamageShouldMissWhenDodgeChanceIsOne() {
        Zombie z = createTestZombie(
                50,
                10,
                0,
                0.0,
                1.5,
                1.0    // dodgeChance = 1
        );

        int hpBefore = z.getHp();

        int damageTaken = z.takeDamage(999);

        assertEquals(0, damageTaken);
        assertEquals(hpBefore, z.getHp());
        assertTrue(z.isAlive());
    }
}