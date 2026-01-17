package entity;

import interfaces.Attackable;

import java.util.Random;

/**
 * Base class representing any character in the game (Player or Zombie).
 */
public abstract class Character implements Attackable {

    /** Random generator for probability calculations. */
    protected final Random random = new Random();

    /** Maximum HP of the character. */
    protected int maxHp;

    /** Current HP of the character. */
    protected int hp;

    /** Attack power of the character. */
    protected int attackPower;

    /** Defense value to reduce incoming damage. */
    protected int defense;

    /** Probability to deal a critical hit (0.0 - 1.0). */
    protected double critChance;

    /** Damage multiplier for critical hits. */
    protected double critDamage;

    /** Probability to dodge an attack (0.0 - 1.0). */
    protected double dodgeChance;

    /** Status indicating if the character is alive. */
    protected boolean alive;

    /** Status indicating if the last attack was a critical hit. */
    protected boolean lastAttackCritical = false;

    /** Remaining turns for poison effect. */
    protected int poisonTurns;

    /** Damage taken per turn from poison. */
    protected int poisonDamagePerTurn;

    /**
     * Initialize a new Character with specific stats.
     * @param maxHp Maximum Health Points
     * @param attackPower Base Attack Power
     * @param defense Base Defense
     * @param critChance Chance to critical hit
     * @param critDamage Damage multiplier on critical hit
     * @param dodgeChance Chance to dodge attacks
     */
    public Character(int maxHp,
                     int attackPower,
                     int defense,
                     double critChance,
                     double critDamage,
                     double dodgeChance) {

        this.maxHp = Math.max(1, maxHp);
        this.hp = this.maxHp;

        this.attackPower = Math.max(0, attackPower);
        this.defense = Math.max(0, defense);

        this.critChance = clamp01(critChance);
        this.critDamage = Math.max(1.0, critDamage);
        this.dodgeChance = clamp01(dodgeChance);

        this.alive = true;
    }

    // ----------------- Helper ----------------- //

    /**
     * Clamps a double value between 0.0 and 1.0.
     * @param value The value to clamp
     * @return Clamped value
     */
    private double clamp01(double value) {
        if (value < 0.0) return 0.0;
        if (value > 1.0) return 1.0;
        return value;
    }

    // ----------------- Getter / Setter ----------------- //

    /**
     * a getter for maxHp
     * @return maxHp
     */
    public int getMaxHp() {
        return maxHp;
    }

    /**
     * a getter for hp
     * @return hp
     */
    public int getHp() {
        return hp;
    }

    /**
     * a setter for hp if hp < 0, set it to 0.
     */
    public void setHp(int hp) {
        this.hp = Math.min(Math.max(0, hp), maxHp);
        if (this.hp <= 0) {
            this.hp = 0;
            this.alive = false;
        }
    }

    /**
     * a getter for attackPower
     * @return attackPower
     */
    public int getAttackPower() {
        return attackPower;
    }

    /**
     * a setter for attackPower if attackPower < 0, set it to 0.
     */
    public void setAttackPower(int attackPower) {
        this.attackPower = Math.max(0, attackPower);
    }

    /**
     *  a getter for defense
     * @return defense
     */
    public int getDefense() {
        return defense;
    }

    /**
     * a setter for defense if defense < 0, set it to 0.
     */
    public void setDefense(int defense) {
        this.defense = Math.max(0, defense);
    }

    /**
     * a getter for critChance
     * @return critChance
     */
    public double getCritChance() {
        return critChance;
    }

    /**
     * a setter for critChance.
     */
    public void setCritChance(double critChance) {
        this.critChance = clamp01(critChance);
    }

    /**
     * a getter for critDamage
     * @return critDamage
     */
    public double getCritDamage() {
        return critDamage;
    }

    /**
     * a setter for critDamage if critDamage < 1, set it to 1.
     */
    public void setCritDamage(double critDamage) {
        this.critDamage = Math.max(1.0, critDamage);
    }

    /**
     * a getter for dodgeChance
     * @return dodgeChance
     */
    public double getDodgeChance() {
        return dodgeChance;
    }

    /**
     * a setter for dodgeChance.
     */
    public void setDodgeChance(double dodgeChance) {
        this.dodgeChance = clamp01(dodgeChance);
    }

    /**
     * a getter for alive
     * @return
     */
    public boolean isAlive() {
        return alive;
    }

    // ----------------- Core Combat Logic ----------------- //

