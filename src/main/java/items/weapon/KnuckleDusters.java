package items.weapon;

import items.Weapon;
import items.WeaponType;

/**
 * Represents knuckle-dusters.
 * An optional heavy weapon available in Stage 3, allowing for faster strikes.
 */
public class KnuckleDusters extends Weapon {

    /**
     * Constructor to initialize the Knuckle-Dusters by using super("Knuckle-Dusters", 45, 0.10, 1.5, 95, 45, WeaponType.HEAVY).
     */
    public KnuckleDusters() {
        super("Knuckle-Dusters", 45, 0.10, 1.5, 95, 45, WeaponType.HEAVY);
    }
}
