package items;

import interfaces.Purchasable;

/**
 * Represents an armor item that provides defense and dodge bonuses.
 */
public class Armor extends Item implements Purchasable {

    /** The cost of this armor in hearts. */
    private final int costHearts;

    /** The cost of this armor in brains. */
    private final int costBrains;

    /** The defense bonus provided by this armor. */
    private final int defenseBonus;

    /** The dodge chance bonus provided by this armor (percentage 0.0 - 1.0). */
    private final double dodgeChanceBonus;

    /**
     * Constructs a new Armor object.
     * @param name Name of the armor
     * @param defenseBonus Amount of defense to add
     * @param dodgeChanceBonus Amount of dodge chance to add (0.0 to 1.0)
     * @param costHearts Cost in hearts
     * @param costBrains Cost in brains
     */
    public Armor(String name, int defenseBonus, double dodgeChanceBonus, int costHearts, int costBrains) {
        super(name);
        this.defenseBonus = Math.max(0, defenseBonus);
        this.dodgeChanceBonus = Math.max(0, dodgeChanceBonus);
        this.costHearts = Math.max(0, costHearts);
        this.costBrains = Math.max(0, costBrains);
    }

    /**
     * Gets the defense bonus.
     * @return Defense value
     */
    public int getDefenseBonus() {
        return defenseBonus;
    }

    /**
     * Gets the dodge chance bonus.
     * @return Dodge chance (0.0 - 1.0)
     */
    public double getDodgeChanceBonus() {
        return dodgeChanceBonus;
    }

    /**
     * Gets the cost of the armor in hearts.
     * @return Cost in hearts
     */
    @Override
    public int getCostHearts() {
        return this.costHearts;
    }

    /**
     * Gets the cost of the armor in brains.
     * @return Cost in brains
     */
    @Override
    public int getCostBrains() {
        return this.costBrains;
    }

    /**
     * Returns a formatted description of the armor.
     * @return String containing stats and name
     */
    @Override
    public String getDescription() {
        return "[Armor] -> " + this.getName() + " | DEF : "
                + this.getDefenseBonus() + " | Dodge rate : "
                + (int) (this.getDodgeChanceBonus() * 100) + "% .";
    }
}
