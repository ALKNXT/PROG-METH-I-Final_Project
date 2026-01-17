package entity;

import interfaces.OrganDroppable;
import items.OrganParts;
import items.WeaponType;

/**
 * A fast zombie that can increase its dodge chance and weak to GUN weapons.
 */
public class RunnerZombie extends Zombie implements OrganDroppable {

    /**
     * Initialize a Runner Zombie by using super(200, 30, 10, 0.25, 1.75, 0.4).
     */
    public RunnerZombie() {
        super(200, 30, 10, 0.25, 1.75, 0.4);
    }

    // if player equip a melee weapon this zombie will increase his dodge chance

    /**
     * Increases dodge chance if the player uses a melee weapon (Heavy or Sharp).
     * @param target The target character (Player)
     */
    @Override
    public void useSpecialAbility(Character target) {
        Player player = (Player) target;

        if (player.getWeapon() == null) return;

        if (player.getWeapon().getWeaponType().equals(WeaponType.HEAVY) || player.getWeapon().getWeaponType().equals(WeaponType.SHARP)) {
            this.setDodgeChance(this.getDodgeChance() + 0.1);
        }
    }

    /**
     * Determines the items dropped when this zombie is defeated.
     * @return OrganParts containing hearts and brains
     */
    @Override
    public OrganParts getDropItem() {
        OrganParts dropItem = new OrganParts();

        // Drop Hearts: 280 - 320
        int hearts = random.nextInt(41) + 280;
        dropItem.addHearts(hearts);

        // Drop Brains: 120 - 150
        int brains = random.nextInt(31) + 120;
        dropItem.addBrains(brains);

        return dropItem;
    }
}
