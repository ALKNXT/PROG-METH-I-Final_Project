package items;

/**
 * Represents the currency items (Hearts and Brains) collected in the game.
 */
public class OrganParts extends Item {

    /** The quantity of hearts collected. */
    private int hearts;

    /** The quantity of brains collected. */
    private int brains;

    /**
     * Constructs a new OrganParts container with zero resources.
     */
    public OrganParts() {
        super("Organ Parts");
        this.hearts = 0;
        this.brains = 0;
    }

    /**
     * Gets the current number of hearts.
     * @return Amount of hearts
     */
    public int getHearts() {
        return hearts;
    }

    /**
     * Gets the current number of brains.
     * @return Amount of brains
     */
    public int getBrains() {
        return brains;
    }

    /**
     * Adds a specified amount of hearts to the inventory.
     * @param amount Amount to add
     */
    public void addHearts(int amount) {
        if (amount <= 0) return;
        hearts += amount;
    }

    /**
     * Adds a specified amount of brains to the inventory.
     * @param amount Amount to add
     */
    public void addBrains(int amount) {
        if (amount <= 0) return;
        brains += amount;
    }

    /**
     * Checks if there are enough resources to pay the specified cost.
     * @param heartsCost Required hearts
     * @param brainsCost Required brains
     * @return true if affordable, false otherwise
     */
    public boolean canAfford(int heartsCost, int brainsCost) {
        return hearts >= heartsCost && brains >= brainsCost;
    }

    /**
     * Deducts the specified cost from the inventory if affordable.
     * @param heartsCost Cost in hearts
     * @param brainsCost Cost in brains
     * @return true if payment was successful, false if insufficient funds
     */
    public boolean pay(int heartsCost, int brainsCost) {
        if (!canAfford(heartsCost, brainsCost)) {
            return false;
        }
        hearts -= heartsCost;
        brains -= brainsCost;
        return true;
    }

    /**
     * Returns a description of the current currency balance.
     * @return String showing hearts and brains count
     */
    @Override
    public String getDescription() {
        return "[Currency] -> Hearts : " + this.getHearts() + ", Brains : " + this.getBrains();
    }
}
