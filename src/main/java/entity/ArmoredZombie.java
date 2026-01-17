package entity;

import interfaces.OrganDroppable;
import items.OrganParts;
import items.WeaponType;

/**
 * A tough zombie that gains defense against sharp weapons and weak to HEAVY weapons.
 */
public class ArmoredZombie extends Zombie implements OrganDroppable {

    /**
     * Initialize an Armored Zombie by using super(250, 30, 20, 0.1, 1.5, 0.0).
     */
    public ArmoredZombie() {
        super(250, 30, 20, 0.1, 1.5, 0.0);
    }

    // if player equip sharp weapon this zombie will increase his defense

    /**
     * Increases defense if the player is equipped with a sharp weapon.
     * @param target The target character (Player)
     */
    @Override
    public void useSpecialAbility(Character target) {
        Player player = (Player) target;
        if (player.getWeapon() != null && player.getWeapon().getWeaponType().equals(WeaponType.SHARP)) {
            this.setDefense(this.getDefense() + 5);
        }
    }

    /**
     * Determines the items dropped when this zombie is defeated.
     * @return OrganParts containing hearts and brains
     */
    @Override
    public OrganParts getDropItem() {
        OrganParts dropItem = new OrganParts();

        int hearts = random.nextInt(21) + 140;
        dropItem.addHearts(hearts);

        int brains = random.nextInt(21) + 50;
        dropItem.addBrains(brains);

        return dropItem;
    }
}
