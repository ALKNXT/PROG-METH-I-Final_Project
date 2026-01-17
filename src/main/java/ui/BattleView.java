package ui;

import controller.GameController;
import entity.*;
import items.Weapon;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import utils.ResourceManager;
import utils.SoundManager;

/**
 * Manages the UI and animations for the battle scene.
 */
public class BattleView {

    /**
     * Reference to the main game controller.
     */
    private final GameController controller;
    // --- [CONFIG] ---

    /**
     * Width of the scene.
     */
    private final double SCENE_WIDTH = 800;

    /**
     * Height of the scene.
     */
    private final double SCENE_HEIGHT = 400;

    /**
     * Y-coordinate of the floor.
     */
    private final double FLOOR_Y = 399.5;

    /**
     * Size of the character images.
     */
    private final double CHAR_SIZE = 180;

    /**
     * X-coordinate of the player.
     */
    private final double PLAYER_X = 200; // แก้กลับเป็น 100 ตามที่คุณเคยใช้ (หรือปรับตามใจชอบ)

    /**
     * X-coordinate of the enemy.
     */
    private final double ENEMY_X = 500;
    // Animation Constants

    /**
     * Duration of the attack animation.
     */
    private final Duration ATTACK_DURATION = Duration.millis(250);

    /**
     * Duration of the shake animation.
     */
    private final Duration SHAKE_DURATION = Duration.millis(50);

    /**
     * Distance to move during attack.
     */
    private final double ATTACK_DISTANCE = 80;

    /**
     * Main layout container.
     */
    private VBox rootLayout;

    /**
     * Pane for the game scene.
     */
    private Pane gameScenePane;
    // UI Components

    /**
     * Progress bar for player health.
     */
    private ProgressBar playerBar;

    /**
     * Progress bar for enemy health.
     */
    private ProgressBar enemyBar;

    /**
     * Label for player status.
     */
    private Label playerLbl;

    /**
     * Label for enemy status.
     */
    private Label enemyLbl;

    /**
     * Label for the current stage.
     */
    private Label stageLbl;

    /**
     * Label to display the turn.
     */
    private Label turnLbl; // ป้ายบอกเทิร์น

    /**
     * Text area for battle logs.
     */
    private TextArea logArea;

    /**
     * Button to trigger attack.
     */
    private Button btnAttack;

    /**
     * Button to trigger healing.
     */
    private Button btnHeal;

    /**
     * Button to open inventory.
     */
    private Button btnInv;
    // Characters & Environment

    /**
     * Image view for the player.
     */
    private ImageView playerImageView;

    /**
     * Image view for the enemy.
     */
    private ImageView enemyImageView;

    /**
     * Image view for the background.
     */
    private ImageView backgroundImageView;
    // --- Cache รูปภาพ ---
    // Default Player Images

    /**
     * Image for player idle state.
     */
    private Image playerIdleImg;

    /**
     * Image for player attack state.
     */
    private Image playerAttackImg;

    /**
     * Image for player hurt state.
     */
    private Image playerHurtImg;

    /**
     * Image for player dead state.
     */
    private Image playerDeadImg;
    // Weapon Specific Images (เพิ่มใหม่ตามโจทย์)

    /**
     * Image for player knife attack.
     */
    private Image playerKnifeAttackImg;

    /**
     * Image for player gun idle.
     */
    private Image playerGunIdleImg;

    /**
     * Image for player gun attack.
     */
    private Image playerGunAttackImg;

    /**
     * Image for player hammer idle.
     */
    private Image playerHammerIdleImg;

    /**
     * Image for player hammer attack.
     */
    private Image playerHammerAttackImg;

    /**
     * Image for player axe idle.
     */
    private Image playerAxeIdleImg;

    /**
     * Image for player axe attack.
     */
    private Image playerAxeAttackImg;

    /**
     * Image for player rifle idle.
     */
    private Image playerRifleIdleImg;

    /**
     * Image for player rifle attack.
     */
    private Image playerRifleAttackImg;
    // Enemy Images

    /**
     * Image for enemy idle state.
     */
    private Image enemyIdleImg;

    /**
     * Image for enemy attack state.
     */
    private Image enemyAttackImg;

    /**
     * Image for enemy hurt state.
     */
    private Image enemyHurtImg;

    /**
     * Image for enemy dead state.
     */
    private Image enemyDeadImg;

