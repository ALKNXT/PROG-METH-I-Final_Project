package controller;

import entity.*;
import interfaces.OrganDroppable;
import items.*;
import items.armor.KevlarJacket;
import items.armor.LeatherJacket;
import items.armor.RiotSuit;
import items.weapon.*;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.BattleView;
import ui.InventoryView;
import ui.ShopView;
import utils.SoundManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Controls the main game logic, flow, and interactions between models and views.
 */
public class GameController {

    // -------------------- CONSTANTS & FIELDS -------------------- //

    /**
     * Maximum number of stages in the game.
     */
    private static final int MAX_STAGE = 5;

    /**
     * The primary stage of the JavaFX application.
     */
    private final Stage primaryStage;

    /**
     * The player entity.
     */
    private final Player player;

    /**
     * List of weapons owned by the player.
     */
    private final List<Weapon> ownedWeapons = new ArrayList<>();

    /**
     * List of armors owned by the player.
     */
    private final List<Armor> ownedArmors = new ArrayList<>();

    /**
     * The view responsible for the battle scenes.
     */
    private final BattleView battleView;

    /**
     * The view responsible for the shop scenes.
     */
    private final ShopView shopView;

    /**
     * The view responsible for the inventory scenes.
     */
    private final InventoryView inventoryView;

    /**
     * Icon image for the win dialog.
     */
    private final Image winDialogIcon = safeLoadImage("/icons/win_icon.png");

    /**
     * Icon image for the loss dialog.
     */
    private final Image loseDialogIcon = safeLoadImage("/icons/lose_icon.png");

    /**
     * Icon image for the stage clear dialog.
     */
    private final Image stageClearIcon = safeLoadImage("/icons/stage_clear.png");

    /**
     * The main scene of the game.
     */
    private Scene mainScene;

    /**
     * The current enemy the player is fighting.
     */
    private Zombie currentEnemy;

    /**
     * The current stage number.
     */
    private int currentStage;

    /**
     * Constructor to initialize the game controller and views.
     * Sets up the player, starting items, and UI components.
     *
     * @param stage The primary stage
     */
    public GameController(Stage stage) {
        this.primaryStage = stage;
        this.player = new Player("Survivor");

        // app icon
        try {
            Image appIcon = new Image(
                    Objects.requireNonNull(getClass().getResourceAsStream("/icons/zombie_hand.png"))
            );
            primaryStage.getIcons().add(appIcon);
        } catch (Exception ignored) {
        }

        // starting weapon
        Weapon startWeapon = new WoodenStick();
        ownedWeapons.add(startWeapon);
        player.equipWeapon(startWeapon);
        player.addHearts(0);
        player.addBrains(0);

        // views
        this.battleView = new BattleView(this);
        this.shopView = new ShopView(this);
        this.inventoryView = new InventoryView(this);

        this.mainScene = new Scene(battleView.getView(), 800, 600);
        this.primaryStage.setScene(mainScene);
    }

    // -------------------- CONSTRUCTOR -------------------- //

    /**
     * Safely loads an image from the given path.
     *
     * @param path The path to the image resource
     * @return The loaded Image object, or null if loading fails
     */
    private Image safeLoadImage(String path) {
        try {
            return new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        } catch (Exception e) {
            return null;
        }
    }

    // -------------------- GAME FLOW / STAGE FLOW -------------------- //

    /**
     * Runs a runnable action after a specified delay.
     */
    public void startGame() {
        startStage(1);
    }

    /**
     * Initializes and starts a specific stage.
     * Sets up the enemy, updates the UI, and resets player status.
     *
     * @param stage The stage number to start
     */
    public void startStage(int stage) {
        player.clearPoison();
        battleView.updateLog("Any lingering poison fades as you move to the next area.");

        this.currentStage = stage;

        switch (stage) {
            case 1:
                currentEnemy = new NormalZombie();
                break;
            case 2:
                currentEnemy = new RottenZombie();
                break;
            case 3:
                currentEnemy = new ArmoredZombie();
                break;
            case 4:
                currentEnemy = new RunnerZombie();
                break;
            case 5:
                currentEnemy = new BossZombie();
                break;
            default:
                showGameWinDialog();
                return;
        }

        battleView.resetLog();
        battleView.updateLog("=== STAGE " + stage + " START ===");

        if (mainScene == null) {
            mainScene = new Scene(battleView.getView(), 800, 600);
            primaryStage.setScene(mainScene);
        } else {
            changeView(battleView.getView());
        }

        updateBattleUI();
        setPlayerTurn();
    }

