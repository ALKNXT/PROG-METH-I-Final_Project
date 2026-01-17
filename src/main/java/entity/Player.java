package entity;

import items.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the main player character controlled by the user.
 */
public class Player extends Character {

    /** Name of the player. */
    private final String name;

    /** Inventory of organ parts collected. */
    private final OrganParts organParts = new OrganParts();
    // inventory for Potions

    /** List of potions in inventory. */
    private final List<Potion> potions = new ArrayList<>();

    /** Currently equipped weapon. */
    private Weapon weapon;

    /** Currently equipped armor. */
    private Armor armor;

    /**
     * Initialize the player with default stats and name.
     * @param name Name of the player
     */
    public Player(String name) {
        super(
                120,   // maxHp
                20,    // attackPower
                8,     // defense
                0.15,  // critChance 15%
                1.7,   // critDamage x1.7
                0.10   // dodgeChance 10%
        );
        this.name = name;
    }

    // ---------- Getter / Setter ---------- //

    /**
     * Gets the name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the current amount of heart parts.
     */
    public int getHeartParts() {
        return organParts.getHearts();
    }

    /**
     * Gets the current amount of brain parts.
     */
    public int getBrainParts() {
        return organParts.getBrains();
    }

    /**
     * Adds heart parts to inventory.
     * @param amount Amount to add
     */
    public void addHearts(int amount) {
        organParts.addHearts(amount);
        System.out.println(name + " gained " + amount +
                " hearts. Total hearts: " + organParts.getHearts());
    }

    /**
     * Adds brain parts to inventory.
     * @param amount Amount to add
     */
    public void addBrains(int amount) {
        organParts.addBrains(amount);
        System.out.println(name + " gained " + amount +
                " brains. Total brains: " + organParts.getBrains());
    }

    /**
     * Checks if player can afford the cost and deducts it if possible.
     */
    public boolean payCost(int heartsCost, int brainsCost) {
        if (!organParts.canAfford(heartsCost, brainsCost)) {
            System.out.println(name + " cannot afford this. " +
                    "Need H:" + heartsCost + " B:" + brainsCost +
                    " but has H:" + organParts.getHearts() +
                    " B:" + organParts.getBrains());
            return false;
        }
        organParts.pay(heartsCost, brainsCost);
        System.out.println(name + " spent H:" + heartsCost +
                " B:" + brainsCost + ". Now H:" + organParts.getHearts() +
                " B:" + organParts.getBrains());
        return true;
    }

    // ---------- ระบบอุปกรณ์สวมใส่ ---------- //

    /**
     * @return current holding weapon
     */
    public Weapon getWeapon() {
        return weapon;
    }

    /**
     * @return current holding armor
     */
    public Armor getArmor() {
        return armor;
    }

    /**
     * this method call when player equip new weapon
     * @param newWeapon
     */
    public void equipWeapon(Weapon newWeapon) {
        if (this.weapon != null) {
            setAttackPower(getAttackPower() - this.weapon.getAttackBonus());
            setCritChance(getCritChance() - this.weapon.getCritChanceBonus());
            setCritDamage(getCritDamage() - this.weapon.getCritDamageBonus());
        }

        this.weapon = newWeapon;

        if (newWeapon != null) {
            setAttackPower(getAttackPower() + newWeapon.getAttackBonus());
            setCritChance(getCritChance() + newWeapon.getCritChanceBonus());
            setCritDamage(getCritDamage() + newWeapon.getCritDamageBonus());
        }

        System.out.println(name + " equipped weapon: " +
                (newWeapon == null ? "None" : newWeapon.getName()));
    }

    public void equipArmor(Armor newArmor) {
        if (this.armor != null) {
            setDefense(getDefense() - this.armor.getDefenseBonus());
            setDodgeChance(getDodgeChance() - this.armor.getDodgeChanceBonus());
        }

        this.armor = newArmor;

        if (newArmor != null) {
            setDefense(getDefense() + newArmor.getDefenseBonus());
            setDodgeChance(getDodgeChance() + newArmor.getDodgeChanceBonus());
        }

        System.out.println(name + " equipped armor: " +
                (newArmor == null ? "None" : newArmor.getName()));
    }

    // ---------- เมธอดช่วยซื้อของแบบสะดวก (อาวุธ/เกราะ) ---------- //

    /**
     * this method call when player buy new weapon
     * @param shopWeapon
     * @return a buy status
     */
    public boolean buyAndEquipWeapon(Weapon shopWeapon) {
        if (shopWeapon == null) return false;
        if (!payCost(shopWeapon.getCostHearts(), shopWeapon.getCostBrains())) {
            return false;
        }
        equipWeapon(shopWeapon);
        return true;
    }

    /**
     * this method call when player buy new armor
     * @param shopArmor
     * @return a buy status
     */
    public boolean buyAndEquipArmor(Armor shopArmor) {
        if (shopArmor == null) return false;
        if (!payCost(shopArmor.getCostHearts(), shopArmor.getCostBrains())) {
            return false;
        }
        equipArmor(shopArmor);
        return true;
    }