    /**
     * Image for player idle state in each hold weapon
     */
    private Image idleImg;

    /**
     * Constructor to initialize the battle view.
     * * @param controller The game controller
     */
    public BattleView(GameController controller) {
        this.controller = controller;
        loadPlayerImages();
        initLayout();
    }

    /**
     * Loads all player images including weapon-specific ones.
     */
    private void loadPlayerImages() {
        // 1. Load Default
        playerIdleImg = ResourceManager.getImage("characters/player/player_idle.png");
        playerAttackImg = ResourceManager.getImage("characters/player/player_attack.png");
        playerHurtImg = ResourceManager.getImage("characters/player/player_hurt.png");
        playerDeadImg = ResourceManager.getImage("characters/player/player_dead.png");

        // 2. Load Weapon Specifics (เพิ่มใหม่)
        // Knife / Machete (ใช้ Idle เดิม, Attack ใหม่)
        playerKnifeAttackImg = ResourceManager.getImage("characters/player/player_knife_attack.png");

        // Gun (Revolver)
        playerGunIdleImg = ResourceManager.getImage("characters/player/player_gun_idle.png");
        playerGunAttackImg = ResourceManager.getImage("characters/player/player_gun_attack.png");

        // Hammer
        playerHammerIdleImg = ResourceManager.getImage("characters/player/player_hammer_idle.png");
        playerHammerAttackImg = ResourceManager.getImage("characters/player/player_hammer_attack.png");

        // Axe
        playerAxeIdleImg = ResourceManager.getImage("characters/player/player_axe_idle.png");
        playerAxeAttackImg = ResourceManager.getImage("characters/player/player_axe_attack.png");

        // Rifle / Shotgun
        playerRifleIdleImg = ResourceManager.getImage("characters/player/player_rifle_idle.png");
        playerRifleAttackImg = ResourceManager.getImage("characters/player/player_rifle_attack.png");

        // --- Fallbacks (กันเหนียว ถ้ารูปไหนหาไม่เจอ ให้ใช้ Default) ---
        if (playerKnifeAttackImg == null) playerKnifeAttackImg = playerAttackImg;

        if (playerGunIdleImg == null) playerGunIdleImg = playerIdleImg;
        if (playerGunAttackImg == null) playerGunAttackImg = playerAttackImg;

        if (playerHammerIdleImg == null) playerHammerIdleImg = playerIdleImg;
        if (playerHammerAttackImg == null) playerHammerAttackImg = playerAttackImg;

        if (playerAxeIdleImg == null) playerAxeIdleImg = playerIdleImg;
        if (playerAxeAttackImg == null) playerAxeAttackImg = playerAttackImg;

        if (playerRifleIdleImg == null) playerRifleIdleImg = playerIdleImg;
        if (playerRifleAttackImg == null) playerRifleAttackImg = playerAttackImg;

        if (playerAttackImg == null) playerAttackImg = playerIdleImg;
        if (playerHurtImg == null) playerHurtImg = playerIdleImg;
        if (playerDeadImg == null) playerDeadImg = playerHurtImg;
    }

    // --- [NEW] Helper Methods: เลือกรูปตามอาวุธ ---

    /**
     * Gets the correct idle image based on the equipped weapon.
     * * @return The idle image
     */
    private Image getPlayerCurrentIdleImage() {
        Weapon w = controller.getPlayer().getWeapon();
        if (w == null) return playerIdleImg;

        String name = w.getName();
        if (name.contains("Revolver")) return playerGunIdleImg;
        if (name.contains("Hammer")) return playerHammerIdleImg;
        if (name.contains("Axe")) return playerAxeIdleImg;
        if (name.contains("Shotgun") || name.contains("Rifle")) return playerRifleIdleImg;

        return playerIdleImg;
    }

    /**
     * Gets the correct attack image based on the equipped weapon.
     * * @return The attack image
     */
    private Image getPlayerCurrentAttackImage() {
        Weapon w = controller.getPlayer().getWeapon();
        if (w == null) return playerAttackImg;

        String name = w.getName();
        if (name.contains("Combat Knife") || name.contains("Machete")) return playerKnifeAttackImg;
        if (name.contains("Revolver")) return playerGunAttackImg;
        if (name.contains("Hammer")) return playerHammerAttackImg;
        if (name.contains("Axe")) return playerAxeAttackImg;
        if (name.contains("Shotgun") || name.contains("Rifle")) return playerRifleAttackImg;

        return playerAttackImg;
    }

