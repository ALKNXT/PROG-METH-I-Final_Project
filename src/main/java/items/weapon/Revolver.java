package items.weapon;

import items.Weapon;
import items.WeaponType;

/**
 * Represents a revolver handgun.
 * An optional firearm available in Stage 4, offering high critical chance.
 */
public class Revolver extends Weapon {

    /**
     * Constructor to initialize the Revolver by using super("Revolver", 80, 0.40, 2.5, 180, 90, WeaponType.GUN).
     */
    public Revolver() {
        super("Revolver", 80, 0.40, 2.5, 180, 90, WeaponType.GUN);
    }
}