    /**
     * Advances to the next stage or wins the game if the last stage is cleared.
     */
    public void nextStage() {
        if (currentStage < MAX_STAGE) {
            startStage(currentStage + 1);
        } else {
            showGameWinDialog();
        }
    }

    // -------------------- BATTLE TURN HELPERS -------------------- //

    /**
     * Sets the game state to the player's turn.
     * Enables controls and updates UI indicators.
     */
    private void setPlayerTurn() {
        battleView.updateTurnLabel("PLAYER'S TURN", Color.LIGHTGREEN);
        battleView.resetPlayerToIdle();
        battleView.resetEnemyToIdle();
        battleView.setControlsDisabled(false);
    }

    /**
     * Sets the game state to the enemy's turn.
     * Disables player controls and updates UI indicators.
     */
    private void setEnemyTurn() {
        battleView.updateTurnLabel("ENEMY'S TURN", Color.TOMATO);
        battleView.resetPlayerToIdle();
        battleView.resetEnemyToIdle();
        battleView.setControlsDisabled(true);
    }

    // -------------------- BATTLE LOGIC -------------------- //

    /**
     * Handles the player's attack action.
     * Calculates damage, handles dodging, and updates the battle log.
     */
    public void handleAttack() {
        if (!player.isAlive()) return;

        battleView.setControlsDisabled(true);
        battleView.resetPlayerToIdle();

        battleView.playPlayerAttack(() -> {
            boolean isDodge = Math.random() < currentEnemy.getDodgeChance();
            int dmg;
            boolean willDie = false;

            if (!isDodge) {
                dmg = player.basicAttack(currentEnemy);
                boolean crit = player.wasLastAttackCritical();

                if (crit) {
                    battleView.updateLog("CRITICAL! Player dealt " + dmg + " damage!");
                } else {
                    battleView.updateLog("Player dealt " + dmg + " damage.");
                }

                willDie = !currentEnemy.isAlive();
            } else {
                battleView.updateLog(
                        currentEnemy.getClass().getSimpleName() + " dodged your attack!"
                );
            }

            battleView.playEnemyDefend(isDodge, willDie);
            updateBattleUI();

            if (willDie) {
                runLater(1200, this::handleEnemyDeath);
            } else {
                runLater(500, this::handleEnemyTurn);
            }
        });
    }

    /**
     * Schedules the start of the enemy's turn action.
     */
    private void handleEnemyTurn() {
        if (!currentEnemy.isAlive()) return;

        setEnemyTurn();

        runLater(1000, () -> {
            if (!player.isAlive()) return;
            executeEnemyAttackLogic();
        });
    }

    /**
     * Executes the enemy's attack logic.
     * Includes handling poison effects, special abilities, and dealing damage to the player.
     */
    private void executeEnemyAttackLogic() {
        int hpBeforePoison = player.getHp();
        player.tickStatusEffectsAtTurnStart();
        int hpAfterPoison = player.getHp();

        if (hpAfterPoison < hpBeforePoison) {
            battleView.updateLog(String.format(
                    "Poison deals %d damage to you at the start of the turn. HP %d → %d.",
                    (hpBeforePoison - hpAfterPoison),
                    hpBeforePoison, hpAfterPoison
            ));
        }

        updateBattleUI();
        if (!player.isAlive()) {
            battleView.playPlayerDefend(false, true);

            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                Platform.runLater(this::handlePlayerDeath);
            }).start();