    // --- [NEW] Helper Method: Play Sound based on Weapon ---

    /**
     * Plays the sound effect corresponding to the current weapon.
     */
    private void playWeaponSound() {
        Weapon w = controller.getPlayer().getWeapon();
        String name = (w != null) ? w.getName() : "";

        if (name.contains("Machete") || name.contains("Combat Knife") || name.contains("Axe")) {
            SoundManager.playSound("sword_slice.wav");
        } else if (name.contains("Hammer") || name.contains("Wooden Stick") || name.contains("Knuckle-Dusters")) {
            SoundManager.playSound("harsh_thud.wav");
        } else if (name.contains("Revolver") || name.contains("Shotgun") || name.contains("Rifle")) {
            SoundManager.playSound("shot.wav");
        } else {
            // Default sound (optional)
        }
    }

    /**
     * Initializes the main layout and UI components.
     */
    private void initLayout() {
        rootLayout = new VBox(0);
        rootLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #050816, #151b28);");

        gameScenePane = new Pane();
        gameScenePane.setPrefSize(SCENE_WIDTH, SCENE_HEIGHT);
        gameScenePane.setStyle("-fx-background-color: transparent; -fx-border-color: rgba(255,255,255,0.08); -fx-border-width: 0 0 2 0;");
        gameScenePane.setClip(new Rectangle(SCENE_WIDTH, SCENE_HEIGHT));

        backgroundImageView = new ImageView();
        backgroundImageView.setFitWidth(SCENE_WIDTH);
        backgroundImageView.setFitHeight(SCENE_HEIGHT);

        playerImageView = new ImageView(playerIdleImg);
        playerImageView.setFitHeight(CHAR_SIZE);
        playerImageView.setPreserveRatio(true);
        // [แก้ไข] ใช้ setLayoutX/Y (ลบ bind ออกเพื่อให้ค่า Config มีผล)
        playerImageView.setLayoutX(PLAYER_X);
        playerImageView.setLayoutY(FLOOR_Y - CHAR_SIZE);

        enemyImageView = new ImageView();
        enemyImageView.setFitHeight(CHAR_SIZE);
        enemyImageView.setPreserveRatio(true);
        enemyImageView.setScaleX(-1);
        // [แก้ไข] ใช้ setLayoutX/Y (ลบ bind ออกเพื่อให้ค่า Config มีผล)
        enemyImageView.setLayoutX(ENEMY_X);
        enemyImageView.setLayoutY(FLOOR_Y - CHAR_SIZE);

        VBox pStats = createStatsBox("Player HP", Color.LIGHTGREEN);
        pStats.setLayoutX(20);
        pStats.setLayoutY(20);
        playerBar = (ProgressBar) pStats.getChildren().get(1);
        playerLbl = (Label) pStats.getChildren().get(0);

        VBox eStats = createStatsBox("Enemy HP", Color.TOMATO);
        eStats.setLayoutX(SCENE_WIDTH - 220);
        eStats.setLayoutY(20);
        enemyBar = (ProgressBar) eStats.getChildren().get(1);
        enemyLbl = (Label) eStats.getChildren().get(0);

        stageLbl = new Label("STAGE 1");
        stageLbl.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        stageLbl.setTextFill(Color.GOLD);
        stageLbl.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 12, 0.3, 0, 3); -fx-letter-spacing: 3px;");
        stageLbl.setLayoutX(SCENE_WIDTH / 2 - 60);
        stageLbl.setLayoutY(30);

        turnLbl = new Label("PLAYER'S TURN");
        turnLbl.setFont(Font.font("Impact", FontWeight.BOLD, 40));
        turnLbl.setTextFill(Color.WHITE);
        turnLbl.setStyle("-fx-effect: dropshadow(gaussian, black, 5, 1, 0, 0);");
        turnLbl.setLayoutX(SCENE_WIDTH / 2 - 120);
        turnLbl.setLayoutY(100);
        turnLbl.setVisible(false);

        gameScenePane.getChildren().addAll(backgroundImageView, playerImageView, enemyImageView, pStats, eStats, stageLbl, turnLbl);

