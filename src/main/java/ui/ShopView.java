package ui;

import controller.GameController;
import interfaces.Purchasable;
import items.Armor;
import items.Item;
import items.Potion;
import items.Weapon;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;
import java.util.Objects;

/**
 * Manages the UI for the shop/merchant scene.
 */
public class ShopView {

    /**
     * Reference to the game controller.
     */
    private final GameController controller;

    /**
     * Icon for heart currency.
     */
    private final Image heartIcon =
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/heart.png")));

    /**
     * Icon for brain currency.
     */
    private final Image brainIcon =
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/brain.png")));

    /**
     * Main layout container.
     */
    private VBox layout;

    /**
     * Container for shop items.
     */
    private HBox itemContainer;

    /**
     * Label for heart currency amount.
     */
    private Label heartsLabel;

    /**
     * Label for brain currency amount.
     */
    private Label brainsLabel;

    /**
     * Constructor to initialize the shop view.
     * * @param controller The game controller
     */
    public ShopView(GameController controller) {
        this.controller = controller;
        initLayout();
    }

    /**
     * Initialize the layout and UI components.
     */
    private void initLayout() {
        layout = new VBox(25);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(30, 40, 40, 40));
        layout.setStyle(
                "-fx-background-color: linear-gradient(#020617 0%, #020617 40%, #0b1120 100%);"
        );