            return;
        }

        battleView.playEnemyAttack(() -> {
            int dmg = 0;
            boolean willDie = false;
            boolean isDodge = false;

            if (currentEnemy instanceof BossZombie boss) {
                boss.enrage();
                if (Math.random() <= 0.25) {
                    boss.useSpecialAbility(player);
                    battleView.updateLog("Boss uses Skill!");
                } else {
                    dmg = boss.basicAttack(player);
                    boolean crit = boss.wasLastAttackCritical();

                    if (dmg == 0) {
                        isDodge = true;
                        battleView.updateLog("You dodged the Boss's attack!");
                    } else if (crit) {
                        battleView.updateLog("CRITICAL! Boss dealt " + dmg + " damage!");
                    } else {
                        battleView.updateLog("Boss attacked: " + dmg);
                    }
                }
            } else {
                dmg = currentEnemy.basicAttack(player);
                boolean crit = currentEnemy.wasLastAttackCritical();
                String enemyName = currentEnemy.getClass().getSimpleName();

                if (dmg == 0) {
                    isDodge = true;
                    battleView.updateLog("You dodged " + enemyName + "'s attack!");
                } else if (crit) {
                    battleView.updateLog(
                            "CRITICAL! " + enemyName + " dealt " + dmg + " damage!"
                    );
                } else {
                    battleView.updateLog(enemyName + " attacked: " + dmg);
                }

                if (currentEnemy instanceof NormalZombie &&
                        currentEnemy.getHp() < currentEnemy.getMaxHp() / 2) {
                    currentEnemy.useSpecialAbility(player);
                }
            }

            willDie = !player.isAlive();

            battleView.playPlayerDefend(isDodge, willDie);
            updateBattleUI();

            if (willDie) {
                handlePlayerDeath();
            } else {
                runLater(1000, this::setPlayerTurn);
            }
        });

    }

    /**
     * Handles the player's heal action.
     * Uses the player's special ability to restore HP.
     */
    public void handleHeal() {
        battleView.setControlsDisabled(true);
        battleView.resetPlayerToIdle();

        int hpBefore = player.getHp();
        player.useSpecialAbility(player);
        int hpAfter = player.getHp();
        int healed = hpAfter - hpBefore;

        if (healed > 0) {
            battleView.updateLog(String.format(
                    "You use Adrenaline Shot and heal %d HP (%d → %d).",
                    healed, hpBefore, hpAfter
            ));
        } else {
            battleView.updateLog("You use Adrenaline Shot, but your HP is already full.");
        }

        SoundManager.playSound("heal.wav");
        updateBattleUI();

        runLater(1000, this::handleEnemyTurn);
    }

    /**
     * Handles the logic when an enemy is defeated.
     * Collects loot and transitions to the shop or victory screen.
     */
    private void handleEnemyDeath() {
        battleView.updateLog("Enemy Defeated!");
        SoundManager.playSound("Zombie_dead_1.wav");

        if (currentEnemy instanceof OrganDroppable droppable) {
            OrganParts loot = droppable.getDropItem();
            player.addHearts(loot.getHearts());
            player.addBrains(loot.getBrains());
            battleView.updateLog(
                    "Looted: " + loot.getHearts() + " H, " + loot.getBrains() + " B"
            );
        }

        battleView.setControlsDisabled(false);

        if (currentStage < MAX_STAGE) {
            showStageClearDialog();
            goToShop();
        } else {
            showGameWinDialog();
        }
    }

    /**
     * Handles the logic when the player is defeated.
     * Shows the game over dialog.
     */
    private void handlePlayerDeath() {
        battleView.updateLog(">>> YOU DIED <<<");
        runLater(1500, this::showGameOverDialog);
    }

    // -------------------- NAVIGATION / VIEW SWITCHING -------------------- //

    /**
     * Changes the current view root of the main scene.
     *
     * @param root The new root node to display
     */
    private void changeView(javafx.scene.Parent root) {
        mainScene.setRoot(root);
    }

    /**
     * Transitions the view to the Shop screen.
     * Generates appropriate items for sale based on the next stage.
     */
    public void goToShop() {
        List<Item> shopItems = getShopItemsForNextStage(currentStage + 1);
        shopView.setupShop(shopItems);
        changeView(shopView.getView());
    }

    /**
     * Transitions the view to the Inventory screen.
     *
     * @param returnToBattle true if the inventory is accessed from battle, false otherwise
     */
    public void goToInventory(boolean returnToBattle) {
        inventoryView.setReturnToBattle(returnToBattle);
        inventoryView.refreshList();
        changeView(inventoryView.getView());
    }

    /**
     * Handles returning from the Inventory screen.
     *
     * @param toBattle true to return to battle, false to return to the shop
     */
    public void backFromInventory(boolean toBattle) {
        if (toBattle) {
            changeView(battleView.getView());
            updateBattleUI();
        } else {
            goToShop();
        }
    }

    /**
     * Updates the battle UI with current stats of player and enemy.
     */
    private void updateBattleUI() {
        battleView.updateStats(player, currentEnemy, currentStage);
    }

    // -------------------- SHOP ITEMS -------------------- //

    /**
     * Generates a list of items available in the shop for the specified stage.
     *
     * @param nextStage The upcoming stage number
     * @return List of items available for purchase
     */
    private List<Item> getShopItemsForNextStage(int nextStage) {
        List<Item> items = new ArrayList<>();

        if (nextStage == 2) {
            items.add(new CombatKnife());
            items.add(new Machete());
            items.add(new LeatherJacket());
            items.add(new Potion("Small Medkit", 30, 3, 1));
        } else if (nextStage == 3) {
            items.add(new Hammer());
            items.add(new KnuckleDusters());
            items.add(new RiotSuit());
            items.add(new Potion("Medium Medkit", 50, 5, 2));
        } else if (nextStage == 4) {
            items.add(new Revolver());
            items.add(new ShotGun());
            items.add(new KevlarJacket());
            items.add(new Potion("Big Medkit", 80, 7, 3));
        } else if (nextStage == 5) {
            items.add(new Rifle());
            items.add(new Axe());
            items.add(new RiotSuit());
            items.add(new Potion("Emergency Kit", 100, 10, 5));
        }

        return items;
    }

    // -------------------- DIALOG / UI HELPERS -------------------- //

    /**
     * Styles a primary action button with a specific theme.
     *
     * @param btn The button to style
     */
    private void stylePrimaryButton(Button btn) {
        String base = "#e53935";
        btn.setTextFill(Color.WHITE);
        btn.setPrefWidth(140);
        btn.setStyle(
                "-fx-background-radius: 999;" +
                        "-fx-background-color: linear-gradient(" + base + ", derive(" + base + ", -15%));" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;" +
                        "-fx-cursor: hand;"
        );
    }

    /**
     * Shows a dialog indicating the stage has been cleared.
     */
    private void showStageClearDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(primaryStage);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.setTitle("Stage Clear");
        alert.setHeaderText("STAGE CLEARED!");
        alert.setContentText("You survived this wave.\nVisit the merchant before the next stage.");

        DialogPane pane = getDialogPane(alert, "-fx-background-color: linear-gradient(#0F172A, #020617);" +
                "-fx-border-color: #FACC15;" +
                "-fx-border-width: 3;" +
                "-fx-border-radius: 18;" +
                "-fx-background-radius: 18;" +
                "-fx-padding: 20;" +
                "-fx-font-family: 'Segue UI';", stageClearIcon);

        var headerRegion = pane.lookup(".header-panel");
        if (headerRegion != null) {
            headerRegion.setStyle(
                    "-fx-background-color: linear-gradient(#92400E, #713F12);" +
                            "-fx-background-radius: 14;" +
                            "-fx-padding: 8 18;"
            );
            var headerLabelNode = headerRegion.lookup(".label");
            if (headerLabelNode instanceof Label h) {
                h.setStyle(
                        "-fx-text-fill: #FEF9C3;" +
                                "-fx-font-size: 20px;" +
                                "-fx-font-weight: 900;" +
                                "-fx-effect: drop shadow(gaussian, rgba(0,0,0,0.9), 10, 0.4, 0, 2);"
                );
            }
        }

        var contentRegion = pane.lookup(".content");
        if (contentRegion != null) {
            var contentLabelNode = contentRegion.lookup(".label");
            if (contentLabelNode instanceof Label c) {
                c.setStyle(
                        "-fx-text-fill: #E5E7EB;" +
                                "-fx-font-size: 14px;" +
                                "-fx-line-spacing: 3;"
                );
            }
        }

        Button okButton = (Button) pane.lookupButton(ButtonType.OK);
        okButton.setText("GO TO SHOP");
        stylePrimaryButton(okButton);

        Stage dialogStage = (Stage) pane.getScene().getWindow();
        dialogStage.initStyle(StageStyle.TRANSPARENT);
        dialogStage.getScene().setFill(Color.TRANSPARENT);

        alert.showAndWait();
    }

    /**
     * Creates and configures a custom DialogPane.
     *
     * @param alert          The alert owning the pane
     * @param value          The CSS style string for the pane
     * @param stageClearIcon The icon to display in the header
     * @return The configured DialogPane
     */
    private DialogPane getDialogPane(Alert alert, String value, Image stageClearIcon) {
        DialogPane pane = alert.getDialogPane();
        pane.setPrefWidth(420);
        pane.setStyle(
                value
        );

        if (stageClearIcon != null) {
            ImageView iconView = new ImageView(stageClearIcon);
            iconView.setFitWidth(40);
            iconView.setFitHeight(40);
            iconView.setPreserveRatio(true);
            pane.setGraphic(iconView);
        }
        return pane;
    }

    /**
     * Shows a dialog indicating the game is over.
     */
    private void showGameOverDialog() {
        SoundManager.stopBGM();
        SoundManager.playSound("game-over-417465.mp3");

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(primaryStage);
        alert.setTitle("Game Over");
        alert.setHeaderText("GAME OVER");
        alert.setContentText("The horde was too strong...\nTry again with better gear!");

        DialogPane pane = getDialogPane(alert, "-fx-background-color: linear-gradient(#020617, #020617);" +
                "-fx-border-color: #F97373;" +
                "-fx-border-width: 3;" +
                "-fx-border-radius: 18;" +
                "-fx-background-radius: 18;" +
                "-fx-padding: 20;" +
                "-fx-font-family: 'Segue UI';", loseDialogIcon);

        var headerRegion = pane.lookup(".header-panel");
        if (headerRegion != null) {
            headerRegion.setStyle(
                    "-fx-background-color: linear-gradient(#7F1D1D, #450A0A);" +
                            "-fx-background-radius: 14;" +
                            "-fx-padding: 8 18;"
            );
            var headerLabelNode = headerRegion.lookup(".label");
            if (headerLabelNode instanceof Label h) {
                h.setStyle(
                        "-fx-text-fill: #FEE2E2;" +
                                "-fx-font-size: 22px;" +
                                "-fx-font-weight: 900;" +
                                "-fx-effect: drop shadow(gaussian, rgba(0,0,0,0.9), 10, 0.4, 0, 2);"
                );
            }
        }

        var contentRegion = pane.lookup(".content");
        if (contentRegion != null) {
            var contentLabelNode = contentRegion.lookup(".label");
            if (contentLabelNode instanceof Label c) {
                c.setStyle(
                        "-fx-text-fill: #E5E7EB;" +
                                "-fx-font-size: 14px;" +
                                "-fx-line-spacing: 3;"
                );
            }
        }

        Button ok = (Button) pane.lookupButton(ButtonType.OK);
        ok.setText("EXIT");
        stylePrimaryButton(ok);

        Stage dialogStage = (Stage) pane.getScene().getWindow();
        dialogStage.initStyle(StageStyle.TRANSPARENT);
        dialogStage.getScene().setFill(Color.TRANSPARENT);

        alert.showAndWait();
        primaryStage.close();
    }

    /**
     * Shows a dialog indicating the game has been won.
     */
    private void showGameWinDialog() {
        SoundManager.stopBGM();
        SoundManager.playSound("synth_bass_level_complete.wav");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(primaryStage);
        alert.setTitle("Victory");
        alert.setHeaderText("YOU SURVIVED!");
        alert.setContentText("You cleared all stages and obtained the cure.\nHumanity has a chance again.");

        DialogPane pane = getDialogPane(alert, "-fx-background-color: linear-gradient(#047857, #022c22);" +
                "-fx-border-color: #22C55E;" +
                "-fx-border-width: 3;" +
                "-fx-border-radius: 15;" +
                "-fx-background-radius: 15;" +
                "-fx-padding: 20;", winDialogIcon);

        var headerRegion = pane.lookup(".header-panel");
        if (headerRegion != null) {
            headerRegion.setStyle(
                    "-fx-background-color: linear-gradient(#064E3B, #022C22);" +
                            "-fx-padding: 8 16;" +
                            "-fx-background-radius: 8 8 0 0;"
            );
            var headerLabel = headerRegion.lookup(".label");
            if (headerLabel instanceof Label h) {
                h.setTextFill(Color.web("#F9FAFB"));
                h.setStyle(
                        "-fx-font-size: 20px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-effect: drop shadow(gaussian, rgba(0,0,0,0.8), 8, 0.4, 0, 1);"
                );
            }
        }

        var contentRegion = pane.lookup(".content");
        if (contentRegion != null) {
            var contentLabel = contentRegion.lookup(".label");
            if (contentLabel instanceof Label c) {
                c.setTextFill(Color.web("#E5E7EB"));
                c.setStyle(
                        "-fx-font-size: 14px;" +
                                "-fx-line-spacing: 3;" +
                                "-fx-opacity: 1.0;"
                );
            }
        }

        Button ok = (Button) pane.lookupButton(ButtonType.OK);
        ok.setText("FINISH");
        stylePrimaryButton(ok);

        Stage dialogStage = (Stage) pane.getScene().getWindow();
        dialogStage.initStyle(StageStyle.TRANSPARENT);
        dialogStage.getScene().setFill(Color.TRANSPARENT);

        alert.showAndWait();
        primaryStage.close();
    }

    // -------------------- UTILS -------------------- //

    /**
     * Runs a specified action after a set delay on the JavaFX Application Thread.
     *
     * @param delayMillis The delay time in milliseconds
     * @param action      The Runnable action to execute
     */
    private void runLater(long delayMillis, Runnable action) {
        new Thread(() -> {
            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException ignored) {
            }
            Platform.runLater(action);
        }).start();
    }

    // -------------------- GETTERS -------------------- //

    /**
     * Gets the player entity.
     *
     * @return The player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the list of owned weapons.
     *
     * @return List of owned weapons
     */
    public List<Weapon> getOwnedWeapons() {
        return ownedWeapons;
    }

    /**
     * Gets the list of owned armors.
     *
     * @return List of owned armors
     */
    public List<Armor> getOwnedArmors() {
        return ownedArmors;
    }
}