        VBox controlsArea = new VBox(15);
        controlsArea.setPadding(new Insets(20));
        controlsArea.setAlignment(Pos.CENTER);
        controlsArea.setPrefHeight(Region.USE_COMPUTED_SIZE);
        VBox.setVgrow(controlsArea, Priority.ALWAYS);
        controlsArea.setStyle("-fx-background-color: #333;");

        HBox btnBox = new HBox(20);
        btnBox.setAlignment(Pos.CENTER);

        // [แก้ไข] ใช้ createButton
        btnAttack = createButton("ATTACK", "#e74c3c");
        btnAttack.setOnAction(e -> controller.handleAttack());

        btnHeal = createButton("HEAL", "#2ecc71");
        btnHeal.setOnAction(e -> controller.handleHeal());

        btnInv = createButton("INVENTORY", "#3498db");
        btnInv.setOnAction(e -> controller.goToInventory(true));

        btnBox.getChildren().addAll(btnAttack, btnHeal, btnInv);

        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefRowCount(4);
        logArea.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(logArea, Priority.ALWAYS);
        logArea.setStyle("-fx-control-inner-background: rgba(0,0,0,0.85); -fx-text-fill: #76ff76; -fx-font-family: 'Consolas'; -fx-font-size: 12px; -fx-background-radius: 10; -fx-border-color: rgba(255,255,255,0.12); -fx-border-radius: 10;");

