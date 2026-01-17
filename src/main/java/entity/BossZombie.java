package entity;

import interfaces.OrganDroppable;
import items.OrganParts;

/**
 * The powerful Boss Zombie with enrage mechanics.
 */
public class BossZombie extends Zombie implements OrganDroppable {

    /** Status indicating if the boss is enraged. */
    private boolean isEnraged;

    /**
     * Initialize the Boss Zombie by using super(350,25,12,0.20,1.5,0.01).
     */
    public BossZombie() {
        super(
                350,
                25,
                12,
                0.20,
                1.5,
                0.01
        );
        this.isEnraged = false;
    }

    /**
     * Checks if the boss is currently enraged.
     * @return true if enraged
     */
    public boolean isEnraged() {
        return isEnraged;
    }

    /**
     * Triggers enrage mode if HP is below 50%, increasing attack power.
     */
    public void enrage() {
        if (!isAlive()) return;

        if (!isEnraged && getHp() <= getMaxHp() * 0.5) {
            isEnraged = true;

            int boostAmount = (int) (getAttackPower() * 0.5);
            setAttackPower(getAttackPower() + boostAmount);

            System.out.println(">>> " + getClass().getSimpleName() + " ROARS LOUDLY! It becomes ENRAGED! (Attack UP for 50%)");
            return;
        }
    }

    /**
     * Uses Toxic Smash to deal high damage to the target.
     * @param target The target character
     */
    @Override
    public void useSpecialAbility(Character target) {
        if (!isAlive()) return;

        System.out.println(getClass().getSimpleName() + " uses Toxic Smash!");

        int skillDamage = (int) (getAttackPower() * 1.5);
        int actualDamage = target.takeDamage(skillDamage);

        System.out.println("... smashing " + target.getClass().getSimpleName() + " for " + actualDamage + " damage!");
    }

    /**
     * Determines the items dropped when this zombie is defeated.
     * @return OrganParts containing hearts and brains
     */
    @Override
    public OrganParts getDropItem() {
        OrganParts dropItem = new OrganParts();

        dropItem.addHearts(100);
        dropItem.addBrains(100);

        return dropItem;
    }
}
