package shop;

import entity.Player;
import items.Armor;
import items.Potion;
import items.Weapon;

import java.util.Collections;
import java.util.List;

public class Shop {

    private final List<Weapon> weapons;
    private final List<Armor> armors;
    private final List<Potion> potions;

    public Shop(List<Weapon> weapons, List<Armor> armors) {
        this(weapons, armors, List.of());
    }

    /**
     * Canonical constructor to initialize the shop with all item types.
     * Ensures lists are not null.
     *
     * @param weapons List of weapons
     * @param armors  List of armors
     * @param potions List of potions
     */
    public Shop(List<Weapon> weapons, List<Armor> armors, List<Potion> potions) {
        this.weapons = (weapons != null) ? List.copyOf(weapons) : List.of();
        this.armors  = (armors  != null) ? List.copyOf(armors)  : List.of();
        this.potions = (potions != null) ? List.copyOf(potions) : List.of();
    }

    public List<Weapon> weapons() {
        return Collections.unmodifiableList(weapons);
    }
    public List<Armor> armors() {
        return Collections.unmodifiableList(armors);
    }
    public List<Potion> potions() {
        return Collections.unmodifiableList(potions);
    }

    /**
     * Attempts to buy a weapon for the player.
     *
     * @param player The player attempting to buy
     * @param weapon The weapon to buy
     * @return true if purchase is successful, false otherwise
     */
    public boolean buyWeapon(Player player, Weapon weapon) {
        if (player == null || weapon == null) return false;
        if (!weapons.contains(weapon)) return false;
        return player.buyAndEquipWeapon(weapon);
    }

    /**
     * Attempts to buy armor for the player.
     *
     * @param player The player attempting to buy
     * @param armor  The armor to buy
     * @return true if purchase is successful, false otherwise
     */
    public boolean buyArmor(Player player, Armor armor) {
        if (player == null || armor == null) return false;
        if (!armors.contains(armor)) return false;
        return player.buyAndEquipArmor(armor);
    }

    /**
     * Attempts to buy potion for the player.
     *
     * @param player The player attempting to buy
     * @param potion  The potion to buy
     * @return true if purchase is successful, false otherwise
     */
    public boolean buyPotion(Player player, Potion potion) {
        if (player == null || potion == null) return false;
        if (!potions.contains(potion)) return false;
        return player.buyPotionToInventory(potion);
    }
}
