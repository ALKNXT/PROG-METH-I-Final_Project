package items;

import interfaces.Purchasable;

/**
 * Represents a potion item used to restore health.
 */
public class Potion extends Item implements Purchasable {

    /** The amount of HP this potion restores. */
    private final int healAmount;

    /** The cost of this potion in hearts. */
    private final int costHearts;

    /** The cost of this potion in brains. */
    private final int costBrains;

    /**
     * Constructs a new Potion.
     * @param name Name of the potion
     * @param healAmount Amount of HP to heal
     * @param costHearts Cost in hearts
     * @param costBrains Cost in brains
     */
    public Potion(String name, int healAmount, int costHearts, int costBrains) {
        super(name);
        this.healAmount = Math.max(0, healAmount);
        this.costHearts = Math.max(0, costHearts);
        this.costBrains = Math.max(0, costBrains);
    }

    /**
     * Gets the healing amount of the potion.
     * @return Amount of HP restored
     */
    public int getHealAmount() {
        return healAmount;
    }

    /**
     * Gets the cost of the potion in hearts.
     * @return Cost in hearts
     */
    @Override
    public int getCostHearts() {
        return costHearts;
    }

    /**
     * Gets the cost of the potion in brains.
     * @return Cost in brains
     */
    @Override
    public int getCostBrains() {
        return costBrains;
    }

    /**
     * Returns a formatted description of the potion.
     * @return String containing heal amount and costs
     */
    @Override
    public String getDescription() {
        return "[Potion] -> " + getName() +
                " | Heal : " + healAmount + " HP" +
                " | Cost H:" + costHearts +
                " B:" + costBrains;
    }
}
