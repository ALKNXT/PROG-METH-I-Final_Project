package interfaces;

/**
 * Interface for items that can be purchased in the shop.
 */
public interface Purchasable {

    /**
     * Gets the cost of the item in hearts.
     *
     * @return The heart cost
     */
    int getCostHearts();

    /**
     * Gets the cost of the item in brains.
     *
     * @return The brain cost
     */
    int getCostBrains();
}
