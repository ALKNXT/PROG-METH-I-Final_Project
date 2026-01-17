package items.weapon;

import items.Weapon;
import items.WeaponType;

/**
 * Represents an axe.
 * A powerful optional weapon for the Boss stage.
 */
public class Axe extends Weapon {

    /**
     * Constructor to initialize the Axe by using super("Axe", 150, 0.50, 3.0, 350, 150, WeaponType.SHARP).
     */
    public Axe() {
        super("Axe", 150, 0.50, 3.0, 350, 150, WeaponType.SHARP);
    }
}
