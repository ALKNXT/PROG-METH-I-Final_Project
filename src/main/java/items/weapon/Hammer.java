package items.weapon;

import items.Weapon;
import items.WeaponType;

/**
 * Represents a heavy hammer weapon.
 * An optional heavy weapon available in Stage 3, dealing high base damage.
 */
public class Hammer extends Weapon {

    /**
     * Constructor to initialize the Hammer by using super("Hammer", 50, 0.05, 1.2, 90, 40, WeaponType.HEAVY).
     */
    public Hammer() {
        super("Hammer", 50, 0.05, 1.2, 90, 40, WeaponType.HEAVY);
    }
}
