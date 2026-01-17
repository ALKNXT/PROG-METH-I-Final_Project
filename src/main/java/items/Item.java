package items;

/**
 * Abstract base class representing any item in the game.
 */
public abstract class Item {

    /** The name of the item. */
    private String name;

    /**
     * Constructs an Item with the specified name.
     * @param name The name of the item
     */
    public Item(String name) {
        this.setName(name);
    }

    /**
     * Gets the name of the item.
     * @return The item's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the item.
     * @param name The new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the item.
     * @return A string describing the item details
     */
    public abstract String getDescription();
}
