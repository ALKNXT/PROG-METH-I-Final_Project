package items;

import interfaces.Purchasable;

/**
 * Represents a weapon item that increases attack power and critical stats.
 */
public class Weapon extends Item implements Purchasable {

    /** The cost of this weapon in hearts. */
    private final int costHearts;

    /** The cost of this weapon in brains. */
    private final int costBrains;

    /** The attack damage bonus provided by this weapon. */
    private final int attackBonus;

    /** The critical chance bonus provided (0.0 - 1.0). */
    private final double critChanceBonus;

    /** The critical damage multiplier bonus. */
    private final double critDamageBonus;

    /** The type of the weapon (e.g., NORMAL, SHARP). */
    private final WeaponType weaponType;

    /**
     * Constructs a new Weapon object.
     * @param name Name of the weapon
     * @param attackBonus Attack power to add
     * @param critChanceBonus Critical chance to add
     * @param critDamageBonus Critical damage multiplier to add
     * @param costHearts Cost in hearts
     * @param costBrains Cost in brains
     * @param type Type of the weapon
     */
    public Weapon(String name, int attackBonus, double critChanceBonus, double critDamageBonus, int costHearts, int costBrains, WeaponType type) {
        super(name);
        this.attackBonus = attackBonus;
        this.critChanceBonus = Math.max(0, critChanceBonus);
        this.critDamageBonus = Math.max(0, critDamageBonus);
        this.costHearts = Math.max(0, costHearts);
        this.costBrains = Math.max(0, costBrains);
        this.weaponType = type;
    }

    /**
     * Gets the type of the weapon.
     * @return The WeaponType enum
     */
    public WeaponType getWeaponType() {
        return weaponType;
    }

    /**
     * Gets the attack bonus.
     * @return Attack power bonus
     */
    public int getAttackBonus() {
        return attackBonus;
    }

    /**
     * Gets the critical chance bonus.
     * @return Critical chance (0.0 - 1.0)
     */
    public double getCritChanceBonus() {
        return critChanceBonus;
    }

    /**
     * Gets the critical damage bonus.
     * @return Critical damage multiplier
     */
    public double getCritDamageBonus() {
        return critDamageBonus;
    }

    /**
     * Gets the cost of the weapon in hearts.
     * @return Cost in hearts
     */
    @Override
    public int getCostHearts() {
        return this.costHearts;
    }

    /**
     * Gets the cost of the weapon in brains.
     * @return Cost in brains
     */
    @Override
    public int getCostBrains() {
        return this.costBrains;
    }

    /**
     * Returns a formatted description of the weapon.
     * @return String containing stats and name
     */
    @Override
    public String getDescription() {
        return "[Weapon] -> " + this.getName() + " | ATK : " + this.getAttackBonus() + " | Crit rate : " + (int) (this.getCritChanceBonus() * 100) + "% .";
    }
}
