package ui;

import controller.GameController;
import items.Armor;
import items.Potion;
import items.Weapon;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Manages the UI for the player's inventory.
 */
public class InventoryView {

    /**
     * Reference to the game controller.
     */
    private final GameController controller;

    /**
     * The main layout container for the inventory view.
     */
    private VBox layout;

    /**
     * The boolean to check for return to battle
     */
    private boolean returnToBattle;

    /**
     * List view to display owned weapons.
     */
    private ListView<Weapon> weaponList;

    /**
     * List view to display owned armors.
     */
    private ListView<Armor> armorList;

    /**
     * ListView for displaying potions.
     */
    private ListView<Potion> potionList;

    /**
     * Label to display current weapon.
     */
    private Label lblCurrentWeapon;

    /**
     * Label to display current armor.
     */
    private Label lblCurrentArmor;

    /**
     * Constructor to initialize the InventoryView.
     *
     * @param controller The game controller
     */
    public InventoryView(GameController controller) {
        this.controller = controller;
        initLayout();
    }

    /**
     * Initializes the layout and UI components of the inventory.
     */
    private void initLayout() {
        layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        //layout.setStyle("-fx-background-color: #f4f4f4;");
        layout.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #101521, #05070a);"
        );

        Label header = new Label("INVENTORY");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        header.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.3, 0, 3);"
        );

        VBox statusBox = new VBox(5);
        statusBox.setAlignment(Pos.CENTER);
        //statusBox.setStyle("-fx-background-color: #ddd; -fx-padding: 10; -fx-background-radius: 5;");
        statusBox.setStyle(
                "-fx-background-color: rgba(15,22,34,0.95);" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 10 20;"
        );

        lblCurrentWeapon = new Label("Weapon: None");
        lblCurrentWeapon.setTextFill(Color.LIGHTBLUE);
        lblCurrentArmor = new Label("Armor: None");
        lblCurrentArmor.setTextFill(Color.LIGHTGREEN);

        Label equippedTitle = new Label("--- EQUIPPED ---");
        equippedTitle.setTextFill(Color.web("#FFD54F")); // เหลืองทอง
        equippedTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        statusBox.getChildren().addAll(equippedTitle, lblCurrentWeapon, lblCurrentArmor);

        HBox listsContainer = new HBox(20);
        listsContainer.setAlignment(Pos.CENTER);

        // Weapon
        VBox wBox = new VBox(10);
        wBox.setAlignment(Pos.CENTER);
        Label wTitle = new Label("Weapons");
        wTitle.setTextFill(Color.web("#E0E6FF")); // ฟ้าอ่อน ๆ ตัดกับพื้นหลัง
        wTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        wBox.getChildren().add(wTitle);
        weaponList = new ListView<>();
        weaponList.setPrefHeight(250);
        weaponList.setStyle(
                "-fx-background-color: rgba(0,0,0,0.45);" +
                        "-fx-control-inner-background: transparent;" +
                        "-fx-border-color: rgba(255,255,255,0.15);" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;"
        );
        weaponList.setCellFactory(param -> new ListCell<Weapon>() {
            @Override
            protected void updateItem(Weapon item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else {
                    setText(item.getName() + " (Atk: " + item.getAttackBonus() + ")");
                    setTextFill(Color.web("#FAFAFA")); // ขาวเกือบสนิท
                }
            }
        });
        Button btnEquipW = new Button("Equip Weapon");
        stylePillButton(btnEquipW);
        btnEquipW.setOnAction(e -> {
            Weapon w = weaponList.getSelectionModel().getSelectedItem();
            if (w != null) {
                controller.getPlayer().equipWeapon(w);
                refreshDisplay();
            }
        });
        wBox.getChildren().addAll(weaponList, btnEquipW);

        // Armor
        VBox aBox = new VBox(10);
        aBox.setAlignment(Pos.CENTER);
        Label aTitle = new Label("Armors");
        aTitle.setTextFill(Color.web("#E0E6FF"));
        aTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        aBox.getChildren().add(aTitle);
        armorList = new ListView<>();
        armorList.setPrefHeight(250);
        armorList.setStyle(
                "-fx-background-color: rgba(0,0,0,0.45);" +
                        "-fx-control-inner-background: transparent;" +
                        "-fx-border-color: rgba(255,255,255,0.15);" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;"
        );
        armorList.setCellFactory(param -> new ListCell<Armor>() {
            @Override
            protected void updateItem(Armor item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(item.getName() + " (Def: " + item.getDefenseBonus() + ")");
            }
        });
        Button btnEquipA = new Button("Equip Armor");
        stylePillButton(btnEquipA);
        btnEquipA.setOnAction(e -> {
            Armor a = armorList.getSelectionModel().getSelectedItem();
            if (a != null) {
                controller.getPlayer().equipArmor(a);
                refreshDisplay();
            }
        });
        aBox.getChildren().addAll(armorList, btnEquipA);

        // Potion
        VBox pBox = new VBox(10);
        pBox.setAlignment(Pos.CENTER);
        Label pTitle = new Label("Potions");
        pTitle.setTextFill(Color.web("#E0E6FF"));
        pTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        pBox.getChildren().add(pTitle);
        potionList = new ListView<>();
        potionList.setPrefHeight(250);
        potionList.setStyle(
                "-fx-background-color: rgba(0,0,0,0.45);" +
                        "-fx-control-inner-background: transparent;" +
                        "-fx-border-color: rgba(255,255,255,0.15);" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;"
        );
        potionList.setCellFactory(param -> new ListCell<Potion>() {
            @Override
            protected void updateItem(Potion item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(item.getName() + " (Heal: " + item.getHealAmount() + ")");
            }
        });
        Button btnUsePotion = new Button("Use Potion");
        stylePillButton(btnUsePotion);
        btnUsePotion.setOnAction(e -> {
            Potion p = potionList.getSelectionModel().getSelectedItem();
            if (p != null) {
                boolean used = controller.getPlayer().usePotion(p);
                if (used) {
                    refreshList(); // อัปเดต list ให้หายไป
                }
            }
        });
        pBox.getChildren().addAll(potionList, btnUsePotion);

        listsContainer.getChildren().addAll(wBox, aBox, pBox);


        Button btnBack = new Button("BACK");
        stylePillButton(btnBack);
        btnBack.setPrefWidth(100);
        btnBack.setOnAction(e -> controller.backFromInventory(returnToBattle));

        layout.getChildren().addAll(header, statusBox, listsContainer, btnBack);
    }

    /**
     * set returnToBattle to val
     * @param val
     */
    public void setReturnToBattle(boolean val) {
        this.returnToBattle = val;
    }

    /**
     * Refreshes the inventory list with current data.
     */
    public void refreshList() {
        weaponList.getItems().clear();
        weaponList.getItems().addAll(controller.getOwnedWeapons());

        armorList.getItems().clear();
        armorList.getItems().addAll(controller.getOwnedArmors());

        potionList.getItems().clear();
        potionList.getItems().addAll(controller.getPlayer().getPotions());

        refreshDisplay();
    }

    /**
     * Refreshes the inventory display with current data.
     */
    private void refreshDisplay() {
        Weapon w = controller.getPlayer().getWeapon();
        Armor a = controller.getPlayer().getArmor();
        lblCurrentWeapon.setText("Weapon: " + (w != null ? w.getName() : "None"));
        lblCurrentArmor.setText("Armor: " + (a != null ? a.getName() : "None"));
    }

    /**
     * Returns the main view layout.
     *
     * @return The VBox layout
     */
    public VBox getView() {
        return layout;
    }

    /**
     * Applies style to the button.
     *
     * @param btn The button to style
     */
    private void stylePillButton(Button btn) {
        btn.setPrefWidth(120);
        btn.setFont(Font.font("Arial", 13));
        btn.setTextFill(Color.WHITE);

        String base = "#546e7a";

        btn.setStyle(
                "-fx-background-radius: 999;" +
                        "-fx-background-color: linear-gradient(" + base + ", derive(" + base + ", -15%));" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.7), 10, 0.25, 0, 3);"
        );

        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-radius: 999;" +
                        "-fx-background-color: linear-gradient(derive(" + base + ", 10%), " + base + ");" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.9), 12, 0.4, 0, 4);"
        ));

        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-radius: 999;" +
                        "-fx-background-color: linear-gradient(" + base + ", derive(" + base + ", -15%));" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.7), 10, 0.25, 0, 3);"
        ));
    }
}