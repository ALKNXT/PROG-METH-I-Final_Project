package shop;

import entity.Player;
import items.Armor;
import items.Potion;
import items.Weapon;
import items.WeaponType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShopTest {

    @Test
    void buyingWeaponShouldDecreaseOrgansAndEquipWeapon() {
        Player player = new Player("Hunter");

        player.addHearts(10);
        player.addBrains(5);

        Weapon rustyPipe = new Weapon(
                "Rusty Pipe",
                5,          // attackBonus
                0.0,        // critChanceBonus
                0.0,        // critDamageBonus
                3,          // costHearts
                1,          // costBrains
                WeaponType.NORMAL
        );

        Shop shop = new Shop(List.of(rustyPipe), List.of());

        boolean bought = shop.buyWeapon(player, rustyPipe);

        assertTrue(bought);
        assertEquals(rustyPipe, player.getWeapon(), "Player should equip bought weapon");
        assertEquals(10 - 3, player.getHeartParts(), "Hearts should be decreased by cost");
        assertEquals(5 - 1, player.getBrainParts(), "Brains should be decreased by cost");
    }

    @Test
    void buyingWeaponShouldFailWhenPlayerCannotAfford() {
        Player player = new Player("Hunter");

        Weapon pistol = new Weapon(
                "Old Pistol",
                10,
                0.05,
                0.3,
                2,
                0,
                WeaponType.GUN
        );

        Shop shop = new Shop(List.of(pistol), List.of());

        boolean bought = shop.buyWeapon(player, pistol);

        assertFalse(bought);
        assertNull(player.getWeapon(), "Player should not equip weapon when purchase fails");
    }

    @Test
    void buyingWeaponNotInShopShouldFail() {
        Player player = new Player("Hunter");
        player.addHearts(10);
        player.addBrains(10);

        Weapon inShop = new Weapon(
                "Pipe",
                5,
                0.0,
                0.0,
                2,
                0,
                WeaponType.NORMAL
        );

        Weapon notInShop = new Weapon(
                "Secret Gun",
                20,
                0.2,
                0.5,
                10,
                5,
                WeaponType.GUN
        );

        Shop shop = new Shop(List.of(inShop), List.of());

        boolean bought = shop.buyWeapon(player, notInShop);

        assertFalse(bought);
        assertNull(player.getWeapon());
    }

    @Test
    void buyingArmorShouldEquipAndDecreaseOrgans() {
        Player player = new Player("Hunter");
        player.addHearts(10);
        player.addBrains(5);

        Armor leatherJacket = new Armor(
                "Leather Jacket",
                5,       // defenseBonus
                0.05,    // dodgeChanceBonus
                3,       // costHearts
                1        // costBrains
        );

        Shop shop = new Shop(List.of(), List.of(leatherJacket));

        boolean bought = shop.buyArmor(player, leatherJacket);

        assertTrue(bought);
        assertEquals(leatherJacket, player.getArmor());
        assertEquals(10 - 3, player.getHeartParts());
        assertEquals(5 - 1, player.getBrainParts());
    }

    @Test
    void buyingPotionShouldAddToInventoryAndConsumeOrgans() {
        Player player = new Player("Hunter");
        player.addHearts(10);
        player.addBrains(5);

        Potion potion = new Potion("Small Medkit", 30, 3, 1);
        Shop shop = new Shop(List.of(), List.of(), List.of(potion));

        int heartsBefore = player.getHeartParts();
        int brainsBefore = player.getBrainParts();
        int hpBefore = player.getHp();

        boolean bought = shop.buyPotion(player, potion);

        int hpAfter = player.getHp();

        assertTrue(bought);
        assertEquals(hpBefore, hpAfter);
        assertEquals(heartsBefore - 3, player.getHeartParts());
        assertEquals(brainsBefore - 1, player.getBrainParts());
        assertTrue(player.getPotions().contains(potion));
    }

    @Test
    void potionConstructorShouldSetFieldsCorrectly() {
        Potion p = new Potion("Small Medkit", 30, 3, 1);

        assertEquals("Small Medkit", p.getName());
        assertEquals(30, p.getHealAmount());
        assertEquals(3, p.getCostHearts());
        assertEquals(1, p.getCostBrains());
    }

    @Test
    void usePotionShouldHealAndRemoveFromInventory() {
        Player player = new Player("Hunter");
        Potion potion = new Potion("Small Medkit", 30, 0, 0);

        player.addPotionToInventory(potion);

        player.setHp(60);

        int hpBefore = player.getHp();

        boolean used = player.usePotion(potion);
        int hpAfter = player.getHp();

        assertTrue(used, "Potion should be used successfully");
        assertTrue(hpAfter > hpBefore, "HP should increase after using potion");
        assertFalse(player.getPotions().contains(potion),
                "Potion should be removed from inventory after use");
    }
}