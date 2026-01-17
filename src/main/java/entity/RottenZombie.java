package entity;

import interfaces.OrganDroppable;
import items.OrganParts;

/**
 * A decaying zombie that inflicts poison on attack and weak to SHARP weapons.
 */
public class RottenZombie extends Zombie implements OrganDroppable {

    /**
     * Initialize a Rotten Zombie by using super(170, 25, 5, 0.1, 1.5, 0.0).
     */
    public RottenZombie() {
        super(170, 25, 5, 0.1, 1.5, 0.0);
    }

    /**
     * Performs basic attack and inflicts poison on the target.
     * @param target The character to attack
     * @return Damage dealt
     */
    @Override
    public int basicAttack(Character target) {
        int damageDealt = super.basicAttack(target);

        if (damageDealt > 0 && target.isAlive()) {
            target.applyPoison(5, 3);
            System.out.println(
                    getClass().getSimpleName() +
                            " inflicts poison! Target will take 5 damage for 3 turns."
            );
        }
        return damageDealt;
    }

    /**
     * Executes the special ability (None for RottenZombie).
     */
    @Override
    public void useSpecialAbility(Character target) {
    }

    /**
     * Determines the items dropped when this zombie is defeated.
     * @return OrganParts containing hearts and brains
     */
    @Override
    public OrganParts getDropItem() {
        OrganParts dropItem = new OrganParts();

        // random dropped heart 70-90
        int hearts = random.nextInt(21) + 70;
        dropItem.addHearts(hearts);

        // random dropped brain 25-35
        int brains = random.nextInt(11) + 25;
        dropItem.addBrains(brains);

        return dropItem;
    }
}