        Label title = new Label("MERCHANT");
        title.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 32));
        title.setStyle(
                "-fx-text-fill: #FACC15;" +  // เหลืองทอง
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.9), 18, 0.5, 0, 4);"
        );

        // แถบ Hearts / Brains พร้อมไอคอน
        HBox currencyBar = buildCurrencyBar();

        itemContainer = new HBox(15);
        itemContainer.setAlignment(Pos.CENTER);
        itemContainer.setFillHeight(true);
        itemContainer.setPrefWidth(Region.USE_COMPUTED_SIZE);
        HBox.setHgrow(itemContainer, Priority.ALWAYS);

        Button btnNext = new Button("GO TO NEXT STAGE >>");
        stylePillButton(btnNext, true);
        btnNext.setPrefWidth(240);
        btnNext.setPrefHeight(46);
        btnNext.setOnAction(e -> controller.nextStage());

        Button btnInv = new Button("CHECK INVENTORY");
        stylePillButton(btnInv, false);
        btnInv.setPrefWidth(180);
        btnInv.setPrefHeight(40);
        btnInv.setOnAction(e -> controller.goToInventory(false));

        VBox bottomButtons = new VBox(12, btnInv, btnNext);
        bottomButtons.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(title, currencyBar, itemContainer, bottomButtons);
    }

    /**
     * set up shop UI for current stage
     */
    public void setupShop(List<Item> items) {
        itemContainer.getChildren().clear();
        updateMoney();

        for (Item item : items) {
            if (item instanceof Purchasable purchasableItem) {

                VBox card = new VBox(10);
                card.setAlignment(Pos.TOP_CENTER);
                card.setPrefWidth(200);
                card.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(card, Priority.ALWAYS);
                card.setStyle(
                        "-fx-background-color: rgba(19,25,38,0.96);" +
                                "-fx-background-radius: 14;" +
                                "-fx-border-color: rgba(255,255,255,0.1);" +
                                "-fx-border-radius: 14;" +
                                "-fx-padding: 14;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.85), 18, 0.35, 0, 8);"
                );

                Label name = new Label(item.getName());
                name.setStyle(
                        "-fx-font-weight: bold;" +
                                "-fx-font-size: 15px;" +
                                "-fx-text-fill: white;"
                );

                Label desc = new Label(item.getDescription());
                desc.setWrapText(true);
                desc.setStyle(
                        "-fx-text-fill: #b0bec5;" +
                                "-fx-font-size: 11px;"
                );

                Label cost = new Label("Price: " + purchasableItem.getCostHearts()
                        + " H / " + purchasableItem.getCostBrains() + " B");
                cost.setStyle(
                        "-fx-text-fill: #81d4fa;" +
                                "-fx-font-size: 12px;"
                );

                // --- เช็กว่ามีอยู่แล้วหรือยัง ---
                boolean owned = isAlreadyOwned(item);

                Button btnBuy = new Button(owned ? "OWNED" : "BUY");
                stylePillButton(btnBuy, false);
                btnBuy.setDisable(owned);

                btnBuy.setOnAction(e -> {
                    // กันคนไปคลิกซ้ำตอน disable
                    if (btnBuy.isDisable()) return;

                    boolean success = false;

                    if (item instanceof Weapon) {
                        success = controller.getPlayer().payCost(
                                purchasableItem.getCostHearts(),
                                purchasableItem.getCostBrains()
                        );
                        if (success) {
                            controller.getOwnedWeapons().add((Weapon) item);
                        }
                    } else if (item instanceof Armor) {
                        success = controller.getPlayer().payCost(
                                purchasableItem.getCostHearts(),
                                purchasableItem.getCostBrains()
                        );
                        if (success) {
                            controller.getOwnedArmors().add((Armor) item);
                        }
                    } else if (item instanceof Potion) {
                        // potion ซื้อได้กี่ครั้งก็ได้
                        success = controller.getPlayer()
                                .buyPotionToInventory((Potion) item);
                    }

                    if (success) {
                        // ✅ ให้ “owned” เฉพาะอาวุธเท่านั้น
                        if (item instanceof Weapon) {
                            btnBuy.setDisable(true);
                            btnBuy.setText("OWNED");
                        }
                        updateMoney();
                    } else {
                        showNotEnoughOrgansAlert();
                    }
                });

                card.getChildren().addAll(name, desc, cost, btnBuy);
                itemContainer.getChildren().add(card);
            }
        }
    }

    /**
     * Builds the currency display bar.
     * * @return The HBox containing currency info
     */
    private HBox buildCurrencyBar() {
        // Hearts
        ImageView heartView = new ImageView(heartIcon);
        heartView.setFitWidth(22);
        heartView.setFitHeight(22);
        heartView.setPreserveRatio(true);

        heartsLabel = new Label();
        heartsLabel.setStyle(
                "-fx-text-fill: #FB923C;" +   // ส้ม
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;"
        );

        HBox heartsBox = new HBox(8, heartView, heartsLabel);
        heartsBox.setAlignment(Pos.CENTER);

        // Brains
        ImageView brainView = new ImageView(brainIcon);
        brainView.setFitWidth(22);
        brainView.setFitHeight(22);
        brainView.setPreserveRatio(true);

        brainsLabel = new Label();
        brainsLabel.setStyle(
                "-fx-text-fill: #38BDF8;" +   // ฟ้า
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;"
        );

        HBox brainsBox = new HBox(8, brainView, brainsLabel);
        brainsBox.setAlignment(Pos.CENTER);

        // Bar รวม
        HBox bar = new HBox(30, heartsBox, brainsBox);
        bar.setAlignment(Pos.CENTER);
        bar.setPadding(new Insets(8, 24, 10, 24));
        bar.setStyle(
                "-fx-background-color: rgba(15,23,42,0.95);" +
                        "-fx-background-radius: 999;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 18, 0.45, 0, 4);"
        );

        return bar;
    }

    /**
     * Updates the displayed currency amounts.
     */
    private void updateMoney() {
        int hearts = controller.getPlayer().getHeartParts();
        int brains = controller.getPlayer().getBrainParts();

        if (heartsLabel != null) {
            heartsLabel.setText("Hearts : " + hearts);
        }
        if (brainsLabel != null) {
            brainsLabel.setText("Brains : " + brains);
        }
    }

    /**
     * Returns the main view layout.
     * * @return The VBox layout
     */
    public VBox getView() {
        return layout;
    }

    /**
     * Applies style to the button.
     * * @param btn The button to style
     * @param btn True if primary action, false otherwise
     */
    private void stylePillButton(Button btn, boolean primary) {
        btn.setPrefWidth(120);
        btn.setFont(Font.font("Arial", 13));
        btn.setTextFill(Color.WHITE);

        String base = primary ? "#e53935" : "#546e7a";

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

    /**
     * Shows an alert when funds are insufficient.
     */
    private void showNotEnoughOrgansAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Not Enough Organs");

        // เราจะไม่ใช้ header เดิม → ตั้งเป็น null แล้วสร้างเอง
        alert.setHeaderText(null);
        alert.setContentText("You don't have enough hearts/brains\nfor this item.");

        DialogPane pane = alert.getDialogPane();
        pane.setPrefWidth(420);
        pane.setStyle(
                "-fx-background-color: linear-gradient(#3f1f1f, #1b0b0b);" +
                        "-fx-border-color: #fb923c;" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 15;" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 20;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 14px;"
        );

        // ---------- ICON : warning.png ----------
        // ปรับ path ให้ตรงกับที่เก็บจริงใน resources
        Image warningImg = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/icons/warning.png"))
        );

        ImageView iconView = new ImageView(warningImg);
        iconView.setFitWidth(32);
        iconView.setFitHeight(32);
        iconView.setPreserveRatio(true);

        Label headerLabel = new Label("INSUFFICIENT ORGANS");
        headerLabel.setTextFill(Color.web("#F9FAFB"));   // ขาวชัด
        headerLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));

