package items.weapon;

import items.Weapon;
import items.WeaponType;

/**
 * Represents a machete weapon.
 * An optional sharp weapon available in Stage 2, offering better reach than a knife.
 */
public class Machete extends Weapon {

    /**
     * Constructor to initialize the Machete by using super("Machete", 25, 0.15, 1.5, 40, 15, WeaponType.SHARP).
     */
    public Machete() {
        super("Machete", 25, 0.15, 1.5, 40, 15, WeaponType.SHARP);
    }
}
