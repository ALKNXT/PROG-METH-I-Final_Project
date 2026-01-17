package interfaces;

/**
 * Interface for entities that can be attacked and take damage.
 */
public interface Attackable {

    /**
     * Inflicts damage on the entity.
     *
     * @param rawDamage The raw amount of damage dealt before any reduction
     * @return The actual damage taken by the entity
     */
    int takeDamage(int rawDamage);

    /**
     * Checks if the entity is still alive.
     *
     * @return true if the entity has health remaining, false otherwise
     */
    boolean isAlive();
}