// เอา label + icon ใส่ใน HBox เดียวกัน
        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);  // ดัน icon ไปชิดขวาแบบ popup อื่น ๆ

        headerBox.getChildren().addAll(headerLabel, spacer, iconView);
        headerBox.setStyle(
                "-fx-background-color: linear-gradient(#7f1d1d, #451010);" +
                        "-fx-padding: 8 16;" +
                        "-fx-background-radius: 8 8 0 0;"
        );

// ใช้ headerBox เป็น header แทน
        pane.setHeader(headerBox);

        // ---------- CONTENT : ทำตัวหนังสือให้สว่าง ----------
        var contentRegion = pane.lookup(".content");
        if (contentRegion != null) {
            var contentLabelNode = contentRegion.lookup(".label");
            if (contentLabelNode instanceof Label c) {
                c.setStyle(
                        "-fx-text-fill: #FFECE0;" +
                                "-fx-font-size: 14px;" +
                                "-fx-line-spacing: 3;" +
                                "-fx-opacity: 1.0;"
                );
            }
        }

        // ---------- ปุ่ม OK ----------
        Button ok = (Button) pane.lookupButton(ButtonType.OK);
        ok.setText("OK");
        ok.setTextFill(Color.WHITE);
        ok.setPrefWidth(140);
        ok.setStyle(
                "-fx-background-radius: 999;" +
                        "-fx-background-color: linear-gradient(#ea580c, derive(#ea580c, -15%));" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 13px;" +
                        "-fx-cursor: hand;"
        );

        // ---------- ทำให้พื้นหลัง alert โปร่งใส ไม่มีขอบขาว ----------
        Stage dialogStage = (Stage) pane.getScene().getWindow();
        dialogStage.initStyle(StageStyle.TRANSPARENT);
        dialogStage.getScene().setFill(Color.TRANSPARENT);

        alert.showAndWait();
    }

    /**
     * Checks if an item is already owned.
     * * @param item The item to check
     * @return true if owned, false otherwise
     */
    private boolean isAlreadyOwned(Item item) {
        if (item instanceof Weapon) {
            for (Weapon w : controller.getOwnedWeapons()) {
                // เช็คจากชื่อ ถ้าชื่ออาวุธไม่ซ้ำกันก็โอเค
                if (w.getName().equals(item.getName())) return true;
            }
        } else if (item instanceof Armor) {
            for (Armor a : controller.getOwnedArmors()) {
                if (a.getName().equals(item.getName())) return true;
            }
        }
        // Potion ไม่ต้องเช็ค เพราะอยากให้ซื้อได้หลายขวด
        return false;
    }

}