    /**
     * a method to attack the enemy
     * @param target
     * @return damage
     */
    public int basicAttack(Character target) {
        lastAttackCritical = false;

        int damage = attackPower;

        // roll คริ
        boolean isCrit = random.nextDouble() < critChance;
        if (isCrit) {
            lastAttackCritical = true;
            damage = (int) Math.round(damage * critDamage);
            onCriticalHit(target, damage);
        }

        return target.takeDamage(damage);
    }

    /**
     * a method for take damage from enemy
     * @param rawDamage
     * @return actualDamage
     */
    @Override
    public int takeDamage(int rawDamage) {
        if (rawDamage <= 0 || !alive) return 0;

        if (random.nextDouble() < dodgeChance) {
            onDodge();
            return 0;
        }

        int defBefore = defense;
        int damageToHp;

        if (defense >= rawDamage) {
            defense -= rawDamage;
            damageToHp = 0;

            System.out.println(
                    getClass().getSimpleName() +
                            " blocks the hit with armor. " +
                            "DEF " + defBefore + " → " + defense +
                            ", HP " + hp + "/" + maxHp
            );
        } else {
            damageToHp = rawDamage - defense;
            defense = 0;

            System.out.println(
                    getClass().getSimpleName() +
                            "'s armor breaks! " +
                            "DEF " + defBefore + " → 0, " +
                            "taking " + damageToHp + " damage to HP."
            );
        }

        int actualDamageTaken = Math.min(this.hp, damageToHp);
        if (actualDamageTaken > 0) {
            setHp(this.hp - actualDamageTaken);
            onDamaged(actualDamageTaken);
        }

        if (!alive) {
            onDeath();
        }

        return actualDamageTaken;
    }

    /**
     * a method for heal this character
     * @param amount
     */
    public void heal(int amount) {
        if (amount <= 0 || !alive) return;
        int oldHp = hp;
        setHp(hp + amount);
        onHeal(hp - oldHp);
    }

    // ----------------- Hook methods ----------------- //

    /**
     * this method call when character hit a critical
     * @param target
     * @param critDamage
     */
    protected void onCriticalHit(Character target, int critDamage) {
        System.out.println(getClass().getSimpleName() + " lands a CRITICAL hit for " + critDamage + "!");
    }

    /**
     * @return lastAttackCritical
     */
    public boolean wasLastAttackCritical() {
        return lastAttackCritical;
    }

    /**
     * this method call when character dodge
     */
    protected void onDodge() {
        System.out.println(getClass().getSimpleName() + " dodged the attack!");
    }

    /**
     * this method call when this character take damage from enemy
     * @param damageTaken
     */
    protected void onDamaged(int damageTaken) {
        System.out.println(getClass().getSimpleName() + " took " + damageTaken + " damage. HP: " + hp + "/" + maxHp);
    }

    /**
     * this method call when this character heal
     * @param healedAmount
     */
    protected void onHeal(int healedAmount) {
        if (healedAmount > 0) {
            System.out.println(getClass().getSimpleName() + " healed " + healedAmount + " HP. HP: " + hp + "/" + maxHp);
        }
    }

    /**
     * this method call when this character dead
     */
    protected void onDeath() {
        System.out.println(getClass().getSimpleName() + " has been defeated.");
    }

    // ----------------- Status Effects: Poison ----------------- //

    /**
     * Checks if the character is currently poisoned.
     * @return true if poison turns > 0
     */
    public boolean isPoisoned() {
        return poisonTurns > 0;
    }

    /**
     * Applies poison effect to the character.
     * @param damagePerTurn Damage to take each turn
     * @param turns Number of turns the poison lasts
     */
    public void applyPoison(int damagePerTurn, int turns) {
        if (damagePerTurn <= 0 || turns <= 0 || !alive) return;

        poisonDamagePerTurn = Math.max(poisonDamagePerTurn, damagePerTurn);
        poisonTurns = Math.max(poisonTurns, turns);
    }

    /**
     * Clears all poison effects.
     */
    public void clearPoison() {
        poisonTurns = 0;
        poisonDamagePerTurn = 0;
    }

    /**
     * Applies status effects (like poison) at the start of the turn.
     */
    public void tickStatusEffectsAtTurnStart() {
        if (!alive) return;

        if (poisonTurns > 0) {
            int before = hp;

            int dmg = Math.min(poisonDamagePerTurn, hp);
            setHp(hp - dmg);

            int actual = before - hp;
            System.out.println(
                    getClass().getSimpleName() +
                            " suffers " + actual + " poison damage. " +
                            "HP: " + hp + "/" + maxHp +
                            " (" + poisonTurns + " turns of poison left)"
            );

            poisonTurns--;
        }
    }

    // ----------------- Abstract Section ----------------- //

    /**
     * a abstract method for each character
     * @param target
     */
    public abstract void useSpecialAbility(Character target);
}
