package items.weapon;

import items.Weapon;
import items.WeaponType;

/**
 * Represents a combat knife weapon.
 * An optional sharp weapon available in Stage 2.
 */
public class CombatKnife extends Weapon {

    /**
     * Constructor to initialize the Combat Knife by using super("Combat Knife", 12, 0.30, 2.0, 35, 15, WeaponType.SHARP).
     */
    public CombatKnife() {
        super("Combat Knife", 12, 0.30, 2.0, 35, 15, WeaponType.SHARP);
    }
}
