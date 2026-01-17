package entity;

import items.WeaponType;

/**
 * Abstract base class for all Zombie enemies.
 */
public abstract class Zombie extends Character {

    /**
     * Initialize a zombie with specific stats.
     */
    public Zombie(int maxHp, int attackPower, int defense,
                  double critChance, double critDamage, double dodgeChance) {
        super(maxHp, attackPower, defense, critChance, critDamage, dodgeChance);
    }

    /**
     * Executes the zombie's special ability on the target.
     * @param target The target character (usually the player)
     */
    @Override
    public abstract void useSpecialAbility(Character target);

    /**
     * Gets the damage multiplier for a specific weapon type against this zombie.
     * @param weaponType The type of weapon used
     * @return Damage multiplier
     */
    public double getDamageMultiplier(WeaponType weaponType) {
        return 1.0;
    }
}
