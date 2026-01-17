package items.weapon;

import items.Weapon;
import items.WeaponType;

/**
 * Represents a shotgun.
 * An optional firearm available in Stage 4, dealing massive raw damage.
 */
public class ShotGun extends Weapon {

    /**
     * Constructor to initialize the Shotgun by using super("Shotgun", 85, 0.10, 1.5, 170, 80, WeaponType.GUN).
     */
    public ShotGun() {
        super("Shotgun", 85, 0.10, 1.5, 170, 80, WeaponType.GUN);
    }
}
