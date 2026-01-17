package entity;

import items.Armor;
import items.Weapon;
import items.WeaponType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void playerShouldStartAliveWithBaseStats() {
        Player p = new Player("Hunter");

        assertTrue(p.isAlive());
        assertEquals(p.getMaxHp(), p.getHp());
        assertTrue(p.getAttackPower() > 0);
        assertTrue(p.getDefense() >= 0);
    }

    @Test
    void organPartsShouldBeAddedAndPaidCorrectly() {
        Player p = new Player("Hunter");

        p.addHearts(3);
        p.addBrains(2);

        assertEquals(3, p.getHeartParts());
        assertEquals(2, p.getBrainParts());

        boolean canPayTooMuch = p.payCost(4, 1);
        assertFalse(canPayTooMuch);
        assertEquals(3, p.getHeartParts());
        assertEquals(2, p.getBrainParts());

        boolean canPay = p.payCost(2, 1);
        assertTrue(canPay);
        assertEquals(1, p.getHeartParts());  // 3 - 2
        assertEquals(1, p.getBrainParts());  // 2 - 1
    }

    @Test
    void equipWeaponShouldAdjustStats() {
        Player p = new Player("Hunter");

        int baseAttack = p.getAttackPower();
        double baseCritChance = p.getCritChance();
        double baseCritDamage = p.getCritDamage();

        Weapon sword = new Weapon(
                "Sharp Sword",
                10,            // attackBonus
                0.10,          // critChanceBonus
                0.50,          // critDamageBonus
                1,             // costHearts
                0,             // costBrains
                WeaponType.SHARP
        );

        p.equipWeapon(sword);

        assertEquals(baseAttack + 10, p.getAttackPower());
        assertEquals(baseCritChance + 0.10, p.getCritChance(), 1e-9);
        assertEquals(baseCritDamage + 0.50, p.getCritDamage(), 1e-9);
        assertEquals("Sharp Sword", p.getWeapon().getName());
    }

    @Test
    void equipArmorShouldAdjustStats() {
        Player p = new Player("Hunter");

        int baseDefense = p.getDefense();
        double baseDodge = p.getDodgeChance();

        Armor jacket = new Armor(
                "Light Armor",
                5,         // defenseBonus
                0.05,      // dodgeChanceBonus
                1,         // costHearts
                0          // costBrains
        );

        p.equipArmor(jacket);

        assertEquals(baseDefense + 5, p.getDefense());
        assertEquals(baseDodge + 0.05, p.getDodgeChance(), 1e-9);
        assertEquals("Light Armor", p.getArmor().getName());
    }

    @Test
    void specialAbilityShouldHealPlayerWhenAlive() {
        Player p = new Player("Hunter");

        p.setDodgeChance(0.0);
        p.takeDamage(50);
        int hpBefore = p.getHp();

        p.useSpecialAbility(null);

        assertTrue(p.getHp() > hpBefore);
        assertTrue(p.getHp() <= p.getMaxHp());
    }

    @Test
    void sharpWeaponShouldDealMoreDamageToRottenZombie() {
        Player p = new Player("Hunter");
        RottenZombie zombie = new RottenZombie();

        p.setCritChance(0.0);
        zombie.setDodgeChance(0.0);
        zombie.setDefense(0);

        Weapon normalWeapon = new Weapon(
                "Normal Club",
                10,
                0.0,
                0.0,
                0,
                0,
                WeaponType.NORMAL
        );

        Weapon sharpWeapon = new Weapon(
                "Sharp Knife",
                10,
                0.0,
                0.0,
                0,
                0,
                WeaponType.SHARP
        );

        p.equipWeapon(normalWeapon);
        int damageNormal = p.basicAttack(zombie);

        zombie.setHp(zombie.getMaxHp());

        p.equipWeapon(sharpWeapon);
        int damageSharp = p.basicAttack(zombie);

        assertEquals(damageNormal + 10, damageSharp);
    }

    @Test
    void heavyWeaponShouldLowerArmoredZombieDefenseBeforeAttack() {
        Player p = new Player("Hunter");
        ArmoredZombie zombie = new ArmoredZombie();

        p.setCritChance(0.0);
        zombie.setDodgeChance(0.0);

        zombie.setDefense(100);  // defense เริ่มต้น

        Weapon heavyWeapon = new Weapon(
                "War Hammer",
                0,
                0.0,
                0.0,
                0,
                0,
                WeaponType.HEAVY
        );

        p.equipWeapon(heavyWeapon);

        p.basicAttack(zombie);

        assertEquals(75, zombie.getDefense());
    }

    @Test
    void gunShouldLowerRunnerZombieDodgeChanceBeforeAttack() {
        Player p = new Player("Hunter");
        RunnerZombie zombie = new RunnerZombie();

        zombie.setDodgeChance(0.50); // เริ่มจาก 50%

        Weapon gun = new Weapon(
                "Handgun",
                10,
                0.0,
                0.0,
                0,
                0,
                WeaponType.GUN
        );

        p.equipWeapon(gun);

        p.basicAttack(zombie);

        assertEquals(0.45, zombie.getDodgeChance(), 1e-9);
    }
}