        controlsArea.getChildren().addAll(btnBox, logArea);
        rootLayout.getChildren().addAll(gameScenePane, controlsArea);
    }

    /**
     * Creates a VBox for character statistics.
     * @return The VBox containing stats
     */
    private VBox createStatsBox(String title, Color barColor) {
        VBox box = new VBox(5);
        Label lbl = new Label(title);
        lbl.setTextFill(Color.WHITE);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lbl.setStyle("-fx-effect: dropshadow(gaussian, black, 2, 1, 0, 0);");
        ProgressBar bar = new ProgressBar(1.0);
        bar.setPrefWidth(200);
        bar.setStyle("-fx-accent: " + toHexString(barColor) + ";");
        box.getChildren().addAll(lbl, bar);
        return box;
    }

    /**
     * Creates a styled button.
     * * @param text The text on the button
     * @param baseColor The base hex color for the button
     * @return The styled Button
     */
    private Button createButton(String text, String baseColor) {
        Button btn = new Button(text);
        btn.setPrefSize(160, 50);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        btn.setTextFill(Color.WHITE);
        btn.setStyle("-fx-background-color: linear-gradient(" + baseColor + ", derive(" + baseColor + ", -20%)); -fx-background-radius: 14; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.7), 12, 0.25, 0, 4);");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: linear-gradient(derive(" + baseColor + ", 10%), " + baseColor + "); -fx-background-radius: 14; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.9), 14, 0.4, 0, 5);"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: linear-gradient(" + baseColor + ", derive(" + baseColor + ", -20%)); -fx-background-radius: 14; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.7), 12, 0.25, 0, 4);"));
        return btn;
    }

    /**
     * Converts a Color object to a Hex string.
     * * @param color The Color object
     * @return The hex string representation
     */
    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
    }

    /**
     * Updates the turn label text and color.
     * * @param text The text to display
     * @param color The color of the text
     */
    public void updateTurnLabel(String text, Color color) {
        turnLbl.setText(text);
        turnLbl.setTextFill(color);
        turnLbl.setVisible(true);
        double textWidth = text.length() * 20;
        turnLbl.setLayoutX((SCENE_WIDTH - textWidth) / 2 + 20);
    }

    /**
     * Resets the player image to the idle state.
     */
    public void resetPlayerToIdle() {
        if (playerImageView.getImage() != playerDeadImg) {
            playerImageView.setImage(getPlayerCurrentIdleImage());
        }
    }

    /**
     * Resets the enemy image to the idle state.
     */
    public void resetEnemyToIdle() {
        if (enemyImageView.getImage() != enemyDeadImg) {
            enemyImageView.setImage(enemyIdleImg);
        }
    }

    /**
     * Updates the stats and visuals for player and enemy.
     * * @param p The player entity
     * @param z The enemy entity
     * @param stage The current stage number
     */
    public void updateStats(Player p, Zombie z, int stage) {
        double pp = (double) p.getHp() / p.getMaxHp();
        playerBar.setProgress(pp);
        playerLbl.setText("Player HP: " + p.getHp());

        // [UPDATED] เช็คให้ครอบคลุมรูปทุกประเภท เพื่อไม่ให้เด้งผิดจังหวะ
        if (p.isAlive() &&
                playerImageView.getImage() != playerAttackImg &&
                playerImageView.getImage() != playerKnifeAttackImg &&
                playerImageView.getImage() != playerGunAttackImg &&
                playerImageView.getImage() != playerHammerAttackImg &&
                playerImageView.getImage() != playerAxeAttackImg &&
                playerImageView.getImage() != playerRifleAttackImg &&
                playerImageView.getImage() != playerHurtImg) {

            playerImageView.setImage(getPlayerCurrentIdleImage());
        }

        if (z != null) {
            double ep = (double) z.getHp() / z.getMaxHp();
            enemyBar.setProgress(ep);
            enemyLbl.setText(z.getClass().getSimpleName() + " (" + z.getHp() + ")");

            String baseName = "normal_zombie";
            if (z instanceof RottenZombie) baseName = "rotten_zombie";
            else if (z instanceof ArmoredZombie) baseName = "armored_zombie";
            else if (z instanceof RunnerZombie) baseName = "runner_zombie";
            else if (z instanceof BossZombie) baseName = "boss_zombie";

            // สมมติว่าเก็บรูปซอมบี้ไว้ใน subfolder "zombies" หรือที่เดียวกับ player ก็ได้ (แก้ path ตามจริง)
            enemyIdleImg = ResourceManager.getImage("characters/zombies/" + baseName + "_idle.png");
            enemyAttackImg = ResourceManager.getImage("characters/zombies/" + baseName + "_attack.png");
            enemyHurtImg = ResourceManager.getImage("characters/zombies/" + baseName + "_hurt.png");
            enemyDeadImg = ResourceManager.getImage("characters/zombies/" + baseName + "_dead.png");

            if (enemyIdleImg == null)
                enemyIdleImg = ResourceManager.getImage("characters/zombies/normal_zombie_idle.png");
            if (enemyAttackImg == null) enemyAttackImg = enemyIdleImg;
            if (enemyHurtImg == null) enemyHurtImg = enemyIdleImg;
            if (enemyDeadImg == null) enemyDeadImg = enemyHurtImg;

            if (z.isAlive() && enemyImageView.getImage() == null) {
                enemyImageView.setImage(enemyIdleImg);
            }
        }
        stageLbl.setText("STAGE " + stage);

        Image bg = ResourceManager.getImage("backgrounds/bgALL.png");
        if (bg == null) bg = ResourceManager.getImage("backgrounds/bgALL.png");
        backgroundImageView.setImage(bg);
    }

    /**
     * Performs the attack animation sequence.
     * * @param attacker The image view of the attacker
     * @param idleImg The idle image to revert to
     * @param moveX The distance to move on X axis
     * @param cb Callback to run after animation
     */
    private void animateAttack(ImageView attacker, Image idleImg, double moveX, Runnable cb) {
        this.idleImg = idleImg;
        // [UPDATED] ถ้าเป็น Player ตี ให้ใช้รูปตามอาวุธ
        if (attacker == playerImageView) {
            attacker.setImage(getPlayerCurrentAttackImage());
            // [NEW] Play player attack sound
            playWeaponSound();
        } else {
            attacker.setImage(enemyAttackImg); // ถ้าเป็นศัตรู ใช้รูปตีของศัตรู
            SoundManager.playSound("Zombie_dead_2.wav");
        }

        TranslateTransition lunge = new TranslateTransition(ATTACK_DURATION, attacker);
        lunge.setByX(moveX);
        lunge.setOnFinished(e -> {
            if (cb != null) cb.run();
        });
        TranslateTransition retreat = new TranslateTransition(ATTACK_DURATION, attacker);
        retreat.setByX(-moveX);

        // [UPDATED] เมื่อกลับที่เดิม ให้เปลี่ยนเป็น Idle ที่ถูกต้อง
        retreat.setOnFinished(e -> {
            if (attacker == playerImageView) {
                attacker.setImage(getPlayerCurrentIdleImage());
            } else {
                attacker.setImage(enemyIdleImg);
            }
        });

        new SequentialTransition(lunge, retreat).play();
    }

    /**
     * Performs the dodge animation.
     * * @param defender The image view of the defender
     */
    private void animateDodge(ImageView defender) {
        double dir = (defender == playerImageView) ? -40 : 40;
        TranslateTransition d1 = new TranslateTransition(Duration.millis(100), defender);
        d1.setByX(dir);
        TranslateTransition d2 = new TranslateTransition(Duration.millis(100), defender);
        d2.setByX(-dir);
        new SequentialTransition(d1, d2).play();
    }

    // [UPDATED] อนิเมชั่นโดนโจมตี: ค้างรูป Hurt ไว้

    /**
     * Performs the hurt or dead animation.
     * * @param defender The image view of the defender
     * @param hurtImg The image to show when hurt
     * @param deadImg The image to show when dead
     * @param idleImg The image to show when idle
     * @param willDie Flag indicating if the character will die
     */
    private void animateHurtOrDead(ImageView defender, Image hurtImg, Image deadImg, Image idleImg, boolean willDie) {
        this.idleImg = idleImg;
        defender.setImage(hurtImg);

        // [NEW] Play hurt sound
        if (defender == playerImageView) {
            SoundManager.playSound("cough_short.wav");
        } else {
            SoundManager.playSound("Zombie_hit_1.wav");
        }

        double shakeDist = 10;
        TranslateTransition t1 = new TranslateTransition(SHAKE_DURATION, defender);
        t1.setByX(shakeDist);
        TranslateTransition t2 = new TranslateTransition(SHAKE_DURATION, defender);
        t2.setByX(-shakeDist * 2);
        TranslateTransition t3 = new TranslateTransition(SHAKE_DURATION, defender);
        t3.setByX(shakeDist);

        SequentialTransition shakeSeq = new SequentialTransition(t1, t2, t3);
        shakeSeq.setOnFinished(e -> {
            if (willDie) {
                defender.setImage(deadImg);
            }
            // ไม่ตาย -> ค้างรูป Hurt ไว้ รอ Controller สั่ง reset
        });
        shakeSeq.play();
    }

    /**
     * Plays the player's attack animation.
     * * @param cb Callback when animation finishes
     */
    public void playPlayerAttack(Runnable cb) {
        animateAttack(playerImageView, null, ATTACK_DISTANCE, cb);
    }

    /**
     * Plays the enemy's attack animation.
     * * @param cb Callback when animation finishes
     */
    public void playEnemyAttack(Runnable cb) {
        animateAttack(enemyImageView, enemyIdleImg, -ATTACK_DISTANCE, cb);
    }

    /**
     * Plays the player's defense animation.
     * * @param isDodge True if dodging, false if taking damage
     * @param willDie True if player will die
     */
    public void playPlayerDefend(boolean isDodge, boolean willDie) {
        if (isDodge) animateDodge(playerImageView);
        else animateHurtOrDead(playerImageView, playerHurtImg, playerDeadImg, playerIdleImg, willDie);
    }

    /**
     * Plays the enemy's defense animation.
     * * @param isDodge True if dodging, false if taking damage
     * @param willDie True if enemy will die
     */
    public void playEnemyDefend(boolean isDodge, boolean willDie) {
        if (isDodge) animateDodge(enemyImageView);
        else animateHurtOrDead(enemyImageView, enemyHurtImg, enemyDeadImg, enemyIdleImg, willDie);
    }

    /**
     * Updates the battle log with a new message.
     * * @param msg The message to add
     */
    public void updateLog(String msg) {
        logArea.appendText(msg + "\n");
        logArea.setScrollTop(Double.MAX_VALUE);
    }

    /**
     * Clears the battle log.
     */
    public void resetLog() {
        logArea.clear();
    }

    /**
     * Enables or disables the control buttons.
     * * @param val True to disable, false to enable
     */
    public void setControlsDisabled(boolean val) {
        btnAttack.setDisable(val);
        btnHeal.setDisable(val);
        btnInv.setDisable(val);
    }

    /**
     * Returns the main view layout.
     * * @return The VBox layout
     */
    public VBox getView() {
        return rootLayout;
    }

    /**
     * Gets the current idle image.
     * * @return The idle image
     */
    public Image getIdleImg() {
        return idleImg;
    }

    /**
     * Sets the current idle image.
     * * @param idleImg The idle image to set
     */
    public void setIdleImg(Image idleImg) {
        this.idleImg = idleImg;
    }
}