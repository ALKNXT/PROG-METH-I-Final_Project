package entity;

import interfaces.OrganDroppable;
import items.OrganParts;

/**
 * A standard zombie enemy with regeneration abilities.
 */
public class NormalZombie extends Zombie implements OrganDroppable {

    /**
     * Initialize a Normal Zombie by using super(100,15,5,0.10,1.5,0.05).
     */
    public NormalZombie() {
        super(
                100,   // maxHp
                15,    // attackPower
                5,     // defense (โล่)
                0.10,  // critChance 10%
                1.5,   // critDamage x1.5
                0.05   // dodgeChance 5%
        );
    }

    /**
     * Regenerates HP if current HP is below 50%.
     * @param target The target character (unused for this ability)
     */
    @Override
    public void useSpecialAbility(Character target) {
        if (!isAlive()) return;

        if (getHp() > 0 && getHp() < getMaxHp() / 2) {
            int before = getHp();
            int regenAmount = 5;
            heal(regenAmount);
            int healed = getHp() - before;
            if (healed > 0) {
                System.out.println(
                        getClass().getSimpleName() + " regenerates " +
                                healed + " HP! (" + getHp() + "/" + getMaxHp() + ")"
                );
            }
        }
    }

    /**
     * Determines the items dropped when this zombie is defeated.
     * @return OrganParts containing hearts and brains
     */
    @Override
    public OrganParts getDropItem() {
        OrganParts dropItem = new OrganParts();

        // random dropped heart 40-50
        int hearts = random.nextInt(11) + 40;
        dropItem.addHearts(hearts);

        // random dropped brain 15 or 18
        int brains = random.nextInt(4) + 15;
        dropItem.addBrains(brains);

        return dropItem;
    }
}