    // ---------- INVENTORY: Potion ---------- //

    /**
     * @return potion from Collections
     */
    public List<Potion> getPotions() {
        return Collections.unmodifiableList(potions);
    }

    /**
     * add new potion to inventory
     * @param potion
     */
    public void addPotionToInventory(Potion potion) {
        if (potion == null) return;
        potions.add(potion);
        System.out.println(name + " obtained potion: " + potion.getName());
    }

    /**
     * this method call for buy new potion, then add it to player's inventory
     * @param potion
     * @return buyStatus
     */
    public boolean buyPotionToInventory(Potion potion) {
        if (potion == null) return false;
        if (!payCost(potion.getCostHearts(), potion.getCostBrains())) {
            return false;
        }
        addPotionToInventory(potion);
        return true;
    }

    /**
     * this method call when player use potion
     * @param potion
     * @return Restores hp
     */
    public boolean usePotion(Potion potion) {
        if (potion == null) return false;
        if (!isAlive()) return false;
        if (!potions.contains(potion)) return false;

        int before = getHp();
        heal(potion.getHealAmount());
        int healed = getHp() - before;

        potions.remove(potion);

        System.out.println(
                name + " used " + potion.getName() +
                        " and healed " + healed +
                        " HP! (" + getHp() + "/" + getMaxHp() + ")"
        );

        return healed > 0;
    }

    // ---------- สกิลพิเศษของ Player ---------- //

    /**
     * this method call when player using player's special ability
     * @param target
     */
    @Override
    public void useSpecialAbility(Character target) {
        if (!isAlive()) return;

        int healAmount = (int) (getMaxHp() * 0.3); // 30% max HP
        int before = getHp();
        heal(healAmount);
        int healed = getHp() - before;

        if (healed > 0) {
            System.out.println(
                    name + " uses Adrenaline Shot and heals " +
                            healed + " HP! (" + getHp() + "/" + getMaxHp() + ")"
            );
        } else {
            System.out.println(name + " tries to use Adrenaline Shot, but HP is already full.");
        }
    }

    // ---------- Override hook methods ให้ log อ่านง่าย ---------- //

    @Override
    protected void onCriticalHit(Character target, int critDamage) {
        System.out.println(name + " lands a CRITICAL hit on " +
                target.getClass().getSimpleName() +
                " for " + critDamage + " damage!");
    }

    @Override
    protected void onDodge() {
        System.out.println(
                name + " dodged the attack! HP: " + getHp() + "/" + getMaxHp()
        );
    }

    @Override
    protected void onDamaged(int damageTaken) {
        System.out.println(name + " took " + damageTaken +
                " damage. HP: " + getHp() + "/" + getMaxHp());
    }

    @Override
    protected void onHeal(int healedAmount) {
        if (healedAmount > 0) {
            System.out.println(name + " healed " + healedAmount +
                    " HP. HP: " + getHp() + "/" + getMaxHp());
        }
    }

    @Override
    protected void onDeath() {
        System.out.println(name + " has fallen...");
    }

    // ---------- basic attack สำหรับ player vs zombie (แพ้-ชนะตาม type) ---------- //

    /**
     * Performs a basic attack on the target, applying weapon bonuses.
     * @param target The character to attack
     * @return The actual damage dealt
     */
    @Override
    public int basicAttack(Character target) {
        // ถ้าเป้าไม่ใช่ Zombie หรือยังไม่มีอาวุธ → ใช้ logic เดิมใน Character
        if (!(target instanceof Zombie zombie) || getWeapon() == null) {
            return super.basicAttack(target);
        }

        WeaponType weaponType = this.getWeapon().getWeaponType();

        int damage = attackPower;

        if (zombie instanceof RottenZombie) {
            if (weaponType == WeaponType.SHARP) {
                damage += 10; // SHARP แรงขึ้นกับ RottenZombie
            }
        } else if (zombie instanceof ArmoredZombie) {
            if (weaponType == WeaponType.HEAVY) {
                zombie.setDefense(zombie.getDefense() - 5); // HEAVY ลดเกราะก่อน
            }
        } else if (zombie instanceof RunnerZombie) {
            if (weaponType == WeaponType.GUN) {
                zombie.setDodgeChance(zombie.getDodgeChance() - 0.05); // GUN ลด dodge
            }
        }

        // --- ส่วนคริ ---
        lastAttackCritical = false;   // รีเซ็ตก่อน
        boolean isCrit = random.nextDouble() < critChance;
        if (isCrit) {
            lastAttackCritical = true;   // บันทึกว่ารอบนี้คริ
            damage = (int) Math.round(damage * critDamage);
            onCriticalHit(target, damage);
        }

        return target.takeDamage(damage);
    }
}