package items.weapon;

import items.Weapon;
import items.WeaponType;

/**
 * Represents a high-powered rifle.
 * The ultimate firearm for the Boss stage.
 */
public class Rifle extends Weapon {

    /**
     * Constructor to initialize the Rifle by using super("Rifle", 150, 0.50, 3.0, 350, 150, WeaponType.GUN).
     */
    public Rifle() {
        super("Rifle", 150, 0.50, 3.0, 350, 150, WeaponType.GUN);
    }
}
