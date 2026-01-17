package items.weapon;

import items.Weapon;
import items.WeaponType;

/**
 * Represents a basic wooden stick weapon.
 * This is the initial weapon that the player starts with.
 */
public class WoodenStick extends Weapon {

    /**
     * Constructor to initialize the Wooden Stick by using super("Wooden Stick", 5, 0.05, 1.2, 0, 0, WeaponType.NORMAL).
     */
    public WoodenStick() {
        super("Wooden Stick", 5, 0.05, 1.2, 0, 0, WeaponType.NORMAL);
    }
}
